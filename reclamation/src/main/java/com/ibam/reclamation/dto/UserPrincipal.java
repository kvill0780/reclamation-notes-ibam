package com.ibam.reclamation.dto;

import com.ibam.reclamation.entity.User;
import lombok.Getter;

@Getter
public class UserPrincipal {

    private final Long id;
    private final String email;
    private final String role;

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole().name();
    }
}
