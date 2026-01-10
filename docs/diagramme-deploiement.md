# Diagramme de Déploiement — Application Réclamation IBAM

## Architecture de Déploiement

```mermaid
graph TB
    subgraph Client["🖥️ Poste Client"]
        Browser["Navigateur Web<br/>Chrome / Firefox / Safari"]
    end

    subgraph Frontend["📦 Serveur Frontend"]
        direction TB
        Vite["Vite Dev Server<br/>:3000"]
        React["Application React<br/>SPA"]
        Vite --> React
    end

    subgraph Backend["📦 Serveur Backend"]
        direction TB
        Tomcat["Embedded Tomcat<br/>:8080"]
        Spring["Spring Boot 3.2<br/>Java 21"]
        JPA["Spring Data JPA<br/>Hibernate"]
        Security["Spring Security<br/>JWT"]
        Tomcat --> Spring
        Spring --> JPA
        Spring --> Security
    end

    subgraph Database["🗄️ Serveur Base de Données"]
        PostgreSQL["PostgreSQL 15<br/>:5432<br/>reclamations_db"]
    end

    Browser -->|"HTTP/HTTPS<br/>Port 3000"| Vite
    React -->|"REST API<br/>JSON + JWT<br/>Port 8080"| Tomcat
    JPA -->|"JDBC<br/>Port 5432"| PostgreSQL
```

---

## Composants Déployés

### 🖥️ Poste Client
| Composant | Technologie |
|-----------|-------------|
| Navigateur | Chrome, Firefox, Safari |
| Protocole | HTTP/HTTPS |

### 📦 Nœud Frontend (Node.js)
| Composant | Technologie | Port |
|-----------|-------------|------|
| Serveur de développement | Vite 5.4 | 3000 |
| Application | React 18 + React Router | - |
| HTTP Client | Axios | - |

### 📦 Nœud Backend (JVM)
| Composant | Technologie | Port |
|-----------|-------------|------|
| Serveur d'application | Embedded Tomcat | 8080 |
| Framework | Spring Boot 3.2 | - |
| ORM | Hibernate / JPA | - |
| Sécurité | Spring Security + JWT | - |
| Runtime | Java 21 (LTS) | - |

### 🗄️ Nœud Base de Données
| Composant | Technologie | Port |
|-----------|-------------|------|
| SGBD | PostgreSQL 15 | 5432 |
| Base | reclamations_db | - |

---

## Communication entre Nœuds

```mermaid
sequenceDiagram
    participant C as Client Browser
    participant F as Frontend :3000
    participant B as Backend :8080
    participant D as PostgreSQL :5432

    C->>F: GET / (HTML/JS/CSS)
    F-->>C: SPA React

    C->>B: POST /api/auth/login
    B->>D: SELECT user
    D-->>B: User data
    B-->>C: JWT Token

    C->>B: GET /api/reclamations<br/>Authorization: Bearer JWT
    B->>D: SELECT demandes
    D-->>B: Results
    B-->>C: JSON Response
```

---

## Déploiement Production (Recommandé)

```mermaid
graph TB
    subgraph Cloud["☁️ Cloud / VPS"]
        subgraph Nginx["Nginx Reverse Proxy"]
            LB["Load Balancer<br/>:80 / :443"]
        end

        subgraph Docker["Docker Compose"]
            FrontContainer["Frontend Container<br/>nginx:alpine<br/>Static files"]
            BackContainer["Backend Container<br/>openjdk:21-slim<br/>Spring Boot JAR"]
            DBContainer["Database Container<br/>postgres:15<br/>Volume persistant"]
        end
    end

    User["👤 Utilisateur"] -->|HTTPS| LB
    LB -->|/| FrontContainer
    LB -->|/api/*| BackContainer
    BackContainer --> DBContainer
```

---

## Artefacts de Déploiement

| Artefact | Type | Commande |
|----------|------|----------|
| `frontend/dist/` | Static files | `npm run build` |
| `reclamation-0.0.1-SNAPSHOT.jar` | Fat JAR | `./mvnw package` |
| `docker-compose.yml` | Stack complète | `docker-compose up` |
