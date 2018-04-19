package com.cycapservers.game;

public class Particle extends AnimatedEntity {
	
	protected double dx;
	protected double dy;
	protected double dw;
	protected double dh;
	protected double dr;
	protected double da;
	
	public Particle(int id, int sprIdx, double x, double y, double w, double h, double r, double a, String entity_id, int frames, int time, boolean looping, double dx, double dy, double dw, double dh, double dr, double da) {
		super(id, sprIdx, x, y, w, h, r, a, entity_id, frames, time, looping);
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
		this.dr = dr;
		this.da = da;
	}
	
	/**
	 * updates the particle
	 * @return returns true if the particle is dead and should be removed
	 */
	public boolean update() {
		this.x += dx;
		this.y += dy;
		setDrawWidth(getDrawWidth() + dw);
		setDrawHeight(getDrawHeight() + dh);
		this.rotation += dr;
		this.alpha += da;
		return this.updateFrame();
	}

	public String toDataString(String client) {
		String output = "003,";
		output += super.toDataString(client);
		return output;
	}
}