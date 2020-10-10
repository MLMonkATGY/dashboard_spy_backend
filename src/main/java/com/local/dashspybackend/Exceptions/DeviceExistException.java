package com.local.dashspybackend.Exceptions;

import lombok.Data;

@Data
public class DeviceExistException extends RuntimeException {

    public DeviceExistException(String deviceId){
        
        super(String.format("Device with id %s already existed", deviceId));
    }
}
