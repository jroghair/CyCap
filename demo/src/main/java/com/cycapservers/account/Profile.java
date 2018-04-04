package com.cycapservers.account;


import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="profile")
public class Profile {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id=0;

    @NotNull
    //@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="UserID")
    private String userID;
    
    @NotNull
    @Column(name="CharClass")
    private String charclass;
    
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
    @Column(name="FlagCaptures")
    private int flagcaptures;
    
    
    //experience int
    //flag grabs, flag returns, flag captures
    
    
    public Profile() {
    	
    }
    
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String getCharclass() {
		return charclass;
	}


	public void setCharclass(String charclass) {
		this.charclass = charclass;
	}


	public int getKills() {
		return kills;
	}


	public void setKills(int kills) {
		this.kills = kills;
	}


	public int getDeaths() {
		return deaths;
	}


	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}


	public int getGamewins() {
		return gamewins;
	}


	public void setGamewins(int gamewins) {
		this.gamewins = gamewins;
	}


	public int getLosses() {
		return losses;
	}


	public void setLosses(int losses) {
		this.losses = losses;
	}


	public int getGamesplayed() {
		return gamesplayed;
	}


	public void setGamesplayed(int gamesplayed) {
		this.gamesplayed = gamesplayed;
	}


	public int getFlaggrabs() {
		return flaggrabs;
	}


	public void setFlaggrabs(int flaggrabs) {
		this.flaggrabs = flaggrabs;
	}


	public int getFlagreturns() {
		return flagreturns;
	}


	public void setFlagreturns(int flagreturns) {
		this.flagreturns = flagreturns;
	}


	public int getFlagcaptures() {
		return flagcaptures;
	}


	public void setFlagcaptures(int flagcaptures) {
		this.flagcaptures = flagcaptures;
	}

   
}

