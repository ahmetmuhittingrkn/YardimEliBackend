package com.yazilim.afet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateSupportRequestDTO {

    private Long personId;
    private Long locationId;

    private String description;
    private Integer arrivalTimeMinutes;
    private String originCity;

    private List<SupportItemDTO> supportItems;

}
