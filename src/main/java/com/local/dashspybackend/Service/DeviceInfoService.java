package com.local.dashspybackend.Service;

import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Entity.LightStateEntity;
import com.local.dashspybackend.Exceptions.DeviceExistException;
import com.local.dashspybackend.Repository.IDeviceInfoRepo;
import com.local.dashspybackend.DTO.DeviceInfoCreateResponseDTO;
import com.local.dashspybackend.DTO.ToggleLightSwitchReqDTO;
import com.local.dashspybackend.DTO.ToggleLightSwitchResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceInfoService {
    @Autowired
    private IDeviceInfoRepo deviceInfoRepo;

    public List<DeviceInfoEntity> findAll() {

        return deviceInfoRepo.findAll();
    }

    public DeviceInfoCreateResponseDTO create(DeviceInfoEntity reqBody) {

        Optional<DeviceInfoEntity> currentRecord = deviceInfoRepo.findById(reqBody.getDeviceId());
        if (currentRecord.isPresent()) {
            throw new DeviceExistException(reqBody.getDeviceId());
        }
        DeviceInfoEntity savedData = deviceInfoRepo.save(reqBody);

        return new DeviceInfoCreateResponseDTO(savedData.getDeviceId(), true);

    }

    public String test() {
        return "";
    }

    public List<DeviceInfoEntity> getAllDevice() {
        return deviceInfoRepo.findAll();
    }

}
