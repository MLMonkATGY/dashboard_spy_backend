package com.local.dashspybackend.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Singleton.MockCacheData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
public class SonoffListenerService {
    
    @Autowired
    private MockCacheData dataSource;
    @Autowired
    private RestService senderService;
   
    public void parser(String rawInput) {
        if (rawInput.contains(":")) {
            String[] updateData = rawInput.split(":");
            switch (updateData[0]) {
                case "connection":
                    dataSource.updateIp(updateData[1], updateData[2]);
                    break;
                case "state":
                    dataSource.updateDeviceState(updateData[1], updateData[2].equals("on"));
                    senderService.toggleFlashLight(updateData[2].equals("on"));
                    senderService.resetMotionDetectionHardware();
                    break;
                default:
                    System.out.println(rawInput);
            }
        }
    }

    public String initDeviceInfoForListener() {
        var allDevice = dataSource.getAllDevice();    
            String deviceInfoCliInput = "--";
            for (DeviceInfoEntity device_info : allDevice) {
                deviceInfoCliInput =  deviceInfoCliInput + device_info.getDeviceId() + ":" + device_info.getApiKey()+ "*";
            }
            String json = String.format("\'%s\'", deviceInfoCliInput);
            json = json.replaceAll("\\n+", "");
            String pythonPath = "py";
            String pPath = "C:\\Program Files\\WindowsApps\\PythonSoftwareFoundation.Python.3.8_3.8.1776.0_x64__qbz5n2kfra8p0\\python3.8.exe";
            return json;
    }

    // public void listen(){

    // }
    // @Override
    // public void run() {
    //     System.out.println("Not implemented");
    //     // listen();
    // }
}