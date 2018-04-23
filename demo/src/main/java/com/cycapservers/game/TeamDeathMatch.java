package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class TeamDeathMatch extends GameState {
	
	//////PLAYERS//////
	protected volatile int playersOnTeam1;
	protected volatile int playersOnTeam2;
	///////////////////
	
	public TeamDeathMatch(String id, int map_number) {
		super(id);
		this.max_players = 8;
		this.playersOnTeam1 = 0;
		this.playersOnTeam2 = 0;
		this.team_scores.put(1, 0); //for TDM and CTF only
		this.team_scores.put(2, 0);
		
		pu_handler = new PowerUpHandler((short) 20000, (short) 5000);
		
		MapLoader.loadPredefinedMap(map_number, this);//load up the map
		
		// generate the map when player is constructed
		this.ai_map = Utils.generate_node_array(this);

		friendlyFire = false;
		respawnTime = 5000; //10 seconds respawn time TODO: fix this on the client side
		time_limit = 10 * 60 * 1000; //10 minutes to ms
		score_limit = 25; //25 kills TODO: find a way to track team kills on the 
	}

	@Override
	public void updateGameState() {
		//////CHECK TO SEE IF END GAME CONDITIONS ARE MET//////
		if(((System.currentTimeMillis() - this.start_time) >= time_limit) || (team_scores.get(1) >= score_limit) || (team_scores.get(2) >= score_limit)) {
			//TODO: set the winner correctly
			if(team_scores.get(1) >= score_limit) {
				this.winner = 1;
			}
			else if(team_scores.get(2) >= score_limit) {
				this.winner = 2;
			}
			endGame(winner);
		}
			
		this.currentDeltaTime = (System.currentTimeMillis() - this.lastGSMessage)/1000.0;
		this.lastGSMessage = System.currentTimeMillis();
		
		//////DEV STUFF//////
		if(Utils.DEBUG) {
			int error = (int) (this.currentDeltaTime * 1000 - 100);
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
		ListIterator<Bullet> bullet_iter = this.bullets.listIterator();
		while(bullet_iter.hasNext()){
			Bullet temp = bullet_iter.next();
		    if(temp.update(this)) {
		    	this.usedEntityIds.remove(temp.entity_id);
		    	bullet_iter.remove(); //remove the bullet from the list if it is done (animation done/hit a wall/etc)
		    }
		}
		
		////UPDATE PARTICLE EFFECTS////
		ListIterator<Particle> part_iter = this.particles.listIterator();
		while(part_iter.hasNext()){
			Particle temp = part_iter.next();
		    if(temp.update(this)) {
		    	this.usedEntityIds.remove(temp.entity_id);
		    	part_iter.remove(); //remove the bullet from the list if it is done (animation done/hit a wall/etc)
		    }
		}
		////UPDATE GROUND MASKS////
		ListIterator<GroundMask> mask_iter = this.ground_masks.listIterator();
		while(mask_iter.hasNext()){
			GroundMask temp = mask_iter.next();
		    if(temp.update()) {
		    	this.usedEntityIds.remove(temp.entity_id);
		    	mask_iter.remove();
		    }
		}
		
		
		//////APPLY INPUT SNAPSHOTS//////
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
		
		////// updating AI players ///////
		for (AI_player ai : AI_players) {
			if(!ai_player_delay) {
				ai.update(this, null);
			}
		}
		
		//////TODO: Check For other team kills?//////
		
		
		pu_handler.update(this); //update the powerups
	}

	@Override
	public List<Item> getItemList() {
		List<Item> list = new ArrayList<Item>();
		list.addAll(this.pu_handler.getPowerUpsList());
		return list;
	}

	@Override
	public String toDataString(Player p) {
		String output = "";
		
		//add game score data
		output += "001," + this.team_scores.get(1) + "," + this.team_scores.get(2) + "," + (time_limit - System.currentTimeMillis() + start_time) + ":";
		
		//////ADD NEW SOUNDS TO PLAY//////
		for(int i = 0; i < new_sounds.size(); i++){
			output += "002," + new_sounds.get(i) + ":";
		}
		
		//////ADD PLAYER MESSAGES///////
		for(int i = 0; i < players.size(); i++) {
			if((players.get(i).team == p.team) || (Utils.distanceBetween(p, players.get(i)) <= (p.visibility * Utils.GRID_LENGTH))) {
				output += players.get(i).toDataString(p.entity_id) + ":";
			}
		}
		
		//////ADD AI PLAYER MESSAGES///////
		for (int i = 0; i < AI_players.size(); i++) {
			if((AI_players.get(i).team == p.team) || (Utils.distanceBetween(p, AI_players.get(i)) <= (p.visibility * Utils.GRID_LENGTH))) {
				output += AI_players.get(i).toDataString(p.entity_id) + ":";
			}
		}
		
		//////ADD ITEM MESSAGES//////
		for (Item i : this.current_item_list) {
			output += i.toDataString(p.entity_id) + ":";
		}
		
		//////ADD PARTICLES//////
		for(Particle parts : particles) {
			output += parts.toDataString(p.entity_id) + ":";
		}
		
		//////ADD GROUND MASKS//////
		for(GroundMask gm : ground_masks) {
			output += gm.toDataString(p.entity_id) + ":";
		}
		
		//////ADD BULLET MESSAGES//////
		for(int i = 0; i < bullets.size(); i++) {
			output += bullets.get(i).toDataString(p.entity_id);
			if(i != bullets.size() - 1) output += ":";
		}
		
		return output; //RETURN THE MESSAGE
	}

	@Override
	public void playerJoin(String client_id, WebSocketSession session, String role, int team) {
		String pass = Utils.getGoodRandomString(this.userPasswords, 6);
		SpawnNode n = Utils.getRandomSpawn(this.spawns, team);
		Player p = new Player(n.getX(), n.getY(), Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, client_id, pass, session);
		this.players.add(p);
		p.stats.setGameType(this.getClass());
		this.userPasswords.add(pass);
		try {
			String message = "join:" + pass + ":" + this.game_id + ":" + "TDM:" + role + ":" + this.mapGridWidth + ":" + this.mapGridHeight;
			//if(Utils.DEBUG) System.out.println(message);
			for(Wall w : this.walls) {
				message += ":" + w.toDataString(client_id);
			}
			session.sendMessage(new TextMessage(message));
		} catch (IOException e) {
			System.out.println("could not send password for " + client_id + "! error!");
			e.printStackTrace();
		}
		
	}

	@Override
	public void add_AI_player(String role) {
		ai_player_delay = true;
		int team = 0;

		if (this.playersOnTeam1 > this.playersOnTeam2) {
			team = 2;
			this.playersOnTeam2++;
		} else {
			team = 1;
			this.playersOnTeam1++;
		}

		String s = Utils.getGoodRandomString(this.usedEntityIds, this.entity_id_len);
		SpawnNode n = Utils.getRandomSpawn(this.spawns, team);
		AI_players.add(new AI_player(n.getX(), n.getY(), Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, s));
		this.usedEntityIds.add(s);
		AI_players.get(AI_players.size() - 1).get_path(this);
		ai_player_delay = false;
	}

	@Override
	public void removePlayer(WebSocketSession session) {
		ListIterator<Player> iter = this.players.listIterator();
		while(iter.hasNext()){
			Player temp = iter.next();
		    if(temp.session.equals(session)) {
		    	if(temp.team == 1) {
		    		this.playersOnTeam1--;
		    	}
		    	else{
		    		this.playersOnTeam2--;
		    	}
		    	temp.leaveGame(); //drops items, et cetera
		    	this.usedEntityIds.remove(temp.entity_id);
		    	this.userPasswords.remove(temp.password);
		    	iter.remove();
		    	return;
		    }
		}
		
	}

	@Override
	public void setUpGame() {
		if(Utils.DEBUG) System.out.println("In TDM setup!");
		for(Player p : players) {
			p.stats.setLevelAndXP();
		}
		
		String roles[] = { "scout", "recruit", "infantry" };
		int number_ai_players = this.max_players - players.size();
		if(Utils.DEBUG) System.out.println("Making " + number_ai_players + " ais!");
		for (int i = 0; i < number_ai_players; i++) {
			add_AI_player(roles[Utils.RANDOM.nextInt(roles.length)]);
		}
		
		this.start_time = System.currentTimeMillis();
		this.started = true;
		if(Utils.DEBUG) System.out.println("TDM setup done!");
	}
}