package CyCapServer.CyCap;

import java.io.IOException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MessageHandler extends TextWebSocketHandler {
	
	public volatile GameManager game = new GameManager();
	
	/*
	 * 1. Look into thread local variables;
	 * 2. fix the scheduler
	 * 3. make sure bryans game works with the basic code
	 * 
	 * 
	 */
    
	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.print("Socket Closed");
    }
	
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("You are now connected to the server. This is the first message."));
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        System.out.println("Message received: " + textMessage.getPayload());
        game.getMessage(textMessage.getPayload());
        for(int i = 0; i < game.getPlayers(); i++){
        	session.sendMessage(new TextMessage(game.playerString(i)));
        }
    }
    
    
}

