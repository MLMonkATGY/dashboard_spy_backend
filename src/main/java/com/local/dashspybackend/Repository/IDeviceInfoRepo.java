package com.local.dashspybackend.Repository;

import java.util.List;

import com.local.dashspybackend.Entity.DeviceInfoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IDeviceInfoRepo extends  JpaRepository<DeviceInfoEntity, String> {
    public DeviceInfoEntity findFirstByDeviceName(String deviceName);
    
}
