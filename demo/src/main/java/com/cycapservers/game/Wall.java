package com.cycapservers.game;

public class Wall extends Entity {
	
	protected int gridX;
	protected int gridY;
	protected boolean invincible;
	
	/**
	 * Creates a new Wall with the variable width and height
	 * @param sprIdx - the sprite index on the image
	 * @param gridX the x position on the grid map
	 * @param gridY the y position on the grid map
	 * @param w - the width of the entity
	 * @param h - the height of the intity
	 * @param invc - whether or not the wall is invincible
	 * @param entity_id - the unique id for this entity
	 */
	public Wall(int sprIdx, int gridX, int gridY, double w, double h, boolean invc, String entity_id) {
		super(3, sprIdx, (gridX*Utils.GRID_LENGTH + w/2), (gridY*Utils.GRID_LENGTH + h/2), w, h, 0, 1.0, entity_id);
		this.gridX = gridX;
		this.gridY = gridY;
		this.invincible = invc;
	}
	
	/**
	 * Creates a new Wall with the width and height of one grid space
	 * @param sprIdx
	 * @param gridX
	 * @param gridY
	 * @param invc
	 * @param entity_id
	 */
	public Wall(int sprIdx, int gridX, int gridY, boolean invc, String entity_id) {
		super(3, sprIdx, (gridX*Utils.GRID_LENGTH + Utils.GRID_LENGTH/2), (gridY*Utils.GRID_LENGTH + Utils.GRID_LENGTH/2), Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, entity_id);
		this.gridX = gridX;
		this.gridY = gridY;
		this.invincible = invc;
	}
	
	@Override
	public String toDataString(String client_id) {
		String output = "012,";
		output += this.spriteIndex + ",";
		output += this.gridX + ",";
		output += this.gridY + ",";
		output += this.invincible;
		return output;
	}
}