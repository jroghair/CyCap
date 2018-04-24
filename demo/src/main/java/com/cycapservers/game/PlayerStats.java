package com.cycapservers.game;

import java.awt.Point;
import java.util.ArrayList;

import org.springframework.web.socket.WebSocketSession;
import com.cycapservers.account.*;

public class PlayerStats {
	
	//////PLAYER DATA//////
	protected String userID; 
	protected String champion; 
	protected int experience;
	protected int level;
	protected int team;
	
	//////BASIC GAME STATS//////
	protected int kills;
	protected int deaths;
	protected int wins;
	protected int losses;
	
	//////MODE SPECIFIC STATS//////
	
	//CTF
	protected int flag_grabs;
	protected int flag_returns;
	protected int flag_captures;
	
	protected String endgame_message;
	

	//GameSpecific Stats
	protected Class<? extends GameState> game_type;
	int gamesplayed; 
	int gamewins;
	int gamelosses;


	public PlayerStats(GameCharacter player){
		this.userID = player.entity_id; //need to add this in later
		this.champion = player.role; //need to ensure role was already assigned
		this.team = player.team;
		this.kills = 0;
		this.deaths = 0; 
		this.wins = 0; 
		this.losses = 0;
		this.flag_returns = 0;
		this.flag_grabs=0;
		this.flag_captures=0; 
		this.experience=0; 
		this.game_type=null;
		this.gamesplayed=0;
		this.gamelosses=0;
		this.gamewins=0;
	}
	
	public void addKill(){
		kills++; 
	}

	public void addDeath(){
		deaths++; 
	}
	
	public void addFlagGrab(){
		flag_grabs++; 
	}	
	
	public void addFlagReturn(){
		flag_returns++; 
	}
	
	public void addFlagCapture(){
		this.flag_captures++; 
	}	
	
	public void playerExperienceGain(int exp){
		this.experience+=exp; 
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
	
	public void setGameType(Class<? extends GameState> gametype){
		this.game_type = gametype; 
	}
	

	public int getGamesplayed() {
		return gamesplayed;
	}

	public void setGamesplayed(int gamesplayed) {
		this.gamesplayed = gamesplayed;
	}

	public void setLevelAndXP() {
		Point p = ProfileDataUpdate.dbGetLevel(userID, champion);
		this.level = p.x;
		this.experience = p.y;
		if(Utils.DEBUG) System.out.println("Start - ID: " + userID + "     Role: " + champion + "     Level: " + level + "     XP: " + experience);
	}
	
	public void updateScore(int winner){ //could have this take in winning team to double scores potentially
		if(this.game_type==null){
			throw new IllegalStateException("Cannot update xp when game type has not been set!");
		}
		this.gamesplayed++;
		if(this.game_type.equals(CaptureTheFlag.class)){
			int new_xp = this.kills*10;
			new_xp += this.flag_grabs*25;
			new_xp += this.flag_captures*100;
			new_xp += this.flag_returns*35;
			if(team == winner) {
				new_xp *= 2;
				wins++;
			}
			else {
				losses++;
			}
			this.experience += new_xp;
			
			//call util class to calculate level and experience it takes in level and experience
			Point p = Utils.calculateLevelAndXP(new Point(this.level, this.experience)); //so util returns a pair
			this.level = p.x;
			this.experience = p.y;
			
			if(wins > losses) {
				endgame_message = "w:";
			}
			else {
				endgame_message = "l:";
			}
			endgame_message += new_xp + ":";
			endgame_message += champion + ":";
			endgame_message += kills + ":";
			endgame_message += deaths + ":";
			endgame_message += level;
		}
		else if(this.game_type.equals(TeamDeathMatch.class)){
			int new_xp = this.kills*30;
			if(team == winner) {
				new_xp *= 2;
				wins++;
			}
			else {
				losses++;
			}
			this.experience += new_xp;
			
			//call util class to calculate level and experience it takes in level and experience
			Point p = Utils.calculateLevelAndXP(new Point(this.level, this.experience)); //so util returns a pair
			this.level = p.x;
			this.experience = p.y;
			
			if(wins > losses) {
				endgame_message = "w:";
			}
			else {
				endgame_message = "l:";
			}
			endgame_message += new_xp + ":";
			endgame_message += champion + ":";
			endgame_message += kills + ":";
			endgame_message += deaths + ":";
			endgame_message += level;
		}
		else if(this.game_type.equals(FreeForAll.class)){
			
		}
	}


}