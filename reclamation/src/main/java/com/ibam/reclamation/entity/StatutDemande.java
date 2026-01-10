package com.ibam.reclamation.entity;

/**
 * États d'une demande de réclamation selon le diagramme d'état UML
 * Cycle de vie : SOUMISE → EN_VERIFICATION → REJETEE/TRANSMISE_DA → IMPUTEE → EN_ANALYSE → ACCEPTEE/REFUSEE → APPLIQUEE
 */
public enum StatutDemande {
    SOUMISE,
    EN_VERIFICATION,
    REJETEE,
    TRANSMISE_DA,
    IMPUTEE,
    EN_ANALYSE,
    ACCEPTEE,
    REFUSEE,
    APPLIQUEE
}