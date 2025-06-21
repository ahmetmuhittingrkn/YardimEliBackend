package com.yazilim.afet.controller;

import com.yazilim.afet.dto.CreateSupportRequestDTO;
import com.yazilim.afet.dto.SupportRequestDetailDTO;
import com.yazilim.afet.dto.SupportRequestResponseDTO;
import com.yazilim.afet.dto.SupportSummaryDTO;
import com.yazilim.afet.entity.SupportRequest;
import com.yazilim.afet.service.SupportRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support-requests")
public class SupportRequestController {

    private final SupportRequestService supportRequestService;

    public SupportRequestController(SupportRequestService supportRequestService) {
        this.supportRequestService = supportRequestService;
    }

    @PostMapping
    public ResponseEntity<Void> createSupportRequest(@RequestBody CreateSupportRequestDTO dto) {
        supportRequestService.createSupportRequest(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveSupportRequest(@PathVariable Long id, @RequestParam Long personId) {
        supportRequestService.approveSupportRequest(id, personId);
        return ResponseEntity.ok("Destek talebi onaylandı.");
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Void> markAsOnTheWay(@PathVariable Long id, @RequestParam Long personId) {
        supportRequestService.markAsOnTheWay(id, personId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/arrived")
    public ResponseEntity<Void> markAsArrived(@PathVariable Long id, @RequestParam Long personId) {
        supportRequestService.markAsArrived(id, personId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/reject")
    public ResponseEntity<String> rejectSupportRequest(@PathVariable Long id, @RequestParam Long personId) {
        supportRequestService.rejectSupportRequest(id, personId);
        return ResponseEntity.ok("Destek talebi reddedildi ve silindi.");
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
