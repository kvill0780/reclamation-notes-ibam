package com.ibam.reclamation.controller;

import com.ibam.reclamation.entity.PeriodeReclamation;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.repository.PeriodeReclamationRepository;
import com.ibam.reclamation.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/periodes")
@PreAuthorize("isAuthenticated()")
public class PeriodeReclamationController {

    private final PeriodeReclamationRepository periodeRepository;
    private final AuthenticationService authenticationService;

    public PeriodeReclamationController(PeriodeReclamationRepository periodeRepository, 
                                      AuthenticationService authenticationService) {
        this.periodeRepository = periodeRepository;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getPeriodeActive() {
        LocalDateTime maintenant = LocalDateTime.now();
        return periodeRepository.findPeriodeActive(maintenant)
                .map(periode -> {
                    Map<String, Object> response = Map.of(
                        "active", true,
                        "nom", periode.getNom(),
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
        return ResponseEntity.ok(periodeRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<PeriodeReclamation> creerPeriode(@RequestBody Map<String, Object> request) {
        User da = authenticationService.getCurrentUser();
        
        PeriodeReclamation periode = new PeriodeReclamation();
        periode.setNom((String) request.get("nom"));
        periode.setDescription((String) request.get("description"));
        periode.setDateDebut(LocalDateTime.parse((String) request.get("dateDebut")));
        periode.setDateFin(LocalDateTime.parse((String) request.get("dateFin")));
        periode.setCreateur(da);
        
        return ResponseEntity.ok(periodeRepository.save(periode));
    }

    @PutMapping("/{id}/fermer")
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<String> fermerPeriode(@PathVariable Long id) {
        return periodeRepository.findById(id)
                .map(periode -> {
                    periode.setActive(false);
                    periodeRepository.save(periode);
                    return ResponseEntity.ok("Période fermée");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}