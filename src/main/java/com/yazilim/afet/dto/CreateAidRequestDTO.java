package com.yazilim.afet.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateAidRequestDTO {
    private Long personId;
    private Long locationId;
    private String description;
    private List<AidItemDTO> aidItems;
}
