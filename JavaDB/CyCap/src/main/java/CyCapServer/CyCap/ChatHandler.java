package CyCapServer.CyCap;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.socket.WebSocketSession;

public class ChatHandler {

	ArrayList<ChatSession> chats = new ArrayList<ChatSession>();
	
	
	public ChatHandler(){
		
	}
	
	
	public void distributeMessage(String sessionId) throws IOException{
		for(int i = 0; i < chats.size(); i++){
			if(chats.get(i).GetID().equals(sessionId)){
				chats.get(i).SendMostRecentMessage();
			}
		}
	}
	
	public void addToChat(WebSocketSession s, String SessionId){
		int i = 0;
		for(i = 0; i < chats.size(); i++){
			if(chats.get(i).GetID().equals(SessionId)){
				chats.get(i).addSession(s);
				break;
			}
		}
	}
	
	public String makeNewChatSession(){
		ChatSession chat = new ChatSession();
		this.chats.add(chat);
		return chat.GetID();
	}
	
	public void addMessage(String ChatID, Message m){
		for(int i = 0; i < chats.size(); i++){
			if(chats.get(i).GetID().equals(ChatID)){
				chats.get(i).addMessage(m);
			}
		}
	}
	
	public void parseMessage(String message, WebSocketSession s) throws IOException{
		int i = 0;
		String mess[] = message.split(",");
		if(mess[0].equals("add")){
			for(i = 0; i < chats.size(); i++){
				if(chats.get(i).GetID().equals(mess[1])){
					chats.get(i).addSession(s);
					break;
				}
			}
		}
		else{
			for(i = 0; i < chats.size(); i++){
				if(chats.get(i).GetID().equals(mess[0])){
					chats.get(i).addAndCreateMessage(mess[1], mess[2]);
					chats.get(i).SendMostRecentMessage();
					break;
				}
			}
		}
	}
	
}
