package com.cycapservers.game;

public abstract class PowerUp extends Item {
	
	protected boolean started = false;
	protected long startTime;
	protected long duration;
	
	public PowerUp(int id, int sprIdx, double x, double y, double w, double h, double r, double a, String name, long duration) {
		super(id, sprIdx, x, y, w, h, r, a, name);
		this.duration = duration;
	}
	
	public PowerUp(PowerUp p, double x, double y) {
		super(p.imageId, p.spriteIndex, x, y, p.drawWidth, p.drawHeight, p.rotation, p.alpha, p.name);
		this.duration = p.duration;
	}
	
	/**
	 * Updates important information about the power up.
	 * @return boolean: whether or not the powerup is finished
	 */
	public abstract boolean update();

	@Override
	public abstract boolean use();

	@Override
	public abstract String toString();
}
