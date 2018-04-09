package com.cycapservers.account;


import com.cycapservers.account.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
/** Chat controller for cycap general chat
 * @author Jeremy 
 * */
@Controller
public class ChatController {
	
	/**Chat handler method that sends and recieves chat messages
	 * @param ChatMessage message to be sent to the chat window
	 * @return ChatMessage message containing event
	 * */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    /**Chat handler for user add/leave events within chat
     * @param ChatMessage message for either a join or leave event
     * @param headerAccessor 
     * @return ChatMessage message containing the event
     * */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                               SimpMessageHeaderAccessor headerAccessor) {
    	
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}