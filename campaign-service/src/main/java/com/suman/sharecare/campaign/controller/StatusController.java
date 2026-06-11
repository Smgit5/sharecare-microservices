package com.suman.sharecare.campaign.controller;

import com.suman.sharecare.campaign.dto.status_dtos.StatusResponseDto;
import com.suman.sharecare.campaign.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns/status")
public class StatusController {
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<List<StatusResponseDto>> getAllStatus() {
        return ResponseEntity.ok(statusService.getAllStatus());
    }
}
