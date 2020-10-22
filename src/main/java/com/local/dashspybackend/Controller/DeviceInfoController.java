package com.local.dashspybackend.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.local.dashspybackend.Service.DeviceInfoService;
import com.local.dashspybackend.Service.LightStateService;
import com.local.dashspybackend.Service.RestService;

import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.local.dashspybackend.DTO.DeviceInfoCreateReqDTO;
import com.local.dashspybackend.DTO.DeviceInfoCreateResponseDTO;
import com.local.dashspybackend.DTO.DeviceManualToggleRespDTO;
import com.local.dashspybackend.DTO.GetDeviceInfoDTO;
import com.local.dashspybackend.DTO.SocketMessageDTO;
import com.local.dashspybackend.DTO.ToggleLightSwitchReqDTO;
import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Entity.LightStateEntity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceInfoController {
  @Autowired
  private DeviceInfoService deviceInfoService;
  @Autowired
  private LightStateService lightStateService;
  @Autowired
  private RestService testService;
  @Autowired
  private ModelMapper modelMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DeviceInfoCreateResponseDTO getAllDevices(@RequestBody DeviceInfoCreateReqDTO req) {
    DeviceInfoEntity newData = modelMapper.map(req, DeviceInfoEntity.class);
    return deviceInfoService.create(newData);
  }

  @GetMapping(path = "/states")
  public List<LightStateEntity> getAllLightStates() {
    return lightStateService.findAll();
  }

  @PostMapping(path = "/switch")
  public DeviceManualToggleRespDTO toggleSwitch(@RequestBody ToggleLightSwitchReqDTO req) {
    DeviceManualToggleRespDTO resp = new DeviceManualToggleRespDTO();
    resp.setReqSuccess(lightStateService.toggleLightSwitchManual(req.getDeviceName()));
    return resp;
  }

  @GetMapping("/all")
  public List<GetDeviceInfoDTO> getAllDevices() {
    List<DeviceInfoEntity> allDevice = deviceInfoService.getAllDevice();
    List<GetDeviceInfoDTO> resp = new ArrayList<>();
    for (DeviceInfoEntity device : allDevice) {
      GetDeviceInfoDTO temp = new GetDeviceInfoDTO();
      temp.setDeviceId(device.getDeviceId());
      temp.setDeviceName(device.getDeviceName());
      temp.setState(device.getSwitchState());

      resp.add(temp);
    }
    return resp;
  }

}
