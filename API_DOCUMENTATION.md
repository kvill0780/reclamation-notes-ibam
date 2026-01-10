# API Documentation - Système de Réclamations IBAM

## Base URL
```
http://localhost:8080
```

## Authentication
- **Type**: JWT Bearer Token
- **Header**: `Authorization: Bearer <token>`
- **Storage**: localStorage (keys: 'token', 'user')

---

## 🔐 Authentication Endpoints

### POST /api/auth/login
**Description**: Authentification utilisateur
```json
// Request
{
  "email": "jean.dupont@ibam.ma",
  "password": "password123"
}

// Response
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "ROLE_ETUDIANT"
}
```

### POST /api/auth/logout
**Description**: Déconnexion (endpoint simple)
```json
// Response
"Logout endpoint"
```

---

## 📝 Notes Endpoints

### GET /api/notes/mes-notes
**Description**: Récupérer les notes de l'étudiant connecté
**Roles**: ETUDIANT
```json
// Response
[
  {
    "id": 1,
    "valeur": 12.5,
    "matiereNom": "Mathématiques",
    "matiereCode": "MATH101",
    "enseignantNom": "Benali",
    "enseignantPrenom": "Ahmed",
    "filiere": "Informatique",
    "niveau": "L3"
  }
]
```

---

## 📋 Réclamations Endpoints

### GET /api/reclamations
**Description**: Récupérer toutes les réclamations selon le rôle
**Roles**: ALL
```json
// Response
[
  {
    "id": 1,
    "description": "Je pense que ma note est incorrecte...",
    "statut": "SOUMISE", // SOUMISE, TRANSMISE_DA, IMPUTEE, ACCEPTEE, REFUSEE, APPLIQUEE, REJETEE
    "dateCreation": "2024-01-15T10:30:00",
    "dateDerniereAction": "2024-01-15T10:30:00",
    "commentaireScolarite": null,
    "commentaireEnseignant": null,
    "etudiantId": 1,
    "etudiantNom": "Dupont",
    "etudiantPrenom": "Jean",
    "etudiantEmail": "jean.dupont@ibam.ma",
    "noteId": 1,
    "noteValeur": 12.5,
    "matiereNom": "Mathématiques",
    "matiereCode": "MATH101",
    "enseignantImputeId": null,
    "enseignantImputeNom": null,
    "enseignantImputePrenom": null,
    "justificatifNom": "justificatif.pdf",
    "hasJustificatif": true,
    "nouvelleNoteProposee": null
  }
]
```

### GET /api/reclamations/{id}
**Description**: Récupérer une réclamation par ID
**Roles**: ALL (selon accès)
```json
// Response: même structure que ci-dessus
```

### POST /api/reclamations
**Description**: Créer une nouvelle réclamation
**Roles**: ETUDIANT
**Content-Type**: multipart/form-data
```javascript
// FormData
const formData = new FormData();
formData.append('noteId', '1');
formData.append('description', 'Ma réclamation...');
formData.append('justificatif', file); // PDF, JPEG, PNG max 5MB

// Response: DemandeResponse object
```

### GET /api/reclamations/{id}/justificatif
**Description**: Télécharger le justificatif
**Roles**: ALL (selon accès)
```javascript
// Response: Binary file with headers
// Content-Type: application/pdf | image/jpeg | image/png
// Content-Disposition: attachment; filename="justificatif.pdf"
```

---

## 🔄 Actions sur Réclamations

### PUT /api/reclamations/{id}/verifier
**Description**: Vérifier la recevabilité (Scolarité)
**Roles**: SCOLARITE, DA
```javascript
// Query params
?recevable=true&commentaire=Recevable

// Response
"Recevabilité vérifiée"
```

### PUT /api/reclamations/{id}/imputer
**Description**: Imputer à un enseignant (DA)
**Roles**: DA
```javascript
// Query params
?enseignantId=3

// Response
"Demande imputée"
```

### PUT /api/reclamations/{id}/imputer-auto
**Description**: Imputation automatique (DA)
**Roles**: DA
```javascript
// Response
"Demande imputée automatiquement"
```

### PUT /api/reclamations/{id}/analyser
**Description**: Analyser la réclamation (Enseignant)
**Roles**: ENSEIGNANT, DA
```javascript
// Query params
?acceptee=true&commentaire=Analyse...&nouvelleNoteProposee=15.5

// Response
"Demande analysée"
```

### PUT /api/reclamations/{id}/appliquer
**Description**: Appliquer la décision (Scolarité)
**Roles**: SCOLARITE, DA
```javascript
// Query params (optionnel)
?nouvelleNote=15.5

// Response
"Décision appliquée"
```

---

## 👥 Ressources

### GET /api/reclamations/enseignants
**Description**: Liste des enseignants (pour imputation)
**Roles**: DA
```json
// Response
[
  {
    "id": 3,
    "nom": "Benali",
    "prenom": "Ahmed",
    "email": "ahmed.benali@ibam.ma"
  }
]
```

---

## 🎭 Rôles et Permissions

### ROLE_ETUDIANT
- Voir ses réclamations
- Créer des réclamations
- Voir ses notes

### ROLE_ENSEIGNANT
- Voir les réclamations qui lui sont imputées
- Analyser les réclamations (accepter/refuser)

### ROLE_SCOLARITE
- Voir toutes les réclamations
- Vérifier la recevabilité
- Appliquer les décisions

### ROLE_DA
- Voir toutes les réclamations
- Imputer aux enseignants
- Toutes les actions (supervision)

---

## 📊 Workflow des Statuts

```
SOUMISE → (vérification) → TRANSMISE_DA → (imputation) → IMPUTEE 
                      ↓                                      ↓
                   REJETEE                    (analyse) → ACCEPTEE/REFUSEE
                                                           ↓
                                              (application) → APPLIQUEE
```

---

## 🔧 Comptes de Test

```javascript
// Étudiant
{ email: "jean.dupont@ibam.ma", password: "password123" }

// Enseignant  
{ email: "ahmed.benali@ibam.ma", password: "password123" }

// Scolarité
{ email: "omar.tazi@ibam.ma", password: "password123" }

// DA
{ email: "rachid.bennani@ibam.ma", password: "password123" }
```

---

## 🎨 Frontend Requirements

### Technologies Recommandées
- **Framework**: React/Vue/Angular
- **HTTP Client**: Axios/Fetch
- **Routing**: React Router / Vue Router
- **State**: Context API / Vuex / NgRx
- **UI**: Tailwind CSS / Material UI / Ant Design

### Fonctionnalités Clés
1. **Dashboard par rôle** avec statistiques
2. **Gestion des réclamations** (CRUD + actions)
3. **Upload de fichiers** (justificatifs)
4. **Notifications** temps réel
5. **Interface responsive**
6. **Authentification JWT**

### UX/UI Suggestions
- **Couleurs par statut** (bleu=soumise, orange=en cours, vert=acceptée, rouge=refusée)
- **Icônes expressives** pour chaque statut
- **Métriques visuelles** (graphiques, pourcentages)
- **Workflow visuel** pour suivre l'avancement
- **Filtres et recherche** pour les listes
- **Modales** pour les détails et actions