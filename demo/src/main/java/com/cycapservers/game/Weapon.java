package com.cycapservers.game;

public abstract class Weapon{
	
	protected String name;
	
	protected int damage;
	protected int bullet_type;
	
	protected int fire_rate;
	protected long last_shot;
	protected int bullet_speed;
	protected double shot_variation;

	protected int reload_time;
	protected boolean is_reloading;
	protected long reload_start_time;

	protected int mag_size;
	protected int ammo_in_clip;
	protected int extra_ammo;

	public Weapon(String name, int damage, int bt, int rate, int bullet_speed, int mag_size, int extra_mags, int reload_time, double shot_variation) {
		this.name = name;
		this.damage = damage;
		this.bullet_type = bt;
		this.fire_rate = rate;
		this.last_shot = System.currentTimeMillis();
		this.bullet_speed = bullet_speed;
		this.shot_variation = shot_variation;
		this.reload_time = reload_time;
		this.is_reloading = false;
		this.reload_start_time = 0;
		this.mag_size = mag_size;
		this.ammo_in_clip = this.mag_size;
		this.extra_ammo = extra_mags * this.mag_size;
	}
	
	/**
	 * A copy constructor for Weapon
	 * @param w the weapon to based the new object on
	 */
	public Weapon(Weapon w) {
		this.name = w.name;
		this.damage = w.damage;
		this.bullet_type = w.bullet_type;
		this.fire_rate = w.fire_rate;
		this.last_shot = w.last_shot;
		this.bullet_speed = w.bullet_speed;
		this.shot_variation = w.shot_variation;
		this.reload_time = w.reload_time;
		this.is_reloading = w.is_reloading;
		this.reload_start_time = w.reload_start_time;
		this.mag_size = w.mag_size;
		this.ammo_in_clip = w.ammo_in_clip;
		this.extra_ammo = w.extra_ammo;
	}
	
	public abstract void update(Player p, InputSnapshot s, GameState g);
	
	public abstract void checkFire(Player p, InputSnapshot s, GameState g);
	
	public abstract void fire(Player p, InputSnapshot s, GameState g);
	
	public void reload() {
		int bullets_to_refill = this.mag_size - this.ammo_in_clip;
		if(this.extra_ammo == 0){
			return;
		}
		else if(this.extra_ammo < bullets_to_refill){
			this.ammo_in_clip += this.extra_ammo;
			this.extra_ammo = 0;
		}
		else{
			this.ammo_in_clip += bullets_to_refill;
			this.extra_ammo -= bullets_to_refill;
		}
	}
	
	public String toString() {
		return this.name;
	}
}