package com.suman.sharecare.donation.service;

import com.suman.sharecare.donation.config.CampaignClient;
import com.suman.sharecare.donation.dto.donation_dtos.DonationRequestDto;
import com.suman.sharecare.donation.dto.donation_dtos.DonationResponseDto;
import com.suman.sharecare.donation.dto.campaign_donation_dtos.CampaignDonationCheckResponseDto;
import com.suman.sharecare.donation.dto.donation_dtos.DonationStatisticsResponseDto;
import com.suman.sharecare.donation.dto.page_dtos.PageResponseDto;
import com.suman.sharecare.donation.entity.Donation;
import com.suman.sharecare.donation.enums.Status;
import com.suman.sharecare.donation.exception.ActionNotAllowedException;
import com.suman.sharecare.donation.exception.ResourceNotFoundException;
import com.suman.sharecare.donation.repository.DonationRespository;
import com.suman.sharecare.donation.util.DonationMapper;
import com.suman.sharecare.donation.util.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public PageResponseDto<DonationResponseDto> viewMyDonationHistory(String donorId, Pageable pageable) {
        Page<Donation> donations = donationRespository.findAllByDonorId(UUID.fromString(donorId), pageable);
        Page<DonationResponseDto> donationResponseDtos = donations.map(donationMapper::generateDto);
        return PageMapper.generateResponseDto(donationResponseDtos);
    }

    public PageResponseDto<DonationResponseDto> getDonationHistoryOfCampaign(String campaignId, String userId, String userRole, Pageable pageable) {
        if(!"ADMIN".equals(userRole) && !campaignClient.checkOwnership(campaignId, userId)) {
            throw new ResourceNotFoundException("Campaign not found!");
        }
        Page<Donation> donations = donationRespository.findByCampaignId(UUID.fromString(campaignId), pageable);
        Page<DonationResponseDto> donationResponseDtos = donations.map(donationMapper::generateDto);
        return PageMapper.generateResponseDto(donationResponseDtos);
    }

    public DonationStatisticsResponseDto getDonationStatistics(String campaignId, String userId, String userRole) {
        if(!"ADMIN".equals(userRole) && !campaignClient.checkOwnership(campaignId, userId)) {
            throw new ResourceNotFoundException("Campaign not found!");
        }
        long totalDonations = donationRespository.countByCampaignId(UUID.fromString(campaignId));
        BigDecimal totalAmount = donationRespository.sumAmountByCampaignId(UUID.fromString(campaignId));
        return new DonationStatisticsResponseDto(totalDonations, totalAmount);
    }

    public DonationResponseDto getDonation(String donationId, String userId, String userRole) {
        Donation donation = donationRespository.findById(UUID.fromString(donationId)).orElseThrow(() -> new ResourceNotFoundException("Donation not found!"));
        if ("ADMIN".equals(userRole)) {
            return donationMapper.generateDto(donation);
        }

        if ("CITIZEN".equals(userRole)
                && userId.equals(donation.getDonorId().toString())) {
            return donationMapper.generateDto(donation);
        }

        if ("NGO_REP".equals(userRole)
                && campaignClient.checkOwnership(donation.getCampaignId().toString(), userId)) {
            return donationMapper.generateDto(donation);
        }

        throw new ResourceNotFoundException("Donation not found!");
    }

    public Boolean isAlreadyDonated(String campaignId, String citizenId) {
        return donationRespository.existsByCampaignIdAndDonorId(UUID.fromString(campaignId), UUID.fromString(citizenId));
    }

    public PageResponseDto<DonationResponseDto> viewMyDonationsToCampaign(String campaignId, String citizenId, Pageable pageable) {
        Page<Donation> donations = donationRespository.findAllByCampaignIdAndDonorId(UUID.fromString(campaignId), UUID.fromString(citizenId), pageable);
        Page<DonationResponseDto> donationResponseDtos = donations.map(donationMapper::generateDto);
        return PageMapper.generateResponseDto(donationResponseDtos);
    }
}
