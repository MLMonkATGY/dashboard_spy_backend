package com.local.dashspybackend.DTO;

import lombok.Data;

@Data
public class DeviceInfoCreateResponseDTO {
    private String deviceId;
    private boolean isSuccess;
    public DeviceInfoCreateResponseDTO(String deviceId,boolean isSuccess){
        this.isSuccess = isSuccess;
        this.deviceId = deviceId;
        
    }
}
