package com.local.dashspybackend.Config;

import com.local.dashspybackend.Service.WebsocketClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebsocketConfig {
    
    @Lazy
    @Autowired
    private WebsocketClient handler;
    private final String webSocketUri = "ws://192.168.1.9:8765";

    @Bean
    public WebSocketConnectionManager wsConnectionManager(WebsocketClient handler) {

    	//Generates a web socket connection
    	WebSocketConnectionManager manager = new WebSocketConnectionManager(
    			new StandardWebSocketClient(),
    			handler, //Must be defined to handle messages
    			this.webSocketUri);
    	
    	//Will connect as soon as possible
    	manager.setAutoStartup(true);
    	
    	return manager;
    }
}
