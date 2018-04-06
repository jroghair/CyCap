package com.cycapservers.account;


import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="profiles")
public class Profiles {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id=0;

    @NotNull
    //@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="UserID")
    private String userID;
    
    @NotNull
    @Column(name="Champion")
    private String champion;
    
    
    @NotNull
    @Column(name="Kills")
    private int kills;
    

    @NotNull
    @Column(name="Deaths")
    private int deaths;
    
    @NotNull
    @Column(name="GameWins")
    private int gamewins;

    @NotNull
    @Column(name="GameLosses")
    private int losses;

    
    @NotNull
    @Column(name="GamesPlayed")
    private int gamesplayed;
    

    @NotNull
    @Column(name="FlagGrabs")
    private int flaggrabs;
    
    @NotNull
    @Column(name="FlagReturns")
    private int flagreturns;
    
    @NotNull
    @Column(name="Experience")
    private int experience; 
    //experience int
    //flag grabs, flag returns, flag captures


	public String getUserID() {
		return this.userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String getChampion() {
		return this.champion;
	}


	public void setCharclass(String champion) {
		this.champion = champion;
	}


	public int getKills() {
		return this.kills;
	}


	public void setKills(int kills) {
		this.kills = kills;
	}


	public int getDeaths() {
		return this.deaths;
	}


	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}


	public int getGamewins() {
		return this.gamewins;
	}


	public void setGamewins(int gamewins) {
		this.gamewins = gamewins;
	}


	public int getLosses() {
		return this.losses;
	}


	public void setLosses(int losses) {
		this.losses = losses;
	}


	public int getGamesplayed() {
		return this.gamesplayed;
	}


	public void setGamesplayed(int gamesplayed) {
		this.gamesplayed = gamesplayed;
	}


	public int getFlaggrabs() {
		return this.flaggrabs;
	}


	public void setFlaggrabs(int flaggrabs) {
		this.flaggrabs = flaggrabs;
	}


	public int getFlagreturns() {
		return this.flagreturns;
	}


	public void setFlagreturns(int flagreturns) {
		this.flagreturns = flagreturns;
	}
	
	public int getExperience(){
		return this.experience;
	}
	
	public void setExperience(int experience){
		this.experience=experience;
	}
	

   
}
