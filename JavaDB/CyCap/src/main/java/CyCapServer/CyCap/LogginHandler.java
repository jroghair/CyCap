package CyCapServer.CyCap;

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
public class LogginHandler extends TextWebSocketHandler{

	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.print("Socket Closed");
    }
	
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	System.out.println("Using me");
        session.sendMessage(new TextMessage("You are now connected to the server. This is the first message."));
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        System.out.println("Message received: " + textMessage.getPayload());
    }
    

}
