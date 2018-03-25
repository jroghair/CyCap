package com.cycapservers.game;

import java.util.List;

public final class Utils{
	public final static float GRAVITY = (float) 9.81;
	public final static int GRID_LENGTH = 32;
	public final static int UP    = 0b1000;
	public final static int DOWN  = 0b0100;
	public final static int LEFT  = 0b0010;
	public final static int RIGHT = 0b0001;
	public final static double SIN_45 = Math.sin(Math.PI/4);
	
	//////THE WEAPONS//////
	public final static Shotgun REMINGTON_870 = new Shotgun("Remington870", 25, 500, 500, 5, 4, 6000, 0.35);
	public final static Pistol M1911 = new Pistol("Pistol", 10, 175, 400, 8, 4, 200, 0.05);
	public final static Shotgun SAWED_OFF_SHOTGUN = new Shotgun("Sawed-Off Shotgun", 37, 350, 550, 2, 10, 2500, 0.55);
	public final static AutomaticGun SMG = new AutomaticGun("SMG", 5, 100, 600, 40, 4, 500, 0.1);
	public final static AutomaticGun ASSAULT_RIFLE = new AutomaticGun("Assault Rifle", 7, 120, 550, 30, 3, 1200, 0.08);
	public final static AutomaticGun MACHINE_GUN = new AutomaticGun("Machine Gun", 8, 134, 450, 100, 2, 1750, 0.15);
	
	private Utils(){} //prevents the class from being constructed
	
	public static boolean isBetween(double num, double lower, double upper){
		if(num >= lower && num <= upper){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static double distanceBetweenEntities(Entity ent1, Entity ent2){
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
	
	public static boolean isColliding(Entity ent_1, Entity ent_2){
		//TODO: fix the entity variable references
		//QUICK COLLISION DETECTION
		if(distanceBetweenEntities(ent_1, ent_2) >= (ent_1.collision_radius + ent_2.collision_radius)){
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
	
	public static void generateWallLine(List<Wall> walls, int startX, int startY, int length, char axis) {
		if(axis == 'x'){
			for(int i = 0; i < length; i++){
				walls.add(new Wall(0, startX + i, startY, false));
			}
		}
		else if(axis == 'y'){
			for(int i = 0; i < length; i++){
				walls.add(new Wall(0, startX, startY + i, false));
			}
		}
		else{
			throw new IllegalArgumentException("Error in the function generateWallLine");
		}
	}
	
	public static void generateWallLine(List<Wall> walls, int startX, int startY, int length, char axis, boolean invincible) {
		if(axis == 'x'){
			for(int i = 0; i < length; i++){
				walls.add(new Wall(0, startX + i, startY, invincible));
			}
		}
		else if(axis == 'y'){
			for(int i = 0; i < length; i++){
				walls.add(new Wall(0, startX, startY + i, invincible));
			}
		}
		else{
			throw new IllegalArgumentException("Error in the function generateWallLine");
		}
	}
	
	public static void placeBorder(List<Wall> walls, int width, int height, int startX, int startY){
		generateWallLine(walls, startX, startY, width, 'x');
		generateWallLine(walls, startX, height + startY - 1, width, 'x');
		generateWallLine(walls, startX, startY + 1, height - 2, 'y');
		generateWallLine(walls, width + startX - 1, startY + 1, height - 2, 'y');
	}
	
	public static void placeBorder(List<Wall> walls, int width, int height, int startX, int startY, boolean invincible){
		generateWallLine(walls, startX, startY, width, 'x', invincible);
		generateWallLine(walls, startX, height + startY - 1, width, 'x', invincible);
		generateWallLine(walls, startX, startY + 1, height - 2, 'y', invincible);
		generateWallLine(walls, width + startX - 1, startY + 1, height - 2, 'y', invincible);
	}
	
	/**
	 * Sets the role data of the player based off of their "role" field
	 * @param p The player which we are setting the role/class data
	 */
	public static void setRole(Player p) {
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
}