package com.yazilim.afet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stk_members")
public class StkMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stk_id", nullable = false)
    private Stk stk;

    @Column(nullable = false, unique = true, length = 255)
    private String tc;
}
