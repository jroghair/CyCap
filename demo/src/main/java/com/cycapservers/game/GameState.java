package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.TimerTask;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public abstract class GameState extends TimerTask
{
	//////NITTY GRITTY STUFF//////
	protected String game_id;
	protected String game_type;
	protected List<String> usedEntityIds;
	protected int entity_id_len;
	protected List<String> userPasswords;
	protected List<InputSnapshot> unhandledInputs;
	
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
	protected ArrayList<ArrayList<mapNode>> map;
	
	protected List<Bullet> bullets;
	
	//Map Related Stuff
	protected List<Wall> walls;
	protected int mapGridWidth;
	protected int mapGridHeight;
	protected List<SpawnNode> spawns;
	
	//////GRAPHICAL OBJECTS//////
	protected List<Particle> particles;
	protected List<ParticleEffect> effects;
	
	protected HashMap<Integer, Integer> team_scores;
	
	protected long lastGSMessage;
	protected double currentDeltaTime; //the time since the last game state update in seconds
	
	public GameState() {
		this.usedEntityIds = new ArrayList<String>();
		entity_id_len = 6;
		this.userPasswords = new ArrayList<String>();

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
		
		this.unhandledInputs = new ArrayList<InputSnapshot>();
		this.lastGSMessage = System.currentTimeMillis();
		
		friendlyFire = false;
		respawnTime = 10000; //10 seconds respawn time
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
	
	public abstract void playerJoin(String client_id, WebSocketSession session, String role);
	
	public abstract void add_AI_player(int team, String role);
	
	public abstract void removePlayer(WebSocketSession session);
	
	public abstract void setUpGame();
	
	public void endGame() {
		for(Player p : this.players) {
			p.stats.updateScore();
			//TODO: ProfileRepo.update(p)
		}
	}

	@Override
	public void run() {
		updateGameState();
	}
}