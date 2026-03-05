package com.ibam.reclamation.controller;

import com.ibam.reclamation.dto.CreatePeriodeRequest;
import com.ibam.reclamation.entity.PeriodeReclamation;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.service.AuthenticationService;
import com.ibam.reclamation.service.PeriodeReclamationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/periodes")
@PreAuthorize("isAuthenticated()")
public class PeriodeReclamationController {

    private final PeriodeReclamationService periodeService;
    private final AuthenticationService authenticationService;

    public PeriodeReclamationController(PeriodeReclamationService periodeService,
            AuthenticationService authenticationService) {
        this.periodeService = periodeService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getPeriodeActive() {
        return periodeService.getPeriodeActive()
                .map(periode -> {
                    Map<String, Object> response = Map.of(
                        "id", periode.getId(),
                        "active", true,
                        "nom", periode.getNom(),
                        "dateDebut", periode.getDateDebut(),
                        "dateFin", periode.getDateFin(),
                        "heuresRestantes", periode.getHeuresRestantes(),
                        "description", periode.getDescription() != null ? periode.getDescription() : ""
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.ok(Map.of("active", false)));
    }

    @GetMapping
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<List<PeriodeReclamation>> getAllPeriodes() {
        return ResponseEntity.ok(periodeService.getAllPeriodes());
    }

    @PostMapping
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<PeriodeReclamation> creerPeriode(@Valid @RequestBody CreatePeriodeRequest request) {
        User da = authenticationService.getCurrentUser();
        return ResponseEntity.ok(periodeService.creerPeriode(request, da));
    }

    @PutMapping("/{id}/fermer")
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<String> fermerPeriode(@PathVariable Long id) {
        periodeService.fermerPeriode(id);
        return ResponseEntity.ok("Période fermée");
    }
}
