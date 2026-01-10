package com.ibam.reclamation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "periodes_reclamation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodeReclamation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom; // Ex: "Réclamations Semestre 1 - 2024"
    
    @Column(nullable = false)
    private LocalDateTime dateDebut;
    
    @Column(nullable = false)
    private LocalDateTime dateFin;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @ManyToOne
    @JoinColumn(name = "createur_id", nullable = false)
    private User createur; // Le DA qui a créé la période
    
    @Column
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    /**
     * Vérifie si la période est actuellement ouverte
     */
    public boolean isOuverte() {
        LocalDateTime maintenant = LocalDateTime.now();
        return active && 
               maintenant.isAfter(dateDebut) && 
               maintenant.isBefore(dateFin);
    }
    
    /**
     * Calcule le temps restant en heures
     */
    public long getHeuresRestantes() {
        if (!isOuverte()) return 0;
        return java.time.Duration.between(LocalDateTime.now(), dateFin).toHours();
    }
}