package com.yazilim.afet.dto;

import lombok.Data;

@Data
public class SupportRequestResponseDTO {
    private Long id;
    private String description;
    private Integer arrivalTimeMinutes;
    private String originCity;
    private String status;
    private String createdAt;
    private String locationName;
}
