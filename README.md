# 📚 Système de Réclamation de Notes - IBAM

Application web de dématérialisation des réclamations de notes pour l'Institut Burkinabé des Arts et Métiers.

## ⚙️ Configuration

**Avant de lancer l'application, configurez les fichiers :**

1. Copiez `reclamation/src/main/resources/application-example.yml` → `application.yml`
2. Copiez `reclamation/src/main/resources/data-example.sql` → `data.sql`
3. Modifiez les valeurs :
   - **DB** : `username`, `password`
   - **JWT** : `secret` (32+ caractères)
   - **Hash** : Utilisez `HashGen.java` pour les mots de passe

## Technologies

**Backend :**
- Spring Boot 3.2.0 (Java 21)
- PostgreSQL
- Spring Security + JWT
- Maven

**Frontend :**
- React 18 + Vite
- Axios
- CSS Vanilla

## Fonctionnalités

- **Étudiants** : Consultation notes, soumission réclamations avec **justificatif** + **note attendue**
- **Scolarité** : Vérification recevabilité, validation/rejet
- **DA** : Gestion périodes, imputation enseignants (avec suggestion automatique)
- **Enseignants** : Analyse réclamations (acceptation/refus avec commentaire, **sans saisie de nouvelle note**)

## Installation

### Backend
```bash
cd reclamation
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

## Workflow

SOUMISE (avec justificatif + note attendue) → TRANSMISE_DA → IMPUTEE → ACCEPTEE/REFUSEE → APPLIQUEE/REJETEE

### Détail du workflow post-délibération

1. **Étudiant**
   - Sélectionne une note déjà publiée
   - Renseigne une **description**
   - Renseigne la **note attendue** (`0` à `20`)
   - Joint un **justificatif** (PDF/JPG/PNG)
2. **Scolarité**
   - Vérifie la recevabilité
   - Si recevable: `SOUMISE` → `TRANSMISE_DA`
   - Sinon: `SOUMISE` → `REJETEE` (commentaire obligatoire)
3. **DA**
   - Impute la demande à un enseignant (manuel ou automatique)
   - `TRANSMISE_DA` → `IMPUTEE`
4. **Enseignant**
   - Analyse et commente
   - **N’entre pas de nouvelle note**
   - `IMPUTEE` → `ACCEPTEE` ou `REFUSEE`
5. **Application**
   - Si `ACCEPTEE`, la note appliquée est la **note attendue par l’étudiant**
   - Si `REFUSEE`, la note reste inchangée
   - Statut final: `APPLIQUEE`

### Règles métier

- Une seule réclamation par couple `(étudiant, note)`.
- `noteAttendue` est obligatoire et doit être comprise entre `0` et `20`.
- Le justificatif est obligatoire à la soumission.
- Le commentaire enseignant est obligatoire lors de l’analyse.

## Comptes de test

- **Étudiant** : jean.dupont@ibam.ma (quelques réclamations)
- **Enseignant** : yaya.traore@ibam.ma ⭐ **RECOMMANDÉ** - 5 demandes avec statuts variés
- **Scolarité** : omar.tazi@ibam.ma (toutes les demandes)
- **DA** : rachid.bennani@ibam.ma (gestion complète + suggestions d'imputation)

*Mot de passe* : `password123`

## Améliorations récentes

- **DA** : Affichage de l'enseignant responsable sur les cartes pour faciliter l'imputation
- **Données de test** : Yaya Traoré dispose de 5 demandes avec différents statuts
- **Interface** : Distinction claire entre enseignant responsable et enseignant imputé
- **Sécurité** : Blocage de compte après 3 tentatives de connexion échouées
- **Validation** : Contrôle des notes (0-20) côté frontend et backend

## Tests automatisés

**Prérequis** : `pip install selenium requests`

### Tests disponibles (répertoire `/tests`)

1. **Exercice 1 - Tests fonctionnels** (`exercice1_test_fonctionnel.py`)
   - Authentification (succès/échec)
   - Blocage après 3 tentatives
   - Déconnexion

2. **Exercice 2 - Tests de charge** (`exercice2_test_charge.py`)
   - 10 puis 100 utilisateurs simultanés
   - 8 threads parallèles
   - Génère `resultats_charge.csv`

3. **Exercice 3 - Tests de performance** (`exercice3_test_performance.py`)
   - Mesure temps de chargement, remplissage formulaire, réponse serveur
   - 10 puis 100 utilisateurs
   - Génère `resultats_performance.csv`

4. **Exercice 6 - Tests unitaires UI** (`exercice6_test_unitaire.py`)
   - Validation structure page (titre, formulaire, labels, boutons)

5. **Tests validation note**
   - `test_api_validation_note.py` : Tests API REST (notes 0-20)
   - `test_validation_note_selenium.py` : Tests UI avec navigateur visible (notes -2 et 25)

### Exécution
```bash
cd tests
python exercice1_test_fonctionnel.py
python test_validation_note_selenium.py
```

## Licence

Projet académique - IBAM 2025
