package com.cycapservers.game;

public class HealthPack extends PowerUp {
	
	public HealthPack(int x, int y, int w, int h, int r, double a, String entity_id) {
		super(4, 0, x, y, w, h, r, a, "Health Pack", 0, entity_id);
	}
	
	public HealthPack(HealthPack hp, int x, int y, String entity_id) {
		super(hp, x, y, entity_id);
	}

	@Override
	public boolean update() {
		if(this.started && ((System.currentTimeMillis() - this.startTime) > this.duration)){
			return true;
		}
		return false;
	}

	@Override
	public boolean use() {
		if(this.grabber == null) {
			throw new IllegalStateException("Error, this powerup has no grabber and cannot be used");
		}
		if(!this.started){
			this.grabber.health = this.grabber.max_health;
			this.startTime = System.currentTimeMillis();
			this.started = true;
			return true;
		}
		return false;
	}

}
