package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.status_dtos.StatusResponseDto;
import com.suman.sharecare.campaign.entity.CampaignStatus;
import com.suman.sharecare.campaign.repository.StatusRepository;
import com.suman.sharecare.campaign.utility.StatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    public List<StatusResponseDto> getAllStatus() {
        List<CampaignStatus> campaignStatusList = statusRepository.findAll();
        return campaignStatusList.stream().map(statusMapper::toDto).toList();
    }
}
