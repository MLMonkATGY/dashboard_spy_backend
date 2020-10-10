package com.local.dashspybackend.Service;

import com.local.dashspybackend.DTO.DeviceManualToggleRespDTO;
import com.local.dashspybackend.Entity.DeviceInfoEntity;
import com.local.dashspybackend.Singleton.MockCacheData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Handle server connection.
 */
@Component
public class WebsocketClient implements WebSocketHandler {

  
   
    @Autowired
    private SonoffListenerService service;
    public WebsocketClient(SonoffListenerService service ){
        this.service = service;
    }
    /**
	 * Called when WS connects to the server.
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {		
        System.out.println("Connected");
        var t = this.service.initDeviceInfoForListener();
        var a = new TextMessage(t);
        session.sendMessage(a);		

	}	

	/**
	 * Main method to handle server messages.
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String b = message.getPayload().toString();
        System.out.println(b);
        var a = new TextMessage("aasdasdfasdv");
        session.sendMessage(a);
	}

	/**
	 * Error handling.
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error");		

	}

	/**
	 * Called when WS is closed.
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Closed");		

	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
    }
}
