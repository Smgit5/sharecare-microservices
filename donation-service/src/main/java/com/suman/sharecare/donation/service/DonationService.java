package com.suman.sharecare.donation.service;

import com.suman.sharecare.donation.config.CampaignClient;
import com.suman.sharecare.donation.dto.DonationRequestDto;
import com.suman.sharecare.donation.dto.DonationResponseDto;
import com.suman.sharecare.donation.dto.campaign_donation_dtos.CampaignDonationCheckResponseDto;
import com.suman.sharecare.donation.entity.Donation;
import com.suman.sharecare.donation.entity.DonationStatus;
import com.suman.sharecare.donation.enums.Status;
import com.suman.sharecare.donation.exception.ActionNotAllowedException;
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
    private final CampaignClient campaignClient;

    public DonationResponseDto donate(DonationRequestDto donationRequestDto, String donorId) {
        CampaignDonationCheckResponseDto donationCheckResponseDto = campaignClient.checkIfDonationAllowed(donationRequestDto.getCampaignId());
        if(!donationCheckResponseDto.isDonationAllowed()) {
            throw new ActionNotAllowedException("Donation is allowed only for active campaigns");
        }
        Donation donation = new Donation();
        donation.setCampaignId(UUID.fromString(donationRequestDto.getCampaignId()));
        donation.setDonorId(UUID.fromString(donorId));
        donation.setAmount(donationRequestDto.getAmount());
        donation.setStatus(statusService.getStatusByName(Status.SUCCESS.name()));
        Donation savedDonation = donationRespository.save(donation);
        campaignClient.updateRaisedAmount(donationRequestDto.getCampaignId(), donationRequestDto.getAmount());
        return donationMapper.generateDto(savedDonation);
    }
}
