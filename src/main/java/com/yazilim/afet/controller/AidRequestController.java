package com.yazilim.afet.controller;

import com.yazilim.afet.dto.AidRequestDetailDTO;
import com.yazilim.afet.dto.AidSummaryDTO;
import com.yazilim.afet.dto.CreateAidRequestDTO;
import com.yazilim.afet.service.AidRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aid-requests")
@RequiredArgsConstructor
public class AidRequestController {

    private final AidRequestService aidRequestService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createAidRequest(
            @Valid @RequestBody CreateAidRequestDTO dto,
            @RequestParam Long personId) {
        aidRequestService.createAidRequest(dto, personId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Yardım talebi başarıyla oluşturuldu."));
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
            @RequestParam Long userId) {
        aidRequestService.deleteAidRequest(id, userId);
        return ResponseEntity.noContent().build();
    }
}
