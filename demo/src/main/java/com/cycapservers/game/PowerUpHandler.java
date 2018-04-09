package com.cycapservers.game;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PowerUpHandler {
	
	protected List<PowerUpNode> nodes;
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
		nodes = new ArrayList<PowerUpNode>();
		if(r > s) {
			throw new IllegalArgumentException("Error: randomness of PowerUpHandler is greater than it's rate");
		}
		this.rate = s;
		this.randomness = r;
		setNextSpawnTime();
	}
	
	public void update(GameState g) {
		for(PowerUpNode n : nodes) {
			n.update();
		}
		
		if(System.currentTimeMillis() >= this.nextSpawnTime) {
			List<PowerUpNode> freeNodes = new ArrayList<PowerUpNode>();
			for(PowerUpNode n : nodes) {
				if(!n.isInUse()) {
					freeNodes.add(n);
				}
			}
			if(!freeNodes.isEmpty()) {
				freeNodes.get(Utils.RANDOM.nextInt(freeNodes.size())).spawnPowerUp(g);
				//inform game state that new powerup has been spawned
			}
			setNextSpawnTime();
		}
	}
	
	private void setNextSpawnTime() {
		this.nextSpawnTime = System.currentTimeMillis() + (this.rate + (Utils.RANDOM.nextInt(this.randomness * 2) - this.randomness));
	}
	
	/**
	 * Returns a list of the ungrabbed powerups for sending to players and calculating collisions
	 * @return
	 */
	public List<PowerUp> getPowerUpsList(){
		List<PowerUp> list = new ArrayList<PowerUp>();
		for(PowerUpNode n : nodes) {
			if(n.isInUse() && !n.getPowerUp().grabbed) {
				list.add(n.getPowerUp());
			}
		}
		return list;
	}
	
	public void setNodeList(List<PowerUpNode> nodes) {
		this.nodes = nodes;
	}
}
