package com.yazilim.afet.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "aid_types")
public class AidType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "aidType")
    private List<AidRequestType> aidRequestTypes;

    @OneToMany(mappedBy = "aidType")
    private List<SupportRequestType> supportRequestTypes;
}
