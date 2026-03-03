package com.ibam.reclamation;

import com.ibam.reclamation.entity.*;
import com.ibam.reclamation.security.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires PURS – aucun contexte Spring, aucune base de données.
 *
 * Nouveau workflow : la validation de la note se fait à la SOUMISSION
 * (noteAttendue),
 * pas à l'analyse. L'enseignant accepte ou refuse sans saisir de note.
 *
 * Ce fichier teste :
 * 1. Validation de noteAttendue à la soumission (0-20 obligatoire)
 * 2. Analyse : acceptation/refus sans note
 * 3. Application : la noteAttendue est appliquée si acceptée
 */
@DisplayName("Validation du workflow réclamation - nouveau modèle")
class NoteInvalideValidationTest {

        private User etudiant;
        private User enseignant;
        private Note note;

        @BeforeEach
        void setUp() {
                // ── Créer un étudiant ──
                etudiant = new User();
                etudiant.setId(1L);
                etudiant.setNom("Soulama");
                etudiant.setPrenom("Joel");
                etudiant.setEmail("joel.soulama@ibam.ma");
                etudiant.setPasswordHash("$2a$10$xxx");
                etudiant.setRole(RoleEnum.ROLE_ETUDIANT);
                etudiant.setNiveau(Niveau.L3);
                etudiant.setFiliere(Filiere.MIAGE);

                // ── Créer un enseignant ──
                enseignant = new User();
                enseignant.setId(2L);
                enseignant.setNom("Traore");
                enseignant.setPrenom("Yaya");
                enseignant.setEmail("yaya.traore@ibam.ma");
                enseignant.setPasswordHash("$2a$10$xxx");
                enseignant.setRole(RoleEnum.ROLE_ENSEIGNANT);

                // ── Créer une matière et un enseignement ──
                Matiere matiere = new Matiere();
                matiere.setId(1L);
                matiere.setCode("INFO301");
                matiere.setNom("Algorithmique");

                Enseignement enseignement = new Enseignement();
                enseignement.setId(1L);
                enseignement.setMatiere(matiere);
                enseignement.setEnseignant(enseignant);
                enseignement.setSemestre(Semestre.S1);

                // ── Créer une note initiale (12.0 / 20) ──
                note = new Note();
                note.setId(1L);
                note.setValeur(12.0);
                note.setEtudiant(etudiant);
                note.setEnseignement(enseignement);
        }

        // ═══════════════════════════════════════════════════════════════
        // SECTION 1 : Validation de noteAttendue à la SOUMISSION
        // ═══════════════════════════════════════════════════════════════

