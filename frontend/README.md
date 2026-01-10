# Frontend - Logiciel de Réclamations IBAM

Application React pour la gestion des réclamations de notes à l'IBAM.

## 🚀 Installation

1. Installer les dépendances :
```bash
npm install
```

2. Démarrer le serveur de développement :
```bash
npm run dev
```

L'application sera accessible sur `http://localhost:3000`

## 📋 Fonctionnalités

### Rôles disponibles

- **Étudiant (ROLE_ETUDIANT)**
  - Consulter ses réclamations
  - Créer une nouvelle réclamation

- **Enseignant (ROLE_ENSEIGNANT)**
  - Consulter les réclamations imputées
  - Analyser une réclamation (accepter/refuser)

- **Scolarité (ROLE_SCOLARITE)**
  - Consulter toutes les réclamations
  - Vérifier la recevabilité
  - Appliquer les décisions

- **Directeur Académique (ROLE_DA)**
  - Consulter toutes les réclamations
  - Imputer une réclamation à un enseignant
  - Imputation automatique

## 🔧 Configuration

L'URL de l'API est configurée dans `src/services/api.js` :
```javascript
const API_BASE_URL = 'http://localhost:8080'
```

Modifiez cette valeur si votre API backend est sur un autre port ou domaine.

## 📝 Notes importantes

- L'authentification utilise des tokens JWT stockés dans le localStorage
- L'ID utilisateur doit être récupéré depuis la réponse de l'API de login (actuellement, un placeholder est utilisé)
- Assurez-vous que le backend API est démarré sur le port 8080 avant d'utiliser l'application

## 🛠️ Scripts disponibles

- `npm run dev` - Démarrer le serveur de développement
- `npm run build` - Construire l'application pour la production
- `npm run preview` - Prévisualiser la build de production
- `npm run lint` - Lancer le linter ESLint


