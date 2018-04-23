package com.cycapservers.game;

import java.awt.Point;

public class SmokeGrenade extends ThrownWeapon {
	
	/**
	 * the amount of time after the grenade hits the ground to emit smoke particles
	 */
	protected int smoke_time;
	/**
	 * the amount of new smoke particles to make every update
	 */
	protected int smoke_intensity;

	public SmokeGrenade(String name, int damage, int bt, int rate, int mag_size, int extra_mags, int reload_time, double max_height, int lifeTime, int range, int smoke_time, int intensity) {
		super(name, damage, bt, rate, mag_size, extra_mags, reload_time, max_height, lifeTime, range * Utils.GRID_LENGTH);
		this.smoke_time = smoke_time;
		this.smoke_intensity = intensity;
	}
	
	public SmokeGrenade(SmokeGrenade g) {
		super(g);
		this.smoke_time = g.smoke_time;
		this.smoke_intensity = g.smoke_intensity;
	}

	@Override
	public void fire(GameCharacter p, InputSnapshot s, GameState g) {
		this.ammo_in_clip--; //lose one bullet from the clip
		String id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		Point point = Utils.mapCoordinatesToGridCoordinates(s.mapX, s.mapY);
		int x = (int) (point.x * Utils.GRID_LENGTH) + Utils.GRID_LENGTH/2;
		int y = (int) (point.y * Utils.GRID_LENGTH) + Utils.GRID_LENGTH/2;
		g.bullets.add(new SmokeGrenadeBullet(this.bullet_type, p.x, p.y, x, y, Utils.GRID_LENGTH * 0.25, Utils.GRID_LENGTH * 0.25, p, id, 9, this.max_height, smoke_time, smoke_intensity));
		g.usedEntityIds.add(id);
		//TODO: make bullet sound
	}
}