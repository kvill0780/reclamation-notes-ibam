# Fiches de test fonctionnel - Compléments UC

Auteurs du cas de test: Équipe de testeur 1  
Date d'exécution du test: 05 mars 2026

## Fiche de test N°: 09
Nom du cas de test: Création période de réclamation (DA)  
Type de test: Test fonctionnel  
Objectif du test: Vérifier dates valides, chevauchement interdit, durée max, et affichage dans l'historique

Préconditions (prérequis/règles):
1. Le DA est connecté.
2. L'écran "Gestion des périodes" est accessible.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Saisir une période valide (date début future, date fin > date début, durée <= 72h) | Le système crée la période avec succès |  | OK / Non OK |  |
| 2 | Saisir une période qui chevauche une période ouverte/planifiée | Le système refuse la création et affiche un message de chevauchement |  | OK / Non OK |  |
| 3 | Saisir une période avec durée > 72h | Le système refuse la création et affiche un message de validation |  | OK / Non OK |  |
| 4 | Ouvrir l'onglet Historique après création valide | La période apparaît dans l'historique avec le bon statut |  | OK / Non OK |  |

Post condition: Période créée uniquement si toutes les règles sont respectées.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

---

## Fiche de test N°: 10
Nom du cas de test: Application décision finale (Scolarité)  
Type de test: Test fonctionnel  
Objectif du test: Vérifier les cas ACCEPTEE puis application, REFUSEE puis clôture, et le statut final

Préconditions (prérequis/règles):
1. La Scolarité est connectée.
2. Une demande ACCEPTEE et une demande REFUSEE existent.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Ouvrir une demande au statut ACCEPTEE puis cliquer sur "Appliquer" | Le système applique la décision et passe au statut final attendu |  | OK / Non OK |  |
| 2 | Vérifier la note liée à la demande ACCEPTEE | La valeur finale correspond à la règle métier attendue |  | OK / Non OK |  |
| 3 | Ouvrir une demande au statut REFUSEE puis clôturer/appliquer le traitement final | Le système clôture correctement la demande avec statut final attendu |  | OK / Non OK |  |
| 4 | Vérifier la note liée à la demande REFUSEE | La note reste inchangée |  | OK / Non OK |  |

Post condition: Toutes les demandes traitées ont un statut final cohérent (ACCEPTEE/REFUSEE -> état final).  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

---

## Fiche de test N°: 11
Nom du cas de test: Validation justificatif à la soumission  
Type de test: Test fonctionnel / validation  
Objectif du test: Vérifier le rejet en cas de fichier absent, type non autorisé, taille trop grande

Préconditions (prérequis/règles):
1. Étudiant connecté.
2. Période de réclamation active.
3. Formulaire de soumission affiché.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Remplir le formulaire sans joindre de justificatif | Le système bloque la soumission et indique que le justificatif est obligatoire |  | OK / Non OK |  |
| 2 | Joindre un fichier de type non autorisé (ex: .exe/.txt) | Le système refuse le fichier et affiche un message d'erreur |  | OK / Non OK |  |
| 3 | Joindre un fichier dépassant la taille maximale autorisée | Le système refuse le fichier et affiche un message de taille invalide |  | OK / Non OK |  |

Post condition: Aucune réclamation n'est créée avec un justificatif invalide.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

---

## Fiche de test N°: 12
Nom du cas de test: Contrôle d'accès par rôle  
Type de test: Test de sécurité  
Objectif du test: Vérifier les restrictions d'accès Étudiant/Enseignant/DA

Préconditions (prérequis/règles):
1. Comptes de test actifs pour les rôles Étudiant, Enseignant, DA.
2. Routes protégées accessibles par URL directe.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Connecter un Étudiant et tenter d'accéder aux actions d'analyse/imputation | Accès refusé, action non visible/non autorisée |  | OK / Non OK |  |
| 2 | Connecter un Enseignant et tenter d'accéder à la gestion des périodes | Accès refusé, redirection ou message "Accès refusé" |  | OK / Non OK |  |
| 3 | Connecter un DA et tenter d'accéder au formulaire de soumission Étudiant | Accès refusé, formulaire inaccessible |  | OK / Non OK |  |

Post condition: Chaque rôle ne peut exécuter que ses fonctionnalités autorisées.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:
