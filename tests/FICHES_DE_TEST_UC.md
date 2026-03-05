# Fiches de test fonctionnel - UC prioritaires

Auteurs du cas de test: Équipe de testeur 1  
Date d'exécution du test: 05 mars 2026

## Fiche de test N°: 01
Nom du cas de test: Se connecter (identifiants valides)  
Type de test: Test fonctionnel  
Objectif du test: Vérifier la connexion à la plateforme

Préconditions (prérequis/règles):
1. L'utilisateur est créé dans la base et actif.
2. L'utilisateur connaît l'URL de connexion.
3. Le backend et le frontend sont démarrés.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Saisir l'URL dans le navigateur | Le système affiche le formulaire demandant l'email et le mot de passe |  | OK / Non OK |  |
| 2 | Remplir le formulaire avec des identifiants valides | Le système vérifie les informations d'identification et redirige vers le dashboard |  | OK / Non OK |  |

Post condition: Session utilisateur ouverte.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/selenium/test_login_valid_credentials.py`

---

## Fiche de test N°: 02
Nom du cas de test: Rejeter une connexion invalide (1ère et 2ème tentatives)  
Type de test: Test fonctionnel  
Objectif du test: Vérifier le message d'erreur et l'enregistrement des échecs

Préconditions (prérequis/règles):
1. L'utilisateur existe et n'est pas bloqué.
2. L'URL de connexion est accessible.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Saisir un email inconnu et un mot de passe | Le système affiche "Identifiant ou mot de passe invalide" |  | OK / Non OK |  |
| 2 | Refaire une tentative invalide | Le système affiche le même message d'erreur |  | OK / Non OK |  |
| 3 | Vérifier l'accès avec identifiants valides juste après 2 échecs | Le compte reste actif après la 2ème tentative échouée |  | OK / Non OK |  |

Post condition: Deux échecs de connexion enregistrés.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/selenium/test_login_invalid_credentials.py`

---

## Fiche de test N°: 03
Nom du cas de test: Bloquer le compte après 3 échecs  
Type de test: Test fonctionnel / sécurité  
Objectif du test: Vérifier la désactivation du compte à la 3ème tentative échouée

Préconditions (prérequis/règles):
1. Compte de test actif dédié au scénario de blocage.
2. Deux tentatives échouées déjà effectuées sur le même compte.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Saisir un mauvais mot de passe pour la 3ème fois | Le système indique l'erreur et désactive le compte |  | OK / Non OK |  |
| 2 | Tenter une connexion avec le bon mot de passe après blocage | Le système refuse l'authentification (compte bloqué) |  | OK / Non OK |  |

Post condition: Compte désactivé (bloqué).  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/exercice1_test_fonctionnel.py`

---

## Fiche de test N°: 04
Nom du cas de test: Se déconnecter  
Type de test: Test fonctionnel  
Objectif du test: Vérifier la fermeture de session et la redirection

Préconditions (prérequis/règles):
1. L'utilisateur est connecté.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Cliquer sur le lien/bouton "Déconnecter" | Le système ferme la session de l'utilisateur |  | OK / Non OK |  |
| 2 | Vérifier la navigation après déconnexion | Le système redirige vers la page d'authentification |  | OK / Non OK |  |

Post condition: Session fermée, token supprimé côté client.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/exercice1_test_fonctionnel.py`

---

## Fiche de test N°: 05
Nom du cas de test: Empêcher l'accès au dashboard sans authentification  
Type de test: Test de sécurité  
Objectif du test: Vérifier la protection des routes privées

Préconditions (prérequis/règles):
1. Aucun utilisateur connecté (session vide).

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Saisir directement une URL de dashboard | Le système redirige vers la page de connexion |  | OK / Non OK |  |
| 2 | Vérifier le contenu affiché | Aucune donnée du dashboard n'est visible sans login |  | OK / Non OK |  |

Post condition: L'utilisateur reste non authentifié.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/selenium/test_dashboard_requires_login.py`

---

## Fiche de test N°: 06
Nom du cas de test: Soumettre une nouvelle réclamation valide  
Type de test: Test fonctionnel  
Objectif du test: Vérifier la création d'une réclamation post-délibération

Préconditions (prérequis/règles):
1. Une période de réclamation est active.
2. Un étudiant connecté dispose d'au moins une note réclamable.
3. Un justificatif fichier est disponible.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Ouvrir l'écran "Nouvelle réclamation" | Le formulaire s'affiche avec les champs obligatoires |  | OK / Non OK |  |
| 2 | Sélectionner une note, saisir description et note attendue valide (0 à 20), joindre justificatif | Le bouton de soumission devient actif |  | OK / Non OK |  |
| 3 | Soumettre le formulaire | Le système crée la réclamation et affiche une confirmation |  | OK / Non OK |  |

Post condition: Réclamation enregistrée au statut initial attendu.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/selenium/test_reclamation_submission.py`

---

## Fiche de test N°: 07
Nom du cas de test: Refuser une note attendue invalide à la soumission  
Type de test: Test fonctionnel / validation  
Objectif du test: Vérifier le contrôle de validité de `noteAttendue`

Préconditions (prérequis/règles):
1. Étudiant connecté.
2. Période de réclamation active.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Saisir une note attendue invalide (ex: -1) | Le système affiche un message de validation et bloque la soumission |  | OK / Non OK |  |
| 2 | Saisir une note attendue invalide (ex: 25) | Le système affiche un message de validation et bloque la soumission |  | OK / Non OK |  |

Post condition: Aucune réclamation invalide n'est créée.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/selenium/test_reclamation_submission_invalid_expected_note.py`

---

## Fiche de test N°: 08
Nom du cas de test: Analyser une réclamation imputée (enseignant)  
Type de test: Test fonctionnel  
Objectif du test: Vérifier l'obligation de commentaire et les décisions accepter/refuser

Préconditions (prérequis/règles):
1. Une réclamation est au statut `IMPUTEE` pour l'enseignant connecté.
2. L'enseignant est authentifié.

| N° étape | Opérations à réaliser ou action à exécuter | Réponses attendues | Réponses obtenues | Résultat du test | Observations |
|---|---|---|---|---|---|
| 1 | Cliquer sur "Accepter" ou "Refuser" sans commentaire | Le système bloque l'action et exige un commentaire |  | OK / Non OK |  |
| 2 | Saisir un commentaire puis accepter | Le système enregistre la décision d'acceptation |  | OK / Non OK |  |
| 3 | Saisir un commentaire puis refuser (autre demande) | Le système enregistre la décision de refus |  | OK / Non OK |  |

Post condition: Le statut de la demande évolue selon la décision enregistrée.  
Anomalie:  
Conformité:  
Ergonomie:  
Validation globale:  
Commentaire:

Référence automatisation: `tests/selenium/test_reclamation_analysis.py`

---

## Grille de décision (reprise)

Anomalie bloquante: un scénario indispensable n'est pas fonctionnel (poids = 100)  
Anomalie moyenne: un scénario non indispensable n'est pas fonctionnel (poids = 50)  
Anomalie basse: n'affecte pas l'utilisation principale (poids = 10)
