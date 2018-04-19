package com.cycapservers.game;

public abstract class Buff {
	
	protected double value;
	protected GameCharacter character;
	/**
	 * What type of buff this is
	 * "multi" for multiplicative
	 * "add" for additive
	 * "set" for set
	 */
	protected String type;
	protected long start_time;
	protected int time_length;
	
	public Buff(GameCharacter gc, double v, String type, int length) {
		this.character = gc;
		this.type = type;
		this.value = v;
		this.time_length = length;
	}
	
	public abstract boolean apply(double field);
	
	public abstract boolean apply(int field);

}
