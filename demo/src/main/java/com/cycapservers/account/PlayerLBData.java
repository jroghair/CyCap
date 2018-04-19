package com.cycapservers.account;

/**
 * This class calculates the ingame statistics for a player which are displayed
 * on the leaderboard screen
 */
public class PlayerLBData {
	/**
	 * Players userID name
	 */
	String userID;
	/**
	 * Character class for a player
	 */
	String role;
	/**
	 * Level of the player based on role
	 */
	int level;
	/**
	 * Total kills of the player in that role
	 */
	int kills;
	/**
	 * Total deaths of a player in that role
	 */
	int deaths;
	/**
	 * Average kills of player in that role over all games played in a role
	 */
	double averagekills;
	/**
	 * Average deaths of player in that role over all games played in a role
	 */
	double averagedeaths;
	/**
	 * Kills/Deaths ratio of a player over all games played in a role
	 */
	double kdratio;
	/**
	 * Win rate of a player based on total wins and games played
	 */
	double winrate;
	/**
	 * Total games played for a player in a specific role
	 */
	int gamesplayed;
	/**
	 * Total game wins of a player out of all the games played
	 */
	int gameswins;

	/**
	 * Constructor for creating PlayerLBData
	 * 
	 * @param userID
	 * @param role
	 * @param level
	 * @param kills
	 * @param deaths
	 * @param gamesplayed
	 * @param gamewins
	 */
	public PlayerLBData(String userID, String role, int level, int kills, int deaths, int gamesplayed, int gamewins) {

		this.userID = userID;
		this.role = role;
		this.level = level;
		this.kills = kills;
		this.deaths = deaths;
		this.gamesplayed = gamesplayed;
		if (gamesplayed != 0) {
			this.averagekills = Math.floor((double) kills / gamesplayed * 100) / 100;
			this.averagedeaths = Math.floor((double) deaths / gamesplayed) / 100;
			this.winrate = Math.floor((double) gamewins / gamesplayed) / 100;

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

	/**
	 * Getter method for userID
	 * 
	 * @return userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Setter method for userID
	 * 
	 * @param userID
	 * @return void
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Getter method for role
	 * 
	 * @return role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Setter method for role
	 * 
	 * @param role
	 * @return void
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Getter method for level
	 * 
	 * @return level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Setter method for level
	 * 
	 * @param level
	 * @return void
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Getter method for kills
	 * 
	 * @return kills
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * Setter method for kills
	 * 
	 * @param kills
	 * @return void
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}

	/**
	 * Getter method for deaths
	 * 
	 * @return deaths
	 */
	public int getDeaths() {
		return deaths;
	}

	/**
	 * Setter method for deaths
	 * 
	 * @param deaths
	 * @return void
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	/**
	 * Getter method for average kills
	 * 
	 * @return averagekills
	 */
	public double getAveragekills() {
		return averagekills;
	}

	/**
	 * Setter method for average kills
	 * 
	 * @param averagekills
	 * @return void
	 */
	public void setAveragekills(double averagekills) {
		this.averagekills = averagekills;
	}

	/**
	 * Getter method for average deaths
	 * 
	 * @return averagedeaths
	 */
	public double getAveragedeaths() {
		return averagedeaths;
	}

	/**
	 * Setter method for average deaths
	 * 
	 * @param averagedeaths
	 * @return void
	 */
	public void setAveragedeaths(double averagedeaths) {
		this.averagedeaths = averagedeaths;
	}

	/**
	 * Getter method for kills/deaths ratio
	 * 
	 * @return kdratio
	 */
	public double getKdratio() {
		return kdratio;
	}

	/**
	 * Setter method for k/d ratio
	 * 
	 * @param kdratio
	 * @return void
	 */
	public void setKdratio(double kdratio) {
		this.kdratio = kdratio;
	}

	/**
	 * Getter method for winrate
	 * 
	 * @return winrate
	 */
	public double getWinrate() {
		return winrate;
	}

	/**
	 * Setter method for winrate
	 * 
	 * @param winrate
	 * @return void
	 */
	public void setWinrate(double winrate) {
		this.winrate = winrate;
	}

	/**
	 * Getter method for gamesplayed
	 * 
	 * @return gamesplayed
	 */
	public int getGamesplayed() {
		return gamesplayed;
	}

	/**
	 * Setter method for gamesplayed
	 * 
	 * @param gamesplayed
	 * @return void
	 */
	public void setGamesplayed(int gamesplayed) {
		this.gamesplayed = gamesplayed;
	}

	/**
	 * Getter method for gamewins
	 * 
	 * @return gamewins
	 */
	public int getGameswins() {
		return gameswins;
	}

	/**
	 * Setter method for gamewins
	 * 
	 * @param gamewins
	 * @return void
	 */
	public void setGameswins(int gameswins) {
		this.gameswins = gameswins;
	}
}
