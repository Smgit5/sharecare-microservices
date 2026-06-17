package com.suman.sharecare.donation.service;

import com.suman.sharecare.donation.entity.DonationStatus;
import com.suman.sharecare.donation.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public DonationStatus getStatusByName(String status) {
        return statusRepository.findByStatus(status).orElseThrow(() -> new RuntimeException("Status not found!"));
    }
}
