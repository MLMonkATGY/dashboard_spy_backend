package com.local.dashspybackend.DTO;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class PollDeviceStatusRespDTO {

    private ArrayList<DeviceStateRespDTO> devices;
}
