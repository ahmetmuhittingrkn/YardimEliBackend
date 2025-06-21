package com.yazilim.afet.mapper;

import com.yazilim.afet.dto.AidItemDTO;
import com.yazilim.afet.dto.AidRequestDetailDTO;
import com.yazilim.afet.dto.CreateAidRequestDTO;
import com.yazilim.afet.entity.AidRequest;
import com.yazilim.afet.entity.AidRequestType;
import com.yazilim.afet.entity.AidType;
import com.yazilim.afet.entity.Person;
import com.yazilim.afet.entity.Location;
import com.yazilim.afet.entity.id.AidRequestTypeId;
import com.yazilim.afet.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AidRequestMapper {

    public AidRequest toEntity(CreateAidRequestDTO dto, Person person, Location location) {
        AidRequest aidRequest = new AidRequest();
        aidRequest.setPerson(person);
        aidRequest.setLocation(location);
        aidRequest.setDescription(dto.getDescription());
        return aidRequest;
    }

    public List<AidRequestType> toAidRequestTypes(List<AidItemDTO> aidItems, AidRequest aidRequest, List<AidType> aidTypes) {
        return aidItems.stream()
                .map(item -> {
                    AidType aidType = aidTypes.stream()
                            .filter(type -> type.getId().equals(item.getAidTypeId()))
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException("Yardım türü bulunamadı: " + item.getAidTypeId()));

                    AidRequestType requestType = new AidRequestType();
                    requestType.setAidRequest(aidRequest);
                    requestType.setAidType(aidType);
                    requestType.setQuantity(item.getQuantity());
                    requestType.setId(new AidRequestTypeId(aidRequest.getId(), aidType.getId()));
                    return requestType;
                })
                .collect(Collectors.toList());
    }

    public AidRequestDetailDTO toDetailDTO(Long id, String description, java.time.LocalDateTime createdAt, List<AidItemDTO> aidItems) {
        AidRequestDetailDTO dto = new AidRequestDetailDTO();
        dto.setId(id);
        dto.setDescription(description);
        dto.setCreatedAt(createdAt);
        dto.setAidItems(aidItems);
        return dto;
    }
} 