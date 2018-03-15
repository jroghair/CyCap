package com.cycapservers.game;

public class Shotgun extends Weapon {
	
	public Shotgun(String name, int damage, int rate, int bullet_speed, int mag_size, int extra_mags, int reload_time, double shot_variation) {
		super(name, damage, 3, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation);
	}

	@Override
	public void update(Player p, InputSnapshot s, GameState g) {
		if(!this.is_reloading){
			this.checkFire(p, s, g);
		}
		else{
			//TODO: play reloading sound or something
		}
	}

	@Override
	public void checkFire(Player p, InputSnapshot s, GameState g) {
		if(s.mouse_clicked && ((System.currentTimeMillis() - this.last_shot) >= this.fire_rate)){
			if(this.ammo_in_clip - 1 != -1){
				this.fire(p, s, g);
				this.last_shot = System.currentTimeMillis();
			}
			else{
				//TODO: play click sound
			}
		}
	}

	@Override
	public void fire(Player p, InputSnapshot s, GameState g) {
		this.ammo_in_clip--; //lose one bullet from the clip
		//TODO: make bullet sound
		//get a random number of buckshot pellets between like 5 and 10 or something
		int num_of_pellets = 5;
		for(int i = 0; i < num_of_pellets; i++){
			g.bullets.add(new Bullet(this.bullet_type, p.x, p.y, s.mapX, s.mapY, Utils.GRID_LENGTH * 0.125, Utils.GRID_LENGTH * 0.125, 0, 1.0, this.bullet_speed, this.damage, p));
		}
	}
}