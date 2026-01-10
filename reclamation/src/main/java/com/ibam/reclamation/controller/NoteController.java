package com.ibam.reclamation.controller;

import com.ibam.reclamation.dto.NoteResponse;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.repository.NoteRepository;
import com.ibam.reclamation.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour les notes
 */
@RestController
@RequestMapping("/api/notes")
@PreAuthorize("isAuthenticated()")
public class NoteController {

    private final NoteRepository noteRepository;
    private final AuthenticationService authenticationService;

    public NoteController(NoteRepository noteRepository, AuthenticationService authenticationService) {
        this.noteRepository = noteRepository;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/mes-notes")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<NoteResponse>> getMesNotes(@RequestParam(required = false) String niveau) {
        User etudiant = authenticationService.getCurrentUser();
        List<NoteResponse> notes;
        
        if (niveau != null) {
            notes = noteRepository.findByEtudiantIdAndEnseignementNiveau(
                etudiant.getId(), 
                com.ibam.reclamation.entity.Niveau.valueOf(niveau)
            ).stream().map(NoteResponse::fromEntity).toList();
        } else {
            notes = noteRepository.findByEtudiantId(etudiant.getId())
                .stream().map(NoteResponse::fromEntity).toList();
        }
        
        return ResponseEntity.ok(notes);
    }
}
