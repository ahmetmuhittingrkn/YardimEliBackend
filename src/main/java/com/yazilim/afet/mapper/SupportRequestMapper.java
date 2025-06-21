package com.yazilim.afet.mapper;

import com.yazilim.afet.dto.SupportItemDTO;
import com.yazilim.afet.dto.SupportRequestDetailDTO;
import com.yazilim.afet.dto.SupportRequestResponseDTO;
import com.yazilim.afet.dto.CreateSupportRequestDTO;
import com.yazilim.afet.entity.SupportRequest;
import com.yazilim.afet.entity.SupportRequestType;
import com.yazilim.afet.entity.AidType;
import com.yazilim.afet.entity.Person;
import com.yazilim.afet.entity.Location;
import com.yazilim.afet.enums.SupportStatus;
import com.yazilim.afet.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupportRequestMapper {

    public SupportRequest toEntity(CreateSupportRequestDTO dto, Person person, Location location) {
        SupportRequest supportRequest = new SupportRequest();
        supportRequest.setPerson(person);
        supportRequest.setLocation(location);
        supportRequest.setDescription(dto.getDescription());
        supportRequest.setArrivalTimeMinutes(dto.getArrivalTimeMinutes());
        supportRequest.setOriginCity(dto.getOriginCity());
        supportRequest.setStatus(SupportStatus.BEKLEMEDE);
        return supportRequest;
    }

    public List<SupportRequestType> toSupportRequestTypes(List<SupportItemDTO> supportItems, SupportRequest supportRequest, List<AidType> aidTypes) {
        return supportItems.stream()
                .map(item -> {
                    AidType aidType = aidTypes.stream()
                            .filter(type -> type.getId().equals(item.getAidTypeId()))
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException("Yardım türü bulunamadı: " + item.getAidTypeId()));

                    SupportRequestType requestType = new SupportRequestType();
                    requestType.setSupportRequest(supportRequest);
                    requestType.setAidType(aidType);
                    requestType.setQuantity(item.getQuantity());
                    return requestType;
                })
                .collect(Collectors.toList());
    }

    public SupportRequestResponseDTO toResponseDTO(SupportRequest supportRequest) {
        SupportRequestResponseDTO dto = new SupportRequestResponseDTO();
        dto.setId(supportRequest.getId());
        dto.setDescription(supportRequest.getDescription());
        dto.setArrivalTimeMinutes(supportRequest.getArrivalTimeMinutes());
        dto.setOriginCity(supportRequest.getOriginCity());
        dto.setStatus(supportRequest.getStatus().name());
        dto.setCreatedAt(supportRequest.getCreatedAt().toString());
        dto.setLocationName(supportRequest.getLocation().getName());
        return dto;
    }

    public SupportRequestDetailDTO toDetailDTO(Long id, String description, java.time.LocalDateTime createdAt, 
                                             String status, Long personId, List<SupportItemDTO> supportItems) {
        SupportRequestDetailDTO dto = new SupportRequestDetailDTO();
        dto.setId(id);
        dto.setDescription(description);
        dto.setCreatedAt(createdAt);
        dto.setStatus(status);
        dto.setPersonId(personId);
        dto.setAidItems(supportItems);
        return dto;
    }
} 