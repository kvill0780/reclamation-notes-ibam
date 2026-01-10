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

## 🚀 Technologies

**Backend :**
- Spring Boot 3.2.0 (Java 21)
- PostgreSQL
- Spring Security + JWT
- Maven

**Frontend :**
- React 18 + Vite
- Axios
- CSS Vanilla

## 📋 Fonctionnalités

- **Étudiants** : Consultation notes, soumission réclamations
- **Scolarité** : Vérification recevabilité, validation
- **DA** : Gestion périodes, imputation enseignants
- **Enseignants** : Analyse réclamations, proposition notes

## 🛠️ Installation

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

## 📊 Workflow

SOUMISE → TRANSMISE_DA → IMPUTEE → ACCEPTEE/REFUSEE → APPLIQUEE/REJETEE

## 👥 Comptes de test

- **Étudiant** : jean.dupont@ibam.ma
- **Enseignant** : ahmed.benali@ibam.ma  
- **Scolarité** : omar.tazi@ibam.ma
- **DA** : rachid.bennani@ibam.ma

*Mot de passe* : `password123`

## 📄 Licence

Projet académique - IBAM 2025