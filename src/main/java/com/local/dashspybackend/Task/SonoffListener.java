package com.local.dashspybackend.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.local.dashspybackend.Service.SonoffListenerService;
import com.local.dashspybackend.Singleton.MockCacheData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
public class SonoffListener {
    private static final Logger log = LoggerFactory.getLogger(SonoffListener.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private SonoffListenerService service;
    @Autowired
    private WebSocketConnectionManager socketManager;
 
    @EventListener
    @Async
    public void listen(ContextRefreshedEvent event){
        // taskExecutor.execute(service);
        socketManager.start();


    }

}
