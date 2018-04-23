package com.cycapservers.game;

public class Shotgun extends Weapon {
	
	public Shotgun(String name, int damage, int rate, int bullet_speed, int mag_size, int extra_mags, int reload_time, double shot_variation) {
		super(name, damage, 3, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation);
	}
	
	public Shotgun(Shotgun shotgun) {
		super(shotgun);
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
				this.fire(p, s, g);
				this.last_shot = System.currentTimeMillis();
			}
			else{
				//TODO: play click sound
			}
		}
	}

	@Override
	public void fire(GameCharacter p, InputSnapshot s, GameState g) {
		this.ammo_in_clip--; //lose one bullet from the clip
		g.new_sounds.add(p.x + "," + p.y + "," + this.shot_sound); //make bullet sound
		int num_of_pellets = 5;
		for(int i = 0; i < num_of_pellets; i++){
			String id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
			g.bullets.add(new Bullet(this.bullet_type, p.x, p.y, s.mapX, s.mapY, Utils.GRID_LENGTH * 0.125, Utils.GRID_LENGTH * 0.125, 0, 1.0, this.bullet_speed, this.damage/num_of_pellets, this.shot_variation, p, id));
			g.usedEntityIds.add(id);
		}
	}
}