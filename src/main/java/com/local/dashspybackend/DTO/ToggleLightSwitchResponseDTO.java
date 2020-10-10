package com.local.dashspybackend.DTO;

import lombok.Data;

@Data
public class ToggleLightSwitchResponseDTO {
    private boolean isSuccess;
    private boolean currentLightState;
    private int seq;
}
