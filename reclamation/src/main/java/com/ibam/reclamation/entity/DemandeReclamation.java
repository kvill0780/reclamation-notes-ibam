package com.ibam.reclamation.entity;

import com.ibam.reclamation.security.RoleEnum;
import com.ibam.reclamation.exception.BusinessException.DescriptionObligatoireException;
import com.ibam.reclamation.exception.BusinessException.NoteNonAutoriseException;
import com.ibam.reclamation.exception.BusinessException.CommentaireObligatoireException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entité centrale DemandeReclamation
 * Implémente le workflow du diagramme d'activité UML
 * Cycle de vie géré par StatutDemande
 */
@Entity
@Table(name = "demandes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "etudiant_id", "note_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeReclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemande statut;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateDerniereAction;

    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    private String commentaireScolarite;
    
    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    private String commentaireEnseignant;

    @Column(name = "justificatif_nom")
    private String justificatifNom;

    @Column(name = "justificatif_type")
    private String justificatifType;

    @Column(name = "nouvelle_note_proposee")
    private Double nouvelleNoteProposee;

    @Lob
    @Column(name = "justificatif_data")
    private byte[] justificatifData;

    // Relations selon diagramme de classes UML
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private User etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_impute_id")
    private User enseignantImpute;

    public static DemandeReclamation soumettre(User etudiant, Note note, String description, 
                                             String justificatifNom, String justificatifType, byte[] justificatifData) {

        if (etudiant == null || note == null) {
            throw new IllegalArgumentException("Étudiant et note obligatoires");
        }
        if (description == null || description.isBlank()) {
            throw new DescriptionObligatoireException("La description est obligatoire");
        }
        if (justificatifData == null || justificatifData.length == 0) {
            throw new IllegalArgumentException("Le justificatif est obligatoire");
        }
        if (!note.getEtudiant().equals(etudiant)) {
            throw new NoteNonAutoriseException("Note non autorisée pour cet étudiant");
        }

        DemandeReclamation demande = new DemandeReclamation();
        demande.note = note;
        demande.etudiant = etudiant;
        demande.description = description;
        demande.justificatifNom = justificatifNom;
        demande.justificatifType = justificatifType;
        demande.justificatifData = justificatifData;
        demande.statut = StatutDemande.SOUMISE;
        demande.dateCreation = LocalDateTime.now();
        demande.dateDerniereAction = LocalDateTime.now();

        return demande;
    }

    public void verifierRecevabilite(boolean recevable, String commentaire) {
        if (statut != StatutDemande.SOUMISE)
            throw new IllegalStateException("Demande déjà traitée");
        
        // Commentaire obligatoire SEULEMENT si rejetée
        if (!recevable && (commentaire == null || commentaire.isBlank()))
            throw new CommentaireObligatoireException("Le commentaire est obligatoire pour expliquer le rejet");

        this.commentaireScolarite = commentaire;
        this.statut = recevable ? StatutDemande.TRANSMISE_DA : StatutDemande.REJETEE;
        this.dateDerniereAction = LocalDateTime.now();
    }

    public void imputer(User enseignant) {
        if (this.statut != StatutDemande.TRANSMISE_DA) {
            throw new IllegalStateException(
                    "La demande doit être transmise au DA avant imputation");
        }
        if (enseignant == null) {
            throw new IllegalArgumentException("Enseignant ne peut pas être null");
        }
        if (!enseignant.getRole().equals(RoleEnum.ROLE_ENSEIGNANT)) {
            throw new IllegalArgumentException(
                    "L'utilisateur doit avoir le rôle ENSEIGNANT");
        }

        this.enseignantImpute = enseignant;
        this.statut = StatutDemande.IMPUTEE;
        this.dateDerniereAction = LocalDateTime.now();
    }

    public void analyser(boolean acceptee, String commentaire, Double nouvelleNoteProposee) {
        if (this.statut != StatutDemande.IMPUTEE) {
            throw new IllegalStateException("La demande doit être imputée avant analyse");
        }
        if (commentaire == null || commentaire.isBlank()) {
            throw new CommentaireObligatoireException("Le commentaire d'analyse est obligatoire");
        }
        if (acceptee && (nouvelleNoteProposee == null || nouvelleNoteProposee < 0 || nouvelleNoteProposee > 20)) {
            throw new IllegalArgumentException("Une nouvelle note valide (0-20) est obligatoire si acceptée");
        }

        this.commentaireEnseignant = commentaire;
        this.nouvelleNoteProposee = nouvelleNoteProposee;
        this.statut = acceptee ? StatutDemande.ACCEPTEE : StatutDemande.REFUSEE;
        this.dateDerniereAction = LocalDateTime.now();
    }

    public void appliquerDecision(Double nouvelleNote) {
        if (this.statut != StatutDemande.ACCEPTEE && this.statut != StatutDemande.REFUSEE) {
            throw new IllegalStateException("Seules les demandes ACCEPTEES ou REFUSEES peuvent être appliquées");
        }
        
        // Si la demande est acceptée → appliquer automatiquement la note proposée par l'enseignant
        if (this.statut == StatutDemande.ACCEPTEE) {
            if (this.nouvelleNoteProposee == null) {
                throw new IllegalStateException("Aucune note proposée trouvée pour cette demande acceptée");
            }
            this.note.modifierValeur(this.nouvelleNoteProposee);
        }
        // Si refusée → ne rien faire, la note reste inchangée

        this.statut = StatutDemande.APPLIQUEE;
        this.dateDerniereAction = LocalDateTime.now();
    }

}