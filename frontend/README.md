# Frontend - Réclamations IBAM

Interface React de gestion des réclamations de notes.

## Démarrage

```bash
npm install
npm run dev
```

Frontend: `http://localhost:3000`

## Configuration API

Le client HTTP est défini dans `src/services/api.js` avec:

- `baseURL: 'http://localhost:8080'`
- Intercepteur JWT (`Authorization: Bearer ...`)
- Redirection vers `/login` sur `401` hors endpoint de login

## Fonctionnalités par rôle

- Étudiant (`ROLE_ETUDIANT`)
- Consulter ses notes publiées
- Soumettre une réclamation (description, note attendue, justificatif)

- Enseignant (`ROLE_ENSEIGNANT`)
- Voir les réclamations imputées
- Analyser une réclamation (accepter/refuser avec commentaire)

- Scolarité (`ROLE_SCOLARITE`)
- Vérifier la recevabilité
- Appliquer les décisions

- DA (`ROLE_DA`)
- Imputer les réclamations
- Créer/fermer des périodes de réclamation
- Consulter l’historique des périodes

## UX et erreurs

- En cas d’échec de login (email inconnu ou mauvais mot de passe), l’écran ne recharge plus brutalement.
- Le message affiché est: `Identifiant ou mot de passe invalide`.
- Les erreurs API sont centralisées via `src/utils/errorHandler.js`.

## Scripts

- `npm run dev` : serveur de développement
- `npm run build` : build production
- `npm run preview` : prévisualisation build
- `npm run lint` : lint ESLint

## Pré-requis

Le backend Spring Boot doit être lancé sur `http://localhost:8080`.
