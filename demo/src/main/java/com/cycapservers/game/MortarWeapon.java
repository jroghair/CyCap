package com.cycapservers.game;

import java.awt.Point;

public class MortarWeapon extends ThrownWeapon {
	
	/**
	 * Creates a new MortarWeapon from scratch (be certain you mean to do this!)
	 * @param name - the name of the weapon
	 * @param damage - amount of damage each bullet does
	 * @param bt - which sprite index to use for bullet
	 * @param rate - amount of time in between shots in ms
	 * @param mag_size - the size of a single magazine
	 * @param extra_mags - the number of extra magazines
	 * @param reload_time - the amount of time it takes to reload in ms
	 * @param max_height - the maximum height of the mortar's travel
	 * @param lifeTime - the amount of time before this weapon's bullets disappear
	 * @param range - the distance this weapon can be shot in grid spaces
	 */
	public MortarWeapon(String name, int damage, int bt, int rate, int mag_size, int extra_mags, int reload_time, double max_height, int lifeTime, int range) {
		super(name, damage, bt, rate, mag_size, extra_mags, reload_time, max_height, lifeTime, range*Utils.GRID_LENGTH);
	}
	
	/**
	 * Creates a new Weapon that is a copy of a pre-existing one
	 * @param m the weapon to copy
	 */
	public MortarWeapon(MortarWeapon m) {
		super(m);
	}

	@Override
	public void fire(Player p, InputSnapshot s, GameState g) {
		this.ammo_in_clip--; //lose one bullet from the clip
		String id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		Point point = Utils.mapCoordinatesToGridCoordinates(s.mapX, s.mapY);
		int x = (int) (point.x * Utils.GRID_LENGTH) + Utils.GRID_LENGTH/2;
		int y = (int) (point.y * Utils.GRID_LENGTH) + Utils.GRID_LENGTH/2;
		g.bullets.add(new ArtilleryShell(this.bullet_type, p.x, p.y, x, y, Utils.GRID_LENGTH * 0.25, Utils.GRID_LENGTH * 0.25, this.damage, p, id, 0, this.max_height));
		g.usedEntityIds.add(id);
		//TODO: make bullet sound
	}
}