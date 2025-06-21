package com.yazilim.afet.dto;

public class LoginResponseDTO {
    private Long personId;
    private String name;
    private String email;
    private String role;

    public LoginResponseDTO(Long personId, String name, String email, String role) {
        this.personId = personId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}

