package com.suman.sharecare.campaign.utility;

import com.suman.sharecare.campaign.entity.CampaignCategory;
import com.suman.sharecare.campaign.entity.CampaignStatus;
import com.suman.sharecare.campaign.entity.Location;
import com.suman.sharecare.campaign.repository.CategoryRepository;
import com.suman.sharecare.campaign.repository.StatusRepository;
import com.suman.sharecare.campaign.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final StatusRepository statusRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public void run(String... args) {
        if (statusRepository.count() == 0) {
            statusRepository.save(new CampaignStatus("PENDING_APPROVAL"));
            statusRepository.save(new CampaignStatus("ACTIVE"));
            statusRepository.save(new CampaignStatus("FULLY_FUNDED"));
            statusRepository.save(new CampaignStatus("REJECTED"));
            statusRepository.save(new CampaignStatus("CLOSED"));
        }

        if (categoryRepository.count() == 0) {
            categoryRepository.save(new CampaignCategory("MEDICAL"));
            categoryRepository.save(new CampaignCategory("DISASTER_RELIEF"));
            categoryRepository.save(new CampaignCategory("FOOD"));
            categoryRepository.save(new CampaignCategory("EDUCATION"));
            categoryRepository.save(new CampaignCategory("COMMUNITY_SUPPORT"));
        }

        if (locationRepository.count() == 0) {
            locationRepository.save(new Location("Bhubaneswar", "Khordha", "Odisha", null));
            locationRepository.save(new Location("Balasore", "Balasore", "Odisha", null));
            locationRepository.save(new Location("Puri", "Puri", "Odisha", null));
            locationRepository.save(new Location("Cuttack", "Cuttack", "Odisha", null));
        }
    }
}