package com.cycapservers.game;

public abstract class Item extends Entity {
	
	protected String name;
	protected GameCharacter grabber = null;
	protected boolean grabbed = false;
	
	public Item(int id, int sprIdx, double x, double y, double w, double h, double r, double a, String name, String entity_id) {
		super(id, sprIdx, x, y, w, h, r, a, entity_id);
		this.name = name;
	}
	
	public void pickUp(GameCharacter grabber) {
		if(!this.grabbed) {
			this.grabber = grabber;
			this.grabbed = true;
			this.grabber.item_slot = this;
		}
	}
	
	/**
	 * Use the item. Throws an IllegalStateException if grabber is null.
	 * @return boolean: returns true if the item is all used up and is to be removed from the grabber's inventory
	 */
	public abstract boolean use();
	
	@Override
	public String toDataString(String client_id) {
		String output = "010,";
		output += super.toDataString(client_id);
		return output;
	}
}