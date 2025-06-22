package com.yazilim.afet.service.impl;

import com.yazilim.afet.dto.*;
import com.yazilim.afet.entity.*;
import com.yazilim.afet.entity.id.SupportRequestTypeId;
import com.yazilim.afet.enums.Role;
import com.yazilim.afet.enums.SupportStatus;
import com.yazilim.afet.exception.ForbiddenException;
import com.yazilim.afet.exception.NotFoundException;
import com.yazilim.afet.exception.UnauthorizedException;
import com.yazilim.afet.mapper.SupportRequestMapper;
import com.yazilim.afet.repository.*;
import com.yazilim.afet.service.SupportRequestService;
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
public class SupportRequestServiceImpl implements SupportRequestService {

    private final SupportRequestRepository supportRequestRepository;
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final AidTypeRepository aidTypeRepository;
    private final SupportRequestMapper supportRequestMapper;

    @Override
    @Transactional
    public void createSupportRequest(CreateSupportRequestDTO dto) {
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new NotFoundException("Konum bulunamadı"));

        SupportRequest supportRequest = supportRequestMapper.toEntity(dto, person, location);

        List<Long> aidTypeIds = dto.getSupportItems().stream()
                .map(SupportItemDTO::getAidTypeId)
                .collect(Collectors.toList());
        
        List<AidType> aidTypes = aidTypeRepository.findAllById(aidTypeIds);
        
        if (aidTypes.size() != aidTypeIds.size()) {
            throw new NotFoundException("Bazı yardım türleri bulunamadı");
        }

        List<SupportRequestType> requestTypes = supportRequestMapper.toSupportRequestTypes(
                dto.getSupportItems(), supportRequest, aidTypes);

        supportRequest.setSupportRequestTypes(requestTypes);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    @Transactional
    public void approveSupportRequest(Long supportRequestId, Long approverPersonId) {
        Person approver = personRepository.findById(approverPersonId)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        if (approver.getRole() != Role.STK_GONULLUSU || !approver.getIsActive()) {
            throw new ForbiddenException("Sadece aktif STK gönüllüleri destek talebini onaylayabilir.");
        }

        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new NotFoundException("Destek talebi bulunamadı."));

        if (supportRequest.getStatus() != SupportStatus.BEKLEMEDE) {
            throw new ForbiddenException("Yalnızca 'BEKLEMEDE' durumundaki talepler onaylanabilir.");
        }

        supportRequest.setStatus(SupportStatus.ONAYLANDI);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    public void markAsOnTheWay(Long supportRequestId, Long personId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new NotFoundException("Destek talebi bulunamadı"));

        if (!supportRequest.getPerson().getId().equals(personId)) {
            throw new UnauthorizedException("Sadece talep sahibi durumu güncelleyebilir.");
        }

        if (supportRequest.getStatus() != SupportStatus.ONAYLANDI) {
            throw new ForbiddenException("Durum 'ONAYLANDI' olmalıdır.");
        }

        supportRequest.setStatus(SupportStatus.YOLDA);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    public void markAsArrived(Long supportRequestId, Long personId) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new NotFoundException("Destek talebi bulunamadı"));

        if (!supportRequest.getPerson().getId().equals(personId)) {
            throw new UnauthorizedException("Sadece talep sahibi durumu güncelleyebilir.");
        }

        if (supportRequest.getStatus() != SupportStatus.YOLDA) {
            throw new ForbiddenException("Durum 'YOLDA' olmalıdır.");
        }

        supportRequest.setStatus(SupportStatus.VARDI);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    @Transactional
    public void rejectSupportRequest(Long supportRequestId, Long approverPersonId) {
        Person approver = personRepository.findById(approverPersonId)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        if (approver.getRole() != Role.STK_GONULLUSU || !approver.getIsActive()) {
            throw new ForbiddenException("Sadece aktif STK gönüllüleri destek talebini reddedebilir.");
        }

        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new NotFoundException("Destek talebi bulunamadı."));

        if (supportRequest.getStatus() != SupportStatus.BEKLEMEDE) {
            throw new ForbiddenException("Yalnızca 'BEKLEMEDE' durumundaki talepler reddedilebilir.");
        }

        supportRequestRepository.delete(supportRequest);
    }

    @Override
    public List<SupportRequestResponseDTO> getPendingSupportRequests() {
        List<SupportRequest> requests = supportRequestRepository.findAllByStatus(SupportStatus.BEKLEMEDE);
        return requests.stream()
                .map(supportRequestMapper::toResponseDTO)
                .collect(Collectors.toList());
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
            String status = (String) row[3];
            Long aidTypeId = ((Number) row[4]).longValue();
            Integer quantity = ((Number) row[5]).intValue();
            Long personId = ((Number) row[6]).longValue();

            SupportRequestDetailDTO dto = requestMap.get(id);
            if (dto == null) {
                dto = supportRequestMapper.toDetailDTO(id, description, createdAt.toLocalDateTime(), 
                        status, personId, new ArrayList<>());
                requestMap.put(id, dto);
            }

            dto.getAidItems().add(new SupportItemDTO(aidTypeId, quantity));
        }

        return new ArrayList<>(requestMap.values());
    }
}
