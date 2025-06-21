package com.yazilim.afet.dto;

import com.yazilim.afet.entity.Person;
import lombok.Data;

@Data
public class PersonResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean isActive;
    private String stkName;

    public PersonResponseDTO(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.email = person.getEmail();
        this.role = person.getRole().name();
        this.isActive = person.getIsActive();
        this.stkName = person.getStk() != null ? person.getStk().getName() : null;
    }
}
