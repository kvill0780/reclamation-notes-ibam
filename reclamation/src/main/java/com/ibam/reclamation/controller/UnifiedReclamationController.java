package com.ibam.reclamation.controller;

import com.ibam.reclamation.dto.CreateDemandeRequest;
import com.ibam.reclamation.dto.CreateDemandeMultipartRequest;
import com.ibam.reclamation.dto.DemandeResponse;
import com.ibam.reclamation.dto.EnseignantResponse;
import com.ibam.reclamation.entity.DemandeReclamation;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.repository.PeriodeReclamationRepository;
import com.ibam.reclamation.repository.UserRepository;
import com.ibam.reclamation.security.RoleEnum;
import com.ibam.reclamation.service.AuthenticationService;
import com.ibam.reclamation.service.DemandeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Contrôleur unifié pour toutes les opérations de réclamation
 * Remplace les contrôleurs spécialisés par rôle
 */
@RestController
@RequestMapping("/api/reclamations")
@PreAuthorize("isAuthenticated()")
public class UnifiedReclamationController {

    private final DemandeService demandeService;
    private final AuthenticationService authenticationService;
    private final PeriodeReclamationRepository periodeReclamationRepository;
    private final UserRepository userRepository;

    public UnifiedReclamationController(DemandeService demandeService, 
                                      AuthenticationService authenticationService,
                                      UserRepository userRepository,
                                      PeriodeReclamationRepository periodeReclamationRepository) {
        this.demandeService = demandeService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.periodeReclamationRepository = periodeReclamationRepository;
    }

    @GetMapping
    public ResponseEntity<List<DemandeResponse>> getReclamations() {
        User user = authenticationService.getCurrentUser();
        List<DemandeResponse> demandes = demandeService.getDemandesPourUtilisateur(user)
                .stream()
                .map(DemandeResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeResponse> getReclamation(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        DemandeReclamation demande = demandeService.getDemandeAccessiblePourUtilisateur(id, user);
        return ResponseEntity.ok(DemandeResponse.fromEntity(demande));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<DemandeResponse> createReclamation(
            @RequestParam Long noteId,
            @RequestParam String description,
            @RequestParam("justificatif") MultipartFile justificatif) throws IOException {
        
        // Vérifier si une période de réclamation est active
        if (!periodeReclamationRepository.existePeriodeActive(LocalDateTime.now())) {
            throw new IllegalStateException("Aucune période de réclamation n'est actuellement ouverte");
        }
        
        // Validation du fichier
        if (justificatif.isEmpty()) {
            throw new IllegalArgumentException("Le justificatif est obligatoire");
        }
        
        String contentType = justificatif.getContentType();
        List<String> allowedTypes = Arrays.asList("application/pdf", "image/jpeg", "image/png", "image/jpg");
        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Format de fichier non autorisé. Formats acceptés: PDF, JPEG, PNG");
        }
        
        if (justificatif.getSize() > 5 * 1024 * 1024) { // 5MB max
            throw new IllegalArgumentException("Le fichier ne peut pas dépasser 5MB");
        }
        
        User etudiant = authenticationService.getCurrentUser();
        DemandeReclamation demande = demandeService.soumettre(
                etudiant, 
                noteId, 
                description,
                justificatif.getOriginalFilename(),
                justificatif.getContentType(),
                justificatif.getBytes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(DemandeResponse.fromEntity(demande));
    }

    @PutMapping("/{id}/verifier")
    @PreAuthorize("hasAnyRole('SCOLARITE', 'DA')")
    public ResponseEntity<String> verifierRecevabilite(@PathVariable Long id,
                                                     @RequestParam boolean recevable,
                                                     @RequestParam String commentaire) {
        demandeService.verifierRecevabilite(id, recevable, commentaire);
        return ResponseEntity.ok("Recevabilité vérifiée");
    }

    @PutMapping("/{id}/imputer")
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<String> imputerReclamation(@PathVariable Long id,
                                                   @RequestParam Long enseignantId) {
        demandeService.imputerDemande(id, enseignantId);
        return ResponseEntity.ok("Demande imputée");
    }

    @PutMapping("/{id}/imputer-auto")
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<String> imputerReclamationAuto(@PathVariable Long id) {
        demandeService.imputerDemandeAutomatiquement(id);
        return ResponseEntity.ok("Demande imputée automatiquement");
    }

    @PutMapping("/imputer-lot")
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<String> imputerReclamationsLot(@RequestBody List<Long> demandeIds) {
        for (Long id : demandeIds) {
            demandeService.imputerDemandeAutomatiquement(id);
        }
        return ResponseEntity.ok(demandeIds.size() + " demandes imputées automatiquement");
    }

    @PutMapping("/{id}/analyser")
    @PreAuthorize("hasAnyRole('ENSEIGNANT', 'DA')")
    public ResponseEntity<String> analyserReclamation(@PathVariable Long id,
                                                    @RequestParam boolean acceptee,
                                                    @RequestParam String commentaire,
                                                    @RequestParam(required = false) Double nouvelleNoteProposee) {
        demandeService.analyserDemande(id, acceptee, commentaire, nouvelleNoteProposee);
        return ResponseEntity.ok("Demande analysée");
    }

    @PutMapping("/{id}/appliquer")
    @PreAuthorize("hasAnyRole('SCOLARITE', 'DA', 'ENSEIGNANT')")
    public ResponseEntity<String> appliquerDecision(@PathVariable Long id) {
        demandeService.appliquerDecision(id, null);
        return ResponseEntity.ok("Décision appliquée");
    }

    @GetMapping("/enseignants")
    @PreAuthorize("hasRole('DA')")
    public ResponseEntity<List<EnseignantResponse>> getEnseignants() {
        List<EnseignantResponse> enseignants = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == RoleEnum.ROLE_ENSEIGNANT)
                .map(EnseignantResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(enseignants);
    }

    @GetMapping("/{id}/justificatif")
    public ResponseEntity<byte[]> getJustificatif(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        DemandeReclamation demande = demandeService.getDemandeAccessiblePourUtilisateur(id, user);
        
        if (demande.getJustificatifData() == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .header("Content-Type", demande.getJustificatifType())
                .header("Content-Disposition", "attachment; filename=\"" + demande.getJustificatifNom() + "\"")
                .body(demande.getJustificatifData());
    }
}