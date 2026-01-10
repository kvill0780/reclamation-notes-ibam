package com.ibam.reclamation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(
    name = "enseignements",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_enseignement_unique",
            columnNames = {"enseignant_id", "matiere_id", "filiere", "niveau", "semestre", "annee_academique"}
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enseignement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private User enseignant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Filiere filiere;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Niveau niveau;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semestre semestre;
    
    @Column(name = "annee_academique", nullable = false)
    private String anneeAcademique;
}