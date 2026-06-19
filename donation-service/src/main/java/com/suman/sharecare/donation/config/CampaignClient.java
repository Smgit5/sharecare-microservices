package com.suman.sharecare.donation.config;

import com.suman.sharecare.donation.dto.campaign_donation_dtos.CampaignAmountUpdateRequestDto;
import com.suman.sharecare.donation.dto.campaign_donation_dtos.CampaignDonationCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class CampaignClient {
    private final RestClient.Builder restClientBuilder;

    public CampaignClient(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    public CampaignDonationCheckResponseDto checkIfDonationAllowed(String campaignId) {
        return restClientBuilder.build()
                .get()
                .uri("http://CAMPAIGN-SERVICE/campaigns/{campaignId}/donation-check", campaignId)
                .retrieve()
                .body(CampaignDonationCheckResponseDto.class);
    }

    public void updateRaisedAmount(String campaignId, BigDecimal amount) {
        restClientBuilder.build()
            .patch()
            .uri("http://CAMPAIGN-SERVICE/campaigns/{campaignId}/raised-amount", campaignId)
            .body(new CampaignAmountUpdateRequestDto(amount))
            .retrieve()
            .toBodilessEntity();
    }

    public Boolean checkOwnership(String campaignId, String userId) {
        return restClientBuilder.build()
                .get()
                .uri("http://CAMPAIGN-SERVICE/campaigns/{campaignId}/ownership-check?userId={userId}", campaignId, userId)
                .retrieve()
                .body(Boolean.class);
    }
}
