package com.ibam.reclamation.dto;

import com.ibam.reclamation.entity.User;
import com.ibam.reclamation.security.RoleEnum;

public class UserPrincipal {
    private final User user;
    
    public UserPrincipal(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
    
    public Long getId() {
        return user.getId();
    }
    
    public String getEmail() {
        return user.getEmail();
    }
    
    public RoleEnum getRole() {
        return user.getRole();
    }
    
    public String getNom() {
        return user.getNom();
    }
    
    public String getPrenom() {
        return user.getPrenom();
    }
}
