package com.cycapservers.game;

public class GridLockedNode extends Node {
	
	private short gridX;
	private short gridY;
	
	public GridLockedNode(short gridX, short gridY) {
		super((gridX * Utils.GRID_LENGTH) /*+ (Utils.GRID_LENGTH/2)*/, (gridY * Utils.GRID_LENGTH) /*+ (Utils.GRID_LENGTH/2)*/);
		this.gridX = gridX;
		this.gridY = gridY;
	}
	
	@Override
	public void setX(int x) {
		return;
	}
	
	@Override
	public void setY(int y) {
		return;
	}

	public short getGridX() {
		return gridX;
	}

	public void setGridX(short gridX) {
		this.gridX = gridX;
	}

	public short getGridY() {
		return gridY;
	}

	public void setGridY(short gridY) {
		this.gridY = gridY;
	}
}