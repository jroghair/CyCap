package com.cycapservers.game;

public class Wall extends Entity {
	
	protected int gridX;
	protected int gridY;
	
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
	public Wall(int id, int sprIdx, int gridX, int gridY, double w, double h, double r, double a) {
		super(id, sprIdx, (gridX*Utils.GRID_LENGTH + w/2), (gridY*Utils.GRID_LENGTH + h/2), w, h, r, a);
		this.gridX = gridX;
		this.gridY = gridY;
	}
}
