package com.yazilim.afet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class AidSummaryDTO {

    private String aidType;
    private Long totalQuantity;


}