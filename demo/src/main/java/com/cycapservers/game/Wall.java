package com.cycapservers.game;

public class Wall extends Entity {
	
	protected int gridX;
	protected int gridY;
	protected boolean invincible;
	
	/**
	 * 
	 * @param id
	 * @param sprIdx
	 * @param gridX the x position on the grid map
	 * @param gridY the y position on the grid map
	 * @param w
	 * @param h
	 * @param r
	 * @param a
	 */
	public Wall(int sprIdx, int gridX, int gridY, double w, double h, boolean invc, String entity_id) {
		super(3, sprIdx, (gridX*Utils.GRID_LENGTH + w/2), (gridY*Utils.GRID_LENGTH + h/2), w, h, 0, 1.0, entity_id);
		this.gridX = gridX;
		this.gridY = gridY;
		this.invincible = invc;
	}
	
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