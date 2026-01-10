package com.ibam.reclamation.service;

import org.springframework.stereotype.Service;
import com.ibam.reclamation.repository.NoteRepository;
import com.ibam.reclamation.entity.Note;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void modifierNote(Long noteId, Double nouvelleValeur) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note introuvable"));

        note.modifierValeur(nouvelleValeur);
        noteRepository.save(note);
    }

    public List<Note> consulterNotesParEtudiant(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }

    public List<Note> consulterNotesParEtudiantEtNiveau(Long etudiantId, String niveau) {
        return noteRepository.findByEtudiantIdAndEnseignementNiveau(etudiantId, 
            com.ibam.reclamation.entity.Niveau.valueOf(niveau));
    }
}