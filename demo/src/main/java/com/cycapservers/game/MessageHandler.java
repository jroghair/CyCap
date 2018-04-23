package com.cycapservers.game;

import java.io.IOException;

import javax.websocket.Session;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MessageHandler extends TextWebSocketHandler {
	
	private volatile GameManager gameManager = new GameManager();
	
	private ThreadLocal<String> UserId = new ThreadLocal<String>();
	
	private ThreadLocal<String> GameId = new ThreadLocal<String>();
	
	private ThreadLocal<Integer> Connect = new ThreadLocal<Integer>();
	
	/*
	 * 1. Look into thread local variables;
	 * 2. fix the scheduler
	 * 
	 * 
	 * 
	 */

    
	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("Socket Closed");
		gameManager.removePlayer(session);
    }
	
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Connect.set(1);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
    	if(Connect.get() != null){
    		if(Connect.get().equals(1)){
        		Connect.set(0);
        	}
    	}
    	gameManager.getMessage(session, textMessage.getPayload());
    	//check to see when the last game state was sent out, if more than 100ms, send now
    }
}