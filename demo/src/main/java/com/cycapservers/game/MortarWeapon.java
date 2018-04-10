package com.cycapservers.game;

public class MortarWeapon extends ThrownWeapon {
	
	public MortarWeapon(String name, int damage, int bt, int rate, int mag_size, int extra_mags, int reload_time, double max_height, int lifeTime) {
		super(name, damage, bt, rate, mag_size, extra_mags, reload_time, max_height, lifeTime, 5*Utils.GRID_LENGTH);
	}
	
	public MortarWeapon(MortarWeapon m) {
		super(m);
	}

	@Override
	public void fire(Player p, InputSnapshot s, GameState g) {
		this.ammo_in_clip--; //lose one bullet from the clip
		String id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		int x = (int) (Math.floor(s.mapX / Utils.GRID_LENGTH) * Utils.GRID_LENGTH);
		int y = (int) (Math.floor(s.mapY / Utils.GRID_LENGTH) * Utils.GRID_LENGTH);
		g.bullets.add(new ArtilleryShell(this.bullet_type, p.x, p.y, x, y, Utils.GRID_LENGTH * 0.125, Utils.GRID_LENGTH * 0.125, this.damage, p, id, 0, 3));
		g.usedEntityIds.add(id);
		//TODO: make bullet sound
	}
}