package com.yazilim.afet.service;

import com.yazilim.afet.dto.CreateSupportRequestDTO;
import com.yazilim.afet.dto.SupportRequestDetailDTO;
import com.yazilim.afet.dto.SupportRequestResponseDTO;
import com.yazilim.afet.dto.SupportSummaryDTO;
import com.yazilim.afet.entity.SupportRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SupportRequestService {

    @Transactional
    void createSupportRequest(CreateSupportRequestDTO dto);

    void approveSupportRequest(Long supportRequestId, Long approverPersonId);

    void markAsOnTheWay(Long supportRequestId, Long personId);
    void markAsArrived(Long supportRequestId, Long personId);

    void rejectSupportRequest(Long supportRequestId, Long approverPersonId);

    List<SupportRequestResponseDTO> getPendingSupportRequests();

    List<SupportSummaryDTO> getSupportSummaryByLocationId(Long locationId);

    List<SupportRequestDetailDTO> getSupportRequestDetailsByLocationId(Long locationId);


}
