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
	
	/**
	 * Creates a new bullet entity
	 * @param sprIdx - sprite index on the bullet image
	 * @param startX - start x position of the bullet
	 * @param startY - start y position of the bullet
	 * @param endX - x position of the mouse (used to calculate direction)
	 * @param endY - y position of the mouse (used to calculate direction)
	 * @param w - width of the entity
	 * @param h - width of the entity
	 * @param r - rotation of the entity
	 * @param a - transparency of the entity
	 * @param speed - movement speed of the bullet in pixels per second
	 * @param damage - amount of damage the bullet deals upon impact
	 * @param variation - the deviation from center
	 * @param p - the owner of the bullet
	 * @param entity_id - the entity's unique id
	 */
	public Bullet(int sprIdx, double startX, double startY, double endX, 
			double endY, double w, double h, double r, double a, double speed, 
			int damage, double variation, GameCharacter p, String entity_id) {
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
				if(Utils.isColliding(this, ai)) {
					if(ai.team != this.team) {
						ai.takeDamage(this.damage, owner);
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
						p.takeDamage(this.damage, this.owner);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the object data for this bullet for the specified client
	 * @param client_id - the name of the client who the data is for
	 * @return the data string of this object
	 */
	@Override
	public String toDataString(String client_id) {
		String output = "";
		output += "020,";
		output += super.toDataString(client_id);
		return output;
	}

}