package com.local.dashspybackend.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.local.dashspybackend.DTO.BroadcastDiscoveryRespDTO;
import com.local.dashspybackend.DTO.DeviceInfoCreateReqDTO;
import com.local.dashspybackend.Entity.LocalDeviceAddressInfoEntity;
import com.local.dashspybackend.Repository.ILocalDeviceAddressInfoRepo;
import com.local.dashspybackend.Service.SonoffListenerService;
import com.local.dashspybackend.Singleton.MockCacheData;
import com.local.dashspybackend.Util.ParseJSON;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BroadcastDeviceDiscovery {

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private SonoffListenerService service;
    private final RestTemplate restTemplate;
    private int isFound;

    @Autowired
    private ObjectMapper modelMapper;
    @Autowired
    private ILocalDeviceAddressInfoRepo addressInfoRepo;
    @Autowired
    private ParseJSON parser;
    private RestTemplateBuilder aaaaa;
    public int currentNum;

    public BroadcastDeviceDiscovery(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.aaaaa = restTemplateBuilder;
        this.isFound = 0;
        this.currentNum = 0;
    }

    public class InnerBroadcastDeviceDiscovery implements Runnable {
        public void run() {
            var i = BroadcastDeviceDiscovery.this.currentNum++;
            System.out.println(i);
            // BroadcastDeviceDiscovery.this.getPostsPlainJSON(Integer.toString(i));
        }

    }

    @Async
    public void testThread() {
        System.out.println("ahah");
    }

    @Async
    public void getPostsPlainJSON(String ip4, RestTemplateBuilder restTemplateBuilder) {
        String currentIp = "http://192.168.1." + ip4;
        String currentPort = "7878";

        String url = currentIp + ":" + currentPort;
        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        try {
            var resp = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            var b = resp.getBody();
            System.out.println(b.toString());
        } catch (Exception e) {
            // TODO: handle exception
        }

        // return b;
    }

    public void asyncGetDeviceInfo(String ip4) {
        String currentIp = "http://192.168.1." + ip4;
        String currentPort = "7878";

        String url = currentIp + ":" + currentPort;
        try {
            Mono<String> tweetFlux = WebClient.create().get().uri(url).accept(MediaType.APPLICATION_JSON).retrieve()
                    .bodyToMono(String.class);
            tweetFlux.subscribe(resp -> {
                BroadcastDiscoveryRespDTO receivedPayload = parser.parse(resp, BroadcastDiscoveryRespDTO.class);
                System.out.println(receivedPayload);
                LocalDeviceAddressInfoEntity entity = new LocalDeviceAddressInfoEntity();
                entity.setBroadcastService(receivedPayload.getBroadcastService());
                entity.setLocalAddress(receivedPayload.getLocalAddress());
                entity.setMac(receivedPayload.getDeviceId());
                addressInfoRepo.save(entity);
            }, err -> {
                // System.err.println("CAUGHT " + err.getMessage());
            });

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Scheduled(fixedDelay = 1000 * 60 * 3)
    public void listen() {
        for (int i = 2; i < 253; i++) {
            try {
                // taskExecutor.execute(new InnerBroadcastDeviceDiscovery());
                this.asyncGetDeviceInfo(Integer.toString(i));
            } catch (Exception e) {
                // TODO: handle exception
            }
            System.out.println(i);

        }
        System.out.println("all done discovering");

    }

}
