package com.cycapservers.game;

public class PowerUpNode extends GridLockedNode {
	
	private boolean inUse;
	private PowerUp powerup;
	
	public PowerUpNode(short gx, short gy) {
		super(gx, gy);
		this.inUse = false;
	}
	
	public void update() {
		if(this.powerup != null) {
			if(this.powerup.update()) {
				clearPowerUp();
			}
		}
	}
	
	public void spawnPowerUp() {
		this.inUse = true;
		this.powerup = new SpeedPotion(Utils.SPEED_POTION, this.getX(), this.getY());
	}
	
	private void clearPowerUp() {
		this.powerup = null;
		this.inUse = false;
	}
	
	public boolean isInUse() {
		return inUse;
	}
	
	public PowerUp getPowerUp() {
		return powerup;
	}
}