package com.yazilim.afet.service;

import com.yazilim.afet.dto.AidRequestDetailDTO;
import com.yazilim.afet.dto.AidSummaryDTO;
import com.yazilim.afet.dto.CreateAidRequestDTO;

import java.util.List;

public interface AidRequestService {

    void createAidRequest(CreateAidRequestDTO dto, Long personId);

    List<AidSummaryDTO> getAidSummaryByLocationId(Long locationId);

    List<AidRequestDetailDTO> getAidRequestsByLocationId(Long locationId);


    void deleteAidRequest(Long requestId, Long userId);
}
