package com.cycapservers.game;

import java.awt.Point;

public class PlayerStats {
	
	protected String username;
	protected int level;
	protected int xp;
	protected String role;
	
	protected int kills;
	protected int deaths;
	protected int wins;
	protected int losses;
	
	protected int flag_grabs;
	protected int flag_returns;
	protected int flag_captures;
	
	protected String game_type;
	
	public PlayerStats(String player, String role) {
		this.username = player;
		this.role = role;
		this.game_type = null;
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
	
	public void addFlagGrab() {
		flag_grabs++;
	}
	
	public void addFlagReturn() {
		flag_returns++;
	}
	
	public void addFlagCap() {
		flag_captures++;
	}
	
	public void setData(int kills, int deaths, int wins, int losses, int flag_grabs, int flag_returns, int flag_captures) {
		this.kills = kills;
		this.deaths = deaths;
		this.wins = wins;
		this.losses = losses;
		this.flag_grabs = flag_grabs;
		this.flag_returns = flag_returns;
		this.flag_captures = flag_captures;
	}
	
	public String toDataString() {
		String output = "";
		output += kills + ",";
		output += deaths + ",";
		output += wins + ",";
		output += losses + ",";
		output += flag_grabs + ",";
		output += flag_returns + ",";
		output += flag_captures;
		return output;
	}
	
	public void updateScore() {
		if(this.game_type == null) {
			throw new IllegalStateException("Cannot update xp when game type has not been set!");
		}
		
		if(this.game_type.equals("ctf")) {
			this.xp += (this.kills * 10);
			this.xp += (this.flag_grabs * 25);
			this.xp += (this.flag_captures * 100);
			this.xp += (this.flag_returns * 35);
			//somehow double if the team won
			//Point p = Utils.calculateLevelAndXP(new Point(this.level, this.xp))
			//parse it
		}
		else if(this.game_type.equals("tdm")) {
			//something
		}
		else if(this.game_type.equals("ffa")) {
			//something
		}
	}
	
	
}
