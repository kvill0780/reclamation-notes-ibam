package com.ibam.reclamation.dto;

import com.ibam.reclamation.entity.User;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour les enseignants (utilisé dans l'imputation DA)
 */
@Data
@Builder
public class EnseignantResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;

    public static EnseignantResponse fromEntity(User user) {
        return EnseignantResponse.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .build();
    }
}
