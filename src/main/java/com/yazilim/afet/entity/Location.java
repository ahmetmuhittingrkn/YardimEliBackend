package com.yazilim.afet.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String name;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "location")
    private List<AidRequest> aidRequests;

    @OneToMany(mappedBy = "location")
    private List<SupportRequest> supportRequests;

}

