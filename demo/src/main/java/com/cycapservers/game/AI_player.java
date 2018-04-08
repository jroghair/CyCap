package com.cycapservers.game;

import java.util.ArrayList;
import java.util.Random;

public class AI_player extends GameCharacter {

	//Pathing Related Stuff
	protected ArrayList<mapNode> path;
	protected boolean moving;
	protected int cur_p_node;
	protected boolean new_path;
	protected long last_path_update_time = 0;
	protected long temp_move_time = 0;

	protected AI_path_generator path_gen;
	// private long lastDeathTime;

	public AI_player(double x, double y, double w, double h, double r, double a, int team, String role, String ai_id, GameState g, PlayerStats stats) {
		super(0, 0, x, y, w, h, r, a, ai_id, team, role, stats);

		mapNode randomNode = getRandomNode(g);
		// set new location
		this.x = randomNode.x;
		this.y = randomNode.y;

		this.moving = true;
	}

	public void get_path(GameState g) {

		path_gen = new AI_path_generator(this, g);
		if (this.moving && g.players.size() > 0) {
			this.path = path_gen.get_a_star_path(this, g.players.get(0));
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

		// if(this.isDead){
		// if((System.currentTimeMillis() - this.lastDeathTime) > g.respawnTime)
		// {
		// 		this.respawn();
		// }
		// }

		// get initial path
		if (this.last_path_update_time == 0) {
			// running the path planning on a separate thread
			Thread t1 = new Thread(new Runnable() {
				public void run() {
					get_path(g);
				}
			});
			t1.start();
			//
			// this.new_path = true;
			// this.last_path_update_time = System.currentTimeMillis();
			// System.out.println("getting initial path...");
		}

		// if its been 2.5 seconds or the path is almost done update the path.
		if (this.path != null && (System.currentTimeMillis() - this.last_path_update_time) > 5000 || (this.get_distance_to_target() < 10 && (System.currentTimeMillis() - this.last_path_update_time) > 1000)) {
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
				 //System.out.println("waited " + (System.currentTimeMillis() -temp_move_time) + " ms to move");
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

	@Override
	public String toDataString(String client_id) {
		String output = "";
		output += "020,";
		output += super.toDataString(client_id);
		return output;
	}

	public void die() {
		this.x = -256;
		this.y = -256;
		this.isDead = true;
		// this.lastDeathTime = System.currentTimeMillis();
	}

	public void takeDamage(int amount) {
		if (!this.is_invincible) {
			this.health -= amount;
		}
		if (this.health <= 0) {
			this.die();
		}
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
		//respawn player
		this.x = 64;
		this.y = 64;
		//set isDead to false
		this.isDead = false;
		//reset ammo and health
		Utils.setRole(this);
	}
}
