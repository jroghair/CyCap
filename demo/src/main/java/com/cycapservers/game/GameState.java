package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.cycapservers.account.ProfileDataUpdate;

public abstract class GameState extends TimerTask
{
	//////NITTY GRITTY STUFF//////
	/**
	 * Players that are planning to join the game.
	 */
	protected ArrayList<IncomingPlayer> incomingPlayers;
	protected String game_id;
	protected List<String> usedEntityIds;
	protected int entity_id_len;
	protected List<String> userPasswords;
	protected List<InputSnapshot> unhandledInputs;
	protected boolean readyToStart;
	/////////////////////////////
	
	//////PLAYERS//////
	protected int max_players;
	protected List<AI_player> AI_players;
	protected List<Player> players;
	protected boolean friendlyFire;
	protected long respawnTime; //the amount of time to respawn after death in ms
	///////////////////
	
	//////ITEMS//////
	protected List<Item> current_item_list;
	protected PowerUpHandler pu_handler;
	////////////////
	
	protected List<String> new_sounds;
	
	// stuff for AI
	protected ArrayList<ArrayList<mapNode>> ai_map;
	
	protected List<Bullet> bullets;
	
	//Map Related Stuff
	protected List<Wall> walls;
	protected int mapGridWidth;
	protected int mapGridHeight;
	protected List<SpawnNode> spawns;
	
	//////GRAPHICAL OBJECTS//////
	protected List<Particle> particles;
	protected List<ParticleEffect> effects;
	protected List<GroundMask> ground_masks;
	
	//////SCORES AND TIME//////
	protected HashMap<Integer, Integer> team_scores;
	protected long start_time;
	protected int time_limit;
	protected boolean started;
	protected int winner;
	protected int score_limit;
	
	protected long lastGSMessage;
	protected double currentDeltaTime; //the time since the last game state update in seconds
	
	public GameState(String id) {
		this.game_id = id;
		this.usedEntityIds = new ArrayList<String>();
		entity_id_len = 6;
		this.userPasswords = new ArrayList<String>();

		this.incomingPlayers = new ArrayList<IncomingPlayer>();
		this.players = new ArrayList<Player>();
		this.AI_players = new ArrayList<AI_player>();
		this.team_scores = new HashMap<Integer, Integer>();
		this.bullets = new ArrayList<Bullet>();
		this.walls = new ArrayList<Wall>();
		this.spawns = new ArrayList<SpawnNode>();
		this.new_sounds = new ArrayList<String>();
		this.current_item_list = new ArrayList<Item>();
		this.particles = new ArrayList<Particle>();
		this.effects = new ArrayList<ParticleEffect>();
		this.ground_masks = new ArrayList<GroundMask>();
		
		this.unhandledInputs = new ArrayList<InputSnapshot>();
		this.lastGSMessage = System.currentTimeMillis();
		
		this.started = false;
		this.readyToStart = false;
	}

	public abstract void updateGameState();
	
	public abstract List<Item> getItemList();
	
	public abstract String toDataString(Player p);
	
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
	
	/**
	 * Works with the lobby to show if a player is going to arrive.
	 * @param userId
	 */
	public void addIncomingPlayer(IncomingPlayer p){
		this.incomingPlayers.add(p);
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
	public boolean findIncomingPlayer(String userId, WebSocketSession session){
		for(IncomingPlayer p : incomingPlayers){
			if(p.client_id.equals(userId)){
				this.playerJoin(p.client_id, session, p.role);
				return true;
			}
		}
		return false;
	}
	
	public abstract void playerJoin(String client_id, WebSocketSession session, String role);
	
	public abstract void add_AI_player(int team, String role);
	
	public abstract void removePlayer(WebSocketSession session);
	
	public abstract void setUpGame();
	
	public void endGame(int winner) {
		started = false;
		for(Player p : this.players) {
			p.stats.updateScore(winner);
			ProfileDataUpdate.dbSaveData(p.stats);
		}
	}

	@Override
	public void run() {
		if(started) {
			////UPDATE GAME STATE////
			updateGameState();
			
			////GET NEW ITEM LIST////
			this.current_item_list = getItemList();
			
			////INFORM PLAYERS OF GS UPDATE////
			for(Player p : players) {
				p.setLastUnsentGameState(this.toDataString(p));
			}
			
			//////CLEAR LISTS/////
			this.new_sounds.clear();
			this.unhandledInputs.clear(); //empty the queue of unhandled inputs
		}
		else if(readyToStart){
			if(players.size() == incomingPlayers.size()) {
				setUpGame();
			}
		}
	}
}