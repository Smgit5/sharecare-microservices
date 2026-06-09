package com.suman.sharecare.campaign.dto.location_dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LocationResponseDto {
    private UUID id;
    private String city;
    private String district;
    private String state;
    private String landmark;
}
