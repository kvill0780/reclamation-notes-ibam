package com.ibam.reclamation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité Matiere - Indépendante des enseignants
 * Les enseignants sont liés via Enseignement
 */
@Entity
@Table(name = "matieres")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Matiere {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column
    private String description;
}