package com.cycapservers.game;

public class Entity {
	
	protected String entity_id;
	
	protected int imageId;
	
	protected int spriteIndex;
	
	protected double x;
	
	protected double y;
	
	private double drawWidth;
	
	private double drawHeight;
	
	protected double collision_radius;
	
	protected double rotation; //in radians
	
	protected double alpha;
	
	public Entity(int id, int sprIdx, double x, double y, double w, double h, double r, double a, String entity_id){
		this.imageId = id;
		this.spriteIndex = sprIdx;
		this.x = x;
		this.y = y;
		this.drawWidth = w;
		this.drawHeight = h;
		this.rotation = r;
		this.alpha = a;
		this.entity_id = entity_id;
		updateCollision_radius();
	}
	
	public String toDataString(String client_id){
		String output = "";
		output += entity_id + ",";
		output += imageId + ",";
		output += spriteIndex + ",";
		output += (int) x + ",";
		output += (int) y + ",";
		output += drawWidth + ",";
		output += drawHeight + ",";
		output += rotation + ",";
		output += alpha;
		return output;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public int getSpriteIndex() {
		return spriteIndex;
	}

	public void setSpriteIndex(int spriteIndex) {
		this.spriteIndex = spriteIndex;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDrawWidth() {
		return drawWidth;
	}

	public void setDrawWidth(double drawWidth) {
		this.drawWidth = drawWidth;
		updateCollision_radius();
	}

	public double getDrawHeight() {
		return drawHeight;
	}

	public void setDrawHeight(double drawHeight) {
		this.drawHeight = drawHeight;
		updateCollision_radius();
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getCollision_radius() {
		return collision_radius;
	}
	
	public void updateCollision_radius() {
		collision_radius = Utils.distanceBetween(x, y, x + drawWidth/2, y + drawHeight/2);
	}
}
