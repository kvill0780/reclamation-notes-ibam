package com.ibam.reclamation.service;

import com.ibam.reclamation.dto.UserPrincipal;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.exception.UnauthorizedException;
import com.ibam.reclamation.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        // Récupérer l'utilisateur complet depuis la base de données
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new UnauthorizedException("Utilisateur non trouvé"));
    }
}
