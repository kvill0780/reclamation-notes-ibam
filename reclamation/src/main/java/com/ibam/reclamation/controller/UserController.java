package com.ibam.reclamation.controller;

import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('SCOLARITE', 'DA')")
public class UserController{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public User createUser(@RequestParam String email, @RequestParam String password, @RequestParam String nom, @RequestParam String prenom) {
        return userService.createUser(email, password, nom, prenom);
    }

}