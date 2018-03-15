package com.cycapservers.game;

public class Item extends Entity {
	
	protected String name;
	
	public Item(int id, int sprIdx, double x, double y, double w, double h, double r, double a, String name) {
		super(id, sprIdx, x, y, w, h, r, a);
		this.name = name;
	}
	
	public void use() {
		//TODO: much much later
	}
	
	public String toString() {
		return this.name;
	}
}