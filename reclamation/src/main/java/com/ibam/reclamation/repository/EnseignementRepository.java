package com.ibam.reclamation.repository;

import com.ibam.reclamation.entity.Enseignement;
import com.ibam.reclamation.entity.Filiere;
import com.ibam.reclamation.entity.Niveau;
import com.ibam.reclamation.entity.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnseignementRepository extends JpaRepository<Enseignement, Long> {
    
    List<Enseignement> findByEnseignantId(Long enseignantId);
    List<Enseignement> findByMatiereId(Long matiereId);
    List<Enseignement> findByFiliere(Filiere filiere);
    List<Enseignement> findByNiveau(Niveau niveau);
    List<Enseignement> findBySemestre(Semestre semestre);
    List<Enseignement> findByAnneeAcademique(String anneeAcademique);
}