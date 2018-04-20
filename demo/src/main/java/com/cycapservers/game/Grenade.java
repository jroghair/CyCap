package com.cycapservers.game;

public class Grenade extends ThrownWeapon {

	public Grenade(String name, int damage, int bt, int rate, int mag_size, int extra_mags, int reload_time,
			double max_height, int lifeTime, int range) {
		super(name, damage, bt, rate, mag_size, extra_mags, reload_time, max_height, lifeTime, range);
		// TODO Auto-generated constructor stub
	}
	
	public Grenade(Grenade g) {
		super(g);
	}

	@Override
	public void fire(Player p, InputSnapshot s, GameState g) {
		// TODO Auto-generated method stub

	}

}
