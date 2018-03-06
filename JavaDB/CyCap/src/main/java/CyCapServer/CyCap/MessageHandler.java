package CyCapServer.CyCap;

import java.io.IOException;

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
	
	public volatile GameManager game = new GameManager();
	
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
		System.out.print("Socket Closed");
    }
	
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("You are now connected to the server. This is the first message."));
        Connect.set(1);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        //System.out.println("Message received: " + textMessage.getPayload());
    	if(Connect.get() != null){
    		if(Connect.get().equals(1)){
        		Connect.set(0);
        		String m = textMessage.getPayload();
        		UserId.set(m.split(",")[0]);
        		System.out.println(UserId.get());
        	}
    	}
        game.getMessage(textMessage.getPayload());
        System.out.println(game.getPlayers());
        for(int i = 0; i < game.getPlayers(); i++){
        	session.sendMessage(new TextMessage(game.playerString(i)));
        }
    }
    
    
}

