package com.cycapservers.game;

public class ArtilleryShell extends Bullet{
	
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
	 * the range, in pixels, in which a player will be damaged if the conditions are correct
	 */
	protected int damage_range;
	
	/**
	 * creates a new artillery shell
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
	public ArtilleryShell(int sprIdx, double startX, double startY, double endX, double endY, double w, double h, int damage, GameCharacter p, String entity_id, int fx, double max_height) {
		super(sprIdx, startX, startY, endX, endY, w, h, 0, 1.0, 0, damage, 0, p, entity_id);
		this.fx_code = fx;
		this.start_width = w;
		this.start_height = h;
		
		this.lifeSpan = 3000; //in milliseconds
		
		this.xRatio = 1000 * (this.endX - this.startX) / this.lifeSpan; //in pixels per second
		this.yRatio = 1000 * (this.endY - this.startY) / this.lifeSpan; 
		this.v_initial = Utils.GRAVITY * this.lifeSpan / 1000;
		this.max_mulitplier = max_height;
		this.max_height = (-Utils.GRAVITY * Math.pow(this.lifeSpan/2000, 2)) + (this.v_initial * this.lifeSpan/2000);
		this.formula_m = (this.max_mulitplier - 1)/this.max_height;
		
		this.damage_range = (int) (1.5 * Utils.GRID_LENGTH); //TODO: pass this as a parameter
	}
	
	public boolean update(GameState game) {
		double total_time = (double) (System.currentTimeMillis() - this.birthTime) / 1000.0;
		if((total_time*1000) > this.lifeSpan){
			String id = Utils.getGoodRandomString(game.usedEntityIds, game.entity_id_len);
			game.particles.add(new Particle(7, 0, this.endX, this.endY, 2*Utils.GRID_LENGTH, 2*Utils.GRID_LENGTH, 0, 1.0, id, 74, 2500, false, 0, 0, 0, 0, 0, 0));
			game.usedEntityIds.add(id);
			id = Utils.getGoodRandomString(game.usedEntityIds, game.entity_id_len);
			game.ground_masks.add(new GroundMask(8, 0, this.endX, this.endY, 3*Utils.GRID_LENGTH, 3*Utils.GRID_LENGTH, 0, id, 15000, 5000));
			game.usedEntityIds.add(id);
			//TODO: add sound
			for(Player p : game.players) {
				if(Utils.distanceBetween(this, p) <= this.damage_range) {
					if(game.friendlyFire || (p.team != this.team) || p.equals(this.owner)) {
						p.takeDamage(this.damage, this.owner);
					}
				}
			}
			for(AI_player p : game.AI_players) {
				if(Utils.distanceBetween(this, p) <= this.damage_range) {
					if(game.friendlyFire || (p.team != this.team) || p.equals(this.owner)) {
						p.takeDamage(this.damage, this.owner);
					}
				}
			}
			return true;
		}
		this.x += (this.xRatio * game.currentDeltaTime);
		this.y += (this.yRatio * game.currentDeltaTime);

		double temp_multiplier = this.formula_m * ((-Utils.GRAVITY * total_time * total_time) + (this.v_initial * total_time)) + 1;
		this.setDrawWidth(this.start_width * temp_multiplier);
		//if(Utils.DEBUG) System.out.println(this.drawWidth);
		this.setDrawHeight(this.start_height * temp_multiplier);
		return false;
	}

}