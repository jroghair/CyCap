package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.cycapservers.account.ProfileDataUpdate;

public class CaptureTheFlag extends GameState {
	
	//////PLAYERS//////
	protected volatile int playersOnTeam1;
	protected volatile int playersOnTeam2;
	///////////////////
	
	//////CTF STUFF//////
	protected GridLockedNode team1_base;
	protected GridLockedNode team2_base;
	protected Flag team1_flag;
	protected Flag team2_flag;
	/////////////////////
	
	public CaptureTheFlag(String id, int map_number) {
		super(id);
		this.max_players = 8;
		this.playersOnTeam1 = 0;
		this.playersOnTeam2 = 0;
		this.team_scores.put(1, 0); //for TDM and CTF only
		this.team_scores.put(2, 0);
		
		pu_handler = new PowerUpHandler((short) 30000, (short) 2500);
		
		MapLoader.loadPredefinedMap(map_number, this);//load up the map
		
		// generate the map when player is constructed
		this.ai_map = Utils.generate_node_array(this);

		friendlyFire = false;
		respawnTime = 10000; //10 seconds respawn time
		time_limit = 2 * 60 * 1000; //5 minutes to ms
		score_limit = 5; //5 flag captures
	}

	public void updateGameState() {
		//////CHECK TO SEE IF END GAME CONDITIONS ARE MET//////
		if(((System.currentTimeMillis() - this.start_time) >= time_limit) || (team_scores.get(1) >= score_limit) || (team_scores.get(2) >= score_limit)) {
			//TODO: set winning team correctly by comparing flag captures first and then kills
			winner = 1;
			endGame(winner);
		}
		
		this.currentDeltaTime = (System.currentTimeMillis() - this.lastGSMessage)/1000.0;
		this.lastGSMessage = System.currentTimeMillis();
		
		//DEV STUFF
		if(Utils.DEBUG && false) {
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
		//UPDATE the flags
		this.team1_flag.update();
		this.team2_flag.update();
		////UPDATE PARTICLE EFFECTS////
		ListIterator<Particle> part_iter = this.particles.listIterator();
		while(part_iter.hasNext()){
			Particle temp = part_iter.next();
		    if(temp.update(this)) {
		    	this.usedEntityIds.remove(temp.entity_id);
		    	part_iter.remove();
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
		
		/*/ updating AI players
		for (AI_player ai : AI_players) {
				ai.update(this, null);
		}*/
		
		//////Check For Flag captures//////
		if(!this.team1_flag.atBase && this.team2_flag.atBase && Utils.isColliding(this.team1_flag, team2_base)) {
			this.team_scores.put(2, this.team_scores.get(2) + 1); //+1 to team 2
			this.team1_flag.grabber.stats.addFlagCapture(); //give the proper player a flag capture
			this.team1_flag.returnToBase(); //return the flag to base
			if(Utils.DEBUG) System.out.println("FLAG 1 CAPTURED!!");
		}
		else if(!this.team2_flag.atBase && this.team1_flag.atBase && Utils.isColliding(this.team2_flag, team1_base)) {
			this.team_scores.put(1, this.team_scores.get(1) + 1); //+1 to team 1
			this.team2_flag.grabber.stats.addFlagCapture(); //give the proper player a flag capture
			this.team2_flag.returnToBase(); //return the flag to base
			if(Utils.DEBUG) System.out.println("FLAG 2 CAPTURED!!");
		}
		
		pu_handler.update(this); //update the powerups
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
	
	public void playerJoin(String client_id, WebSocketSession session, String role) {
		int team;
		//synchronized {
			if(this.playersOnTeam1 == 0 && this.playersOnTeam2 == 0) {
				team = Utils.RANDOM.nextInt(2) + 1;
			}
			else if(this.playersOnTeam1 > this.playersOnTeam2) {
				team = 2;
				this.playersOnTeam2++;
			}
			else {
				team = 1;
				this.playersOnTeam1++;
			}
		//}
		String pass = Utils.getGoodRandomString(this.userPasswords, 6);
		SpawnNode n = Utils.getRandomSpawn(this.spawns, team);
		Player p = new Player(n.getX(), n.getY(), Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, client_id, pass, session);
		this.players.add(p);
		p.stats.setGameType(this.getClass());
		this.userPasswords.add(pass);
		try {
			
			String message = "join:" + pass + ":" + this.game_id + ":" + "CTF:" + role;
			for(Wall w : this.walls) {
				message += ":" + w.toDataString(client_id);
			}
			session.sendMessage(new TextMessage(message));
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
		AI_players.add(new AI_player(n.getX(), n.getY(), Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, team, role, s, this));
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
		    	temp.leaveGame(); //drops items, et cetera
		    	this.usedEntityIds.remove(temp.entity_id);
		    	this.userPasswords.remove(temp.password);
		    	iter.remove();
		    	return;
		    }
		}
	}
	
	public void setUpGame() {
		if(Utils.DEBUG) System.out.println("Num of Players @ setup: " + players.size());
		for(Player p : players) {
			p.stats.setLevelAndXP();
		}
		//for(int i = 0; i < (max_players - players.size()); i++){
		//	addAI_player();
		//}
		this.start_time = System.currentTimeMillis();
		this.started = true;
	}
}