package com.local.dashspybackend.Singleton;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Entity.LightStateEntity;
import com.local.dashspybackend.Repository.IDeviceInfoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import lombok.Data;

@Component
public class MockCacheData {
    private ConcurrentLinkedQueue<DeviceInfoEntity> unclaimedDeviceList;
    private Map<String, DeviceInfoEntity> currentDeviceState;
    private Map<String, Long> lastTriggeredTime;

    @Autowired
    private IDeviceInfoRepo repo;

    @PostConstruct
    public void getCache() {
        this.unclaimedDeviceList = new ConcurrentLinkedQueue<>();
        this.currentDeviceState = new ConcurrentHashMap<>();
        this.lastTriggeredTime = new ConcurrentHashMap<>();
        List<DeviceInfoEntity> allDevices = repo.findAll();
        new Timestamp(System.currentTimeMillis());
        for (DeviceInfoEntity device : allDevices) {
            Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
            this.unclaimedDeviceList.add(device);
            currentDeviceState.put(device.getDeviceId(), device);
            lastTriggeredTime.put(device.getDeviceId(), currentTimeStamp.getTime());

        }

    }

    // public DeviceInfoEntity getDevice() {
    // return unclaimedDeviceList.poll();
    // }

    public DeviceInfoEntity[] getAllDevice() {
        return unclaimedDeviceList.toArray(new DeviceInfoEntity[unclaimedDeviceList.size()]);
    }

    public int getDeviceNum() {
        return unclaimedDeviceList.size();
    }

    public void updateDeviceState(String id, boolean lightState) {
        DeviceInfoEntity targetEntity = currentDeviceState.get(id);
        LightStateEntity currentState = new LightStateEntity();
        targetEntity.setSwitchState(lightState);
        currentState.setLightState(lightState);
        currentState.setDevice(targetEntity);
        List<LightStateEntity> previousStates = targetEntity.getStates();
        previousStates.add(currentState);
        targetEntity.setStates(previousStates);
        repo.save(targetEntity);

    }

    public void updateIp(String id, String ip) {
        DeviceInfoEntity targetEntity = currentDeviceState.get(id);
        if (!ip.equals(targetEntity.getLocalAddress())) {
            targetEntity.setLocalAddress(ip);
            repo.save(targetEntity);
        }

    }

    public long getDeviceLastTriggeredTime(String deviceId) {
        return this.lastTriggeredTime.get(deviceId);
    }

    public void setDeviceLastTriggeredTime(String deviceId, long updatedTime) {
        this.lastTriggeredTime.put(deviceId, updatedTime);
    }

}
