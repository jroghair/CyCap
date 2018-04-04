package com.cycapservers.game;

public class AmmoPack extends PowerUp {
	
	public AmmoPack(int x, int y, int w, int h, int r, double a, String entity_id) {
		super(5, 0, x, y, w, h, r, a, "Ammo Pack", 0, entity_id);
	}
	
	public AmmoPack(AmmoPack ap, int x, int y, String entity_id) {
		super(ap, x, y, entity_id);
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
			if(this.grabber.weapon1 != null) {
				this.grabber.weapon1.extra_ammo = this.grabber.weapon1.max_ammo_refill;
			}
			if(this.grabber.weapon2 != null) {
				this.grabber.weapon2.extra_ammo = this.grabber.weapon2.max_ammo_refill;
			}
			if(this.grabber.weapon3 != null) {
				this.grabber.weapon3.extra_ammo = this.grabber.weapon3.max_ammo_refill;
			}
			if(this.grabber.weapon4 != null) {
				this.grabber.weapon4.extra_ammo = this.grabber.weapon4.max_ammo_refill;
			}
			this.startTime = System.currentTimeMillis();
			this.started = true;
			return true;
		}
		return false;
	}

}
