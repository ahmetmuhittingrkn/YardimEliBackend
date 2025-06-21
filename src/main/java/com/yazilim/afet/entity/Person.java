package com.yazilim.afet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yazilim.afet.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "person")
@Data
@ToString(exclude = {"password", "aidRequests", "supportRequests"})
@EqualsAndHashCode(exclude = {"aidRequests", "supportRequests"})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(nullable = false, length = 32)
    private String surname;

    @Column(nullable = false, unique = true, length = 11)
    private String tc;

    @Column(nullable = false, unique = true, length = 32)
    private String email;

    @Column(nullable = false, length = 255)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true, length = 16)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean isActive = false;

    @ManyToOne
    @JoinColumn(name = "stk_id")
    private Stk stk;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AidRequest> aidRequests;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SupportRequest> supportRequests;

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
