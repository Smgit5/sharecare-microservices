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

    private void seedStatus(String status) {
        if(!statusRepository.existsByName(status)) {
            statusRepository.save(new CampaignStatus(status));
        }
    }
    private void seedCategory(String category) {
        if(!categoryRepository.existsByName(category)) {
            categoryRepository.save(new CampaignCategory(category));
        }
    }
    private void seedLocation(String city, String district, String state) {
        if(!locationRepository.existsByCityAndDistrictAndState(city, district, state)) {
            locationRepository.save(new Location(city, district, state, null));
        }
    }

    @Override
    public void run(String... args) {
        seedStatus("PENDING_APPROVAL");
        seedStatus("ACTIVE");
        seedStatus("FULLY_FUNDED");
        seedStatus("REJECTED");
        seedStatus("CLOSED");
        seedStatus("SENT_BACK");

        seedCategory("MEDICAL");
        seedCategory("DISASTER_RELIEF");
        seedCategory("FOOD");
        seedCategory("EDUCATION");
        seedCategory("COMMUNITY_SUPPORT");

        seedLocation("Bhubaneswar", "Khordha", "Odisha");
        seedLocation("Balasore", "Balasore", "Odisha");
        seedLocation("Puri", "Puri", "Odisha");
        seedLocation("Cuttack", "Cuttack", "Odisha");
    }
}