package com.local.dashspybackend.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Entity.LightStateEntity;
import com.local.dashspybackend.Repository.IDeviceInfoRepo;
import com.local.dashspybackend.Repository.ILightStateRepo;
import com.local.dashspybackend.Singleton.MockCacheData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class LightStateService {
    @Autowired
    private ILightStateRepo lightStateRepo;
    @Autowired
    private IDeviceInfoRepo deviceRepo;
    @Autowired
    private RestService senderService;
    @Autowired
    private MockCacheData cacheData;
    public List<LightStateEntity> findAll(){
        return lightStateRepo.findAll();
    }
    public boolean toggleLightSwitch(String deviceName, int timeoutDuration){
        DeviceInfoEntity device = deviceRepo.findFirstByDeviceName(deviceName);
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        long currentAttemptTime = currentTimeStamp.getTime();
        if(currentAttemptTime - cacheData.getDeviceLastTriggeredTime(device.getDeviceId()) >  timeoutDuration ){
            senderService.sendSwitch(!device.getSwitchState(), device);
            cacheData.setDeviceLastTriggeredTime(device.getDeviceId(), currentAttemptTime);
            System.out.println("Toggle attempt successful executed");
            return true;

        }else{

            System.out.println("Toggle still in timeout");
            return false;

        }
    }
    public boolean toggleLightSwitchManual(String deviceName){
        return this.toggleLightSwitch(deviceName, 0);
    }
    public boolean toggleLightSwitchAuto(String deviceName){
        return this.toggleLightSwitch(deviceName, 20*1000);
    }

}
