package com.cycapservers.game;

public abstract class Item extends Entity {
	
	protected String name;
	protected Player grabber = null;
	protected boolean grabbed = false;
	
	public Item(int id, int sprIdx, double x, double y, double w, double h, double r, double a, String name) {
		super(id, sprIdx, x, y, w, h, r, a);
		this.name = name;
	}
	
	public void pickUp(Player grabber) {
		if(!this.grabbed) {
			this.grabber = grabber;
			this.grabbed = true;
		}
	}
	
	public abstract void use();
	
	@Override
	public abstract String toString();
}