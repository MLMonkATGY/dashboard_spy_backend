package com.local.dashspybackend.Service;

import com.local.dashspybackend.DTO.EdgeDeviceResponseDTO;
import com.local.dashspybackend.DTO.SonoffSwitchReqDTO;
import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Repository.IDeviceInfoRepo;
import com.local.dashspybackend.Util.ParseJSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import reactor.core.publisher.Mono;

@Service
public class ToggleSonoffSwitchService {
    @Autowired
    private RestService restService;

    @Autowired
    private IDeviceInfoRepo deviceInfoRepo;
    private final RestTemplate restTemplate;

    @Autowired
    private ParseJSON parser;

    public ToggleSonoffSwitchService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void toggleSwitch(int batteryLevel) {
        DeviceInfoEntity targetDevice = deviceInfoRepo.findFirstByDeviceName("laptop_charger");
        if (batteryLevel < 25) {
            this.sendSwitch(true, targetDevice);

        } else if (batteryLevel > 99) {
            this.sendSwitch(false, targetDevice);

        }
    }

    public void sendSwitch(boolean targetState, DeviceInfoEntity entity) {
        SonoffSwitchReqDTO req = new SonoffSwitchReqDTO();
        if (targetState) {
            req.setData(entity.getEncryptedPayloadOn());
            req.setIv(entity.getInitializationVectorOn());

        } else {
            req.setData(entity.getEncryptedPayloadOff());
            req.setIv(entity.getInitializationVectorOff());
        }
        req.setSequence("1601340508363");
        req.setSelfApikey(entity.getSelfApi());
        req.setEncrypt(true);
        req.setDeviceid(entity.getDeviceId());
        String absUri = String.format("http://%s:8081/zeroconf/switch", entity.getLocalAddress());
        Mono<String> responseMono = WebClient.create().post().uri(absUri).body(BodyInserters.fromValue(req))
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class);
        ;

        responseMono.subscribe(resp -> {
            EdgeDeviceResponseDTO data = parser.parse(resp, EdgeDeviceResponseDTO.class);
            System.out.println(data);

        }, err -> {
            System.err.println("CAUGHT " + err.getMessage());
        });
    }
}
