package com.yazilim.afet.service.impl;

import com.yazilim.afet.dto.*;
import com.yazilim.afet.entity.*;
import com.yazilim.afet.entity.id.SupportRequestTypeId;
import com.yazilim.afet.enums.Role;
import com.yazilim.afet.enums.SupportStatus;
import com.yazilim.afet.exception.NotFoundException;
import com.yazilim.afet.repository.*;
import com.yazilim.afet.service.SupportRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SupportRequestServiceImpl implements SupportRequestService {

    private final SupportRequestRepository supportRequestRepository;
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final AidTypeRepository aidTypeRepository;


    public SupportRequestServiceImpl(SupportRequestRepository supportRequestRepository, PersonRepository personRepository, LocationRepository locationRepository, AidTypeRepository aidTypeRepository) {
        this.supportRequestRepository = supportRequestRepository;
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
        this.aidTypeRepository = aidTypeRepository;
    }

    @Override
    @Transactional
    public void createSupportRequest(CreateSupportRequestDTO dto) {
        // Person ve Location varlıklarını bul
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new NotFoundException("Konum bulunamadı"));

        // Ana support request nesnesini oluştur
        SupportRequest supportRequest = new SupportRequest();
        supportRequest.setPerson(person);
        supportRequest.setLocation(location);
        supportRequest.setDescription(dto.getDescription());
        supportRequest.setArrivalTimeMinutes(dto.getArrivalTimeMinutes());
        supportRequest.setOriginCity(dto.getOriginCity());
        supportRequest.setStatus(SupportStatus.BEKLEMEDE); // default olarak

        // SupportRequestType listesini oluştur
        List<SupportRequestType> requestTypes = dto.getSupportItems().stream().map(item -> {
            AidType aidType = aidTypeRepository.findById(item.getAidTypeId())
                    .orElseThrow(() -> new NotFoundException("Yardım türü bulunamadı"));

            SupportRequestType requestType = new SupportRequestType();
            requestType.setSupportRequest(supportRequest);
            requestType.setAidType(aidType);
            requestType.setQuantity(item.getQuantity());
            requestType.setId(new SupportRequestTypeId()); // Id zaten MapsId ile set edilecek
            return requestType;
        }).toList();

        supportRequest.setSupportRequestTypes(requestTypes);

        // Kaydet
        supportRequestRepository.save(supportRequest);
    }

    @Override
    @Transactional
    public void approveSupportRequest(Long supportRequestId, Long approverPersonId) {
        // Person'u bul
        Person approver = personRepository.findById(approverPersonId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Role ve aktiflik kontrolü
        if (approver.getRole() != Role.STK_GONULLUSU || !approver.getIsActive()) {
            throw new RuntimeException("Sadece aktif STK gönüllüleri destek talebini onaylayabilir.");
        }

        // Support request'i bul
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new RuntimeException("Destek talebi bulunamadı."));

        // Sadece BEKLEMEDE olanlar onaylanabilir
        if (supportRequest.getStatus() != SupportStatus.BEKLEMEDE) {
            throw new RuntimeException("Yalnızca 'BEKLEMEDE' durumundaki talepler onaylanabilir.");
        }

        // Onayla
        supportRequest.setStatus(SupportStatus.ONAYLANDI);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    public void markAsOnTheWay(Long supportRequestId, Long personId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new RuntimeException("Support request not found"));

        if (!supportRequest.getPerson().getId().equals(personId)) {
            throw new RuntimeException("Only the requester can update the status.");
        }

        if (supportRequest.getStatus() != SupportStatus.ONAYLANDI) {
            throw new RuntimeException("Status must be ONAYLANDI to mark as YOLDA.");
        }

        supportRequest.setStatus(SupportStatus.YOLDA);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    public void markAsArrived(Long supportRequestId, Long personId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new RuntimeException("Support request not found"));

        if (!supportRequest.getPerson().getId().equals(personId)) {
            throw new RuntimeException("Only the requester can update the status.");
        }

        if (supportRequest.getStatus() != SupportStatus.YOLDA) {
            throw new RuntimeException("Status must be YOLDA to mark as VARDI.");
        }

        supportRequest.setStatus(SupportStatus.VARDI);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    @Transactional
    public void rejectSupportRequest(Long supportRequestId, Long approverPersonId) {
        // Person'u bul
        Person approver = personRepository.findById(approverPersonId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Role ve aktiflik kontrolü
        if (approver.getRole() != Role.STK_GONULLUSU || !approver.getIsActive()) {
            throw new RuntimeException("Sadece aktif STK gönüllüleri destek talebini reddedebilir.");
        }

        // Support request'i bul
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new RuntimeException("Destek talebi bulunamadı."));

        // Sadece BEKLEMEDE olanlar reddedilebilir
        if (supportRequest.getStatus() != SupportStatus.BEKLEMEDE) {
            throw new RuntimeException("Yalnızca 'BEKLEMEDE' durumundaki talepler reddedilebilir.");
        }

        // Talebi sil
        supportRequestRepository.delete(supportRequest);
    }

    @Override
    public List<SupportRequestResponseDTO> getPendingSupportRequests() {
        List<SupportRequest> requests = supportRequestRepository.findAllByStatus(SupportStatus.BEKLEMEDE);

        return requests.stream().map(request -> {
            SupportRequestResponseDTO dto = new SupportRequestResponseDTO();
            dto.setId(request.getId());
            dto.setDescription(request.getDescription());
            dto.setArrivalTimeMinutes(request.getArrivalTimeMinutes());
            dto.setOriginCity(request.getOriginCity());
            dto.setStatus(request.getStatus().name());
            dto.setCreatedAt(request.getCreatedAt().toString());
            dto.setLocationName(request.getLocation().getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SupportSummaryDTO> getSupportSummaryByLocationId(Long locationId) {
        return supportRequestRepository.getSupportSummaryByLocationId(locationId);
    }

    @Override
    public List<SupportRequestDetailDTO> getSupportRequestDetailsByLocationId(Long locationId) {
        List<Object[]> results = supportRequestRepository.getSupportRequestDetailsByLocationId(locationId);
        Map<Long, SupportRequestDetailDTO> requestMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long id = ((Number) row[0]).longValue();
            String description = (String) row[1];
            Timestamp createdAt = (Timestamp) row[2];
            String status = (String) row[3];  // Enum string olarak geliyor
            Long aidTypeId = ((Number) row[4]).longValue();
            Integer quantity = ((Number) row[5]).intValue();
            Long personId =  ((Number) row[6]).longValue();

            SupportRequestDetailDTO dto = requestMap.get(id);
            if (dto == null) {
                dto = new SupportRequestDetailDTO();
                dto.setId(id);
                dto.setDescription(description);
                dto.setCreatedAt(createdAt.toLocalDateTime());
                dto.setStatus(status);
                dto.setAidItems(new ArrayList<>());
                dto.setPersonId(personId);
                requestMap.put(id, dto);
            }

            dto.getAidItems().add(new SupportItemDTO(aidTypeId, quantity));
        }

        return new ArrayList<>(requestMap.values());
    }




}
