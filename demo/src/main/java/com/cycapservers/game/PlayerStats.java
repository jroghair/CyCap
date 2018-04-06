package com.cycapservers.game;

public abstract class PlayerStats {
	
	protected int kills;
	protected int deaths;
	protected int wins;
	protected int losses;
	
	public PlayerStats() {
		kills = 0;
		deaths = 0;
		wins = 0;
		losses = 0;
	}
	
	public void addKill() {
		kills++;
	}
	
	public void addDeath() {
		deaths++;
	}
	
	public void addWin() {
		wins++;
	}
	
	public void addLoss() {
		losses++;
	}
	
	public void setData(int kills, int deaths, int wins, int losses) {
		this.kills = kills;
		this.deaths = deaths;
		this.wins = wins;
		this.losses = losses;
	}
	
	public String toDataString() {
		String output = "";
		output += kills + ",";
		output += deaths + ",";
		output += wins + ",";
		output += losses;
		return output;
	}
	
	public abstract int getScore();
}
