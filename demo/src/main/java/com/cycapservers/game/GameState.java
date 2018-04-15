package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TimerTask;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * The state of a single game in the game manager.
 * @author ted
 *
 */
public class GameState extends TimerTask
{
	/**
	 * A list of inputs from the players that need to be handled
	 */
	private List<InputSnapshot> unhandledInputs;
	
	/**
	 * The max players in the game.
	 */
	protected int maxPlayers;
	
	/**
	 * An id that is assiated with the game.
	 */
	protected String GameId;
	
	/**
	 * Players that are planning to join the game.
	 */
	protected ArrayList<String> incomingPlayers;
	
	/**
	 * A list of the active players.
	 */
	protected List<Player> players;
	
	/**
	 *  The number of people on team 1.
	 */
	protected int playersOnTeam1;
	
	/**
	 * The number of people in team 2.
	 */
	protected int playersOnTeam2;
	
	/**
	 * The score of team 1.
	 */
	protected int team1_score;
	
	/**
	 * The score of team 2;
	 */
	protected int team2_score;
	
	/**
	 * If friendly fire is active for the game.
	 */
	protected boolean friendlyFire;
	
	/**
	 * How long a player has until respawn.
	 */
	protected long respawnTime; //the amount of time to respawn after death in ms
	
	/**
	 * A list of the bullets currently active in the game.
	 */
	protected List<Bullet> bullets;
	
	//Map Related Stuff
	/**
	 * A list of all of the walls in the game.
	 */
	protected List<Wall> walls;
	
	/**
	 * The height of the map;
	 */
	protected int mapGridWidth;
	
	/**
	 * The width of the map;
	 */
	protected int mapGridHeight;
	
	/**
	 * How long between game states.
	 */
	private long lastGSMessage;
	
	/**
	 * Time since the game was last updated.
	 */
	protected double currentDeltaTime; //the time since the last game state update in seconds
	
	/**
	 * A constructor for game state that initializes all of the variables.
	 */
	public GameState() {
		this.incomingPlayers = new ArrayList<String>();
		this.players = new ArrayList<Player>();
		this.bullets = new ArrayList<Bullet>();
		this.walls = new ArrayList<Wall>();
		this.unhandledInputs = new ArrayList<InputSnapshot>();
		this.lastGSMessage = System.currentTimeMillis();
		this.GameId = this.createPassword(10);
		
		this.playersOnTeam1 = 0;
		this.playersOnTeam2 = 0;
		team1_score = 0;
		team2_score = 0;
		friendlyFire = false;
		respawnTime = 10000; //10 seconds respawn time
		
		MapLoader.loadPredefinedMap(0, this);//load up the map
	}
	//remove
	
	/**
	 * Does the game have space.
	 * @return
	 * returns true if the game has space and false if it doesn't.
	 */
	public boolean hasSpace(){
		if(this.players.size() > this.maxPlayers){
			return true;
		}
		return false;
	}
	
	/**
	 * Works with the lobby to show if a player is going to arrive.
	 * @param userId
	 */
	public void addIncomingPlayer(String userId){
		this.incomingPlayers.add(userId);
	}
	
