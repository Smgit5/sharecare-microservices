package com.suman.sharecare.donation.repository;

import com.suman.sharecare.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DonationRespository extends JpaRepository<Donation, UUID> {
}
