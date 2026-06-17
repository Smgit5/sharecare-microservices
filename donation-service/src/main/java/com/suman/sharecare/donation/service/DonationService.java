package com.suman.sharecare.donation.service;

import com.suman.sharecare.donation.dto.DonationRequestDto;
import com.suman.sharecare.donation.dto.DonationResponseDto;
import com.suman.sharecare.donation.entity.Donation;
import com.suman.sharecare.donation.entity.DonationStatus;
import com.suman.sharecare.donation.enums.Status;
import com.suman.sharecare.donation.repository.DonationRespository;
import com.suman.sharecare.donation.repository.StatusRepository;
import com.suman.sharecare.donation.util.DonationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRespository donationRespository;
    private final DonationMapper donationMapper;
    private final StatusService statusService;

    public DonationResponseDto donate(DonationRequestDto donationRequestDto, String donorId) {
        Donation donation = new Donation();
        donation.setCampaignId(donationRequestDto.getCampaignId());
        donation.setDonorId(UUID.fromString(donorId));
        donation.setAmount(donationRequestDto.getAmount());
        donation.setStatus(statusService.getStatusByName(Status.SUCCESS.name()));
        return donationMapper.generateDto(donationRespository.save(donation));
    }
}
