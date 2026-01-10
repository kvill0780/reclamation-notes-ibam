package com.ibam.reclamation.entity;


public enum Filiere {
    ADB("Assistanat de Direction Bilingue"),
    ABF("Assurance Banque Finance"),
    CCA("Comptabilité Contrôle Audit"),
    MID("Marketing et Innovation Digitale"),
    MIAGE("Méthode Informatique Appliquée à la Gestion");
    
    private final String libelle;
    
    Filiere(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}