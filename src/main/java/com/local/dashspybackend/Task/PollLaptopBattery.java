package com.local.dashspybackend.Task;

import com.local.dashspybackend.DTO.BatteryLevelRespDTO;
import com.local.dashspybackend.Entity.LocalDeviceAddressInfoEntity;
import com.local.dashspybackend.Repository.ILocalDeviceAddressInfoRepo;
import com.local.dashspybackend.Service.ToggleSonoffSwitchService;
import com.local.dashspybackend.Util.ParseJSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class PollLaptopBattery {
    @Autowired
    private ToggleSonoffSwitchService toggleSonoffSwitchService;
    // @Autowired
    // private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private ParseJSON parser;
    @Autowired
    private ILocalDeviceAddressInfoRepo deviceAddressInfoRepo;

    @Scheduled(fixedDelay = 60 * 1000)
    public void checkLaptopBattery() {
        LocalDeviceAddressInfoEntity deviceMapping = deviceAddressInfoRepo.findByBroadcastService("gateway");
        if (deviceMapping == null) {
            return;

        }
        String url = "http://" + deviceMapping.getLocalAddress() + ":7878/batteryLevel";

        Mono<String> tweetFlux = WebClient.create().get().uri(url).accept(MediaType.APPLICATION_JSON).retrieve()
                .bodyToMono(String.class);
        tweetFlux.subscribe(resp -> {
            BatteryLevelRespDTO batteryLevelDTO = parser.parse(resp, BatteryLevelRespDTO.class);
            toggleSonoffSwitchService.toggleSwitch(batteryLevelDTO.getBatteryLevel());
        }, err -> {
            System.err.println("CAUGHT " + err.getMessage());
        });
    }
}
