package com.yazilim.afet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yazilim.afet.enums.SupportStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "support_requests")
@NoArgsConstructor
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "arrival_time_minutes")
    private Integer arrivalTimeMinutes;

    @Column(name = "origin_city")
    private String originCity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupportStatus status = SupportStatus.BEKLEMEDE;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "supportRequest", cascade = CascadeType.ALL)
    private List<SupportRequestType> supportRequestTypes;
}
