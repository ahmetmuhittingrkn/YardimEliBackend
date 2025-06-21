package com.yazilim.afet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AidRequestDetailDTO {

    private Long id;
    private String description;
    private LocalDateTime createdAt;
    private List<AidItemDTO> aidItems;

}
