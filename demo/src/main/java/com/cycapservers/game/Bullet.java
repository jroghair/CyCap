package com.cycapservers.game;

public class Bullet extends Entity {
	
	protected double speed;
	protected int damage;
	
	protected GameCharacter owner;
	protected int team;
	
	protected double startX;
	protected double startY;
	protected double endX;
	protected double endY;
	protected double xRatio;
	protected double yRatio;
	
	protected long lifeSpan;
	protected long birthTime;
	
	public Bullet(int sprIdx, double startX, double startY, double endX,
			double endY, double w, double h, double r, double a,
			double speed, int damage, double variation, GameCharacter p,
			String entity_id) {
		super(2, sprIdx, startX, startY, w, h, r, a, entity_id);
		
		this.birthTime = System.currentTimeMillis();
		this.lifeSpan = 5000;
		
		this.speed = speed;
		this.damage = damage;
		this.owner = p;
		this.team = this.owner.team;
		
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		
		//generate normalized x & y ratios
		double c = Utils.distanceBetween(this.startX, this.startY, this.endX, this.endY);
		this.xRatio = (this.endX - this.startX) / c; 
		this.yRatio = (this.endY - this.startY) / c;
		this.xRatio += (Math.random() - 0.5) * variation;
		this.yRatio += (Math.random() - 0.5) * variation;
	}
	
	/**
	 * Returns true if the bullet is to be removed
	 * @param game - the GameState
	 * @return boolean
	 */
	public boolean update(GameState game) {
		if((System.currentTimeMillis() - this.birthTime) > this.lifeSpan){
			return true;
		}
		int subUpdateResolution = 4; //we want to move the bullet smaller amounts so it can't jump walls
		double deltaX = this.speed * this.xRatio * game.currentDeltaTime / subUpdateResolution;
		double deltaY = this.speed * this.yRatio * game.currentDeltaTime / subUpdateResolution;
		
		for(short i = 0; i < subUpdateResolution; i++) {
			this.x += deltaX;
			this.y += deltaY;
	
			for(Wall w : game.walls){
				if(Utils.isColliding(this, w))
				{
					return true;
				}
			}
			for(AI_player ai : game.AI_players) {
				if(ai.equals(this.owner)){
					continue;
					
				}
				if(Utils.isColliding(this, ai)) {
					if(ai.team != this.team) {
						ai.takeDamage(this.damage);
						return true;
					}
					
				}
			}
			for(Player p : game.players) {
				if(p.equals(this.owner)) {
					continue;
				}
				if(Utils.isColliding(this, p)) {
					if(game.friendlyFire || (p.team != this.team)) {
						p.takeDamage(this.damage);
						return true;

					}
					
				}
			}
		}
		return false;
	}
	
	@Override
	public String toDataString(String client_id) {
		String output = "";
		output += "020,";
		output += super.toDataString(client_id);
		return output;
	}

}