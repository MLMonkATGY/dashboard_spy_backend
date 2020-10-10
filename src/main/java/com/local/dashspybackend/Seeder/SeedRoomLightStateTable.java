package com.local.dashspybackend.Seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Entity.LightStateEntity;
import com.local.dashspybackend.Repository.IDeviceInfoRepo;
import com.local.dashspybackend.Repository.ILightStateRepo;
@Component
public class SeedRoomLightStateTable {
    @Autowired
    private IDeviceInfoRepo repo;
    @Autowired
    private ILightStateRepo lightRepo;
    @EventListener
    public void seed(ContextRefreshedEvent event){
        seedDevice();
       

    }
    public void seedDevice(){
        if(repo.count() == 0){
            DeviceInfoEntity d1 = 
            new DeviceInfoEntity();
            d1.setApiKey("f6b401fb-028d-4009-ab16-eec64e529b16");
            

            d1.setDeviceId("10006c0aff");
            d1.setDeviceName("light switch");
            d1.setLocalAddress("192.168.1.15");
            d1.setInitializationVectorOn("XhWfC4JmNbovXdQ4IoJqSw==");
            d1.setEncryptedPayloadOn("atwd4pAfXVAwcmsht0isEQ==");
            d1.setInitializationVectorOff("OehnhgrhYVpwTZBquvf8Ug==");
            d1.setEncryptedPayloadOff("x0jSlXZnHz8a4fFlegAtHHHPcHv66/RX8xmw8JMi9mw=");
            d1.setSelfApi("123");
            DeviceInfoEntity d2 = 
            new DeviceInfoEntity();
            d2.setApiKey("6a55cae4-ba5d-4377-b941-247eb2036477");
            d2.setDeviceId("100073bfba");
            d2.setDeviceName("room_light");
            d2.setLocalAddress("192.168.1.14");
            d2.setInitializationVectorOn("5pZnlWC7KdPyFZ00UzwApQ==");
            d2.setEncryptedPayloadOn("Dz0N23alAgDlFKpBgyqwIA==");
            d2.setInitializationVectorOff("JI1HulKTTms06VNtIAwbzw==");
            d2.setEncryptedPayloadOff("Kf6MAn6McL1m4BitCHWdcV7OO+aj7YRTlljheHTj55o=");
            d2.setSelfApi("123");
            repo.save(d1);
            repo.save(d2);
            generateSeedData(2, "10006c0aff");
            generateSeedData(2, "100073bfba");
        }

    }
    public void generateSeedData(int seedNum, String deviceId){
        List<DeviceInfoEntity> devices =  repo.findAll();
        
        for (DeviceInfoEntity device : devices) {
            List<LightStateEntity> a = new ArrayList<>();

            for(int i = 0 ; i< seedNum; i++){
                LightStateEntity mockData = new LightStateEntity();
                mockData.setSeq(i);
                mockData.setLightState(Math.random()>0.5);
                mockData.setDevice(device);
                a.add(mockData);
               
            }   

            device.setStates(a);
            repo.save(device);
        }
        
    }
}
