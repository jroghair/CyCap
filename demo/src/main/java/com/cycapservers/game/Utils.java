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
	
	public static void placeBorder(List<Wall> walls, int width, int height, int startX, int startY){
		generateWallLine(walls, startX, startY, width, 'x');
		generateWallLine(walls, startX, height + startY - 1, width, 'x');
		generateWallLine(walls, startX, startY + 1, height - 2, 'y');
		generateWallLine(walls, width + startX - 1, startY + 1, height - 2, 'y');
	}
	
	public static void setRole(Player p) {
		switch(p.role) {
			case "recruit":
				//something
				break;
				
			default:
				throw new IllegalStateException("Player role is unacceptable!");
		}
	}
}