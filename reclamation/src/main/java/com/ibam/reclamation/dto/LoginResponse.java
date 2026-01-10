package com.ibam.reclamation.dto;

public class LoginResponse {
    private String token;
    private String role;
    private String niveau;
    private String filiere;
    
    public LoginResponse() {}
    
    public LoginResponse(String token, String role, String niveau, String filiere) {
        this.token = token;
        this.role = role;
        this.niveau = niveau;
        this.filiere = filiere;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    
    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
}