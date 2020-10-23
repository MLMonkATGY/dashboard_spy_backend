package com.local.dashspybackend.DTO;

import lombok.Data;

@Data
public class DeviceStateRespDTO {
    private String startup;
    private int pulseWidth;
    private int rssi;
    private String ssid;
    private String bssid;
    private String state;

}
