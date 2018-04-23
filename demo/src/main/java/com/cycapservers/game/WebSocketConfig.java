package com.cycapservers.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Bean
	public WebSocketHandler MessageHandler() {
		return new MessageHandler();
	}

	@Bean
	public WebSocketHandler ProfileScreen() {
		return new MessageHandler();
	}

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(MessageHandler(), "/my-websocket-endpoint");
		registry.addHandler(ProfileScreen(), "/profilescreen-websocket-endpoint");
	}

}
