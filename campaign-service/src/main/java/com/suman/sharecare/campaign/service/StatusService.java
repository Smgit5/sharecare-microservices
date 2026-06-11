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
    private static final UUID DEFAULT_STATUS_ID = UUID.fromString("bbba17f4-c776-4a06-b7b0-ea3039e7b672");
    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    public List<StatusResponseDto> getAllStatus() {
        List<CampaignStatus> campaignStatusList = statusRepository.findAll();
        return campaignStatusList.stream().map(statusMapper::toDto).toList();
    }

    public CampaignStatus getDefaultStatus() {
        return statusRepository.findById(DEFAULT_STATUS_ID).orElseThrow(() -> new ResourceNotFoundException("Status not found!"));
    }
}
