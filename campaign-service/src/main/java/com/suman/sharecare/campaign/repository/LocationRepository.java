package com.suman.sharecare.campaign.repository;

import com.suman.sharecare.campaign.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    boolean existsByCityAndDistrictAndState(String city, String district, String state);
}
