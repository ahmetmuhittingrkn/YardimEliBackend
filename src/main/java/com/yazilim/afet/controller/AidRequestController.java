package com.yazilim.afet.controller;

import com.yazilim.afet.dto.AidRequestDetailDTO;
import com.yazilim.afet.dto.AidSummaryDTO;
import com.yazilim.afet.dto.CreateAidRequestDTO;
import com.yazilim.afet.service.AidRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aid-requests")
public class AidRequestController {

    private final AidRequestService aidRequestService;

    public AidRequestController(AidRequestService aidRequestService) {
        this.aidRequestService = aidRequestService;
    }

    @PostMapping
    public ResponseEntity<String> createAidRequest(@RequestBody CreateAidRequestDTO dto,
                                                   @RequestParam Long personId) {
        aidRequestService.createAidRequest(dto, personId);
        return ResponseEntity.ok("Yardım talebi başarıyla oluşturuldu.");
    }

    @GetMapping("/summary")
    public ResponseEntity<List<AidSummaryDTO>> getAidSummary(@RequestParam Long locationId) {
        List<AidSummaryDTO> summary = aidRequestService.getAidSummaryByLocationId(locationId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<AidRequestDetailDTO>> getByLocationId(@PathVariable Long locationId) {
        List<AidRequestDetailDTO> result = aidRequestService.getAidRequestsByLocationId(locationId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAidRequest(
            @PathVariable Long id,
            @RequestParam Long userId // Gerçekte bu JWT'den alınmalı
    ) {
        aidRequestService.deleteAidRequest(id, userId);
        return ResponseEntity.noContent().build();
    }


}
