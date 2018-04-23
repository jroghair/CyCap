package com.cycapservers.game;

public class HealthGun extends AutomaticGun {
	
	protected int heal_range;

	public HealthGun(String name, int damage, int rate, int mag_size, int extra_mags, int reload_time, int heal_range) {
		super(name, damage, rate, 0, mag_size, extra_mags, reload_time, 0);
		this.heal_range = heal_range;
	}
	
	public HealthGun(HealthGun hg) {
		super(hg);
	}

	@Override
	public void fire(GameCharacter p, InputSnapshot s, GameState g) {
		this.ammo_in_clip--; //lose one bullet from the clip
		for(Player player : g.players) {
			if(player.team == p.team && (Utils.distanceBetween(p, player) <= heal_range)) {
				player.takeHeals(this.damage);
			}
		}
		for(AI_player player : g.AI_players) {
			if(player.team == p.team && (Utils.distanceBetween(p, player) <= heal_range)) {
				player.takeHeals(this.damage);
			}
		}
		//String id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		//TODO: draw new heal ring ground mask (0 full_alpha_time and 100ms fade time);
		//populate a few new particle effects that are the heal particle
		//g.usedEntityIds.add(id);
		//g.new_sounds.add(p.x + "," + p.y + "," + this.shot_sound); //make heal sound
	}
}
