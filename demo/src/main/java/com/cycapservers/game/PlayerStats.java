package com.cycapservers.game;

import java.awt.Point;
import java.util.ArrayList;

import org.springframework.web.socket.WebSocketSession;
import com.cycapservers.account.*;

public class PlayerStats {
	
	protected String userID; 
	protected String champion; 
	protected int experience;
	protected int level;
	
	protected int kills;
	protected int deaths;
	protected int wins;
	protected int losses;
	
	//MODE SPECIFIC STATS//
	//CTF
	protected int flag_grabs;
	protected int flag_returns;
	protected int flag_captures;
	
	
	

	//GameSpecific Stats
	protected String game_type;
	int gamesplayed; 
	int gamewins;
	int gamelosses;


	public PlayerStats(Player player, String champion){
		this.userID=player.getEntity_id(); //need to add this in later
		this.userID=player.role; //need to ensure role was already assigned
		this.kills=0;
		this.deaths=0; 
		this.wins=0; 
		this.losses=0;
		this.flag_returns=0;
		this.flag_grabs=0;
		this.flag_captures=0; 
		this.experience=0; 
		this.game_type=null;
		this.gamesplayed=0;
		this.gamelosses=0;
		this.gamewins=0; 

	}
	
	public void playerGetsKill(){
		kills++; 
	}

	public void playerDies(){
		deaths++; 
	}
	
	public void playerWinsGame(){
		wins++; 
	}
	
	public void playerLossesGame(){
		losses++; 
	}
	
	public void playerflag_grabs(){
		flag_grabs++; 
	}	
	
	public void playerFlagReturns(){
		flag_returns++; 
	}
	
	public void playerFlagCaptures(){
		this.losses++; 
	}	
	
	public void playerExperienceGain(int exp){
		this.experience+=exp; 
	}
	
	public void calculatePlayerLevel(){
		this.level= experience/100;
	}

	//getter methods for saving to DB
	public String getUserID() {
		return userID;
	}

	public String getChampion() {
		return champion;
	}

	public int getExperience() {
		return experience;
	}

	public int getLevel() {
		return level;
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}

	public int getFlag_grabs() {
		return flag_grabs;
	}

	public int getFlag_returns() {
		return flag_returns;
	}

	public int getFlag_captures() {
		return flag_captures;
	}
	
	public void setGameType(String gametype){
		this.game_type=gametype; 
	}
	

	public int getGamesplayed() {
		return gamesplayed;
	}

	public void setGamesplayed(int gamesplayed) {
		this.gamesplayed = gamesplayed;
	}

	public int getGamewins() {
		return gamewins;
	}

	public void setGamewins(int gamewins) {
		this.gamewins = gamewins;
	}

	public int getGamelosses() {
		return gamelosses;
	}

	public void setGamelosses(int gamelosses) {
		this.gamelosses = gamelosses;
	}

	
	public void updateScore(){ //could have this take in winning team to double scores potentially
		if(this.game_type==null){
			throw new IllegalStateException("Cannot update xp when game type has not been set!");
		}
		if(this.game_type.equals("ctf")){
			this.experience+=this.kills*10;
			this.experience+=this.flag_grabs*25;
			this.experience+=this.flag_captures*100;
			this.experience+=this.flag_returns*35;
			//somehow double if team won
			//call util class to calculate level and experience it takes in level and experience
			
			//Point p = Utils.calculateLevelAndXP(new Point(this.level, this.experience)); //so util returns a pair
			
			//parse the point and send to database 
		}
		else if(this.game_type.equals("tdm")){
				}
		else if(this.game_type.equals("ffa")){
		}
	}


}







