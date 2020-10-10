package com.local.dashspybackend.DTO;

import lombok.Data;

@Data
public class GetDeviceInfoDTO {
    private String deviceId;
    private String deviceName;
    private boolean state;

}
