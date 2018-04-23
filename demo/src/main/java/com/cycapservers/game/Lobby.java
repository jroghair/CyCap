package com.cycapservers.game;

import java.util.ArrayList;

public class Lobby {
	
	private GameState game;
	private ArrayList<String> players = new ArrayList<String>();
	private int curSize;
	private int maxSize;
	
	public Lobby(String gamemode){
		this.curSize=0;
		if(gamemode.equals("Death")){
			this.game = new Death();
		}
		else if(gamemode.equals("Capture")){
			this.game = new Capture();
		}
		else{
			this.game = new GameState();
		}
		this.maxSize = game.maxPlayers;
		
	}
	
	public int getCurrentSize(){
		return this.curSize;
	}
	
	public String getPlayer(int num){
		return this.players.get(num);
	}
	
	public String getId(){
		return game.game_id;
	}
	
	public boolean hasSpace(){
		if(this.curSize < this.maxSize){
			return true;
		}
		return false;
	}
	
	
	public GameState getGame(){
		return this.game;
	}
	
	public void addPlayer(String p){
		players.add(p);
		this.curSize++;
	}
	
	public void removePlayer(String p){
		players.remove(p);
		this.curSize--;
	}
	
	public ArrayList<String> getPlayersList(){
		return players; 
	}
	
	public int getOpenSlots(){
		return maxSize-curSize; 
	}
	
	
}
