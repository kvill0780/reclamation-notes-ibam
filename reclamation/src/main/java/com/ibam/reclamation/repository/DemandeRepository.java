package com.ibam.reclamation.repository;

import com.ibam.reclamation.entity.DemandeReclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeRepository extends JpaRepository<DemandeReclamation, Long> {

    List<DemandeReclamation> findByEtudiantId(Long EtudiantId);

    List<DemandeReclamation> findByEnseignantImputeId(Long enseignantId);
}