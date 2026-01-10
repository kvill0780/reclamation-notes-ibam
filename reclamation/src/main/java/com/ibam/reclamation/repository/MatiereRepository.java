package com.ibam.reclamation.repository;

import com.ibam.reclamation.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    
    Matiere findByCode(String code);
}