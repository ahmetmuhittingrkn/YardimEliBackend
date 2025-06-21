package com.yazilim.afet.controller;

import com.yazilim.afet.dto.CreateSupportRequestDTO;
import com.yazilim.afet.dto.SupportRequestDetailDTO;
import com.yazilim.afet.dto.SupportRequestResponseDTO;
import com.yazilim.afet.dto.SupportSummaryDTO;
import com.yazilim.afet.service.SupportRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support-requests")
@RequiredArgsConstructor
public class SupportRequestController {

    private final SupportRequestService supportRequestService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createSupportRequest(@Valid @RequestBody CreateSupportRequestDTO dto) {
        supportRequestService.createSupportRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Destek talebi başarıyla oluşturuldu."));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, String>> approveSupportRequest(
            @PathVariable Long id, 
            @RequestParam Long personId) {
        supportRequestService.approveSupportRequest(id, personId);
        return ResponseEntity.ok(Map.of("message", "Destek talebi onaylandı."));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Map<String, String>> markAsOnTheWay(
            @PathVariable Long id, 
            @RequestParam Long personId) {
        supportRequestService.markAsOnTheWay(id, personId);
        return ResponseEntity.ok(Map.of("message", "Destek talebi yola çıktı olarak işaretlendi."));
    }

    @PutMapping("/{id}/arrived")
    public ResponseEntity<Map<String, String>> markAsArrived(
            @PathVariable Long id, 
            @RequestParam Long personId) {
        supportRequestService.markAsArrived(id, personId);
        return ResponseEntity.ok(Map.of("message", "Destek talebi vardı olarak işaretlendi."));
    }

    @DeleteMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectSupportRequest(
            @PathVariable Long id, 
            @RequestParam Long personId) {
        supportRequestService.rejectSupportRequest(id, personId);
        return ResponseEntity.ok(Map.of("message", "Destek talebi reddedildi ve silindi."));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SupportRequestResponseDTO>> getPendingSupportRequests() {
        List<SupportRequestResponseDTO> response = supportRequestService.getPendingSupportRequests();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<SupportSummaryDTO>> getSupportSummary(@RequestParam Long locationId) {
        List<SupportSummaryDTO> summary = supportRequestService.getSupportSummaryByLocationId(locationId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<SupportRequestDetailDTO>> getSupportDetailsByLocation(@PathVariable Long locationId) {
        List<SupportRequestDetailDTO> details = supportRequestService.getSupportRequestDetailsByLocationId(locationId);
        return ResponseEntity.ok(details);
    }
}
