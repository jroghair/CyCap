package com.cycapservers.game;

import java.util.ArrayList;

public class LobbyManager {

	private volatile ArrayList<Lobby> lobbys;
	
	public LobbyManager(){
		
	}
	
	public void addPlayer(Lobby lob, Player p){
		lob.addPlayer(p);
	}
	
	public void removePlayer(Lobby lob, Player p){
		lob.removePlayer(p);
	}
	
	
	
	//need to associate game lobbys with specific game from gameManager
	
	
	
}
