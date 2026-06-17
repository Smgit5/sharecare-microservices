package com.suman.sharecare.donation.util;

import com.suman.sharecare.donation.dto.DonationRequestDto;
import com.suman.sharecare.donation.dto.DonationResponseDto;
import com.suman.sharecare.donation.entity.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DonationMapper {
    @Mapping(source = "status.status", target = "status")
    DonationResponseDto generateDto(Donation donation);

    Donation generateEntity(DonationRequestDto dto);
}
