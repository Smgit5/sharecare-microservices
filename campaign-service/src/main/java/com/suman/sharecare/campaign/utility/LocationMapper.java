package com.suman.sharecare.campaign.utility;

import com.suman.sharecare.campaign.dto.location_dtos.LocationResponseDto;
import com.suman.sharecare.campaign.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationResponseDto toDto(Location location);
}
