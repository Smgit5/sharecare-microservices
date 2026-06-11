package com.suman.sharecare.campaign.controller;

import com.suman.sharecare.campaign.dto.location_dtos.LocationResponseDto;
import com.suman.sharecare.campaign.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campaigns/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAllLocation() {
        return ResponseEntity.ok(locationService.getAllLocation());
    }
}
