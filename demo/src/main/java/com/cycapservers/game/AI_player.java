package com.cycapservers.game;

import java.util.ArrayList;
import java.util.Random;

public class AI_player extends Entity {

	protected int team;
	protected String role;
	protected Weapon weapon1;
	protected Weapon weapon2;
	protected Weapon weapon3;
	protected Weapon weapon4;
	protected Weapon currentWeapon;
	protected Item item_slot;
	protected int health;
	protected int max_health;
	protected boolean isDead;
	protected double speed;
	protected int visibility;
	protected boolean is_invincible;
	protected double speed_boost;
	protected double damage_boost;

	protected ArrayList<mapNode> path;
	protected boolean moving;
	protected int cur_p_node;
	protected boolean new_path;
	protected long last_path_update_time = 0;
	protected long temp_move_time = 0;
	protected ArrayList<ArrayList<mapNode>> map;
	protected AI_path_generator path_gen;
	protected AI_map_generator map_gen;
	protected AI_utils AI_util;
	private boolean map_finished = false;

	public AI_player(int x, int y, int w, int h, int r, double a, int team, String role, GameState g) {
		super(0, 0, x, y, w, h, r, a);
		// creates a new map generator
		this.map_gen = new AI_map_generator(g);
		// generate the map when player is constructed
		this.map = map_gen.generate_node_array(g);
		this.map_finished = true;

		AI_util = new AI_utils(this, g);
		
		mapNode randomNode = getRandomNode();
		// set new location
		this.x = randomNode.x;
		this.y = randomNode.y;

		this.moving = true;

		if (team == 1) {
			this.spriteIndex = 4;
		} else {
			this.spriteIndex = 0;
		}
		this.team = team;
		this.role = role;

		this.isDead = false;
		this.is_invincible = false;
		this.speed_boost = 1.0;
		this.damage_boost = 1.0;

		this.item_slot = null;
		if (this.role.equals("recruit")) {
			this.speed = 150;
			this.max_health = 100;
			this.health = this.max_health;
			this.weapon1 = new Shotgun("Remington870", 30, 500, 500, 5, 4, 6000, 0.35);
			this.weapon2 = new Pistol("Pistol", 11, 100, 400, 8, 4, 200, 0.05); // pistol
			this.weapon3 = null;
			this.weapon4 = null;
			this.currentWeapon = this.weapon1;
		}
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

	public void update(GameState g) {

		// if(this.isDead){
		// if((System.currentTimeMillis() - this.lastDeathTime) > g.respawnTime)
		// {
		// //re spawn player
		// this.x = 64;
		// this.y = 64;
		// //set isDead to false
		// this.isDead = false;
		// //reset ammo and health
		// Utils.setRole(this);
		// }
		// }

		// get initial path if the map is done being generated
		if (this.last_path_update_time == 0 && this.map_finished) {
			// running the path planning on a separate thread
//			Thread t1 = new Thread(new Runnable() {
//				public void run() {
					get_path(g);
//				}
//			});
//			t1.start();

		}

		// if its been 2.5 seconds or the path is almost done update the path.
		if (this.path != null && (System.currentTimeMillis() - this.last_path_update_time) > 5000
				|| (this.get_distance_to_target() < 10
						&& (System.currentTimeMillis() - this.last_path_update_time) > 1000)) {
			// running the path planning on a separate thread
//			Thread t2 = new Thread(new Runnable() {
//				public void run() {
					get_path(g);
//				}
//			});
//			t2.start();
		}

		if (path != null && this.moving) {
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
				// -temp_move_time) + " ms to move");
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
	public String toString() {
		String output = "";
		output += "000,";// 003
		output += ",";
		output += ",";
		output += 1 + ",";
		output += 1 + ",";
		output += this.x + ",";
		output += this.y + ",";
		output += this.drawWidth + ",";
		output += this.drawHeight + ",";
		output += this.rotation + ",";
		output += this.alpha + ",";
		output += this.role + ",";
		output += this.team + ",";
		output += this.currentWeapon.toString() + ",";
		if (this.item_slot == null) {
			output += "empty" + ",";
		} else {
			output += this.item_slot.toString() + ",";
		}
		output += this.health + ",";
		output += this.is_invincible + ",";
		output += this.speed_boost + ",";
		output += this.damage_boost;
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

	public mapNode getRandomNode() {
		boolean trav = false;
		Random rangen = new Random();
		int randi = rangen.nextInt(map.size());
		int randj = rangen.nextInt(map.get(0).size());
		while (!trav) {
			randi = rangen.nextInt(map.size());
			randj = rangen.nextInt(map.get(0).size());
			if (map.get(randi).get(randj).node_trav == true) {
				trav = true;
			}
		}
		return this.map.get(randi).get(randj);
	}

}
