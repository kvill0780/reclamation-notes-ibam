# Système de Réclamation de Notes - IBAM

Application web de gestion des réclamations de notes post-délibération.

## Structure du projet

- `reclamation/` : backend Spring Boot (API REST + sécurité JWT)
- `frontend/` : interface React (Vite)
- `tests/` : tests Selenium et scripts de charge/performance

## Pré-requis

- Java 21
- Node.js 18+ et npm
- PostgreSQL 14+

Vérification rapide:

```bash
java -version
node -v
npm -v
psql --version
```

## Configuration backend

1. Aller dans le dossier des ressources:

```bash
cd reclamation/src/main/resources
```

2. Si besoin, copier l’exemple:

```bash
cp application-example.yml application.yml
```

3. Modifier `application.yml`:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `jwt.secret` (minimum 32 caractères)

## Création de la base PostgreSQL

Créer la base attendue par défaut (`reclamations_db`):

```bash
sudo -u postgres psql
```

Puis dans `psql`:

```sql
CREATE DATABASE reclamations_db;
CREATE USER kvill WITH PASSWORD 'postgre';
GRANT ALL PRIVILEGES ON DATABASE reclamations_db TO kvill;
\q
```

Si vous utilisez d’autres identifiants, adaptez `application.yml`.

## Données de démarrage

- `data.sql` est chargé automatiquement au démarrage (`spring.sql.init.mode=always`)
- `data-example.sql` est un jeu d’exemple minimal

## Lancement

### 1) Backend

```bash
cd reclamation
./mvnw spring-boot:run
```

Backend API: `http://localhost:8080`

### 2) Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend: `http://localhost:3000`

## Workflow métier (post-délibération)

`SOUMISE -> TRANSMISE_DA -> IMPUTEE -> ACCEPTEE/REFUSEE -> APPLIQUEE/REJETEE`

1. Étudiant
- Sélectionne une note publiée
- Saisit la description + la note attendue (0-20)
- Joint un justificatif (obligatoire)

2. Scolarité
- Vérifie la recevabilité
- Reçoit la demande ou la rejette avec commentaire

3. DA
- Impute la demande à un enseignant (manuel/auto)
- Gère les périodes de réclamation

4. Enseignant
- Analyse une demande imputée
- Accepte/refuse avec commentaire
- N’entre pas de nouvelle note

## Règles importantes

- Une seule réclamation par couple `(étudiant, note)`
- `noteAttendue` obligatoire, comprise entre `0` et `20`
- Justificatif obligatoire à la soumission
- Commentaire obligatoire lors de l’analyse enseignant

## Comptes de test (mot de passe: `password123`)

- Étudiant: `jean.dupont@ibam.ma`
- Étudiant: `marie.martin@ibam.ma`
- Enseignant: `yaya.traore@ibam.ma`
- Scolarité: `omar.tazi@ibam.ma`
- DA: `rachid.bennani@ibam.ma`

## 🚀 Déploiement

### Déploiement Local (Docker Compose)

```bash
docker-compose up -d
# Application sur http://localhost:3000
# API sur http://localhost:8080
```

### Déploiement DigitalOcean

Voir [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) pour les instructions complètes.

Résumé:
1. Configurer GitHub Secrets (`DIGITALOCEAN_ACCESS_TOKEN`)
2. Push sur `main` → CI/CD automatique
3. DigitalOcean déploie backend + frontend + BD PostgreSQL

Caractéristiques:
- ✅ HTTPS automatique
- ✅ Scaling automatique
- ✅ Monitoring intégré
- ✅ Backups automatiques (BD)

## 🧪 Tests

Voir [TESTING_GUIDE.md](TESTING_GUIDE.md) pour les instructions complètes.

### Tests Locaux Rapides

```bash
# Tests unitaires backend
cd reclamation && mvn test

# Tests fonctionnels Selenium
pip install selenium requests pytest
cd tests && python -m pytest exercice1_test_fonctionnel.py -v

# Tests charge
python exercice2_test_charge.py

# Tests performance
python exercice3_test_performance.py
```

### Vérification Santé

```bash
# Backend health check
curl http://localhost:8080/actuator/health

# API Documentation
open http://localhost:8080/swagger-ui.html
```

## 📁 Structure des Fichiers Importants

```
.
├── .app.yaml                  # Configuration DigitalOcean
├── docker-compose.yml         # Orchestration locale
├── Dockerfile.backend         # Image backend
├── Dockerfile.frontend        # Image frontend
├── nginx.conf                 # Configuration Nginx
├── .env.example              # Variables d'environnement
├── DEPLOYMENT_GUIDE.md       # Guide complet de déploiement
├── TESTING_GUIDE.md          # Guide complet de test
├── reclamation/              # Backend Spring Boot
├── frontend/                 # Frontend React
└── tests/                    # Tests automatisés
```

## Dépannage rapide

- **Erreur DB connexion**: Vérifier que PostgreSQL tourne et que `application.yml` contient les bons identifiants
- **Port 8080 occupé**: Changer `server.port` côté backend ou libérer le port
- **Port 3000 occupé**: Lancer `npm run dev -- --port 3001`
- **Dépendances manquantes**: Exécuter `mvn install` et `npm install`
- **Tests Selenium échouent**: Vérifier que l'application tourne sur `http://localhost:3000`
