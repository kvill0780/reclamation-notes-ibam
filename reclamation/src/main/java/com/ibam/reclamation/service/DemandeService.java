package com.ibam.reclamation.service;

import com.ibam.reclamation.entity.*;
import com.ibam.reclamation.exception.BusinessException.*;
import com.ibam.reclamation.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service métier DemandeService
 * Implémente le workflow du diagramme d'activité UML
 * Respecte les règles métier des cas d'utilisation
 */
@Service
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    public DemandeService(DemandeRepository demandeRepository, UserRepository userRepository,
            NoteRepository noteRepository) {
        this.demandeRepository = demandeRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    // CU: Soumettre réclamation (Étudiant)
    @Transactional
    public DemandeReclamation soumettre(User etudiant, Long noteId, String description,
            Double noteAttendue,
            String justificatifNom, String justificatifType, byte[] justificatifData) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException(
                        "Note non trouvée avec ID:" + noteId));

        DemandeReclamation demande = DemandeReclamation.soumettre(etudiant, note, description,
                noteAttendue,
                justificatifNom, justificatifType, justificatifData);

        return demandeRepository.save(demande);
    }

    // CU: Vérifier recevabilité (Scolarité)
    @Transactional
    public void verifierRecevabilite(Long demandeId, boolean recevable, String commentaire) {
        DemandeReclamation demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée"));

        demande.verifierRecevabilite(recevable, commentaire);
        demandeRepository.save(demande);
    }

    // CU: Imputer demande (DA)
    @Transactional
    public void imputerDemande(Long demandeId, Long enseignantId) {
        DemandeReclamation demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée"));
        User enseignant = userRepository.findById(enseignantId)
                .orElseThrow(() -> new EnseignantNotFoundException("Enseignant non trouvé"));
        User enseignantResponsable = demande.getNote().getEnseignantResponsable();

        if (!enseignantResponsable.getId().equals(enseignant.getId())) {
            throw new IllegalArgumentException(
                    "Imputation invalide: la demande doit être imputée à l'enseignant responsable de la note");
        }

        demande.imputer(enseignant);
        demandeRepository.save(demande);
    }

    // CU: Imputation automatique (DA)
    @Transactional
    public void imputerDemandeAutomatiquement(Long demandeId) {
        DemandeReclamation demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée"));

        // Récupération automatique de l'enseignant responsable
        User enseignantResponsable = demande.getNote().getEnseignantResponsable();

        demande.imputer(enseignantResponsable);
        demandeRepository.save(demande);
    }

    // CU: Analyser demande (Enseignant) — accepte ou refuse, sans saisir de note
    @Transactional
    public void analyserDemande(Long demandeId, boolean acceptee, String commentaire) {
        DemandeReclamation demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée"));

        demande.analyser(acceptee, commentaire);
        demandeRepository.save(demande);
    }

    // Variante sécurisée: contrôle d'accès basé sur l'utilisateur courant
    @Transactional
    public void analyserDemande(Long demandeId, boolean acceptee, String commentaire, User utilisateur) {
        DemandeReclamation demande = getDemandeAccessiblePourUtilisateur(demandeId, utilisateur);
        demande.analyser(acceptee, commentaire);
        demandeRepository.save(demande);
    }

    // CU: Appliquer décision (Scolarité) — applique la noteAttendue de l'étudiant
    // si acceptée
    @Transactional
    public void appliquerDecision(Long demandeId) {
        DemandeReclamation demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée"));

        demande.appliquerDecision();
        demandeRepository.save(demande);
    }

    // Variante sécurisée: contrôle d'accès basé sur l'utilisateur courant
    @Transactional
    public void appliquerDecision(Long demandeId, User utilisateur) {
        DemandeReclamation demande = getDemandeAccessiblePourUtilisateur(demandeId, utilisateur);
        demande.appliquerDecision();
        demandeRepository.save(demande);
    }

    // Consultation par rôle - LOGIQUE MÉTIER DANS LE SERVICE
    @Transactional(readOnly = true)
    public List<DemandeReclamation> getDemandesPourUtilisateur(User user) {
        return switch (user.getRole()) {
            case ROLE_ETUDIANT ->
                demandeRepository.findByEtudiantId(user.getId());
            case ROLE_ENSEIGNANT ->
                demandeRepository.findByEnseignantImputeId(user.getId());
            case ROLE_DA, ROLE_SCOLARITE ->
                demandeRepository.findAll();
        };
    }

    @Transactional(readOnly = true)
    public DemandeReclamation getDemandeAccessiblePourUtilisateur(Long demandeId, User user) {
        DemandeReclamation demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée"));

        // Vérification des permissions selon le rôle
        boolean hasAccess = switch (user.getRole()) {
            case ROLE_ETUDIANT ->
                demande.getEtudiant().getId().equals(user.getId());
            case ROLE_ENSEIGNANT ->
                demande.getEnseignantImpute() != null &&
                        demande.getEnseignantImpute().getId().equals(user.getId());
            case ROLE_DA, ROLE_SCOLARITE ->
                true; // Accès total
        };

        if (!hasAccess) {
            throw new DemandeNotFoundException("Accès non autorisé à cette demande");
        }

        return demande;
    }

    // Méthodes utilitaires pour les autres controllers (compatibilité)
    @Transactional(readOnly = true)
    public List<DemandeReclamation> getDemandesParEtudiant(Long etudiantId) {
        return demandeRepository.findByEtudiantId(etudiantId);
    }

    @Transactional(readOnly = true)
    public List<DemandeReclamation> getDemandesParEnseignant(Long enseignantId) {
        return demandeRepository.findByEnseignantImputeId(enseignantId);
    }

    @Transactional(readOnly = true)
    public List<DemandeReclamation> getToutesDemandes() {
        return demandeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getEnseignantsImputables(Long demandeId) {
        DemandeReclamation demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée"));

        return List.of(demande.getNote().getEnseignantResponsable());
    }
}
