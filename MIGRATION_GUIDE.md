# Guide de Migration - API Unifiée

## 🔄 Changements Backend

### API Endpoints - AVANT vs APRÈS

**AVANT (Multiple contrôleurs):**
```
GET  /api/etudiant/reclamations
POST /api/etudiant/reclamation
GET  /api/scolarite/reclamations
PUT  /api/scolarite/reclamation/{id}/verifier
GET  /api/da/reclamations
PUT  /api/da/reclamation/{id}/imputer
GET  /api/enseignant/reclamations
PUT  /api/enseignant/reclamation/{id}/analyser
```

**APRÈS (API unifiée):**
```
GET  /api/reclamations                    # Filtré par rôle automatiquement
GET  /api/reclamations/{id}              # Accès contrôlé par rôle
POST /api/reclamations                   # Création (ETUDIANT)
PUT  /api/reclamations/{id}/verifier     # Scolarité/DA
PUT  /api/reclamations/{id}/imputer      # DA
PUT  /api/reclamations/{id}/imputer-auto # DA
PUT  /api/reclamations/{id}/analyser     # Enseignant/DA
PUT  /api/reclamations/{id}/appliquer    # Scolarité/DA
GET  /api/reclamations/enseignants       # DA
```

### Validations Ajoutées

**Entité DemandeReclamation:**
- `description`: `@Size(max = 1000)`
- `commentaireScolarite`: `@Size(max = 500)`
- `commentaireEnseignant`: `@Size(max = 500)`
- Validation obligatoire des commentaires dans les méthodes métier

**Entité Note:**
- Nouvelle méthode `modifierValeur(Double)` avec validation (0-20)
- Remplacement de `setValeur()` direct

### Exceptions Standardisées

**Nouvelles exceptions métier:**
- `NoteNonAutoriseException` (remplace `RuntimeException`)
- `CommentaireObligatoireException`
- Toutes héritent de `BusinessException`

## 🎨 Changements Frontend

### API Client Simplifié

**AVANT:**
```javascript
import { daApi, scolariteApi, enseignantApi } from './api';
await daApi.getReclamations();
await scolariteApi.verifier(id, recevable, commentaire);
```

**APRÈS:**
```javascript
import { reclamationApi } from './api';
await reclamationApi.getAll();           // Filtré automatiquement
await reclamationApi.verifier(id, recevable, commentaire);
```

### Validations Côté Client

**Ajoutées:**
- Description: max 1000 caractères avec compteur
- Commentaires: obligatoires avec max 500 caractères
- Note: validation 0-20 pour les demandes acceptées
- Feedback visuel en temps réel

### Interface Améliorée

**Champs obligatoires:**
- Marqués avec `*`
- Validation en temps réel
- Boutons désactivés si validation échoue
- Compteurs de caractères

## 🚀 Bénéfices

1. **API Unifiée**: Un seul endpoint, logique centralisée
2. **Sécurité Renforcée**: Validations métier strictes
3. **UX Améliorée**: Feedback immédiat, validation claire
4. **Maintenance**: Code simplifié, moins de duplication
5. **Cohérence**: Exceptions et validations standardisées

## 📋 Migration Checklist

- [x] Unifier les contrôleurs backend
- [x] Ajouter validations métier
- [x] Standardiser les exceptions
- [x] Déléguer modification de note
- [x] Adapter API frontend
- [x] Améliorer validations client
- [x] Tester compilation backend/frontend
- [x] Documenter les changements

## 🔧 Tests Recommandés

1. **Backend**: Tester les nouvelles validations
2. **Frontend**: Vérifier les limites de caractères
3. **Intégration**: Tester le workflow complet
4. **Sécurité**: Vérifier les autorisations par rôle