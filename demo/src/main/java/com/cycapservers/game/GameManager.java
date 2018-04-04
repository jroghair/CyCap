package com.cycapservers.game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;


public class GameManager {
	
	private volatile ArrayList<GameState> games;
	
	//create player afk list that has the players time out after 30 seconds and get deleted
	
	private volatile ArrayList<String> removedPlayer = new ArrayList<String>();
	
	private long time = 0;
	private Timer timer;
	
	private boolean afkPlayers;
	
	static final int TOLERABLE_UPDATE_ERROR = 10; //IN MS
	static final int BULLET_WARNING_LEVEL = 250;
	static final int ADVANCED_BULLET_WARNING_LEVEL = 500;
	
	//Get a method to check last message.
	
	public GameManager(){
		timer = new Timer(true);
		games = new ArrayList<GameState>();
		games.add(new GameState());
		timer.scheduleAtFixedRate(games.get(0), 500, 100);
	}
	
	//Ask about sending purpose of message;
	public void getMessage(WebSocketSession session, String message){
		String[] arr = message.split(":");
		if(arr[0].equals("input")) {
			games.get(0).addInputSnap(new InputSnapshot(message));
		}
		else if(arr[0].equals("join")) {
			games.get(0).playerJoin(arr[1], session, arr[2]);
		}
	}
	
	public boolean playerToRemove(){
		if(afkPlayers){
			afkPlayers = false;
			return true;
		}
		return false;
	}
	
	public void removePlayer(WebSocketSession session) {
		games.get(0).removePlayer(session);
	}
	
	/*
	public void removePlayer(String Id){
		for(int i = 0; i < this.player.size(); i++){
			if(this.player.get(i).getName().equals(Id)){
				this.player.remove(i);
				break;
			}
		}
	}
	
	public void getAFK(){
		long time = System.currentTimeMillis();
		for(int i = 0; i < this.player.size(); i++){
			if((time - this.player.get(i).getTime()) > 5000){
				this.removedPlayer.add(this.player.get(i).getName());
				this.player.remove(i);
				this.afkPlayers = true;
			}
		}
	}
	
	public long GetPlayerTime(int i){
		return this.player.get(i).getTime();
	}
	
	
	public int getPlayers(){
		return this.player.size();
	}
	
	public String playerString(int i){
		return this.player.get(i).toString();
	}
	
	public void MakeGame(String ids, int playerNum){
		
	}
	*/
}
