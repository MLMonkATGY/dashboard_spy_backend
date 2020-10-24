package com.local.dashspybackend.Service;

import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Repository.IDeviceInfoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToggleSonoffSwitchService {
    @Autowired
    private RestService restService;

    @Autowired
    private IDeviceInfoRepo deviceInfoRepo;

    public void toggleSwitch(int batteryLevel) {
        DeviceInfoEntity targetDevice = deviceInfoRepo.findFirstByDeviceName("laptop_charger");
        if (batteryLevel < 20) {
            restService.sendSwitch(true, targetDevice);

        } else if (batteryLevel > 99) {
            restService.sendSwitch(false, targetDevice);

        }
    }
}
