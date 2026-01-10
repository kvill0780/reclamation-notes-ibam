# Configuration du projet

## Fichiers à créer localement

1. **Copiez** `application-example.yml` vers `application.yml`
2. **Copiez** `data-example.sql` vers `data.sql`
3. **Modifiez** les valeurs dans ces fichiers :

### application.yml
```yaml
spring:
  datasource:
    username: votre_username_db
    password: votre_password_db

jwt:
  secret: votre_cle_jwt_secrete_32_chars_minimum
```

### data.sql
- Remplacez `$2a$10$EXAMPLE_HASH_HERE` par des hash BCrypt réels
- Utilisez le générateur : `HashGen.java` dans le projet