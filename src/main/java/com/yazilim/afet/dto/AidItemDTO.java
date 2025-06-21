package com.yazilim.afet.dto;

import lombok.Data;

@Data
public class AidItemDTO {
    private Long aidTypeId;
    private Integer quantity;

    public AidItemDTO(Long aidTypeId, Integer quantity) {
        this.aidTypeId = aidTypeId;
        this.quantity = quantity;
    }
}
