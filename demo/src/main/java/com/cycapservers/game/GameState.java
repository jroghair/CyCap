package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class GameState extends TimerTask {
	private List<InputSnapshot> unhandledInputs;

	protected List<Player> players;
	protected List<AI_player> AI_players;
	protected int playersOnTeam1;
	protected int playersOnTeam2;
	protected int team1_score;
	protected int team2_score;
	protected boolean friendlyFire;
	protected long respawnTime; // the amount of time to respawn after death in
								// ms

	// stuff for AI
	//protected ArrayList<ArrayList<mapNode>> map;// map of nodes for AI
	//protected AI_map_generator map_gen;
	protected AI_path_generator path_gen;
	protected AI_utils AI_util;
	protected int bg_width_grids = 41;
	protected int bg_height_grids = 30;
	double node_pixel_dist = 16.0;

	protected List<Bullet> bullets;

	// Map Related Stuff
	protected List<Wall> walls;
	protected int mapGridWidth;
	protected int mapGridHeight;

	private long lastGSMessage;
	protected double currentDeltaTime; // the time since the last game state
										// update in seconds

	public GameState() {
		this.players = new ArrayList<Player>();
		this.AI_players = new ArrayList<AI_player>();
		this.bullets = new ArrayList<Bullet>();
		this.walls = new ArrayList<Wall>();
		this.unhandledInputs = new ArrayList<InputSnapshot>();
		this.lastGSMessage = System.currentTimeMillis();

		this.playersOnTeam1 = 0;
		this.playersOnTeam2 = 0;
		team1_score = 0;
		team2_score = 0;
		friendlyFire = false;
		respawnTime = 10000; // 10 seconds respawn time

		MapLoader.loadPredefinedMap(0, this);// load up the map

		//map_gen = new AI_map_generator(this);
		//path_gen = new AI_path_generator(this);// may need game state
		//AI_util = new AI_utils(this);

		//map = map_gen.generate_node_array(this);
	}

	public void updateGameState() {
		this.currentDeltaTime = (System.currentTimeMillis() - this.lastGSMessage) / 1000.0;
		// System.out.println("Time error in Gamestate sending: " +
		// (this.currentDeltaTime*1000 - 100));

		// move all of the bullets first
		ListIterator<Bullet> iter = this.bullets.listIterator();
		while (iter.hasNext()) {
			if (iter.next().update(this)) {
				iter.remove(); // remove the bullet from the list if it is done
								// (animation done/hit a wall/etc)
			}
		}

		for (int i = 0; i < this.unhandledInputs.size(); i++) {
			try {
				Player p = this.unhandledInputs.get(i).client;
				p.update(this, this.unhandledInputs.get(i));
			} catch (ConcurrentModificationException e) {
				System.out.println("unhandled input " + i + ": " + e);
			}
		}

		// updating AI players
		if (AI_players.size() > 0) {
			for (int i = 0; i < AI_players.size(); i++) {
				AI_players.get(i).update(this);
			}
		}

		this.unhandledInputs.clear(); // empty the queue of unhandled inputs

		this.lastGSMessage = System.currentTimeMillis();

		String message = this.toString();
		for (Player p : players) {
			p.setLastUnsentGameState(message);
		}
	}

	public String toString() {
		String output = "";
		// fill the output
		for (int i = 0; i < players.size(); i++) {
			output += players.get(i).toString() + ":";
		}
		for (int i = 0; i < AI_players.size(); i++) {
			output += AI_players.get(i).toString() + ":";
		}
		for (int i = 0; i < bullets.size(); i++) {
			output += bullets.get(i).toString();
			if (i != bullets.size() - 1)
				output += ":";
		}

		return output;
	}

	public void addInputSnap(InputSnapshot s) {
		for (Player p : this.players) {
			if (s.password.equals(p.getPassword())) {
				s.setClient(p);
				unhandledInputs.add(s);
				if (p.getLastUnsentGameState() != null) {
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
		if (this.playersOnTeam1 > this.playersOnTeam2) {
			team = 2;
			this.playersOnTeam2++;
		} else {
			team = 1;
			this.playersOnTeam1++;
		}
		String pass = createPassword(10);
		while (!isPasswordGood(pass)) {
			pass = createPassword(10);
		}
		this.players.add(
				new Player(64, 64, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, client_id, pass, session));
		System.out.println("player's team: " + this.players.get(this.players.size()-1).team);
		this.add_AI_player(1, role);
		this.add_AI_player(1, role);
		this.add_AI_player(2, role);
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
	
	//moved to ai player class
//	public mapNode getRandomNode(){
//		boolean trav = false;
//		Random rangen = new Random();
//		int randi = rangen.nextInt(map.size());
//		int randj = rangen.nextInt(map.get(0).size());
//		while(!trav){
//			randi = rangen.nextInt(map.size());
//			randj = rangen.nextInt(map.get(0).size());
//			if(map.get(randi).get(randj).node_trav == true){
//				trav = true;
//			}
//		}
//		return this.map.get(randi).get(randj);
//	}
	
	public void removePlayer(WebSocketSession session) {
		ListIterator<Player> iter = this.players.listIterator();
		while (iter.hasNext()) {
			Player temp = iter.next();
			if (temp.session.equals(session)) {
				if (temp.team == 1) {
					this.playersOnTeam1--;
				} else {
					this.playersOnTeam2--;
				}
				iter.remove();
				return;
			}
		}
	}

	public boolean isPasswordGood(String pw) {
		for (int i = 0; i < players.size(); i++) {
			if (pw == players.get(i).getPassword()) {
				return false;
			}
		}
		return true;
	}

	public String createPassword(int length) {
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
		Random rand = new Random();
		String pass = "";
		for (int i = 0; i < length; i++) {
			pass += s.charAt(rand.nextInt(s.length()));
		}
		return pass;
	}

	@Override
	public void run() {
		updateGameState();
	}
}