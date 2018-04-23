package com.cycapservers.account;

public class RoleLevels {

	private String userID;

	private int totalLevel;

	private int recruitLevel;

	private int scoutLevel;

	private int artilleryLevel;

	private int infantryLevel;

	public RoleLevels() {

	}

	public RoleLevels(String userID, int recruitLevel, int scoutLevel, int artilleryLevel, int infantryLevel) {
		this.userID = userID;
		this.recruitLevel = recruitLevel;
		this.scoutLevel = scoutLevel;
		this.artilleryLevel = artilleryLevel;
		this.infantryLevel = infantryLevel;
		this.totalLevel = recruitLevel + scoutLevel + artilleryLevel + infantryLevel;

	}

	public String getUserID() {
		return userID;
	}

	public int getTotalLevel() {
		return totalLevel;
	}

	public int getRecruitLevel() {
		return recruitLevel;
	}

	public int getScoutLevel() {
		return scoutLevel;
	}

	public int getArtilleryLevel() {
		return artilleryLevel;
	}

	public int getInfantryLevel() {
		return infantryLevel;
	}

}
