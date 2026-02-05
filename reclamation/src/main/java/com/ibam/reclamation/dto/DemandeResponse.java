package com.ibam.reclamation.dto;

import com.ibam.reclamation.entity.DemandeReclamation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour les demandes de réclamation
 * Évite les problèmes de sérialisation des entités JPA
 * (LazyInitializationException)
 */
@Data
@Builder
public class DemandeResponse {
    private Long id;
    private String description;
    private String statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereAction;
    private String commentaireScolarite;
    private String commentaireEnseignant;

    // Étudiant
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantPrenom;
    private String etudiantEmail;

    // Note
    private Long noteId;
    private Double noteValeur;
    private String matiereNom;
    private String matiereCode;
    private String semestre;

    // Enseignant responsable de la matière (pour aider le DA dans l'imputation)
    private Long enseignantResponsableId;
    private String enseignantResponsableNom;
    private String enseignantResponsablePrenom;
    
    // Enseignant imputé (peut être null)
    private Long enseignantImputeId;
    private String enseignantImputeNom;
    private String enseignantImputePrenom;

    // Justificatif
    private String justificatifNom;
    private boolean hasJustificatif;
    
    // Note proposée par l'enseignant
    private Double nouvelleNoteProposee;

    /**
     * Convertit une entité DemandeReclamation en DTO
     */
    public static DemandeResponse fromEntity(DemandeReclamation demande) {
        DemandeResponseBuilder builder = DemandeResponse.builder()
                .id(demande.getId())
                .description(demande.getDescription())
                .statut(demande.getStatut().name())
                .dateCreation(demande.getDateCreation())
                .dateDerniereAction(demande.getDateDerniereAction())
                .commentaireScolarite(demande.getCommentaireScolarite())
                .commentaireEnseignant(demande.getCommentaireEnseignant());

        // Étudiant
        if (demande.getEtudiant() != null) {
            builder.etudiantId(demande.getEtudiant().getId())
                    .etudiantNom(demande.getEtudiant().getNom())
                    .etudiantPrenom(demande.getEtudiant().getPrenom())
                    .etudiantEmail(demande.getEtudiant().getEmail());
        }

        // Note et matière
        if (demande.getNote() != null) {
            builder.noteId(demande.getNote().getId())
                    .noteValeur(demande.getNote().getValeur());

            // Accès à la matière via l'enseignement
            if (demande.getNote().getEnseignement() != null && 
                demande.getNote().getEnseignement().getMatiere() != null) {
                builder.matiereNom(demande.getNote().getEnseignement().getMatiere().getNom())
                        .matiereCode(demande.getNote().getEnseignement().getMatiere().getCode())
                        .semestre(demande.getNote().getEnseignement().getSemestre().name());
            }
        }

        // Enseignant responsable de la matière
        if (demande.getNote() != null && demande.getNote().getEnseignantResponsable() != null) {
            builder.enseignantResponsableId(demande.getNote().getEnseignantResponsable().getId())
                    .enseignantResponsableNom(demande.getNote().getEnseignantResponsable().getNom())
                    .enseignantResponsablePrenom(demande.getNote().getEnseignantResponsable().getPrenom());
        }

        // Enseignant imputé (optionnel)
        if (demande.getEnseignantImpute() != null) {
            builder.enseignantImputeId(demande.getEnseignantImpute().getId())
                    .enseignantImputeNom(demande.getEnseignantImpute().getNom())
                    .enseignantImputePrenom(demande.getEnseignantImpute().getPrenom());
        }

        // Justificatif
        builder.justificatifNom(demande.getJustificatifNom())
                .hasJustificatif(demande.getJustificatifData() != null && demande.getJustificatifData().length > 0)
                .nouvelleNoteProposee(demande.getNouvelleNoteProposee());

        return builder.build();
    }
}