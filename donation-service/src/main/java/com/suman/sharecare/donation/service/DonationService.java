package com.suman.sharecare.donation.service;

import com.suman.sharecare.donation.config.CampaignClient;
import com.suman.sharecare.donation.dto.donation_dtos.DonationRequestDto;
import com.suman.sharecare.donation.dto.donation_dtos.DonationResponseDto;
import com.suman.sharecare.donation.dto.campaign_donation_dtos.CampaignDonationCheckResponseDto;
import com.suman.sharecare.donation.dto.donation_dtos.DonationStatisticsResponseDto;
import com.suman.sharecare.donation.dto.donation_dtos.PaymentStatusUpdateRequestDto;
import com.suman.sharecare.donation.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.donation.dto.page_dtos.PageResponseDto;
import com.suman.sharecare.donation.entity.Donation;
import com.suman.sharecare.donation.enums.DonationStatusNames;
import com.suman.sharecare.donation.exception.ActionNotAllowedException;
import com.suman.sharecare.donation.exception.ResourceNotFoundException;
import com.suman.sharecare.donation.repository.DonationRespository;
import com.suman.sharecare.donation.util.DonationMapper;
import com.suman.sharecare.donation.util.PageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonationService {
    private static final int MAX_ATTEMPT_PAYMENT = 3;
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
        donation.setStatus(statusService.getStatusByName(DonationStatusNames.PENDING.name()));
        donation.setPaymentReferenceId(UUID.randomUUID().toString());
        Donation pendingDonation = donationRespository.save(donation);
        return donationMapper.generateDto(pendingDonation);
    }

    public PageResponseDto<DonationResponseDto> viewMyDonationHistory(String donorId, Pageable pageable) {
        Page<Donation> donations = donationRespository.findAllByDonorId(UUID.fromString(donorId), pageable);
        Page<DonationResponseDto> donationResponseDtos = donations.map(donationMapper::generateDto);
        return PageMapper.generateResponseDto(donationResponseDtos);
    }

    public PageResponseDto<DonationResponseDto> getDonationHistoryOfCampaign(String campaignId, String userId, String userRoles, Pageable pageable) {
        if(!userRoles.contains("ADMIN") && !campaignClient.checkOwnership(campaignId, userId)) {
            throw new ResourceNotFoundException("Campaign not found!");
        }
        Page<Donation> donations = donationRespository.findByCampaignId(UUID.fromString(campaignId), pageable);
        Page<DonationResponseDto> donationResponseDtos = donations.map(donationMapper::generateDto);
        return PageMapper.generateResponseDto(donationResponseDtos);
    }

    public DonationStatisticsResponseDto getDonationStatistics(String campaignId, String userId, String userRoles) {
        if(!userRoles.contains("ADMIN") && !campaignClient.checkOwnership(campaignId, userId)) {
            throw new ResourceNotFoundException("Campaign not found!");
        }
        long totalDonations = donationRespository.countByCampaignId(UUID.fromString(campaignId));
        BigDecimal totalAmount = donationRespository.sumAmountByCampaignId(UUID.fromString(campaignId));
        return new DonationStatisticsResponseDto(totalDonations, totalAmount);
    }

    public DonationResponseDto getDonation(String donationId, String userId, String userRoles) {
        Donation donation = donationRespository.findById(UUID.fromString(donationId)).orElseThrow(() -> new ResourceNotFoundException("Donation not found!"));
        if (userRoles.contains("ADMIN")) {
            return donationMapper.generateDto(donation);
        }

        if (userRoles.contains("CITIZEN")
                && userId.equals(donation.getDonorId().toString())) {
            return donationMapper.generateDto(donation);
        }

        if (userRoles.contains("NGO_REP")
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

    public ApiResponseDto checkPaymentStatus(String paymentReferenceId, PaymentStatusUpdateRequestDto paymentStatusUpdateRequestDto) {
        Donation donation = donationRespository.findByPaymentReferenceId(paymentReferenceId).orElseThrow(() -> new ResourceNotFoundException("Donation not found!"));
        if(!DonationStatusNames.PENDING.name().equals(donation.getStatus().getStatus())) {
            throw new ActionNotAllowedException("Payment already processed");
        }
        String normalizedStatus = paymentStatusUpdateRequestDto.getPaymentStatus().toUpperCase();
        if(DonationStatusNames.FAILED.name().equals(normalizedStatus)) {
            donation.setStatus(statusService.getStatusByName(DonationStatusNames.FAILED.name()));
            donationRespository.save(donation);
            return new ApiResponseDto(HttpStatus.OK.value(), "Payment failed! Donation could not be complete.");
        } else if(DonationStatusNames.SUCCESS.name().equals(normalizedStatus)) {
            updateRaisedAmountWithRetry(donation.getCampaignId().toString(), donation.getAmount());
            donation.setStatus(statusService.getStatusByName(DonationStatusNames.SUCCESS.name()));
            donation.setPaidAt(LocalDateTime.now());
            donationRespository.save(donation);
            return new ApiResponseDto(HttpStatus.OK.value(), "Payment successful! Donation complete.");
        } else {
            throw new ActionNotAllowedException("Invalid payment status.");
        }
    }

    private void updateRaisedAmountWithRetry(String campaignId, BigDecimal amount) {
        int maxAttempt = MAX_ATTEMPT_PAYMENT;
        for(int attempt=1; attempt<=maxAttempt; attempt++) {
            try {
                campaignClient.updateRaisedAmount(campaignId, amount);
                return;
            } catch(HttpClientErrorException.Conflict ex) {
                if(attempt == maxAttempt) {
                    throw new ActionNotAllowedException("Payment was received, but donation processing is delayed. Please do not make another payment.");
                }
            }
        }
    }
}
