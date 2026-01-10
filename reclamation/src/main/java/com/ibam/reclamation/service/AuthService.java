package com.ibam.reclamation.service;

import com.ibam.reclamation.dto.LoginRequest;
import com.ibam.reclamation.dto.LoginResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ibam.reclamation.repository.UserRepository;
import com.ibam.reclamation.entity.User;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
        
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(user);
        String niveau = user.getNiveau() != null ? user.getNiveau().name() : null;
        String filiere = user.getFiliere() != null ? user.getFiliere().name() : null;
        
        return new LoginResponse(token, user.getRole().name(), niveau, filiere);
    }
    
    public void logout(String token) {
        throw new UnsupportedOperationException("Logout non encore implémenté");
    }
}