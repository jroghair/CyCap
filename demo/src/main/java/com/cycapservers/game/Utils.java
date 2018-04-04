package com.cycapservers.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Utils{
	public final static boolean DEBUG = false;
	public final static float GRAVITY = (float) 9.81;
	public final static int GRID_LENGTH = 32;
	public final static int UP    = 0b1000;
	public final static int DOWN  = 0b0100;
	public final static int LEFT  = 0b0010;
	public final static int RIGHT = 0b0001;
	public final static double SIN_45 = Math.sin(Math.PI/4);
	public final static int AI_NODE_PIXEL_DISTANCE = 8;
	public final static Random RANDOM = new Random();
	
	//////THE WEAPONS//////
	public final static Shotgun REMINGTON_870 = new Shotgun("Remington870", 25, 500, 500, 5, 4, 6000, 0.35);
	public final static Pistol M1911 = new Pistol("Pistol", 10, 175, 400, 8, 4, 200, 0.05);
	public final static Shotgun SAWED_OFF_SHOTGUN = new Shotgun("Sawed-Off Shotgun", 37, 350, 550, 2, 10, 2500, 0.55);
	public final static AutomaticGun SMG = new AutomaticGun("SMG", 5, 100, 600, 40, 4, 500, 0.1);
	public final static AutomaticGun ASSAULT_RIFLE = new AutomaticGun("Assault Rifle", 7, 120, 550, 30, 3, 1200, 0.08);
	public final static AutomaticGun MACHINE_GUN = new AutomaticGun("Machine Gun", 8, 134, 450, 100, 2, 1750, 0.15);
	
	//////THE POWERUPS//////
	public final static SpeedPotion SPEED_POTION = new SpeedPotion(0, 0, GRID_LENGTH, GRID_LENGTH, 0, 1.0, "speed_pot_template");
	public final static HealthPack HEALTH_PACK = new HealthPack(0, 0, GRID_LENGTH, GRID_LENGTH, 0, 1.0, "health_pack_template");
	
	private Utils(){} //prevents the class from being constructed
	
	public static boolean isBetween(double num, double lower, double upper){
		if(num >= lower && num <= upper){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static double distanceBetween(Entity ent1, Entity ent2){
		return Math.sqrt(Math.pow(ent1.getX() - ent2.getX(), 2) + Math.pow(ent1.getY() - ent2.getY(), 2));
	}
	
	/**
	 * Returns the distance between two coordinate pairs
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance between the two points
	 */
	public static double distanceBetween(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2.0) + Math.pow(y2 - y1, 2.0));
	}
	
	public static double distanceBetween(Node n1, Node n2) {
		return Math.sqrt(Math.pow(n2.getX() - n1.getX(), 2.0) + Math.pow(n2.getY() - n1.getY(), 2.0));
	}
	
	public static double distanceBetween(Entity ent, Node n) {
		return Math.sqrt(Math.pow(n.getX() - ent.x, 2.0) + Math.pow(n.getY() - ent.y, 2.0));
	}
	
	public static boolean isColliding(Entity ent, Node n) {
		if(distanceBetween(ent, n) >= ent.collision_radius){
			return false;
		}
		else {
			if(n.getX() > (ent.x - ent.drawWidth/2) && n.getX() < (ent.x + ent.drawWidth/2)) {
				if(n.getY() > (ent.y - ent.drawHeight/2) && n.getY() < (ent.y + ent.drawHeight/2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isColliding(Entity ent_1, Entity ent_2){
		//QUICK COLLISION DETECTION
		if(distanceBetween(ent_1, ent_2) >= (ent_1.collision_radius + ent_2.collision_radius)){
			return false;
		}
		
		//ADVANCED COLLISION DETECTION
		boolean y_collision = isBetween(ent_1.y - (ent_1.drawHeight/2), ent_2.y - (ent_2.drawHeight/2), ent_2.y + (ent_2.drawHeight/2)) || isBetween(ent_1.y + (ent_1.drawHeight/2), ent_2.y - (ent_2.drawHeight/2), ent_2.y + (ent_2.drawHeight/2)) || isBetween(ent_1.y, ent_2.y - (ent_2.drawHeight/2), ent_2.y + (ent_2.drawHeight/2));

		if(isBetween(ent_1.x - (ent_1.drawWidth/2), ent_2.x - (ent_2.drawWidth/2), ent_2.x + (ent_2.drawWidth/2)) && y_collision){
			return true;
		}
		else if(isBetween(ent_1.x + (ent_1.drawWidth/2), ent_2.x - (ent_2.drawWidth/2), ent_2.x + (ent_2.drawWidth/2)) && y_collision){
			return true;
		}
		else if(isBetween(ent_1.x, ent_2.x - (ent_2.drawWidth/2), ent_2.x + (ent_2.drawWidth/2)) && y_collision){
			return true;
		}
		else{
			return false;
		}
		
		/* This was the older method of collision detection. it is simpler and could still be used for more basic detection
		if (isBetween(ent_1.x, (ent_2.x -  (ent_2.dWidth/2)), (ent_2.x +  (ent_2.dWidth/2)))
		 && isBetween(ent_1.y, (ent_2.y -  (ent_2.dHeight/2)), (ent_2.y +  (ent_2.dHeight/2)))){
			return true;
		}
		else{
			return false;
		}
		*/
	}
	
	//returns a random int between 0 and max, not including max
	public static int getRandomInt(int max){
		return (int) Math.floor(Math.random() * max);
	}

	//returns a random int between lower and upper, inclusive
	public static int getRandomInRange(int lower, int upper){
		return getRandomInt(upper - lower + 1) + lower;
	}
	
	public static void generateWallLine(GameState g, int startX, int startY, int length, char axis) {
		if(axis == 'x'){
			for(int i = 0; i < length; i++){
				String s = getGoodRandomString(g.usedEntityIds, g.entity_id_len);
				g.walls.add(new Wall(0, startX + i, startY, false, s));
				g.usedEntityIds.add(s);
			}
		}
		else if(axis == 'y'){
			for(int i = 0; i < length; i++){
				String s = getGoodRandomString(g.usedEntityIds, g.entity_id_len);
				g.walls.add(new Wall(0, startX, startY + i, false, s));
				g.usedEntityIds.add(s);
			}
		}
		else{
			throw new IllegalArgumentException("Error in the function generateWallLine");
		}
	}
	
	public static void generateWallLine(GameState g, int startX, int startY, int length, char axis, boolean invincible) {
		if(axis == 'x'){
			for(int i = 0; i < length; i++){
				String s = getGoodRandomString(g.usedEntityIds, g.entity_id_len);
				g.walls.add(new Wall(0, startX + i, startY, invincible, s));
				g.usedEntityIds.add(s);
			}
		}
		else if(axis == 'y'){
			for(int i = 0; i < length; i++){
				String s = getGoodRandomString(g.usedEntityIds, g.entity_id_len);
				g.walls.add(new Wall(0, startX, startY + i, invincible, s));
				g.usedEntityIds.add(s);
			}
		}
		else{
			throw new IllegalArgumentException("Error in the function generateWallLine");
		}
	}
	
	public static void placeBorder(GameState g, int width, int height, int startX, int startY){
		generateWallLine(g, startX, startY, width, 'x');
		generateWallLine(g, startX, height + startY - 1, width, 'x');
		generateWallLine(g, startX, startY + 1, height - 2, 'y');
		generateWallLine(g, width + startX - 1, startY + 1, height - 2, 'y');
	}
	
	public static void placeBorder(GameState g, int width, int height, int startX, int startY, boolean invincible){
		generateWallLine(g, startX, startY, width, 'x', invincible);
		generateWallLine(g, startX, height + startY - 1, width, 'x', invincible);
		generateWallLine(g, startX, startY + 1, height - 2, 'y', invincible);
		generateWallLine(g, width + startX - 1, startY + 1, height - 2, 'y', invincible);
	}
	
	/**
	 * Sets the role data of the player based off of their "role" field
	 * @param p The player which we are setting the role/class data
	 */
	public static void setRole(GameCharacter p) {
		String role = p.role;
		if(role.equals("recruit")) {
			p.speed = 140;
			p.max_health = 100;
			p.health = p.max_health;
			p.weapon1 = new AutomaticGun(ASSAULT_RIFLE);
			p.weapon2 = new Shotgun(REMINGTON_870);
			p.weapon3 = null;
			p.weapon4 = null;
			p.currentWeapon = p.weapon1;
			p.visibility = 6;
			return;
		}
		else if(role.equals("artillery")) {
			return;
		}
		else if(role.equals("infantry")) {	
			p.speed = 140;
			p.max_health = 105;
			p.health = p.max_health;
			p.weapon1 = new AutomaticGun(MACHINE_GUN);
			p.weapon2 = null;
			p.weapon3 = new Pistol(M1911); //pistol
			p.weapon4 = null;
			p.currentWeapon = p.weapon1;
			p.visibility = 5;
			return;
		}
		else if(role.equals("scout")) {	
			p.speed = 180;
			p.max_health = 75;
			p.health = p.max_health;
			p.weapon1 = new Shotgun(SAWED_OFF_SHOTGUN);
			p.weapon2 = new Pistol(M1911); //pistol
			p.weapon3 = null;
			p.weapon4 = null;
			p.currentWeapon = p.weapon1;
			p.visibility = 7;
			return;
		}
		else {
			throw new IllegalStateException("Player role is unacceptable!");
		}
	}
	
	public static Point get_nearest_map_node(Entity e, GameState g) {
		int x = (int) (Math.ceil(e.x / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
		int y = (int) (Math.ceil(e.y / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
		short i = 0, j = 0;
		while (g.map.get(i).get(j).y != y) {
			j++;
		}
		while (g.map.get(i).get(j).x != x) {
			i++;
		}
		if (g.map.get(i).get(j).node_trav != false) {
			return new Point(i, j);
		} else {
			x = (int) (Math.floor(e.x / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
			y = (int) (Math.floor(e.y / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
			i = 0;
			j = 0;
			while (g.map.get(i).get(j).y != y) {
				j++;
			}
			while (g.map.get(i).get(j).x != x) {
				i++;
			}
			if (g.map.get(i).get(j).node_trav != false) {
				return new Point(i, j);
			} else {
				x = (int) (Math.floor(e.x / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
				y = (int) (Math.ceil(e.y / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
				i = 0;
				j = 0;
				while (g.map.get(i).get(j).y != y) {
					j++;
				}
				while (g.map.get(i).get(j).x != x) {
					i++;
				}
				if (g.map.get(i).get(j).node_trav != false) {
					return new Point(i, j);
				} else {
					x = (int) (Math.ceil(e.x / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
					y = (int) (Math.floor(e.y / AI_NODE_PIXEL_DISTANCE) * AI_NODE_PIXEL_DISTANCE);
					i = 0;
					j = 0;
					while (g.map.get(i).get(j).y != y) {
						j++;
					}
					while (g.map.get(i).get(j).x != x) {
						i++;
					}
					if (g.map.get(i).get(j).node_trav != false) {
						return new Point(i, j);
					} else {
						System.out.println("Couldn't find a traversable node near entity");
					}
				}
			}
		}
		return null;
	}
	
	public static ArrayList<ArrayList<mapNode>> generate_node_array(GameState g){ //passing GameState so it doesn't have to be static
		short index_i = 0;
		short index_j = 0;
		ArrayList<ArrayList<mapNode>> map = new ArrayList<ArrayList<mapNode>>(); //2d map of nodes
		for(int i = 0; i < (GRID_LENGTH * g.mapGridWidth); i += Utils.AI_NODE_PIXEL_DISTANCE){
			ArrayList<mapNode> node_col = new ArrayList<mapNode>();
			for(int j = 0;j < (GRID_LENGTH * g.mapGridHeight );j += Utils.AI_NODE_PIXEL_DISTANCE){
				Entity test_player_ent = new Entity(0, 0, i, j, GRID_LENGTH, GRID_LENGTH, 0, 1, "temp");//used for traversing
				boolean traversable = true;
				for(int t = 0; t < g.walls.size(); t++){
					if(isColliding(test_player_ent, g.walls.get(t))){
						traversable = false;
						break; //colliding with one wall is enough to know this isn't a possible space
					}
				}
				node_col.add(new mapNode(i, j, traversable, index_i, index_j));
				index_j++;
			}
			map.add(node_col);
			index_i++;
			index_j = 0;
		}
		return map;
	}
	
	public static ArrayList<mapNode> get_neighbors(GameState g, mapNode node, ArrayList<mapNode> closed_list, ArrayList<mapNode> open_list) {
		ArrayList<mapNode> neighbors = new ArrayList<mapNode>();
		if (g.map.get(node.gridX - 1).get(node.gridY - 1).node_trav == true) {
			neighbors.add(g.map.get(node.gridX - 1).get(node.gridY - 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
						&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = true;
		}
		if (g.map.get(node.gridX - 1).get(node.gridY).node_trav == true) {
			neighbors.add(g.map.get(node.gridX - 1).get(node.gridY));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		if (g.map.get(node.gridX - 1).get(node.gridY + 1).node_trav == true) {
			neighbors.add(g.map.get(node.gridX - 1).get(node.gridY + 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = true;
		}
		if (g.map.get(node.gridX).get(node.gridY + 1).node_trav == true) {
			neighbors.add(g.map.get(node.gridX).get(node.gridY + 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		// neighbor number 5
		if (g.map.get(node.gridX + 1).get(node.gridY + 1).node_trav == true) {
			neighbors.add(g.map.get(node.gridX + 1).get(node.gridY + 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = true;
		}
		if (g.map.get(node.gridX + 1).get(node.gridY).node_trav == true) {
			neighbors.add(g.map.get(node.gridX + 1).get(node.gridY));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		if (g.map.get(node.gridX + 1).get(node.gridY - 1).node_trav == true) {
			neighbors.add(g.map.get(node.gridX + 1).get(node.gridY - 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = true;
		}
		if (g.map.get(node.gridX).get(node.gridY - 1).node_trav == true) {
			neighbors.add(g.map.get(node.gridX).get(node.gridY - 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * AI_NODE_PIXEL_DISTANCE);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		return neighbors;
	}
	
	public static String getGoodRandomString(List<String> currentList, int length) {
		String output = createString(length);
		while(!isStringGood(currentList, output)) {
			output = createString(length);
		}
		return output;
	}
	
	private static boolean isStringGood(List<String> currentList, String pw) {
		for(String s : currentList) {
			if(pw.equals(s)) {
				return false;
			}
		}
		return true;
	}
	
	private static String createString(int length){
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
		Random rand = new Random();
		String pass = "";
		for(int i = 0; i < length; i++){
			pass += s.charAt(rand.nextInt(s.length()));	
		}
		return pass;
	}
	
	public static int getSpriteIndexFromTeam(int team) {
		switch(team) {
			case 1:
				return 4;
				
			case 2:
				return 0;
				
			default:
				throw new IllegalArgumentException("illegal team number, no sprite index associated!");
		}
	}
	
	/**
	 * Returns a random spawn node that has the same team as the parameter
	 * @param nodes
	 * @param team
	 * @return
	 */
	public static SpawnNode getRandomSpawn(List<SpawnNode> nodes, int team) {
		List<SpawnNode> goodNodes = new ArrayList<SpawnNode>();
		for(SpawnNode n : nodes) {
			if(n.team == team) {
				goodNodes.add(n);
			}
		}
		return goodNodes.get(RANDOM.nextInt(goodNodes.size()));
	}
	
	public static SpawnNode getRandomSpawn(List<SpawnNode> nodes) {
		return nodes.get(RANDOM.nextInt(nodes.size()));
	}
}