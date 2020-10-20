package com.local.dashspybackend.DTO;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class PollDeviceStatusReqDTO {

    private ArrayList<DeviceStateReqDTO> devices;
}
