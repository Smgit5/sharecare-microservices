package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.location_dtos.LocationResponseDto;
import com.suman.sharecare.campaign.entity.Location;
import com.suman.sharecare.campaign.repository.LocationRepository;
import com.suman.sharecare.campaign.utility.LocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public List<LocationResponseDto> getAllLocation() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream().map(locationMapper::toDto).toList();
    }
}
