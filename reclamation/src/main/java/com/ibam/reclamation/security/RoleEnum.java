package com.ibam.reclamation.security;

/**
 * Énumération des rôles utilisateurs selon l'analyse UML
 * Respecte le principe de séparation des responsabilités
 */
public enum RoleEnum {
    ROLE_ETUDIANT,    // Étudiant - peut soumettre des réclamations
    ROLE_ENSEIGNANT,  // Enseignant - analyse les réclamations imputées
    ROLE_SCOLARITE,   // Scolarité - vérifie recevabilité et applique décisions
    ROLE_DA           // Directeur Académique - impute et supervise
}