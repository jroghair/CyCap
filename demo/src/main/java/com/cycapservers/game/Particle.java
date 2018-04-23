package com.cycapservers.game;

public class Particle extends AnimatedEntity {
	
	protected double dx;
	protected double dy;
	protected double dw;
	protected double dh;
	protected double dr;
	protected double da;
	
	/**
	 * 
	 * @param id image code
	 * @param sprIdx starting sprite index
	 * @param x starting x pos
	 * @param y starting y pos
	 * @param w starting width
	 * @param h starting height
	 * @param r starting rotation
	 * @param a starting alpha
	 * @param entity_id
	 * @param frames number of frames
	 * @param time amount of time to go through all frames
	 * @param looping whether or not it loops
	 * @param dx how much to increase x by each second (in pixels)
	 * @param dy how much to increase y by each second (in pixels)
	 * @param dw how much to increase w by each second (in pixels)
	 * @param dh how much to increase h by each second (in pixels)
	 * @param dr how much to increase r by each second (in pixels)
	 * @param da how much to increase a by each second (in pixels)
	 */
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
	public boolean update(GameState g) {
		this.x += (dx * g.currentDeltaTime);
		this.y += (dy * g.currentDeltaTime);
		setDrawWidth(getDrawWidth() + (dw * g.currentDeltaTime));
		setDrawHeight(getDrawHeight() + (dh * g.currentDeltaTime));
		this.rotation += (dr * g.currentDeltaTime);
		this.alpha += (da * g.currentDeltaTime);
		return this.updateFrame();
	}

	public String toDataString(String client) {
		String output = "003,";
		output += super.toDataString(client);
		return output;
	}
}