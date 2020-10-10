package com.local.dashspybackend;

import com.local.dashspybackend.Service.SonoffListenerService;
import com.local.dashspybackend.Service.WebsocketClient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DashSpyBackendApplication {

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	public static void main(String[] args) {
		SpringApplication.run(DashSpyBackendApplication.class, args);
	}


}
