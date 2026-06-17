package com.suman.sharecare.donation.util;

import com.suman.sharecare.donation.entity.DonationStatus;
import com.suman.sharecare.donation.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final StatusRepository statusRepository;

    private void seedStatus(List<String> statusList) {
        statusList.forEach(status -> {
            if(!statusRepository.existsByStatus(status)) {
                statusRepository.save(new DonationStatus(status));
            }
        });
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> statusList = List.of(
                "SUCCESS",
                "PENDING",
                "FAILED"
        );

        seedStatus(statusList);
    }
}
