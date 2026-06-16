package com.suman.sharecare.campaign.dto.status_dtos;

import com.suman.sharecare.campaign.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusRequestDto {
    private Status status;
}
