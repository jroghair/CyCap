package com.cycapservers.game;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PowerUpHandler {
	
	protected List<PowerUp> power_ups;
	//protected List<PowerUpNode> nodes;
	/**
	 * the average time in between power up spawns, if feasible
	 */
	protected short rate;
	
	/**
	 * the range +/- from rate until the next power up spawn, used when a power up is spawned to determine when the next spawn is
	 */
	protected short randomness;
	
	/**
	 * The time of the next power up spawn
	 */
	protected long nextSpawnTime;
	
	
	/**
	 * Creates a new PowerUpHandler which spawns new powerups at one of a collection of nodes every so often
	 * @param s time in between each spawn
	 * @param r randomness added to the spawn time 
	 */
	public PowerUpHandler(short s, short r) {
		power_ups = new ArrayList<PowerUp>();
		if(r > s) {
			throw new IllegalArgumentException("Error: randomness of PowerUpHandler is greater than it's rate");
		}
		this.rate = s;
		this.randomness = r;
		setNextSpawnTime();
	}
	
	public void update() {
		ListIterator<PowerUp> iter = this.power_ups.listIterator();
		while(iter.hasNext()){
		    if(iter.next().update()) {
		    	iter.remove(); //remove the bullet from the list if it is done (animation done/hit a wall/etc)
		    }
		}
		
		if(System.currentTimeMillis() >= this.nextSpawnTime) {
			//spawn new power up
			//inform game state that new powerup has been spawned
			setNextSpawnTime();
		}
	}
	
	private void setNextSpawnTime() {
		this.nextSpawnTime = System.currentTimeMillis() + (this.rate + (Utils.RANDOM.nextInt(this.randomness * 2) - this.randomness));
	}
}
