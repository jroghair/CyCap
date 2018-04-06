package com.cycapservers.game;

public class CTF_PlayerStats extends PlayerStats {
	//CTF
	protected int flag_grabs;
	protected int flag_returns;
	protected int flag_captures;
	
	public CTF_PlayerStats() {
		super();
		flag_grabs = 0;
		flag_returns = 0;
		flag_captures = 0;
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
		super.setData(kills, deaths, wins, losses);
		this.flag_grabs = flag_grabs;
		this.flag_returns = flag_returns;
		this.flag_captures = flag_captures;
	}
	
	public String toDataString() {
		String output = "";
		output += super.toDataString() + ",";
		output += flag_grabs + ",";
		output += flag_returns + ",";
		output += flag_captures;
		return output;
	}
	
	public int getScore() {
		//TODO return xp
		return -1;
	}

}
