package com.local.dashspybackend.DTO;

import lombok.Data;

@Data
public class DeviceInfoCreateReqDTO {

    private String deviceId;
    private String deviceName;
    private String localAddress;
    private String apiKey;
    private String initializationVectorOn;
    private String initializationVectorOff;
    private String encryptedPayloadOn;
    private String encryptedPayloadOff;
    private String selfApi;
}
