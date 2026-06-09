package com.suman.sharecare.campaign.dto.status_dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StatusResponseDto {
    private UUID id;
    private String name;
}
