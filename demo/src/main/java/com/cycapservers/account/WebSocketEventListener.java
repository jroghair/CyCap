package com.cycapservers.account;


import com.cycapservers.account.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/** Websocket Event listener for chat system 
 *@author Jeremy 
 * */
@Component
public class WebSocketEventListener {
	   /**Creates a logger that generates messages in the applications log
     * */
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    /**Auto wires a messageTemplate
     * */
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**Listens for event for a websocket connection
     * @param event received event
     * @return void
     * */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }
    
    /**Event listener for user disconnections 
     * @return event recieved event
     * @return void 
     * */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
