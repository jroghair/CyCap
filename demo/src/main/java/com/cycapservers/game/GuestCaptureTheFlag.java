package com.cycapservers.game;

public class GuestCaptureTheFlag extends CaptureTheFlag {
	
	public GuestCaptureTheFlag(String id) {
		super(id, 0);
		this.max_players = 16; //TODO: disallow player if they join and the game is full
		time_limit = 2 * 60 * 1000; //2 minutes to ms
	}
	
	public void setUpGame() {
		//for(int i = 0; i < (max_players - players.size()); i++){
		//	addAI_player();
		//}
		this.start_time = System.currentTimeMillis();
		this.started = true;
	}
	
	public void endGame(int winner) {
		started = false;
		//TODO: make this so it repeats the game over and over
	}
}
