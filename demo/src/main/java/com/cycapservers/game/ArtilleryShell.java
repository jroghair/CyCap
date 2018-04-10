package com.cycapservers.game;

public class ArtilleryShell extends Bullet{
	
	protected int fx_code;
	protected double start_width;
	protected double start_height;
	
	protected double v_initial;
	protected double max_height;
	
	public ArtilleryShell(int sprIdx, double startX, double startY, double endX, double endY, double w, double h, int damage, Player p, String entity_id, int fx, double max_height) {
		super(sprIdx, startX, startY, endX, endY, w, h, 0, 1.0, 0, damage, 0, p, entity_id);
		this.fx_code = fx;
		this.start_width = w;
		this.start_height = h;
		
		this.lifeSpan = 3000; //in milliseconds
		
		this.xRatio = 1000 * (this.endX - this.startX) / this.lifeSpan; //in pixels per second
		this.yRatio = 1000 * (this.endY - this.startY) / this.lifeSpan; 
		this.v_initial = Utils.GRAVITY * this.lifeSpan / 1000;
		this.max_height = max_height;
	}
	
	public boolean update(GameState game) {
		double total_time = (double) (System.currentTimeMillis() - this.birthTime) / 1000.0;
		if((total_time*1000) > this.lifeSpan){
			//TODO: add new particle effect (explosion) & sound & explosion ground mask
			//TODO: deal damage to players within range
			return true;
		}
		this.x += (this.xRatio * game.currentDeltaTime);
		this.y += (this.yRatio * game.currentDeltaTime);

		double temp_multiplier = 0.0907 * ((-9.8 * total_time * total_time) + (this.v_initial * total_time)) + 1;
		this.drawWidth = this.start_width * temp_multiplier;
		//if(Utils.DEBUG) System.out.println(this.drawWidth);
		this.drawHeight = this.start_height * temp_multiplier;
		
		return false;
	}

}