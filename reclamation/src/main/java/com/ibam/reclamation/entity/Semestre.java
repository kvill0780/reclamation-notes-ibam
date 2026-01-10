package com.ibam.reclamation.entity;

public enum Semestre {
    S1("Semestre 1"),
    S2("Semestre 2"),
    S3("Semestre 3"),
    S4("Semestre 4"),
    S5("Semestre 5"),
    S6("Semestre 6");
    
    private final String libelle;
    
    Semestre(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}