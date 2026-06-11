package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.status_dtos.StatusResponseDto;
import com.suman.sharecare.campaign.entity.CampaignStatus;
import com.suman.sharecare.campaign.exception.custom_exception.ResourceNotFoundException;
import com.suman.sharecare.campaign.repository.StatusRepository;
import com.suman.sharecare.campaign.utility.StatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatusService {
    public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
    public static final String ACTIVE = "ACTIVE";
    public static final String REJECTED = "REJECTED";
    public static final String CLOSED = "CLOSED";
    public static final String FULLY_FUNDED = "FULLY_FUNDED";
    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    public List<StatusResponseDto> getAllStatus() {
        List<CampaignStatus> campaignStatusList = statusRepository.findAll();
        return campaignStatusList.stream().map(statusMapper::toDto).toList();
    }

    public CampaignStatus getStatusByName(String status) {
        return statusRepository.findByName(status).orElseThrow(() -> new ResourceNotFoundException("Status not found!"));
    }
}
