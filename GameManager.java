package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


public class GameManager {
	
	private volatile ArrayList<GameState> games;
	
	private volatile ArrayList<Lobby> lobby;
	
	//create player afk list that has the players time out after 30 seconds and get deleted
	
	private volatile ArrayList<String> removedPlayer = new ArrayList<String>();
	
	private long time = 0;
	private Timer timer;
	
	private boolean afkPlayers;
	
	//Get a method to check last message.
	
	public GameManager(){
		timer = new Timer(true);
		games = new ArrayList<GameState>();
		lobby = new ArrayList<Lobby>();
		games.add(new GameState());
		timer.scheduleAtFixedRate(games.get(0), 0, 100);
	}
	
	//Ask about sending purpose of message;
	public void getMessage(WebSocketSession session, String message) throws IOException{
		String[] arr = message.split(":");
		if(arr[0].equals("input")) {
			games.get(0).addInputSnap(new InputSnapshot(message));
		}
		else if(arr[0].equals("join")) {
			games.get(0).playerJoin(arr[1], session, arr[2]);
		}
		else if(arr[0].equals("lobby")){
			if(arr[1].equals("playerList")){
				GivePlayerList(session,arr[2]);
			}
			else if(arr[1].equals("join")){
				boolean foundGame = false;
				if(arr[2].equals("Death")){
					for(int i = 0; i < lobby.size(); i++){
						if(lobby.get(i).getGame().getClass().equals(Death.class)){
							if(lobby.get(i).hasSpace()){
								lobby.get(i).addPlayer(arr[3]);
								session.sendMessage(new TextMessage("joined:" + lobby.get(i).getId()));
								foundGame = true;
								break;
							}
						}
					}
					if(!foundGame){
						Lobby l = new Lobby("Death");
						l.addPlayer(arr[3]);
						session.sendMessage(new TextMessage("joined:" + l.getId()));
						lobby.add(l);
					}
				}
				else if(arr[2].equals("Capture")){
					for(int i = 0; i < lobby.size(); i++){
						if(lobby.get(i).getGame().getClass().equals(Capture.class)){
							if(lobby.get(i).hasSpace()){
								lobby.get(i).addPlayer(arr[3]);
								session.sendMessage(new TextMessage("joined:" + lobby.get(i).getId()));
								foundGame = true;
								break;
							}
						}
					}
					if(!foundGame){
						Lobby l = new Lobby("Capture");
						l.addPlayer(arr[3]);
						session.sendMessage(new TextMessage("joined:" + l.getId()));
						lobby.add(l);
					}
				}
				else{
					//For if a game id is given.
					for(int i = 0; i < lobby.size(); i++){
						if(lobby.get(i).getId().equals(arr[2])){
							if(lobby.get(i).hasSpace()){
								lobby.get(i).addPlayer(arr[3]);
								session.sendMessage(new TextMessage("joined:" + games.get(i).GameId));
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private void GivePlayerList(WebSocketSession session, String id) throws IOException{
		int game = -1;
		for(int i = 0; i < lobby.size(); i++){
			if(lobby.get(i).getId().equals(id)){
				game = i;
				break;
			}
		}
		if(game != -1){
			session.sendMessage(new TextMessage("clean"));
			for(int i = 0; i < lobby.get(game).getCurrentSize(); i++){
				session.sendMessage(new TextMessage("player:"+ lobby.get(game).getPlayer(i)));
				System.out.println(lobby.get(game).getPlayer(i));
			}
		}
		return;
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
