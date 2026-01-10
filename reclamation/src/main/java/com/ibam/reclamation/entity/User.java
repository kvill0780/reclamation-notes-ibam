package com.ibam.reclamation.entity;

import jakarta.persistence.*;
import com.ibam.reclamation.security.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité User représentant tous les acteurs du système
 * Correspond aux acteurs du diagramme de cas d'utilisation UML
 */
@Entity
@Table(name = "app_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String passwordHash;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private Niveau niveau; // Niveau de l'étudiant (null pour les autres rôles)

    @Enumerated(EnumType.STRING)
    @Column(name = "filiere")
    private Filiere filiere; // Filière de l'étudiant (null pour les autres rôles)

}