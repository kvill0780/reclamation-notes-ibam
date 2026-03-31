# 🧪 Guide de Test Local

## ✅ Configuration Locale

### 1. Prérequis

```bash
# Vérifier les versions
java -version      # Java 21+
node -v            # Node 18+
psql --version     # PostgreSQL 14+
docker --version   # Pour docker-compose
```

### 2. Setup Base de Données

```bash
# Créer la base
createdb reclamations_db -U kvill

# Charger les données
psql -U kvill -d reclamations_db < reclamation/src/main/resources/data.sql

# Vérifier
psql -U kvill -d reclamations_db -c "SELECT COUNT(*) FROM utilisateur;"
```

### 3. Configuration Application

```bash
# Copier la config d'exemple
cp reclamation/src/main/resources/application-example.yml \
   reclamation/src/main/resources/application.yml

# Vérifier les paramètres :
# - spring.datasource.url
# - spring.datasource.username
# - spring.datasource.password
# - jwt.secret
```

## 🚀 Lancement Local

### Option A : Sans Docker

```bash
# Terminal 1 - Backend
cd reclamation
mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend
npm install
npm run dev
```

Application accessible : http://localhost:3000

### Option B : Avec Docker Compose

```bash
# À la racine du projet
docker-compose up -d

# Arrêter
docker-compose down

# Logs
docker-compose logs -f api-backend
docker-compose logs -f frontend
```

## 🧪 Tests

### Tests Unitaires (Backend)

```bash
cd reclamation
mvn test
```

### Tests Fonctionnels Selenium (Frontend)

```bash
# Installer dépendances
pip install -r tests/requirements.txt

# Lancer les tests
cd tests
python -m pytest exercice1_test_fonctionnel.py -v
```

### Tests Charge

```bash
cd tests
python exercice2_test_charge.py
```

### Tests Performance

```bash
cd tests
python exercice3_test_performance.py
```

## 📊 Vérification Santé

```bash
# Backend health check
curl http://localhost:8080/actuator/health

# Frontend est opérationnel
curl http://localhost:3000

# Swagger API
open http://localhost:8080/swagger-ui.html
```

## 🔍 Debugging

### Logs Backend

```bash
# Activer DEBUG logging
tail -f ~/.m2/logs/spring-boot.log
```

### Logs Frontend

```bash
# Voir la console du navigateur (F12)
# Ou les logs du serveur de dev
```

## 🧹 Cleanup

```bash
# Arrêter tous les services
docker-compose down -v

# Nettoyer Maven
cd reclamation && mvn clean

# Nettoyer Node
cd frontend && rm -rf node_modules package-lock.json

# Réinitialiser la BD
dropdb reclamations_db
createdb reclamations_db -U kvill
psql -U kvill -d reclamations_db < reclamation/src/main/resources/data.sql
```

## 📝 Cas de Test Principaux

### Authentification
- Email: `joel.soulama@ibam.ma`
- Mot de passe: `password123`

### Comptes de Test
Consultez `reclamation/src/main/resources/data.sql` pour les autres utilisateurs de test.
