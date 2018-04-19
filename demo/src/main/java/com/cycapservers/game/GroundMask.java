package com.cycapservers.game;

public class GroundMask extends Entity {
	
	protected int total_time;
	protected int full_alpha_time;
	protected int fade_time;
	protected long start_time;

	public GroundMask(int id, int sprIdx, double x, double y, double w, double h, double r, String entity_id, int time_length, int fade_time) {
		super(id, sprIdx, x, y, w, h, r, 1.0, entity_id);
		this.total_time = time_length;
		this.full_alpha_time = time_length - fade_time;
		this.fade_time = fade_time;
		this.start_time = System.currentTimeMillis();
	}

	/**
	 * 
	 * @return true if the ground mask is finished fading away
	 */
	public boolean update() {
		long runtime = (System.currentTimeMillis() - this.start_time);
		if(runtime >= this.total_time) {
			return true;
		}
		else if(runtime >= this.full_alpha_time) {
			this.alpha = 1.0 - ((runtime - this.full_alpha_time) / (double) this.fade_time);
		}
		else {
			this.alpha = 1.0;
		}
		return false;
	}
	
	public String toDataString(String client_id) {
		String output = "010,";
		output += super.toDataString(client_id);
		return output;
	}
}
