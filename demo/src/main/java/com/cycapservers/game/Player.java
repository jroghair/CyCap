package com.cycapservers.game;

import org.springframework.web.socket.WebSocketSession;

public class Player extends Entity {
	
	protected int team;
	protected String client_id;
	protected String password;
	protected WebSocketSession session;
	protected int highestHandledSnapshot;
	protected String lastUnsentGameState;
	
	protected String role;
	protected Weapon weapon1;
	protected Weapon weapon2;
	protected Weapon weapon3;
	protected Weapon weapon4;
	protected Weapon currentWeapon;
	protected Item item_slot;
	
	protected int health;
	protected int max_health;
	protected boolean isDead;
	protected long lastDeathTime;
	
	protected double speed; //TODO determine if this needs to be sent
	protected int visibility; //TODO: handle visibility!
	
	protected boolean is_invincible;
	protected double speed_boost;
	protected double damage_boost;
	
	public Player(double x, double y, double width, double height, double rotation, double alpha, int team, String role, String client_id, String password, WebSocketSession session){
		super(0, 0, x, y, width, height, rotation, alpha);
		if(team == 1) {
			this.spriteIndex = 4;
		}
		else {
			this.spriteIndex = 0;
		}
		this.team = team;
		this.role = role;
		
		this.client_id = client_id;
		this.password = password;
		this.session = session;
		this.highestHandledSnapshot = 0;
		this.lastUnsentGameState = null;
		
		this.isDead = false;
		this.is_invincible = false;
		this.speed_boost = 1.0;
		this.damage_boost = 1.0;
		
		this.item_slot = null;
		Utils.setRole(this); //TODO check if this causes a concurrent edit error
	}
	
	public void takeDamage(int amount) {
		if(!this.is_invincible){
			this.health -= amount;
		}
		if(this.health <= 0){
			this.die(); //idk what this is gonna do yet
		}
	}
	
	public void die() {
		this.x = -256;
		this.y = -256;
		this.isDead = true;
		this.lastDeathTime = System.currentTimeMillis();
	}
	
	public void update(GameState game, InputSnapshot s) {
		if(s.snapshotNum > this.highestHandledSnapshot) {
			this.highestHandledSnapshot = s.snapshotNum;
		}
		
		if(this.isDead){
			if((System.currentTimeMillis() - this.lastDeathTime) > game.respawnTime) {
				//respawn player
				this.x = 64;
				this.y = 64;
				//set isDead to false
				this.isDead = false;
				//reset ammo and health
				Utils.setRole(this);
			}
		}
		else {
			this.movePlayer(game, s); //move the player first
			this.currentWeapon.update(this, s, game); //checks to see if the current weapon is to be fired
			
			if(this.item_slot == null) {
				for(Item i : game.current_item_list) {
					if(Utils.isColliding(this, i)) {
						i.pickUp(this);
					}
				}
			}
			
			//WEAPON AND ITEM RELATED KEYPRESSES
			if(s.keys_pnr.contains(49)){
				this.switchWeapon(1);
			}
			else if(s.keys_pnr.contains(50)){
				this.switchWeapon(2);
			}
			else if(s.keys_pnr.contains(51)){
				this.switchWeapon(3);
			}
			else if(s.keys_pnr.contains(52)){
				this.switchWeapon(4);
			}
			if(s.keys_pnr.contains(82)){
				this.currentWeapon.reload();
			}
			if(s.keys_pnr.contains(70)){
				this.useItem();
			}
		}
	}
	
