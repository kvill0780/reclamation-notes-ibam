package com.ibam.reclamation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreatePeriodeRequest {

    @NotBlank(message = "Le nom de la période est obligatoire")
    @Size(max = 150, message = "Le nom de la période ne doit pas dépasser 150 caractères")
    private String nom;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateFin;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;
}
