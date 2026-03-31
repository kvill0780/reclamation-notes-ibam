# 🚀 Guide de Déploiement DigitalOcean

## 📋 Pré-requis

- Compte DigitalOcean avec accès à App Platform
- Repository GitHub (kvill0780/reclamation-notes-ibam)
- Token d'accès DigitalOcean (GitHub Secrets)

## 1️⃣ Configuration GitHub Secrets

Allez sur : **GitHub → Settings → Secrets and variables → Actions**

Ajoutez le secret suivant :
- `DIGITALOCEAN_ACCESS_TOKEN` : Votre token DigitalOcean App Platform

## 2️⃣ Déploiement via DigitalOcean App Platform

### Option A : Déploiement via Interface Web

1. Allez sur [DigitalOcean Dashboard](https://cloud.digitalocean.com/apps)
2. Cliquez sur **Create App**
3. Sélectionnez **GitHub** comme source
4. Autorisez et sélectionnez `kvill0780/reclamation-notes-ibam`
5. Choisissez la branche `main`
6. Dans **Source directories**, ajoutez :
   - `/reclamation` pour le backend
   - `/frontend` pour le frontend

### Option B : Déploiement via Fichier .app.yaml

```bash
# Placez-vous à la racine du projet
doctl apps create --spec .app.yaml
```

## 3️⃣ Variables d'Environnement

Configurez sur DigitalOcean :

```yaml
# Backend
SPRING_DATASOURCE_URL: postgresql://user:pass@host/reclamations_db?sslmode=require
SPRING_DATASOURCE_USERNAME: <db_user>
SPRING_DATASOURCE_PASSWORD: <db_password>
JWT_SECRET: <your-secure-secret-min-32-chars>
SPRING_PROFILES_ACTIVE: prod

# Frontend
VITE_API_BASE_URL: https://<your-backend-url>/api
```

## 4️⃣ Build Configuration

### Backend (Spring Boot)
- **Build command**: `cd reclamation && mvn clean package -DskipTests -Pprod`
- **Run command**: `java -jar target/reclamation-0.0.1-SNAPSHOT.jar`
- **Port**: 8080

### Frontend (React + Vite)
- **Build command**: `cd frontend && npm install && npm run build`
- **Port**: 3000 (servi par Nginx)

## 5️⃣ Base de Données

DigitalOcean créera automatiquement une base PostgreSQL :

1. Créez une base PostgreSQL 14+ dans votre cluster
2. Notez les identifiants de connexion
3. Configurez les variables d'environnement
4. La base sera initialisée automatiquement avec `data.sql`

## 6️⃣ Domaines Personnalisés

1. Allez dans **App → Settings → Domains**
2. Ajoutez votre domaine (ex: reclamation.example.com)
3. Configurez les DNS chez votre registraire

## 7️⃣ CI/CD avec GitHub Actions

Le fichier `.github/workflows/deploy.yml` contient :

- ✅ Tests backend (Maven)
- ✅ Tests frontend (ESLint, Build)
- ✅ Déploiement automatique sur main
- ✅ Pull Request checks

## 8️⃣ Vérification du Déploiement

```bash
# Vérifier les logs
doctl apps logs <app-id>

# Voir l'état
doctl apps get <app-id>

# Redéployer
doctl apps update <app-id> --no-wait --force-build
```

## 9️⃣ Commandes Utiles

```bash
# Lister les apps
doctl apps list

# Supprimer une app
doctl apps delete <app-id>

# Voir les détails
doctl apps describe <app-id>

# Voir les logs d'une composante
doctl apps logs <app-id> --component=<component-name>
```

## 🔒 Points Importants

1. **JWT Secret** : Remplacez par une clé sécurisée (minimum 32 caractères)
2. **Credentials BD** : Gardez les secrets en variables d'environnement
3. **CORS** : Configuré pour la URL du frontend
4. **HTTPS** : Automatique sur DigitalOcean
5. **Backups** : Activez les backups automatiques pour la BD

## 🐛 Troubleshooting

### Application démarre mais ne répond pas
- Vérifiez les logs : `doctl apps logs <app-id>`
- Vérifiez les variables d'environnement
- Vérifiez la connexion à la base de données

### Erreur 502 Bad Gateway
- Backend ne démarre pas
- Vérifiez le port 8080
- Vérifiez les logs d'application

### Frontend ne se connecte pas à l'API
- Vérifiez `VITE_API_BASE_URL`
- Vérifiez les CORS dans le backend
- Vérifiez la URL du service backend

## 📞 Support

- [DigitalOcean App Platform Docs](https://docs.digitalocean.com/products/app-platform/)
- [DigitalOcean Databases](https://docs.digitalocean.com/products/databases/)
