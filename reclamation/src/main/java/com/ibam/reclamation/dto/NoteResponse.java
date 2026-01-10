package com.ibam.reclamation.dto;

import com.ibam.reclamation.entity.Note;
import lombok.Builder;
import lombok.Data;

/**
 * DTO de réponse pour les notes
 */
@Data
@Builder
public class NoteResponse {
    private Long id;
    private Double valeur;
    private String matiereNom;
    private String matiereCode;
    private String enseignantNom;
    private String enseignantPrenom;
    private String filiere;
    private String niveau;
    private String semestre;

    public static NoteResponse fromEntity(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .valeur(note.getValeur())
                .matiereNom(note.getEnseignement().getMatiere().getNom())
                .matiereCode(note.getEnseignement().getMatiere().getCode())
                .enseignantNom(note.getEnseignement().getEnseignant().getNom())
                .enseignantPrenom(note.getEnseignement().getEnseignant().getPrenom())
                .filiere(note.getEnseignement().getFiliere().name())
                .niveau(note.getEnseignement().getNiveau().name())
                .semestre(note.getEnseignement().getSemestre().name())
                .build();
    }
}
