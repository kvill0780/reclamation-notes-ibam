package com.ibam.reclamation.repository;

import com.ibam.reclamation.entity.PeriodeReclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodeReclamationRepository extends JpaRepository<PeriodeReclamation, Long> {
    
    /**
     * Trouve la période actuellement active
     */
    @Query("SELECT p FROM PeriodeReclamation p WHERE p.active = true AND p.dateDebut <= :maintenant AND p.dateFin > :maintenant")
    Optional<PeriodeReclamation> findPeriodeActive(LocalDateTime maintenant);
    
    /**
     * Vérifie s'il existe une période active
     */
    @Query("SELECT COUNT(p) > 0 FROM PeriodeReclamation p WHERE p.active = true AND p.dateDebut <= :maintenant AND p.dateFin > :maintenant")
    boolean existePeriodeActive(LocalDateTime maintenant);

    boolean existsByActiveTrueAndDateDebutLessThanAndDateFinGreaterThan(LocalDateTime dateFin, LocalDateTime dateDebut);

    List<PeriodeReclamation> findAllByOrderByDateDebutDesc();
}
