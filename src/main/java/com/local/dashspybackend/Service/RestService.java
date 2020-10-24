package com.local.dashspybackend.Service;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.local.dashspybackend.DTO.EdgeDeviceResponseDTO;
import com.local.dashspybackend.DTO.PollDeviceStatusRespDTO;
import com.local.dashspybackend.DTO.SonoffSwitchReqDTO;
import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Util.ParseJSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import lombok.Data;
import reactor.core.publisher.Mono;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    @Autowired
    private ParseJSON parser;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String resetMotionDetectionHardware() {

        String offUri = String.format("http://192.168.1.5:8080/settings/motion_detect?set=off");
        RequestHeadersSpec<?> requestSpec1 = WebClient.create().get().uri(URI.create(offUri));

        String response1 = requestSpec1.retrieve().bodyToMono(String.class).block();
        String onUri = String.format("http://192.168.1.5:8080/settings/motion_detect?set=on");
        RequestHeadersSpec<?> requestSpec2 = WebClient.create().get().uri(URI.create(onUri));
        String response2 = requestSpec2.retrieve().bodyToMono(String.class).block();

        return response2;
    }

    public String toggleFlashLight(boolean isOnLight) {
        String flashLightCommand;
        if (isOnLight) {
            flashLightCommand = "disable";
        } else {
            flashLightCommand = "enable";

        }
        String absUri = String.format("http://192.168.1.5:8080/%storch", flashLightCommand);
        RequestHeadersSpec<?> requestSpec2 = WebClient.create().get().uri(URI.create(absUri));

        String response = requestSpec2.retrieve().bodyToMono(String.class).block();

        return response;
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
        RequestHeadersSpec<?> requestSpec2 = WebClient.create().post().uri(URI.create(absUri))
                .body(BodyInserters.fromValue(req));

        Mono<String> responseMono = requestSpec2.retrieve().bodyToMono(String.class);
        responseMono.subscribe(resp -> {
            EdgeDeviceResponseDTO data = parser.parse(resp, EdgeDeviceResponseDTO.class);
            System.out.println(data);

        }, err -> {
            System.err.println("CAUGHT " + err.getMessage());
        });
    }

    public String getPostsPlainJSON() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        return this.restTemplate.getForObject(url, String.class);
    }

    public String createPost() {
        String url = "http://192.168.1.2:8081/zeroconf/switch";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("sequence", "1601340508363");
        map.put("deviceid", "10006c0aff");
        map.put("selfApikey", "123");
        map.put("iv", "JI1HulKTTms06VNtIAwbzw==");
        map.put("encrypt", "true");
        map.put("data", "Kf6MAn6McL1m4BitCHWdcV7OO+aj7YRTlljheHTj55o=");

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
    }
}