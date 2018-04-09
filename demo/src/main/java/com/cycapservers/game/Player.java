package com.cycapservers.game;

import org.springframework.web.socket.WebSocketSession;

public class Player extends GameCharacter {
	
	protected String password;
	protected WebSocketSession session;
	protected int highestHandledSnapshot;
	protected String lastUnsentGameState;
	
	public Player(double x, double y, double width, double height, double rotation, double alpha, int team, String role, String client_id, String password, WebSocketSession session){
		super(0, 0, x, y, width, height, rotation, alpha, client_id, team, role);
	
		this.password = password;
		this.session = session;
		this.highestHandledSnapshot = 0;
		this.lastUnsentGameState = null;
	}
	
	public void die() {
		this.isDead = true;
		if(this.item_slot !=  null) {
			this.item_slot.drop();
			this.item_slot = null;
		}
		this.x = -256;
		this.y = -256;
		this.lastDeathTime = System.currentTimeMillis();
	}
	
	public void update(GameState game, InputSnapshot s) {
		if(s.snapshotNum > this.highestHandledSnapshot) {
			this.highestHandledSnapshot = s.snapshotNum;
		}
		
		if(this.isDead){
			if((System.currentTimeMillis() - this.lastDeathTime) > game.respawnTime) {
				this.respawn(game);
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
	
	@Override
	public String toDataString(String client_id){
		if(this.entity_id.equals(client_id)) {
			String output = "";
			output += "000,";
			output += this.highestHandledSnapshot + ",";
			output += super.toDataString(client_id) + ",";
			output += this.role + ",";
			output += this.team + ",";
			output += this.currentWeapon.toString() + ",";
			if(this.item_slot == null) {
				output += "empty" + ",";
			}
			else {
				output += this.item_slot.imageId + ",";
			}
			output += this.health + ",";
			output += this.is_invincible + ",";
			output += this.speed_boost + ",";
			output += this.damage_boost + ",";
			output += this.visibility;
			return output;
		}
		else {
			String output = "";
			output += "020,";
			output += super.toDataString(client_id);
			return output;
		}
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

	@Override
	protected void respawn(GameState g) {
		SpawnNode n = Utils.getRandomSpawn(g.spawns, this.team);
		//respawn player
		this.x = n.getX();
		this.y = n.getY();
		//set isDead to false
		this.isDead = false;
		//reset ammo and health
		Utils.setRole(this);
	}
}