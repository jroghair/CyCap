package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * The Class that manages all of the games and the lobbies for the website.
 * @author ted
 *
 */
public class GameManager {
	
	/**
	 * A list of all of the currently running games.
	 */
	private volatile ArrayList<GameState> games;
	
	/**
	 * A list of all of the currently active lobbies
	 */
	private volatile ArrayList<Lobby> lobby;
	
	//create player afk list that has the players time out after 30 seconds and get deleted
	/**
	 * a list of afkplayers that need to be removed from their respective games.
	 */
	private volatile ArrayList<String> removedPlayer = new ArrayList<String>();
	
	private long time = 0;
	
	/**
	 * A timer that works to keep the games updated.
	 */
	private Timer timer;
	
	/**
	 * Keeps track of if there are afk players.
	 */
	private boolean afkPlayers;
	
	//Get a method to check last message.
	/**
	 * The constructor for game manager which creates a basic game and initalizes everything else.
	 */
	public GameManager(){
		timer = new Timer(true);
		games = new ArrayList<GameState>();
		lobby = new ArrayList<Lobby>();
		games.add(new GameState());
		timer.scheduleAtFixedRate(games.get(0), 0, 100);
	}
	
	//Ask about sending purpose of message;
	/**
	 * Takes in the messages from all of the different websockets and sorts them appropriately. This functions
	 * for both the lobby system and the game system. It makes new games and lobbies when needed.
	 * @param session
	 * The websocket that sent the current message. Used to subscribe players to games or lobbies.
	 * @param message
	 * A string that represents the messsage that the client sent to the server.
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void getMessage(WebSocketSession session, String message) throws IOException, InterruptedException{
		boolean found = false;
		boolean erase = false;
		String[] arr = message.split(":");
		if(arr[0].equals("input")) {
			games.get(0).addInputSnap(new InputSnapshot(message));
		}
		else if(arr[0].equals("join")) {
			for(GameState s: games){
				found = s.findIncomingPlayer(arr[1], session, arr[2]);
				if(found){
					break;
				}
			}
			if(!found){
				games.get(0).playerJoin(arr[1], session, arr[2]);
			}
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
								erase = lobby.get(i).addPlayer(arr[3],session);
								session.sendMessage(new TextMessage("joined:" + lobby.get(i).getId()));
								foundGame = true;
								if(erase){
									lobby.remove(i);
								}
								break;
							}
						}
					}
					if(!foundGame){
						System.out.println(arr[3]);
						Lobby l = new Lobby("Death");
						l.addPlayer(arr[3],session);
						session.sendMessage(new TextMessage("joined:" + l.getId()));
						lobby.add(l);
						games.add(l.getGame());
						timer.scheduleAtFixedRate(l.getGame(), 0, 100);
					}
				}
				else if(arr[2].equals("Capture")){
					for(int i = 0; i < lobby.size(); i++){
						if(lobby.get(i).getGame().getClass().equals(Capture.class)){
							if(lobby.get(i).hasSpace()){
								erase = lobby.get(i).addPlayer(arr[3], session);
								session.sendMessage(new TextMessage("joined:" + lobby.get(i).getId()));
								foundGame = true;
								if(erase){
									lobby.remove(i);
								}
								break;
							}
						}
					}
					if(!foundGame){
						Lobby l = new Lobby("Capture");
						l.addPlayer(arr[3], session);
						session.sendMessage(new TextMessage("joined:" + l.getId()));
						lobby.add(l);
						games.add(l.getGame());
						timer.scheduleAtFixedRate(l.getGame(), 0, 100);
						
					}
				}
				else{
					//For if a game id is given.
					for(int i = 0; i < lobby.size(); i++){
						if(lobby.get(i).getId().equals(arr[2])){
							if(lobby.get(i).hasSpace()){
								lobby.get(i).addPlayer(arr[3],session);
								session.sendMessage(new TextMessage("joined:" + games.get(i).GameId));
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gives a list of all of the players in the lobby that is represented by the id. 
	 * @param session
	 * The person who requested the lists websocket session.
	 * @param id
	 * The id of the lobby that the player belongs in.
	 * @throws IOException
	 */
	private void GivePlayerList(WebSocketSession session, String id) throws IOException{
		int game = -1;
		for(int i = 0; i < lobby.size(); i++){
			if(lobby.get(i).getId().equals(id)){
				game = i;
				break;
			}
		}
		if(game != -1){
			lobby.get(game).GivePlayerList(session, id);
			//session.sendMessage(new TextMessage("clean"));
			//for(int i = 0; i < lobby.get(game).getCurrentSize(); i++){
				//session.sendMessage(new TextMessage("player:"+ lobby.get(game).getPlayer(i)));
				//System.out.println(lobby.get(game).getPlayer(i));
			//}
		}
		return;
	}
	/**
	 * finds if their are afk players.
	 * @return
	 */
	public boolean playerToRemove(){
		if(afkPlayers){
			afkPlayers = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the given player from the game.
	 * @param session
	 * The session belonging to the person who left.
	 */
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
