package com.cycapservers.game;

import java.util.ArrayList;
import java.util.Random;

public class AI_player extends GameCharacter {

	// Path Related Stuff
	protected ArrayList<mapNode> path;
	protected boolean moving;
	protected int cur_p_node;
	protected boolean new_path;
	protected long last_path_update_time = 0;
	protected long temp_move_time = 0;
	protected double firing_chance;

	protected AI_path_generator path_gen;
	// private long lastDeathTime;
	private long last_shoot_time = 0;

	public AI_player(double x, double y, double w, double h, double r, double a, int team, String role, String ai_id,
			GameState g, PlayerStats stats) {
		super(0, 0, x, y, w, h, r, a, ai_id, team, role, stats);

//		mapNode randomNode = getRandomNode(g);
		// set new location
		this.x = x;//randomNode.x;
		this.y = y;//randomNode.y;

		Random ran = new Random();
		this.firing_chance = (ran.nextDouble() * 40.0 + 20.0) / 100.0;//20-60%
		
		this.moving = true;
	}

	public void get_path(GameState g) {

		/*
		 * if it has the flag then path to home base
		 * if enemy has the flag path to them and shoot
		 */
		
		Entity target;
		
		if(this.team == 1){
			if(g.team1_flag.grabbed){//if our flag is grabbed
				target = g.team1_flag.grabber;
			}else{
				if(this.item_slot == g.team2_flag){
					//quick fix to target to base
					Entity e = new Entity(0,0,(double)g.team1_base.getX(), 
							(double)g.team1_base.getY(), 0.0, 0.0, 0.0, 0.0, "");
					target = e;
				}else{
					target = g.team2_flag;
				}
			}
		}else{
			if(g.team2_flag.grabbed){//if our flag is grabbed
				target = g.team2_flag.grabber;
			}else{
				if(this.item_slot == g.team1_flag){
					//quick fix to target to base
					Entity e = new Entity(0,0,(double)g.team2_base.getX(), 
							(double)g.team2_base.getY(), 0.0, 0.0, 0.0, 0.0, "");
					target = e;
				}else{
					target = g.team1_flag;
				}
			}
		}

		path_gen = new AI_path_generator(this, g);
		if (this.moving && g.players.size() > 0) {
			this.path = path_gen.get_a_star_path(this, target);
		}
		this.new_path = true;
		this.last_path_update_time = System.currentTimeMillis();

	}

	public double get_distance_to_target() {
		if (path != null) {
			return Math.sqrt(Math.pow((this.x - path.get(0).x), 2) + Math.pow((this.y - path.get(0).y), 2));
		} else {
			return 0.0;
		}
	}

	public void update(GameState g, InputSnapshot s) {
		
		 if(this.isDead){
			 if((System.currentTimeMillis() - this.lastDeathTime) > g.respawnTime)
			 {
				 this.respawn(g);
			 }
		 }else{

			if(this.item_slot == null) {
				for(Item i : g.current_item_list) {
					if(Utils.isColliding(this, i)) {
						i.pickUp(this);
					}
				}
			}
			 
			if(System.currentTimeMillis() - this.last_shoot_time  > 100){
				Random r = new Random();
				if(r.nextDouble() <= this.firing_chance){
					//shoot
					System.out.println("ai shot or reloaded: " + this.entity_id);
					
					//will crash if there are no players
					double map_x = g.players.get(0).x;
					double map_y = g.players.get(0).y;
					
					//form new snap shot and fire weapon
					InputSnapshot ai_snapshot = new InputSnapshot(":test:" + map_x + ":" + map_y + ":0.0:0.0:true:true:::0:0.0");
					
					if(this.currentWeapon.ammo_in_clip > 1){
						this.currentWeapon.update(this, ai_snapshot, g);
					}else{
						this.currentWeapon.reload();
					}
				}
				this.last_shoot_time = System.currentTimeMillis();
			}
			 
			 
			 
			 
			// get initial path
			if (this.last_path_update_time == 0) {
				get_path(g);
			}
	
			// if its been 2.5 seconds or the path is almost done update the path.
			if (this.path != null && (System.currentTimeMillis() - this.last_path_update_time) > 5000
					|| (this.get_distance_to_target() < 10
							&& (System.currentTimeMillis() - this.last_path_update_time) > 1000)) {
				get_path(g);
			}
	
			if (this.moving && path != null) {
				if (this.new_path && (System.currentTimeMillis() - temp_move_time) > 150) {
					this.cur_p_node = path.size() - 1;
					this.x = path.get(cur_p_node).x;
					this.y = path.get(cur_p_node).y;
					this.new_path = false;
					this.cur_p_node--;
					this.temp_move_time = System.currentTimeMillis();// time of
																		// movement
				}
				if ((System.currentTimeMillis() - temp_move_time) > 0 && this.cur_p_node >= 0 && !this.new_path) {
					// System.out.println("waited " + (System.currentTimeMillis()
					// -temp_move_time) + " milliseconds to move");
					while (this.cur_p_node >= path.size()) {
						this.cur_p_node--;// just for safety
					}
					this.x = path.get(cur_p_node).x;
					this.y = path.get(cur_p_node).y;
					this.cur_p_node--;
					this.temp_move_time = System.currentTimeMillis();// time of
																		// movement
				}
			}
		 }
	}

	@Override
	public String toDataString(String client_id) {
		String output = "";
		output += "020,";
		output += super.toDataString(client_id);
		return output;
	}

	public void die() {
		this.isDead = true;
		if(this.item_slot !=  null) {
			this.item_slot.drop();
			this.item_slot = null;
		}
		this.x = -256;
		this.y = -256;
		this.lastDeathTime = System.currentTimeMillis();
	}

	public mapNode getRandomNode(GameState g) {
		boolean trav = false;
		Random rangen = new Random();
		int randi = rangen.nextInt(g.map.size());
		int randj = rangen.nextInt(g.map.get(0).size());
		while (!trav) {
			randi = rangen.nextInt(g.map.size());
			randj = rangen.nextInt(g.map.get(0).size());
			if (g.map.get(randi).get(randj).node_trav == true) {
				trav = true;
			}
		}
		return g.map.get(randi).get(randj);
	}

	@Override
	protected void respawn(GameState g) {
		// re-spawn player
		this.x = 64;
		this.y = 64;
		// set isDead to false
		this.isDead = false;
		// reset ammo and health
		Utils.setRole(this);
	}
}
