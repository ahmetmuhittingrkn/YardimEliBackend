package com.yazilim.afet.service.impl;

import com.yazilim.afet.dto.AidItemDTO;
import com.yazilim.afet.dto.AidRequestDetailDTO;
import com.yazilim.afet.dto.AidSummaryDTO;
import com.yazilim.afet.dto.CreateAidRequestDTO;
import com.yazilim.afet.entity.*;
import com.yazilim.afet.entity.id.AidRequestTypeId;
import com.yazilim.afet.exception.ForbiddenException;
import com.yazilim.afet.exception.NotFoundException;
import com.yazilim.afet.mapper.AidRequestMapper;
import com.yazilim.afet.repository.AidRequestRepository;
import com.yazilim.afet.repository.AidTypeRepository;
import com.yazilim.afet.repository.LocationRepository;
import com.yazilim.afet.repository.PersonRepository;
import com.yazilim.afet.service.AidRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AidRequestServiceImpl implements AidRequestService {

    private final AidRequestRepository aidRequestRepository;
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final AidTypeRepository aidTypeRepository;
    private final AidRequestMapper aidRequestMapper;

    @Override
    @Transactional
    public void createAidRequest(CreateAidRequestDTO dto, Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new NotFoundException("Konum bulunamadı"));

        AidRequest aidRequest = aidRequestMapper.toEntity(dto, person, location);
        aidRequest = aidRequestRepository.save(aidRequest);

        List<Long> aidTypeIds = dto.getAidItems().stream()
                .map(AidItemDTO::getAidTypeId)
                .collect(Collectors.toList());
        
        List<AidType> aidTypes = aidTypeRepository.findAllById(aidTypeIds);
        
        if (aidTypes.size() != aidTypeIds.size()) {
            throw new NotFoundException("Bazı yardım türleri bulunamadı");
        }

        List<AidRequestType> requestTypes = aidRequestMapper.toAidRequestTypes(
                dto.getAidItems(), aidRequest, aidTypes);
        
        aidRequest.setAidRequestTypes(requestTypes);
        aidRequestRepository.save(aidRequest);
    }

    @Override
    public List<AidSummaryDTO> getAidSummaryByLocationId(Long locationId) {
        return aidRequestRepository.getAidSummaryByLocationId(locationId);
    }

    @Override
    public List<AidRequestDetailDTO> getAidRequestsByLocationId(Long locationId) {
        List<Object[]> results = aidRequestRepository.getAidRequestDetailsByLocationId(locationId);
        Map<Long, AidRequestDetailDTO> requestMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long id = ((Number) row[0]).longValue();
            String description = (String) row[1];
            Timestamp createdAt = (Timestamp) row[2];
            Long aidTypeId = ((Number) row[3]).longValue();
            Integer quantity = ((Number) row[4]).intValue();

            AidRequestDetailDTO dto = requestMap.get(id);
            if (dto == null) {
                dto = aidRequestMapper.toDetailDTO(id, description, createdAt.toLocalDateTime(), new ArrayList<>());
                requestMap.put(id, dto);
            }

            dto.getAidItems().add(new AidItemDTO(aidTypeId, quantity));
        }

        return new ArrayList<>(requestMap.values());
    }

    @Override
    public void deleteAidRequest(Long requestId, Long userId) {
        AidRequest aidRequest = aidRequestRepository.findByIdAndPerson_Id(requestId, userId)
                .orElseThrow(() -> new ForbiddenException("Talep bulunamadı veya silme yetkiniz yok."));

        aidRequestRepository.delete(aidRequest);
    }
}
