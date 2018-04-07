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

public class GameState extends TimerTask
{
	private List<InputSnapshot> unhandledInputs;
	
	protected List<PlayerStats> playerstats; 
	protected List<AI_player> AI_players;
	protected List<Player> players;
	protected int playersOnTeam1;
	protected int playersOnTeam2;
	protected int team1_score;
	protected int team2_score;
	protected boolean friendlyFire;
	protected long respawnTime; //the amount of time to respawn after death in ms
	
	//////ITEMS//////
	protected List<Item> current_item_list;
	protected PowerUpHandler pu_handler;
	
	protected List<String> new_sounds;
	
	// stuff for AI
	protected ArrayList<ArrayList<mapNode>> map;
	
	protected List<Bullet> bullets;
	
	//Map Related Stuff
	protected List<Wall> walls;
	protected int mapGridWidth;
	protected int mapGridHeight;
	
	private long lastGSMessage;
	protected double currentDeltaTime; //the time since the last game state update in seconds
	
	public GameState() {
		
		this.players = new ArrayList<Player>();

		
		this.bullets = new ArrayList<Bullet>();
		this.walls = new ArrayList<Wall>();
		this.new_sounds = new ArrayList<String>();
		this.current_item_list = new ArrayList<Item>();
		
		this.unhandledInputs = new ArrayList<InputSnapshot>();
		this.lastGSMessage = System.currentTimeMillis();
		
		this.playersOnTeam1 = 0;
		this.playersOnTeam2 = 0;
		team1_score = 0;
		team2_score = 0;
		
		friendlyFire = false;
		respawnTime = 10000; //10 seconds respawn time
		pu_handler = new PowerUpHandler((short) 30000, (short) 2500);
		
		MapLoader.loadPredefinedMap(0, this);//load up the map
		
		this.AI_players = new ArrayList<AI_player>();
		// generate the map when player is constructed
		this.map = Utils.generate_node_array(this);
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
		// updating AI players
		for (AI_player ai : AI_players) {
				ai.update(this);
		}
		
		pu_handler.update(); //update the powerups
		
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
		return list;
	}
	
	public String toDataString(Player p) {
		String output = "";
		//fill the output
		for(int i = 0; i < players.size(); i++) {
			if((players.get(i).team == p.team) || (Utils.distanceBetweenEntities(p, players.get(i)) <= (p.visibility * Utils.GRID_LENGTH))) {
				output += players.get(i).toDataString(p.client_id) + ":";
			}
		}
		for (int i = 0; i < AI_players.size(); i++) {
			if((AI_players.get(i).team == p.team) || (Utils.distanceBetweenEntities(p, AI_players.get(i)) <= (p.visibility * Utils.GRID_LENGTH))) {
				output += AI_players.get(i).toDataString(p.client_id) + ":";
			}
		}
		for (Item i : this.current_item_list) {
			output += i.toDataString(p.client_id) + ":";
		}
		for(int i = 0; i < bullets.size(); i++) {
			output += bullets.get(i).toDataString(p.client_id);
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
		this.add_AI_player(1, role);
		this.add_AI_player(2, role);
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
		AI_players.add(new AI_player(64, 64, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, this));
		AI_players.get(AI_players.size() - 1).get_path(this);
		System.out.println("team: " + this.AI_players.get(this.AI_players.size()-1).team);
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