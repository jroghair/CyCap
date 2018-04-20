package com.cycapservers.game;

public class SpeedPotion extends PowerUp {
	
	protected final double BOOST_AMOUNT = 2.0;
	
	public SpeedPotion(double x, double y, double w, double h, double r, double a, String entity_id) {
		super(3, 0, x, y, w, h, r, a, "Speed Potion", 10000, entity_id);
	}
	
	public SpeedPotion(SpeedPotion sp, double x, double y, String entity_id) {
		super(sp, x, y, entity_id);
	}

	@Override
	public boolean update() {
		if(this.started && ((System.currentTimeMillis() - this.startTime) > this.duration)){
			this.grabber.speed_boost /= this.BOOST_AMOUNT;
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
			this.grabber.speed_boost *= this.BOOST_AMOUNT;
			this.startTime = System.currentTimeMillis();
			this.started = true;
			return true;
		}
		return false;
	}
}