package com.cycapservers.game;

public class AnimatedEntity extends Entity {
	
	protected int num_of_frames;
	protected int time_length;
	protected boolean looping;
	protected long startTime;
	/**
	 * the amount of time in ms for each frame in the animation
	 */
	protected double frame_time;
	
	public AnimatedEntity(int id, int sprIdx, double x, double y, double w, double h, double r, double a, String entity_id, int frames, int time, boolean looping) {
		super(id, sprIdx, x, y, w, h, r, a, entity_id);
		this.num_of_frames = frames;
		this.time_length = time;
		this.looping = looping;
		this.startTime = System.currentTimeMillis();
		this.frame_time = (double) this.time_length / (double) this.num_of_frames;
	}
	
	/**
	 * Updates the current frame(sprite index)
	 * @return if the entity is finished with it's animation, this will return true, signaling it is to be deleted
	 */
	protected boolean updateFrame() {
		long run_time = (System.currentTimeMillis() - this.startTime);
		if(run_time > this.time_length) {
			if(!this.looping) return true;
		}
		
		this.spriteIndex = (int) ((run_time % this.time_length) / (this.frame_time));
		
		if(this.spriteIndex == this.num_of_frames) {
			this.spriteIndex = this.num_of_frames - 1; //we don't want to go too many frames
		}
		return false;
	}

}
