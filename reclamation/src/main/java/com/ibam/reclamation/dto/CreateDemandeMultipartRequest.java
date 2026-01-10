package com.ibam.reclamation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDemandeMultipartRequest {
    @NotNull(message = "L'ID note est requis")
    private Long noteId;

    @NotBlank(message = "La description ne peut pas être vide")
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;
}