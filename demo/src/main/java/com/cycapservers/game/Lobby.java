package com.cycapservers.game;

import java.util.ArrayList;

public class Lobby {
	
	private ArrayList<Player> players;
	private int curSize;
	private int maxSize;
	private String gamemode;
	
	public Lobby(String gamemode){
		this.curSize=0;
		this.maxSize=0; 
		
	}
	
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public void removePlayer(Player p){
		players.remove(p);
	}
	
	public ArrayList<Player> getPlayersList(){
		return players; 
	}
	
	public int getOpenSlots(){
		return maxSize-curSize; 
	}
	
	
}
