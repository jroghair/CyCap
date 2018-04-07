package com.cycapservers.game;

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


	public PlayerStats(Player player, String champion){
		this.userID=player.client_id; //may need to change
		this.userID=player.role; //need to ensure role was already assigned
		this.kills=0;
		this.deaths=0; 
		this.wins=0; 
		this.losses=0;
		this.flag_returns=0;
		this.flag_grabs=0;
		this.flag_captures=0; 
		this.experience=0; 
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
	
	public ArrayList callDB(String userID, String champion){
		//call db controller and get list of player attributes for that role.
		
		ProfileDataUpdate db = new ProfileDataUpdate(); 
		
		
		
		return new ArrayList();  //delete later
		
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
}