	/**
	 * Checks to see if the given userId is in the incoming players list and if they are adds them to the game.
	 * @param userId
	 * The person who wants to joins user id.
	 * @param session
	 * The person who wants to join websocketsession.
	 * @param role
	 * The role that the person wants.
	 * @return
	 * returns true if the person is in the incoming player list and they joined the game. returns false if they arn't.
	 */
	public boolean findIncomingPlayer(String userId, WebSocketSession session, String role){
		for(int i = 0; i < this.incomingPlayers.size(); i++){
			if(this.incomingPlayers.equals(userId)){
				this.playerJoin(userId, session, role);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gives the player at the given index.
	 * @param i
	 * The index of players that the game wants to look at.
	 * @return
	 * the players id.
	 */
	public String getPlayers(int i){
		return this.players.get(i).getName();
	}
	
	/**
	 * Gives the current number of players in the game.
	 * @return
	 * An int that holds the current number of players.
	 */
	public int getNumPlayers(){
		return this.players.size();
	}
	//remove
	
	/**
	 * Updates the game state.
	 */
	public void updateGameState() {
		this.currentDeltaTime = (System.currentTimeMillis() - this.lastGSMessage)/1000.0;
		//System.out.println("Time error in Gamestate sending: " + (this.currentDeltaTime*1000 - 100));
		
		//move all of the bullets first
		ListIterator<Bullet> iter = this.bullets.listIterator();
		while(iter.hasNext()){
		    if(iter.next().update(this)) {
		    	iter.remove(); //remove the bullet from the list if it is done (animation done/hit a wall/etc)
		    }
		}
		
		for(int i = 0; i < this.unhandledInputs.size(); i++) {
			try {
				Player p = this.unhandledInputs.get(i).client;
				p.update(this, this.unhandledInputs.get(i));
			}
			catch(ConcurrentModificationException e) {
				System.out.println("unhandled input " + i + ": " + e);
			}
		}
		this.unhandledInputs.clear(); //empty the queue of unhandled inputs
		
		this.lastGSMessage = System.currentTimeMillis();
		
		String message = this.toString();
		for(Player p : players) {
			p.setLastUnsentGameState(message);
		}
	}
	
	/**
	 * Puts the current game state into a string.
	 * @return
	 * A string representing the current game state.
	 */
	public String toString() {
		String output = "";
		//fill the output
		for(int i = 0; i < players.size(); i++) {
			output += players.get(i).toString() + ":";
		}
		for(int i = 0; i < bullets.size(); i++) {
			output += bullets.get(i).toString();
			if(i != bullets.size() - 1) output += ":";
		}
		
		return output;
	}
	
	public void addInputSnap(InputSnapshot s) {
		for(Player p : this.players) {
			if(s.password.equals(p.getPassword())) {
				s.setClient(p);
				unhandledInputs.add(s);
				if(p.getLastUnsentGameState() != null) {
					try {
						p.session.sendMessage(new TextMessage(p.getLastUnsentGameState()));
						p.setLastUnsentGameState(null);
					} catch (IOException e) {
						System.out.println("Error when sending game state");
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}
	
	public void playerJoin(String client_id, WebSocketSession session, String role) {
		int team;
		if(this.playersOnTeam1 > this.playersOnTeam2) {
			team = 2;
			this.playersOnTeam2++;
		}
		else {
			team = 1;
			this.playersOnTeam1++;
		}
		String pass = createPassword(10);
		while(!isPasswordGood(pass)) {
			pass = createPassword(10);
		}
		this.players.add(new Player(64, 64, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, client_id, pass, session));
		try {
			session.sendMessage(new TextMessage("join:" + pass));
		} catch (IOException e) {
			System.out.println("could not send password for " + client_id + "! error!");
			e.printStackTrace();
		}
	}
	
	public void removePlayer(WebSocketSession session) {
		ListIterator<Player> iter = this.players.listIterator();
		while(iter.hasNext()){
			Player temp = iter.next();
		    if(temp.session.equals(session)) {
		    	if(temp.team == 1) {
		    		this.playersOnTeam1--;
		    	}
		    	else {
		    		this.playersOnTeam2--;
		    	}
		    	iter.remove();
		    	return;
		    }
		}
	}
	
	public boolean isPasswordGood(String pw) {
		for(int i = 0; i < players.size(); i++) {
			if(pw == players.get(i).getPassword()) {
				return false;
			}
		}
		return true;
	}
	
	public String createPassword(int length){
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
		Random rand = new Random();
		String pass = "";
		for(int i = 0; i < length; i++){
			pass += s.charAt(rand.nextInt(s.length()));	
		}
		return pass;
	}

	@Override
	public void run() {
		updateGameState();
	}
}