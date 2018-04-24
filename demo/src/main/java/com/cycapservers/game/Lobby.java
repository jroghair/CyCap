package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.cycapservers.account.ProfileDataUpdate;

/**
 * Represents one instance of a lobby.
 * @author ted
 *
 */
public class Lobby {
	
	/**
	 * Stores the GameState associated with the lobby.
	 */
	private GameState game;
	
	/**
	 * timer used to keep track of when to start the game
	 */
	public Timer t;
	
	/**
	 * Stores the usernames of all of the players in the lobby.
	 */
	private ArrayList<IncomingPlayer> players = new ArrayList<IncomingPlayer>();
	
	/**
	 * The amount of people in the lobby.
	 */
	private int curSize;
	
	/**
	 * The maximum amount of people allowed in the lobby.
	 */
	private int maxSize;
	
	private int team1;
	
	private int team2;
	
	private int freeTeam;
	
	/**
	 * Takes in the game mode that the lobby is going to represent.
	 * @param gamemode
	 * String representing the game mode.
	 */
	public Lobby(Class<? extends GameState> c, String id){
		this.curSize=0;
		this.t = new Timer();
		if(c.equals(TeamDeathMatch.class)){
			int map_num = Utils.RANDOM.nextInt(2);
			this.game = new TeamDeathMatch(id, map_num);
		}
		else if(c.equals(CaptureTheFlag.class)){
			int map_num = Utils.RANDOM.nextInt(2);
			this.game = new CaptureTheFlag(id, map_num);
		}
		else if(c.equals(FreeForAll.class)){
			this.game = new FreeForAll(id, 1);
			this.freeTeam = 0;
		}
		this.maxSize = game.max_players;
		
	}
	
	public TimerTask newTask(){
		TimerTask t = new TimerTask(){

			@Override
			public void run() {
				if(!game.readyToStart) {
					for(IncomingPlayer i : players){
						game.addIncomingPlayer(i);
						try {
							i.session.sendMessage(new TextMessage("play"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					game.readyToStart = true;
				}
			}
			
		};
		return t;
		
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
	public IncomingPlayer getPlayer(int num){
		return this.players.get(num);
	}
	
	/**
	 * Gets the lobby id which is also the game id.
	 * @return
	 * A String representing the game id.
	 */
	public String getId(){
		return game.game_id;
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
	public void GivePlayerList(WebSocketSession session) throws IOException{
		String output = "player";
		for(int i = 0; i < curSize; i++){
			output += ":" + players.get(i).client_id + ":" + players.get(i).role + ":" + players.get(i).team;
		}
		session.sendMessage(new TextMessage(output));
	}
	
	public void ChangePlayerClass(WebSocketSession session, String id, String role){
		for(IncomingPlayer p : players){
			if(p.client_id.equals(id)){
				if(ProfileDataUpdate.dbCheckLock(id, role)) {
					p.role = role;
				}
				else {
					role = "no";
				}
				try {
					session.sendMessage(new TextMessage("role:" + role));
					for(IncomingPlayer p1 : players){
						GivePlayerList(p1.session);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
	
	/**
	 * Adds a player to the lobby. Also checks to see if the game is full and if so sends a message to start the game.
	 * @param p
	 * The user id of the person joining the game.
	 * @param s
	 * The websocketsession of the person joining the game.
	 * @return
	 * returns true if the game has started.
	 * @throws IOException
	 */
	public void addPlayer(String p, WebSocketSession s) throws IOException{
		int team = 1;
		if(this.game.getClass().equals(CaptureTheFlag.class) || this.game.getClass().equals(TeamDeathMatch.class)){
			if(team1 == 0 && team2 == 0){
				team1++;
				team = 1;
			}
			else if(team1 < team2){
				team1++;
				team = 1;
			}
			else{
				team2++;
				team = 2;
			}
		}
		else if(this.game.getClass().equals(FreeForAll.class)){
			this.freeTeam++;
			team = freeTeam;
		}
		players.add(new IncomingPlayer(p, "recruit", s, team));
		this.curSize++;
		
		////////////////////////
		int time_to_start;
		if(curSize >= game.max_players/2) {
			time_to_start = 30000;
		}
		else {
			time_to_start = 45000;
		}
		for(IncomingPlayer i : players){
			this.GivePlayerList(i.session);
			i.session.sendMessage(new TextMessage("time:" + time_to_start));
		}
		this.t.cancel();
		this.t = new Timer();
		this.t.schedule(newTask(), time_to_start);
	}
	
	/**
	 * Removes a player from the lobby.
	 * @param p
	 */
	public void removePlayer(WebSocketSession s){
		for(IncomingPlayer i : players) {
			if(i.session.equals(s)) {
				if(i.team == 1) {
					team1--;
				}
				else if(i.team == 2) {
					team2--;
				}
				players.remove(i);
				this.curSize--;
				
				//////UPDATE TIMER//////
				int time_to_start;
				if(curSize >= game.max_players/2) {
					time_to_start = 30000;
				}
				else {
					time_to_start = 45000;
				}
				this.t.cancel();
				this.t = new Timer();
				this.t.schedule(newTask(), time_to_start);
				for(IncomingPlayer j: players){
					try {
						this.GivePlayerList(j.session);
						i.session.sendMessage(new TextMessage("time:" + time_to_start));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return;
			}
		}
	}
	
	/**
	 * Returns how many open slots are left in the game.
	 * @return
	 */
	public int getOpenSlots(){
		return maxSize-curSize; 
	}
}
