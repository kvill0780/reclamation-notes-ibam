package com.ibam.reclamation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.repository.UserRepository;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository  = userRepository;
    }
    
    public User createUser(String email, String plainPassword, String nom, String prenom) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(plainPassword));
        user.setNom(nom);
        user.setPrenom(prenom);
        return userRepository.save(user);
    }
    
    public boolean validatePassword(User user, String plainPassword) {
        return passwordEncoder.matches(plainPassword, user.getPasswordHash());
    }
}