	private void movePlayer(GameState game, InputSnapshot s) {
		int movement_code  = 0b0000; //the binary code for which directions the player moving

		//this section will probably end up on the server
		if (s.keys_down.contains(87)) {
			movement_code |= Utils.UP; //trying to move up
		}
		if (s.keys_down.contains(65)) {
			movement_code |= Utils.LEFT; //trying to move left
		}
		if (s.keys_down.contains(68)) {
			movement_code |= Utils.RIGHT; //trying to move right
		}
		if (s.keys_down.contains(83)) {
			movement_code |= Utils.DOWN; //trying to move down
		}

		if((movement_code & (Utils.UP | Utils.DOWN)) == 0b1100){ //if both up and down are pressed
			movement_code &= ~(Utils.UP | Utils.DOWN); //clear the up and down bits
		}
		if((movement_code & (Utils.LEFT | Utils.RIGHT)) == 0b0011){ //if both left and right are pressed
			movement_code &= ~(Utils.LEFT | Utils.RIGHT); //clear the left and right bits
		}

		double delta_x = 0;
		double delta_y = 0;
		if(movement_code == 0b1010){
			delta_y = -1 * this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
			delta_x = -1 * this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
		}
		else if(movement_code == 0b1001){
			delta_y = -1 * this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
			delta_x = this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
		}
		else if(movement_code == 0b0110){
			delta_y = this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
			delta_x = -1 * this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
		}
		else if(movement_code == 0b0101){
			delta_y = this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
			delta_x = this.speed * this.speed_boost * Utils.SIN_45 * s.frameTime;
		}
		else if(movement_code == 0b1000){
			delta_y = -1 * this.speed * this.speed_boost * s.frameTime;
		}
		else if(movement_code == 0b0100){
			delta_y = this.speed * this.speed_boost * s.frameTime;
		}
		else if(movement_code == 0b0010){
			delta_x = -1 * this.speed * this.speed_boost * s.frameTime;
		}
		else if(movement_code == 0b0001){
			delta_x = this.speed * this.speed_boost * s.frameTime;
		}

		if(delta_x != 0){
			this.x += delta_x;
			for(Wall w : game.walls){
				if(Utils.isColliding(this, w))
				{
					//if the player hit a wall, reset the player positions and global transforms
					this.x -= delta_x;
					break; //does not check the other walls if at least one was hit
				}
			}
		}
		if(delta_y != 0){
			this.y += delta_y;
			for(Wall w : game.walls){
				if(Utils.isColliding(this, w))
				{
					//if the player hit a wall, reset the player positions
					this.y -= delta_y;
					break; //does not check the other walls if at least one was hit
				}
			}
		}
	}
	
	private void useItem() {
		if(this.item_slot == null){
			return;
		}
		else{
			if(this.item_slot.use()) {
				this.item_slot = null;
			}
		}
	}
	
	private void switchWeapon(int weaponNum) {
		switch(weaponNum){
			case 1:
				if(this.weapon1 != null){
					this.currentWeapon = this.weapon1;
				}
				break;
				
			case 2:
				if(this.weapon2 != null){
					this.currentWeapon = this.weapon2;
				}
				break;
				
			case 3:
				if(this.weapon3 != null){
					this.currentWeapon = this.weapon3;
				}
				break;
				
			case 4:
				if(this.weapon4 != null){
					this.currentWeapon = this.weapon4;
				}
				break;
				
			default:
				throw new IllegalArgumentException("Illegal argument in player.switchWeapon()");
		}
	}
	
	@Override
	public String toString(){
		String output = "";
		output += "000,";
		output += this.client_id + ",";
		output += this.highestHandledSnapshot + ",";
		output += this.imageId + ",";
		output += this.spriteIndex + ",";
		output += this.x + ",";
		output += this.y + ",";
		output += this.drawWidth + ",";
		output += this.drawHeight + ",";
		output += this.rotation + ",";
		output += this.alpha + ",";
		output += this.role + ",";
		output += this.team + ",";
		output += this.currentWeapon.toString() + ",";
		if(this.item_slot == null) {
			output += "empty" + ",";
		}
		else {
			output += this.item_slot.toString() + ",";
		}
		output += this.health + ",";
		output += this.is_invincible + ",";
		output += this.speed_boost + ",";
		output += this.damage_boost + ",";
		output += this.visibility;
		return output;
	}
	
	public String getPassword() {
		return password;
	}

	public String getLastUnsentGameState() {
		return lastUnsentGameState;
	}

	public void setLastUnsentGameState(String lastUnsentGameState) {
		this.lastUnsentGameState = lastUnsentGameState;
	}
}