        @Test
        @DisplayName("✔ Soumission valide : noteAttendue=15.0 doit être acceptée")
        void soumission_noteAttendue_valide() {
                assertDoesNotThrow(() -> DemandeReclamation.soumettre(
                                etudiant, note, "Je mérite mieux.", 15.0,
                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                System.out.println("[✔] noteAttendue=15.0 acceptée à la soumission");
        }

        @Test
        @DisplayName("✔ Soumission valide : noteAttendue=0.0 (limite basse)")
        void soumission_noteAttendue_zero() {
                assertDoesNotThrow(() -> DemandeReclamation.soumettre(
                                etudiant, note, "Note minimale.", 0.0,
                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                System.out.println("[✔] noteAttendue=0.0 acceptée à la soumission");
        }

        @Test
        @DisplayName("✔ Soumission valide : noteAttendue=20.0 (limite haute)")
        void soumission_noteAttendue_vingt() {
                assertDoesNotThrow(() -> DemandeReclamation.soumettre(
                                etudiant, note, "Excellent confirmé.", 20.0,
                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                System.out.println("[✔] noteAttendue=20.0 acceptée à la soumission");
        }

        @Test
        @DisplayName("✖ Soumission invalide : noteAttendue=25.0 doit être rejetée")
        void soumission_noteAttendue_25_invalide() {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> DemandeReclamation.soumettre(
                                                etudiant, note, "Réclamation.", 25.0,
                                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                assertTrue(ex.getMessage().contains("0 et 20"),
                                "Le message doit mentionner la plage '0 et 20'");
                System.out.println("[✔] noteAttendue=25.0 rejetée : " + ex.getMessage());
        }

        @Test
        @DisplayName("✖ Soumission invalide : noteAttendue=-2.0 doit être rejetée")
        void soumission_noteAttendue_negative_invalide() {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> DemandeReclamation.soumettre(
                                                etudiant, note, "Réclamation.", -2.0,
                                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                assertTrue(ex.getMessage().contains("0 et 20"));
                System.out.println("[✔] noteAttendue=-2.0 rejetée : " + ex.getMessage());
        }

        @Test
        @DisplayName("✖ Soumission invalide : noteAttendue=null doit être rejetée")
        void soumission_noteAttendue_null_invalide() {
                assertThrows(IllegalArgumentException.class,
                                () -> DemandeReclamation.soumettre(
                                                etudiant, note, "Réclamation.", null,
                                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                System.out.println("[✔] noteAttendue=null rejetée à la soumission");
        }

        @ParameterizedTest(name = "✖ noteAttendue={0} invalide (> 20)")
        @ValueSource(doubles = { 20.01, 21.0, 25.0, 100.0, 999.99 })
        @DisplayName("✖ Toutes les notes > 20 doivent être rejetées à la soumission")
        void soumission_notesSuperieure20_invalides(double noteInvalide) {
                assertThrows(IllegalArgumentException.class,
                                () -> DemandeReclamation.soumettre(
                                                etudiant, note, "Réclamation.", noteInvalide,
                                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                System.out.println("[✔] noteAttendue=" + noteInvalide + " rejetée");
        }

        @ParameterizedTest(name = "✖ noteAttendue={0} invalide (< 0)")
        @ValueSource(doubles = { -0.01, -1.0, -2.0, -10.0, -100.0 })
        @DisplayName("✖ Toutes les notes < 0 doivent être rejetées à la soumission")
        void soumission_notesNegatives_invalides(double noteInvalide) {
                assertThrows(IllegalArgumentException.class,
                                () -> DemandeReclamation.soumettre(
                                                etudiant, note, "Réclamation.", noteInvalide,
                                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 }));
                System.out.println("[✔] noteAttendue=" + noteInvalide + " rejetée");
        }

        // ═══════════════════════════════════════════════════════════════
        // SECTION 2 : Analyse par l'enseignant (accepter/refuser SANS note)
        // ═══════════════════════════════════════════════════════════════

        private DemandeReclamation creerDemandeImputee(double noteAttendue) {
                DemandeReclamation demande = DemandeReclamation.soumettre(
                                etudiant, note, "Je mérite mieux.", noteAttendue,
                                "justificatif.pdf", "application/pdf", new byte[] { 1, 2, 3 });
                demande.verifierRecevabilite(true, null);
                demande.imputer(enseignant);
                return demande;
        }

        @Test
        @DisplayName("✔ Analyse : acceptation sans saisie de note")
        void analyse_acceptation_sansSaisieNote() {
                DemandeReclamation demande = creerDemandeImputee(15.0);
                assertDoesNotThrow(() -> demande.analyser(true, "Vérification faite, note justifiée."));
                assertEquals(StatutDemande.ACCEPTEE, demande.getStatut());
                System.out.println("[✔] Demande acceptée sans saisie de note. Statut → ACCEPTEE");
        }

        @Test
        @DisplayName("✔ Analyse : refus sans saisie de note")
        void analyse_refus_sansSaisieNote() {
                DemandeReclamation demande = creerDemandeImputee(15.0);
                assertDoesNotThrow(() -> demande.analyser(false, "Réclamation non justifiée."));
                assertEquals(StatutDemande.REFUSEE, demande.getStatut());
                System.out.println("[✔] Demande refusée. Statut → REFUSEE");
        }

        @Test
        @DisplayName("✖ Analyse sans commentaire doit lever une exception")
        void analyse_sansCommentaire_invalide() {
                DemandeReclamation demande = creerDemandeImputee(15.0);
                assertThrows(Exception.class, () -> demande.analyser(true, ""));
                assertEquals(StatutDemande.IMPUTEE, demande.getStatut());
                System.out.println("[✔] Analyse sans commentaire rejetée");
        }

        // ═══════════════════════════════════════════════════════════════
        // SECTION 3 : Application de la décision (noteAttendue appliquée)
        // ═══════════════════════════════════════════════════════════════

        @Test
        @DisplayName("✔ Application : noteAttendue=15.0 appliquée si acceptée")
        void application_noteAttendueAppliquee() {
                DemandeReclamation demande = creerDemandeImputee(15.0);
                demande.analyser(true, "Note justifiée.");
                demande.appliquerDecision();

                assertEquals(StatutDemande.APPLIQUEE, demande.getStatut());
                assertEquals(15.0, note.getValeur(), "La note de l'étudiant doit être mise à 15.0");
                System.out.println("[✔] noteAttendue=15.0 appliquée sur la note. Statut → APPLIQUEE");
        }

        @Test
        @DisplayName("✔ Application : note inchangée si refusée")
        void application_noteInchangeeSiRefusee() {
                double noteInitiale = note.getValeur();
                DemandeReclamation demande = creerDemandeImputee(15.0);
                demande.analyser(false, "Réclamation non fondée.");
                demande.appliquerDecision();

                assertEquals(StatutDemande.APPLIQUEE, demande.getStatut());
                assertEquals(noteInitiale, note.getValeur(), "La note ne doit pas changer si refusée");
                System.out.println("[✔] Note inchangée après refus. Statut → APPLIQUEE");
        }
}
