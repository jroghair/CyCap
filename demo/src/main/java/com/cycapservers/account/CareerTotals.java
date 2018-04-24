package com.cycapservers.account;

public class CareerTotals {

	private String userID;

	private int kills;

	private int deaths;

	private int gamewins;

	private int gamelosses;

	private int gamesplayed;

	private int flaggrabs;

	private int flagreturns;

	private int flagcaptures;

	private int level;

	private double kdratio;

	private double averagekills;

	private double averagedeaths;

	private double winrate;

	public CareerTotals() {

	}

	public CareerTotals(String userID, int kills, int deaths, int gamewins, int gamelosses, int gamesplayed,
			int flaggrabs, int flagreturns, int flagcaptures, int level) {
		this.kills = kills;
		this.deaths = deaths;
		this.gamewins = gamewins;
		this.gamelosses = gamelosses;
		this.gamesplayed = gamesplayed;
		this.flaggrabs = flaggrabs;
		this.flagreturns = flagreturns;
		this.flagcaptures = flagcaptures;
		this.level = level;
		if (gamesplayed != 0) {
			this.averagekills = Math.floor((double) kills / gamesplayed * 100) / 100;
			this.averagedeaths = Math.floor((double) deaths / gamesplayed * 100) / 100;
			this.winrate = Math.floor((double) gamewins / gamesplayed * 100) / 100;

		} else {
			this.averagekills = 0;
			this.averagedeaths = 0;
			this.winrate = 0;
		}
		if (this.deaths != 0) {
			this.kdratio = Math.floor((double) kills / deaths * 100) / 100;
		} else
			this.kdratio = 0;
	}

	public String getUserID() {
		return userID;
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getGamewins() {
		return gamewins;
	}

	public int getGamelosses() {
		return gamelosses;
	}

	public int getGamesplayed() {
		return gamesplayed;
	}

	public int getFlaggrabs() {
		return flaggrabs;
	}

	public int getFlagreturns() {
		return flagreturns;
	}

	public int getFlagcaptures() {
		return flagcaptures;
	}

	public int getLevel() {
		return level;
	}

	public double getKdratio() {
		return kdratio;
	}

	public double getAveragekills() {
		return averagekills;
	}

	public double getAveragedeaths() {
		return averagedeaths;
	}

	public double getWinrate() {
		return winrate;
	}

}
