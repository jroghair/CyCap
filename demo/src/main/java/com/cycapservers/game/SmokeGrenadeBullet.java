package com.cycapservers.game;

public class SmokeGrenadeBullet extends Bullet {
	/**
	 * the unique code of the particle effect this artillery shell creates
	 */
	protected int fx_code;
	/**
	 * the initial width of this bullet
	 */
	protected double start_width;
	/**
	 * the starting height of this bullet
	 */
	protected double start_height;
	
	protected double v_initial;
	protected double max_mulitplier;
	protected double max_height; //for physics
	protected double formula_m;
	
	/**
	 * the amount of time after the grenade hits the ground to emit smoke particles
	 */
	protected int smoke_time;
	/**
	 * the amount of new smoke particles to make every update
	 */
	protected int smoke_intensity;
	
	/**
	 * the range, in pixels, in which a player will be damaged if the conditions are correct
	 */
	protected int damage_range;
	
	/**
	 * creates a new smoke grenade bullet
	 * @param sprIdx
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param w
	 * @param h
	 * @param damage
	 * @param p
	 * @param entity_id
	 * @param fx
	 * @param max_height
	 */
	public SmokeGrenadeBullet(int sprIdx, double startX, double startY, double endX, double endY, double w, double h, GameCharacter p, String entity_id, int fx, double max_height, int smoke_time, int intensity) {
		super(sprIdx, startX, startY, endX, endY, w, h, 0, 1.0, 0, 0, 0, p, entity_id);
		this.fx_code = fx;
		this.start_width = w;
		this.start_height = h;
		
		this.lifeSpan = 1000; //in milliseconds
		
		this.xRatio = 1000 * (this.endX - this.startX) / this.lifeSpan; //in pixels per second
		this.yRatio = 1000 * (this.endY - this.startY) / this.lifeSpan; 
		this.v_initial = Utils.GRAVITY * this.lifeSpan / 1000;
		this.max_mulitplier = max_height;
		this.max_height = (-Utils.GRAVITY * Math.pow(this.lifeSpan/2000, 2)) + (this.v_initial * this.lifeSpan/2000);
		this.formula_m = (this.max_mulitplier - 1)/this.max_height;
		
		this.damage_range = (int) (1.5 * Utils.GRID_LENGTH); //TODO: pass this as a parameter
		this.smoke_time = smoke_time;
		this.smoke_intensity = intensity;
	}
	
	public boolean update(GameState game) {
		double total_time = (double) (System.currentTimeMillis() - this.birthTime) / 1000.0;
		if((total_time*1000) > (this.lifeSpan + this.smoke_time)) {
			return true;
		}
		else if((total_time*1000) > this.lifeSpan){
			this.alpha = 0.0; //make sure the bullet is invisible after it hits the ground
			for(int i = 0; i < this.smoke_intensity; i++) {
				double tempX = this.endX + ((Utils.RANDOM.nextDouble() * Utils.GRID_LENGTH * 5) - Utils.GRID_LENGTH * 2.5);
				double tempY = this.endY + ((Utils.RANDOM.nextDouble() * Utils.GRID_LENGTH * 5) - Utils.GRID_LENGTH * 2.5);
				String id = Utils.getGoodRandomString(game.usedEntityIds, game.entity_id_len);
				game.particles.add(new Particle(this.fx_code, 0, tempX, tempY, Utils.GRID_LENGTH, Utils.GRID_LENGTH, Utils.RANDOM.nextDouble() * Math.PI * 2.0, 1.0, id, 1, 3500, false, Utils.RANDOM.nextInt(7), Utils.RANDOM.nextInt(7), 13, 13, Utils.RANDOM.nextDouble() * 0.5 - 0.25, -0.1));
				game.usedEntityIds.add(id);
			}
			//TODO: add sound
			return false;
		}
		this.x += (this.xRatio * game.currentDeltaTime);
		this.y += (this.yRatio * game.currentDeltaTime);

		double temp_multiplier = this.formula_m * ((-Utils.GRAVITY * total_time * total_time) + (this.v_initial * total_time)) + 1;
		this.setDrawWidth(this.start_width * temp_multiplier);
		this.setDrawHeight(this.start_height * temp_multiplier);
		return false;
	}
}