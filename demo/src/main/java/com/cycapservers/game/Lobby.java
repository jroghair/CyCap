package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;



/**
 * Represents one instance of a lobby.
 * @author ted
 *
 */
public class Lobby{
	
	/**
	 * Stores the GameState assosositated with the lobby.
	 */
	private GameState game;
	
	private boolean startGame;
	
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
	
	/**
	 * How many people below the max before the game starts.
	 */
	private final int startGap = 6;
	
	/**
	 * Takes in the game mode that the lobby is going to represent.
	 * @param gamemode
	 * String representing the game mode.
	 */
	public Lobby(Class<? extends GameState> c, String id){
		this.curSize=0;
		this.t = new Timer();
		if(c.equals(TeamDeathMatch.class)){
			this.game = new TeamDeathMatch(id, 0);
		}
		else if(c.equals(CaptureTheFlag.class)){
			this.game = new CaptureTheFlag(id, 0);
		}
		else if(c.equals(FreeForAll.class)){
			this.game = new FreeForAll(id, 0);
		}
		this.maxSize = game.max_players;
		this.startGame = false;
		
	}
	
	public boolean isStarting(){
		return startGame;
	}
	
	public TimerTask newTask(){
		TimerTask t = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(IncomingPlayer i : players){
					game.addIncomingPlayer(i);
					try {
						i.session.sendMessage(new TextMessage("play"));
						startGame = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
	public void GivePlayerList(WebSocketSession session, String id) throws IOException{
			session.sendMessage(new TextMessage("clean"));
			for(int i = 0; i < curSize; i++){
				session.sendMessage(new TextMessage("player:"+ players.get(i).client_id));
			}
		return;
	}
	
	public void ChangePlayerClass(WebSocketSession session, String id, String role){
		for(IncomingPlayer p : players){
			if(p.client_id.equals(id)){
				//TODO add checking for player level;
				p.role = role;
				try {
					session.sendMessage(new TextMessage("role:" + role));
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
	 * @throws IOException
	 */
	public void addPlayer(String p, WebSocketSession s) throws IOException{
		players.add(new IncomingPlayer(p, "recruit", s)); //TODO: this needs to use the role that was chosen on the lobby page
		this.curSize++;
		
		for(IncomingPlayer i : players){
			this.GivePlayerList(i.session, game.game_id);
			i.session.sendMessage(new TextMessage("time:" + (240000 / (curSize + startGap ))));
		}
		this.t.cancel();
		this.t = new Timer();
		this.t.schedule(newTask(), (240000 / (curSize + startGap)));
		//this.t.schedule(newTask(), 100);
	}
	
	/**
	 * Removes a player from the lobby.
	 * @param p
	 */
	public void removePlayer(WebSocketSession s){
		for(IncomingPlayer i : players) {
			if(i.session.equals(s)) {
				players.remove(i);
				this.curSize--;
				this.t.cancel();
				this.t = new Timer();
				this.t.schedule(newTask(), (240000 /(curSize + startGap)));
				for(IncomingPlayer j: players){
					try {
						this.GivePlayerList(j.session, game.game_id);
					} catch (IOException e) {
						// TODO Auto-generated catch block
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
		return maxSize-curSize + startGap; 
	}
	
	
}
