package com.ibam.reclamation.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

/**
 * Entité Note - Objet central des réclamations
 * Relations : Étudiant (User) et Enseignement
 * Une note appartient à un enseignement précis
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notes")
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @DecimalMin(value = "0.0", message = "La note doit être supérieure ou égale à 0")
    @DecimalMax(value = "20.0", message = "La note doit être inférieure ou égale à 20")
    @Column(nullable = false)
    private Double valeur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private User etudiant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignement_id", nullable = false)
    private Enseignement enseignement;
    
    public void modifierValeur(Double nouvelleValeur) {
        if (nouvelleValeur == null) {
            throw new IllegalArgumentException("La nouvelle note ne peut pas être null");
        }
        if (nouvelleValeur < 0.0 || nouvelleValeur > 20.0) {
            throw new IllegalArgumentException("La note doit être entre 0 et 20");
        }
        this.valeur = nouvelleValeur;
    }
    
    // Méthode utilitaire pour accéder à l'enseignant responsable
    public User getEnseignantResponsable() {
        return enseignement.getEnseignant();
    }
    
    // Méthode utilitaire pour accéder à la matière
    public Matiere getMatiere() {
        return enseignement.getMatiere();
    }
}