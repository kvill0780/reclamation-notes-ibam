package com.ibam.reclamation.repository;

import com.ibam.reclamation.entity.Note;
import com.ibam.reclamation.entity.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    
    List<Note> findByEtudiantId(Long etudiantId);
    
    List<Note> findByEtudiantIdAndEnseignementNiveau(Long etudiantId, Niveau niveau);
}