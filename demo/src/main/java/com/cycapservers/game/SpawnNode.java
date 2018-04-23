package com.cycapservers.game;

public class SpawnNode extends GridLockedNode {
	
	protected int team;
	
	public SpawnNode(short gx, short gy, int team) {
		super(gx, gy);
		this.team = team;
	}

}
