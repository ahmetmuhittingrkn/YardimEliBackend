package com.yazilim.afet.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "stk")
@Getter
public class Stk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "stk")
    private List<Person> persons;

    @OneToMany(mappedBy = "stk")
    private List<StkMember> stkMembers;
}
