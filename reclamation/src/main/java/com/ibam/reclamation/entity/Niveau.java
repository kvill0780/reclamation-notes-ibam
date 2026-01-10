package com.ibam.reclamation.entity;

public enum Niveau {
    L1("Licence 1"),
    L2("Licence 2"),
    L3("Licence 3");
    
    private final String libelle;
    
    Niveau(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}