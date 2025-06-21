package com.yazilim.afet.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportRequestDetailDTO {

    private Long id;
    private String description;
    private LocalDateTime createdAt;
    private String status;
    private List<SupportItemDTO> aidItems;
    private Long personId;

}
