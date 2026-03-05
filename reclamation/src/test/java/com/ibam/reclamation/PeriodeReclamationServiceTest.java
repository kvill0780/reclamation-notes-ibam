package com.ibam.reclamation;

import com.ibam.reclamation.dto.CreatePeriodeRequest;
import com.ibam.reclamation.entity.PeriodeReclamation;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.exception.BusinessException.PeriodeNotFoundException;
import com.ibam.reclamation.repository.PeriodeReclamationRepository;
import com.ibam.reclamation.security.RoleEnum;
import com.ibam.reclamation.service.PeriodeReclamationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests service gestion des périodes")
class PeriodeReclamationServiceTest {

    @Mock
    private PeriodeReclamationRepository periodeRepository;

    private PeriodeReclamationService periodeService;
    private User da;

    @BeforeEach
    void setUp() {
        periodeService = new PeriodeReclamationService(periodeRepository);
        da = new User();
        da.setId(7L);
        da.setNom("Tazi");
        da.setPrenom("Omar");
        da.setRole(RoleEnum.ROLE_DA);
    }

    @Test
    @DisplayName("Création période valide")
    void creerPeriodeValide() {
        CreatePeriodeRequest request = buildRequest(2, 24);
        PeriodeReclamation saved = new PeriodeReclamation();
        saved.setId(11L);
        when(periodeRepository.existsByActiveTrueAndDateDebutLessThanAndDateFinGreaterThan(
                eq(request.getDateFin()), eq(request.getDateDebut()))).thenReturn(false);
        when(periodeRepository.save(any(PeriodeReclamation.class))).thenReturn(saved);

        PeriodeReclamation created = assertDoesNotThrow(() -> periodeService.creerPeriode(request, da));
        assertEquals(11L, created.getId());
        verify(periodeRepository).save(any(PeriodeReclamation.class));
    }

    @Test
    @DisplayName("Création refusée si durée > 72h")
    void creerPeriodeRefuseeDureeMax() {
        CreatePeriodeRequest request = buildRequest(2, 80);
        assertThrows(IllegalArgumentException.class, () -> periodeService.creerPeriode(request, da));
    }

    @Test
    @DisplayName("Création refusée si chevauchement actif")
    void creerPeriodeRefuseeChevauchement() {
        CreatePeriodeRequest request = buildRequest(2, 24);
        when(periodeRepository.existsByActiveTrueAndDateDebutLessThanAndDateFinGreaterThan(
                eq(request.getDateFin()), eq(request.getDateDebut()))).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> periodeService.creerPeriode(request, da));
    }

    @Test
    @DisplayName("Fermeture refusée si période introuvable")
    void fermerPeriodeIntrouvable() {
        when(periodeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PeriodeNotFoundException.class, () -> periodeService.fermerPeriode(99L));
    }

    @Test
    @DisplayName("Fermeture refusée si période déjà fermée")
    void fermerPeriodeDejaFermee() {
        PeriodeReclamation periode = new PeriodeReclamation();
        periode.setId(1L);
        periode.setActive(false);
        when(periodeRepository.findById(1L)).thenReturn(Optional.of(periode));

        assertThrows(IllegalStateException.class, () -> periodeService.fermerPeriode(1L));
    }

    private CreatePeriodeRequest buildRequest(int startInHours, int durationInHours) {
        LocalDateTime start = LocalDateTime.now().plusHours(startInHours);
        LocalDateTime end = start.plusHours(durationInHours);
        CreatePeriodeRequest request = new CreatePeriodeRequest();
        request.setNom("Reclamations Test");
        request.setDescription("Fenetre de reclamation planifiee");
        request.setDateDebut(start);
        request.setDateFin(end);
        return request;
    }
}
