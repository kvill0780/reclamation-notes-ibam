package com.ibam.reclamation.service;

import com.ibam.reclamation.dto.CreatePeriodeRequest;
import com.ibam.reclamation.entity.PeriodeReclamation;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.exception.BusinessException.PeriodeNotFoundException;
import com.ibam.reclamation.repository.PeriodeReclamationRepository;
import com.ibam.reclamation.security.RoleEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodeReclamationService {

    private static final long MAX_DURATION_HOURS = 72;

    private final PeriodeReclamationRepository periodeRepository;

    public PeriodeReclamationService(PeriodeReclamationRepository periodeRepository) {
        this.periodeRepository = periodeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<PeriodeReclamation> getPeriodeActive() {
        return periodeRepository.findPeriodeActive(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<PeriodeReclamation> getAllPeriodes() {
        return periodeRepository.findAllByOrderByDateDebutDesc();
    }

    @Transactional
    public PeriodeReclamation creerPeriode(CreatePeriodeRequest request, User createur) {
        validateCreateRequest(request, createur);

        if (periodeRepository.existsByActiveTrueAndDateDebutLessThanAndDateFinGreaterThan(
                request.getDateFin(), request.getDateDebut())) {
            throw new IllegalStateException("Une période active existe déjà sur ce créneau");
        }

        PeriodeReclamation periode = new PeriodeReclamation();
        periode.setNom(request.getNom().trim());
        periode.setDescription(normalizeDescription(request.getDescription()));
        periode.setDateDebut(request.getDateDebut());
        periode.setDateFin(request.getDateFin());
        periode.setCreateur(createur);
        periode.setActive(true);

        return periodeRepository.save(periode);
    }

    @Transactional
    public void fermerPeriode(Long id) {
        PeriodeReclamation periode = periodeRepository.findById(id)
                .orElseThrow(() -> new PeriodeNotFoundException("Période introuvable"));

        if (!periode.isActive()) {
            throw new IllegalStateException("Cette période est déjà fermée");
        }

        periode.setActive(false);
        periodeRepository.save(periode);
    }

    private void validateCreateRequest(CreatePeriodeRequest request, User createur) {
        if (createur == null) {
            throw new IllegalArgumentException("Créateur de période introuvable");
        }
        if (createur.getRole() != RoleEnum.ROLE_DA) {
            throw new IllegalArgumentException("Seul un DA peut créer une période");
        }
        if (request.getDateFin().isBefore(request.getDateDebut()) || request.getDateFin().isEqual(request.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }

        LocalDateTime now = LocalDateTime.now();
        if (request.getDateDebut().isBefore(now.minusMinutes(1))) {
            throw new IllegalArgumentException("La date de début doit être dans le futur");
        }

        long durationMinutes = Duration.between(request.getDateDebut(), request.getDateFin()).toMinutes();
        if (durationMinutes > MAX_DURATION_HOURS * 60) {
            throw new IllegalArgumentException("La durée maximale autorisée est de 72 heures");
        }
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }
        String trimmed = description.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
