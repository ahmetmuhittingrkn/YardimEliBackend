package com.yazilim.afet.service.impl;

import com.yazilim.afet.dto.AidItemDTO;
import com.yazilim.afet.dto.AidRequestDetailDTO;
import com.yazilim.afet.dto.AidSummaryDTO;
import com.yazilim.afet.dto.CreateAidRequestDTO;
import com.yazilim.afet.entity.*;
import com.yazilim.afet.entity.id.AidRequestTypeId;
import com.yazilim.afet.repository.AidRequestRepository;
import com.yazilim.afet.repository.AidTypeRepository;
import com.yazilim.afet.repository.LocationRepository;
import com.yazilim.afet.repository.PersonRepository;
import com.yazilim.afet.service.AidRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AidRequestServiceImpl implements AidRequestService {

    private final AidRequestRepository aidRequestRepository;
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final AidTypeRepository aidTypeRepository;

    public AidRequestServiceImpl(AidRequestRepository aidRequestRepository, PersonRepository personRepository, LocationRepository locationRepository, AidTypeRepository aidTypeRepository) {
        this.aidRequestRepository = aidRequestRepository;
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
        this.aidTypeRepository = aidTypeRepository;
    }

    @Override
    @Transactional
    public void createAidRequest(CreateAidRequestDTO dto, Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        AidRequest aidRequest = new AidRequest();
        aidRequest.setPerson(person);
        aidRequest.setLocation(location);
        aidRequest.setDescription(dto.getDescription());
        aidRequest = aidRequestRepository.save(aidRequest); // ID lazımsa önce kaydet

        List<AidRequestType> requestTypes = new ArrayList<>();
        for (AidItemDTO item : dto.getAidItems()) {
            AidType aidType = aidTypeRepository.findById(item.getAidTypeId())
                    .orElseThrow(() -> new RuntimeException("Aid type not found"));

            AidRequestType requestType = new AidRequestType();
            requestType.setAidRequest(aidRequest);
            requestType.setAidType(aidType);
            requestType.setQuantity(item.getQuantity());
            requestType.setId(new AidRequestTypeId(aidRequest.getId(), aidType.getId()));

            requestTypes.add(requestType);
        }

        aidRequest.setAidRequestTypes(requestTypes);
        aidRequestRepository.save(aidRequest); // ilişkileri de kaydetmek için tekrar save
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
                dto = new AidRequestDetailDTO();
                dto.setId(id);
                dto.setDescription(description);
                dto.setCreatedAt(createdAt.toLocalDateTime());
                dto.setAidItems(new ArrayList<>());
                requestMap.put(id, dto);
            }

            dto.getAidItems().add(new AidItemDTO(aidTypeId, quantity));
        }

        return new ArrayList<>(requestMap.values());

    }

    @Override
    public void deleteAidRequest(Long requestId, Long userId) {
        AidRequest aidRequest = aidRequestRepository.findByIdAndPerson_Id(requestId, userId)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı veya silme yetkiniz yok."));

        aidRequestRepository.delete(aidRequest);
    }




}
