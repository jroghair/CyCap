package com.cycapservers.game;

import java.awt.Point;

public abstract class ThrownWeapon extends Weapon {
	
	/**
	 * the distance, in pixels, that this weapon can fire an ammo round
	 */
	protected int range;
	
	protected double max_height;
	protected double bullet_life_time;
	
	public ThrownWeapon(String name, int damage, int bt, int rate, int mag_size, int extra_mags, int reload_time, double max_height, int lifeTime, int range) {
		super(name, damage, bt, rate, 0, mag_size, extra_mags, reload_time, 0);
		this.max_height = max_height;
		this.bullet_life_time = lifeTime;
		this.range = range;
	}
	
	public ThrownWeapon(ThrownWeapon tw) {
		super(tw);
		this.max_height = tw.max_height;
		this.bullet_life_time = tw.bullet_life_time;
		this.range = tw.range;
	}

	@Override
	public void update(GameCharacter p, InputSnapshot s, GameState g) {
		if(!this.is_reloading){
			this.checkFire(p, s, g);
		}
		else{
			//TODO: play reloading sound or something
		}
	}

	@Override
	public void checkFire(GameCharacter p, InputSnapshot s, GameState g) {
		if(s.mouse_clicked && ((System.currentTimeMillis() - this.last_shot) >= this.fire_rate)){
			if(this.ammo_in_clip - 1 != -1){
				Point point = Utils.mapCoordinatesToGridCoordinates(s.mapX, s.mapY);
				int x = (int) (point.x * Utils.GRID_LENGTH) + Utils.GRID_LENGTH/2;
				int y = (int) (point.y * Utils.GRID_LENGTH) + Utils.GRID_LENGTH/2;
				double fire_dist = Utils.distanceBetween(p.x, p.y, x, y);
				if(fire_dist < this.range) {
					this.fire(p, s, g);
					this.last_shot = System.currentTimeMillis();
				}
			}
			else{
				//TODO: play click sound
			}
		}
	}

	@Override
	public abstract void fire(GameCharacter p, InputSnapshot s, GameState g);

}
