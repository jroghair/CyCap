package com.cycapservers.game;

import java.util.ArrayList;

import org.springframework.web.socket.WebSocketSession;

public class LobbyManager {

	private volatile ArrayList<Lobby> lobbys;
	
	public LobbyManager(){
		
	}
	
	public void getMessage(WebSocketSession session, String message){
		String[] arr = message.split(":");
		if(arr[0].equals("join")) {
			if(arr[1].equals("Death")){
				
			}
			else if(arr[1].equals("Capture")){
				
			}
			else{
				
			}
		}
	}
	
	
	
	
	
	//need to associate game lobbys with specific game from gameManager
	
	
	
}
