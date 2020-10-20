package com.local.dashspybackend.Task;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.local.dashspybackend.DTO.BroadcastDiscoveryRespDTO;
import com.local.dashspybackend.DTO.DeviceStateReqDTO;
import com.local.dashspybackend.DTO.PollDeviceStatusReqDTO;
import com.local.dashspybackend.DTO.PollDeviceStatusRespDTO;
import com.local.dashspybackend.Singleton.MockCacheData;
import com.local.dashspybackend.Util.ParseJSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import reactor.core.publisher.Mono;

import java.util.Collections;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

@Component
public class PollDeviceStatus {
    @Autowired
    private MockCacheData dataInfo;
    @Autowired
    private ParseJSON parser;
    private final RestTemplate restTemplate;

    public PollDeviceStatus(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        // .defaultHeader(HttpHeaders.CONTENT_TYPE,
        // MediaType.APPLICATION_JSON_VALUE).build();
    }

    public void sendPostRequest(PollDeviceStatusReqDTO req) {

    }

    public String createPost(PollDeviceStatusReqDTO req) {
        String url = "http://localhost:7878/pollStatus";

        Mono<String> tweetFlux = WebClient.create().post().uri(url).body(BodyInserters.fromValue(req))

                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class);
        tweetFlux.subscribe(resp -> {
            PollDeviceStatusRespDTO data = parser.parse(resp, PollDeviceStatusRespDTO.class);
            System.out.println(data);

        }, err -> {
            System.err.println("CAUGHT " + err.getMessage());
        });
        return "a";
    }

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        var allDevice = dataInfo.getAllDevice();
        var resp = new PollDeviceStatusReqDTO();
        ArrayList<DeviceStateReqDTO> a = new ArrayList<DeviceStateReqDTO>();
        // resp.setDevices();
        for (int i = 0; i < allDevice.length; i++) {
            DeviceStateReqDTO info = new DeviceStateReqDTO();
            var device = allDevice[i];
            info.setApiKey(device.getApiKey());
            info.setDeviceId(device.getDeviceId());
            a.add(info);
        }
        resp.setDevices(a);
        this.createPost(resp);
        System.out.println("In here");
    }
}
