package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents one instance of a lobby.
 * @author ted
 *
 */
public class Lobby {
	
	/**
	 * Stores the GameState assosositated with the lobby.
	 */
	private GameState game;
	
	/**
	 * Stores the usernames of all of the players in the lobby.
	 */
	private ArrayList<String> players = new ArrayList<String>();
	
	/**
	 * Stores a list of websocketsessions that are in the lobby.
	 */
	private ArrayList<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	
	/**
	 * The amount of people in the lobby.
	 */
	private int curSize;
	
	/**
	 * The maximum amount of people allowed in the lobby.
	 */
	private int maxSize;
	
	/**
	 * How many people below the max before the game starts.
	 */
	private final int startGap = 4;
	
	/**
	 * Takes in the game mode that the lobby is going to represent.
	 * @param gamemode
	 * String representing the game mode.
	 */
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
	
	/**
	 * The current size of the lobby
	 * @return int
	 */
	public int getCurrentSize(){
		return this.curSize;
	}
	
	/**
	 * Returns the user id assosiated with the index.
	 * @param num
	 * the index
	 * @return
	 * A string representing the user id.
	 */
	public String getPlayer(int num){
		return this.players.get(num);
	}
	
	/**
	 * Gets the lobby id which is also the game id.
	 * @return
	 * A String representing the game id.
	 */
	public String getId(){
		return game.GameId;
	}
	
	/**
	 * Sees if the lobby currently has space for another player
	 * @return
	 * True if it has space.
	 */
	public boolean hasSpace(){
		if(this.curSize < this.maxSize){
			return true;
		}
		return false;
	}
	
	/**
	 * Gives the gameState assosiated with the lobby.
	 * @return
	 * A gameState Assosiated with the lobby.
	 */
	public GameState getGame(){
		return this.game;
	}
	
	/**
	 * Sends to all people in the lobby all of the names of people in the lobby.
	 * @param session
	 * A session Representing the person getting sent the message.
	 * @param id
	 * A String Representing the Game id.
	 * @throws IOException
	 */
	public void GivePlayerList(WebSocketSession session, String id) throws IOException{
			session.sendMessage(new TextMessage("clean"));
			for(int i = 0; i < curSize; i++){
				session.sendMessage(new TextMessage("player:"+ players.get(i)));
			}
		return;
	}
	
	/**
	 * Adds a player to the lobby. Also checks to see if the game is full and if so sends a message to start the game.
	 * @param p
	 * The user id of the person joining the game.
	 * @param s
	 * The websocketsession of the person joing the game.
	 * @return
	 * returns true if the game has started.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean addPlayer(String p, WebSocketSession s) throws IOException, InterruptedException{
		players.add(p);
		sessions.add(s);
		this.curSize++;
		for(WebSocketSession se: sessions){
			this.GivePlayerList(se, game.GameId);
		}
		if((curSize + startGap) == maxSize){
			//this.wait(10000);
			for(int i = 0; i < sessions.size(); i++){
				game.addIncomingPlayer(players.get(i));
				sessions.get(i).sendMessage(new TextMessage("play"));
			}
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Removes a player from the lobby.
	 * @param p
	 */
	public void removePlayer(String p){
		players.remove(p);
		this.curSize--;
	}
	
	/**
	 * Return an ArrayList of all of the user id's in the lobby.
	 * @return
	 */
	public ArrayList<String> getPlayersList(){
		return players; 
	}
	
	/**
	 * Returns how many open slots are left in the game.
	 * @return
	 */
	public int getOpenSlots(){
		return maxSize-curSize + startGap; 
	}
	
	
}
