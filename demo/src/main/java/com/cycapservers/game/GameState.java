package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TimerTask;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class GameState extends TimerTask
{
	
	protected List<String> usedEntityIds;
	protected int entity_id_len;
	protected List<String> userPasswords;
	
	private List<InputSnapshot> unhandledInputs;
	
	//////PLAYERS//////
	protected List<AI_player> AI_players;
	protected List<Player> players;
	protected int playersOnTeam1;
	protected int playersOnTeam2;
	protected boolean friendlyFire;
	protected long respawnTime; //the amount of time to respawn after death in ms
	//TODO: spawn nodes
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
	
	protected HashMap<Integer, Integer> team_scores;
	//////CTF STUFF//////
	protected GridLockedNode team1_base;
	protected GridLockedNode team2_base;
	protected Flag team1_flag;
	protected Flag team2_flag;
	/////////////////////
	
	private long lastGSMessage;
	protected double currentDeltaTime; //the time since the last game state update in seconds
	
	public GameState() {
		this.usedEntityIds = new ArrayList<String>();
		entity_id_len = 6;
		this.userPasswords = new ArrayList<String>();
		
		this.players = new ArrayList<Player>();
		this.bullets = new ArrayList<Bullet>();
		this.walls = new ArrayList<Wall>();
		this.spawns = new ArrayList<SpawnNode>();
		this.new_sounds = new ArrayList<String>();
		this.current_item_list = new ArrayList<Item>();
		
		this.unhandledInputs = new ArrayList<InputSnapshot>();
		this.lastGSMessage = System.currentTimeMillis();
		
		this.playersOnTeam1 = 0;
		this.playersOnTeam2 = 0;
		this.team_scores = new HashMap<Integer, Integer>();
		this.team_scores.put(1, 0); //for TDM and CTF only
		this.team_scores.put(2, 0);
		
		friendlyFire = false;
		respawnTime = 10000; //10 seconds respawn time
		pu_handler = new PowerUpHandler((short) 30000, (short) 2500);
		
		MapLoader.loadPredefinedMap(0, this);//load up the map
		
		this.AI_players = new ArrayList<AI_player>();
		// generate the map when player is constructed
		this.map = Utils.generate_node_array(this);
		//this.add_AI_player(1, "recruit");
		//this.add_AI_player(2, "recruit");
	}

	public void updateGameState() {
		this.currentDeltaTime = (System.currentTimeMillis() - this.lastGSMessage)/1000.0;
		
		//DEV STUFF
		if(Utils.DEBUG) {
			int error = (int) (this.currentDeltaTime*1000 - 100);
			if(error >= GameManager.TOLERABLE_UPDATE_ERROR) {
				System.out.println("Time error in Gamestate sending: " + error);
			}
			if(this.bullets.size() >= GameManager.ADVANCED_BULLET_WARNING_LEVEL) {
				System.out.println("ADVANCED WARNING!! TOO MANY BULLETS");
			}
			else if(this.bullets.size() >= GameManager.BULLET_WARNING_LEVEL) {
				System.out.println("Warning! High number of bullets");
			}
		}
		
		/////////UPDATE GAME OBJECTS///////////
		//move all of the bullets first
		ListIterator<Bullet> iter = this.bullets.listIterator();
		while(iter.hasNext()){
			Bullet temp = iter.next();
		    if(temp.update(this)) {
		    	this.usedEntityIds.remove(temp.entity_id);
		    	iter.remove(); //remove the bullet from the list if it is done (animation done/hit a wall/etc)
		    }
		}
		//UPDATE the flags
		this.team1_flag.update();
		this.team2_flag.update();
		
		for(int i = 0; i < this.unhandledInputs.size(); i++) {
			try {
				Player p = this.unhandledInputs.get(i).client;
				p.update(this, this.unhandledInputs.get(i));
			}
			catch(ConcurrentModificationException e) {
				System.out.println("unhandled input " + i + ": " + e);
			}
			catch(NullPointerException e) {
				System.out.println("Null pointer Exception when getting index " + i + " of unhandled input list when list size is " + this.unhandledInputs.size() + ".");
			}
		}
		
		/*/ updating AI players
		for (AI_player ai : AI_players) {
				ai.update(this, null);
		}*/
		
		//Check For Flag captures
		if(!this.team1_flag.atBase && this.team2_flag.atBase && Utils.isColliding(this.team1_flag, team2_base)) {
			this.team_scores.put(2, this.team_scores.get(2) + 1); //+1 to team 2
			this.team1_flag.returnToBase(); //return the flag to base
			if(Utils.DEBUG) System.out.println("FLAG 1 CAPTURED!!");
		}
		else if(!this.team2_flag.atBase && this.team1_flag.atBase && Utils.isColliding(this.team2_flag, team1_base)) {
			this.team_scores.put(1, this.team_scores.get(1) + 1); //+1 to team 2
			this.team2_flag.returnToBase(); //return the flag to base
			if(Utils.DEBUG) System.out.println("FLAG 2 CAPTURED!!");
		}
		
		pu_handler.update(this); //update the powerups
		
		this.unhandledInputs.clear(); //empty the queue of unhandled inputs
		
		this.lastGSMessage = System.currentTimeMillis();
		
		for(Player p : players) {
			p.setLastUnsentGameState(this.toDataString(p));
		}
		
		this.current_item_list = getItemList();
		
		this.new_sounds.clear();
	}
	
	public List<Item> getItemList(){
		List<Item> list = new ArrayList<Item>();
		list.addAll(this.pu_handler.getPowerUpsList());
		list.add(this.team1_flag);
		list.add(this.team2_flag);
		return list;
	}
	
	public String toDataString(Player p) {
		String output = "";
		//fill the output
		for(int i = 0; i < players.size(); i++) {
			if((players.get(i).team == p.team) || (Utils.distanceBetween(p, players.get(i)) <= (p.visibility * Utils.GRID_LENGTH))) {
				output += players.get(i).toDataString(p.entity_id) + ":";
			}
		}
		for (int i = 0; i < AI_players.size(); i++) {
			if((AI_players.get(i).team == p.team) || (Utils.distanceBetween(p, AI_players.get(i)) <= (p.visibility * Utils.GRID_LENGTH))) {
				output += AI_players.get(i).toDataString(p.entity_id) + ":";
			}
		}
		for (Item i : this.current_item_list) {
			output += i.toDataString(p.entity_id) + ":";
		}
		for(int i = 0; i < bullets.size(); i++) {
			output += bullets.get(i).toDataString(p.entity_id);
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
		String pass = Utils.getGoodRandomString(this.userPasswords, 6);
		SpawnNode n = Utils.getRandomSpawn(this.spawns, team);
		this.players.add(new Player(n.getX(), n.getY(), Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, client_id, pass, session, new CTF_PlayerStats()));
		this.userPasswords.add(pass);
		try {
			session.sendMessage(new TextMessage("join:" + pass));
		} catch (IOException e) {
			System.out.println("could not send password for " + client_id + "! error!");
			e.printStackTrace();
		}
	}
	
	public void add_AI_player(int team, String role) {
		// make AI player and send map reference
		//mapNode randomNode = getRandomNode();
		String s = Utils.getGoodRandomString(this.usedEntityIds, this.entity_id_len);
		SpawnNode n = Utils.getRandomSpawn(this.spawns, team);
		AI_players.add(new AI_player(n.getX(), n.getY(), Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, s, this, new CTF_PlayerStats()));
		this.usedEntityIds.add(s);
		AI_players.get(AI_players.size() - 1).get_path(this);
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
		    	this.usedEntityIds.remove(temp.entity_id);
		    	this.userPasswords.remove(temp.password);
		    	iter.remove();
		    	return;
		    }
		}
	}

	@Override
	public void run() {
		updateGameState();
	}
}