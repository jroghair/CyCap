package com.cycapservers.game;

import java.awt.Point;

import com.cycapservers.account.ProfileDataUpdate;

public class PlayerStats {

	protected String userID;
	protected String champion;
	protected int experience;
	protected int level;
	protected int team;

	protected int kills;
	protected int deaths;
	protected int wins;
	protected int losses;

	// MODE SPECIFIC STATS//
	// CTF
	protected int flag_grabs;
	protected int flag_returns;
	protected int flag_captures;

	// GameSpecific Stats
	protected Class<? extends GameState> game_type;
	int gamesplayed;
	int gamewins;
	int gamelosses;

	public PlayerStats(Player player, String champion) {
		this.userID = player.getEntity_id(); // need to add this in later
		this.userID = player.role; // need to ensure role was already assigned
		this.kills = 0;
		this.deaths = 0;
		this.wins = 0;
		this.losses = 0;
		this.flag_returns = 0;
		this.flag_grabs = 0;
		this.flag_captures = 0;
		this.experience = 0;
		this.game_type = null;
		this.gamesplayed = 0;
		this.gamelosses = 0;
		this.gamewins = 0;

	}

	public void playerGetsKill() {
		kills++;
	}

	public void playerDies() {
		deaths++;
	}

	public void playerWinsGame() {
		wins++;
	}

	public void playerLossesGame() {
		losses++;
	}

	public void playerflag_grabs() {
		flag_grabs++;
	}

	public void playerFlagReturns() {
		flag_returns++;
	}

	public void playerFlagCaptures() {
		this.losses++;
	}

	public void playerExperienceGain(int exp) {
		this.experience += exp;
	}

	public void calculatePlayerLevel() {
		this.level = experience / 100;
	}

	// getter methods for saving to DB
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

	public void setGameType(String gametype) {
		this.game_type = gametype;
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

	public void setLevelAndXP() {
		Point p = ProfileDataUpdate.dbGetLevel(userID, champion);
		this.level = p.x;
		this.experience = p.y;
		if (Utils.DEBUG)
			System.out.println("Start - Client: " + userID + " Level: " + level + " Exp: " + experience);
	}

	public void updateScore(int winner) { // could have this take in winning
											// team to double scores potentially
		if (this.game_type == null) {
			throw new IllegalStateException("Cannot update xp when game type has not been set!");
		}
		if (this.game_type.equals(CaptureTheFlag.class)) {
			int new_xp = this.kills * 10;
			new_xp += this.flag_grabs * 25;
			new_xp += this.flag_captures * 100;
			new_xp += this.flag_returns * 35;
			if (team == winner) {
				new_xp *= 2;
				wins++;
			} else {
				losses++;
			}
			this.experience += new_xp;

			Point p = Utils.calculateLevelAndXP(new Point(this.level, this.experience));
			this.level = p.x;
			this.experience = p.y;
		} else if (this.game_type.equals(TeamDeathMatch.class)) {
			// calculate score
		} else if (this.game_type.equals(FreeForAll.class)) {

		}
	}

}
