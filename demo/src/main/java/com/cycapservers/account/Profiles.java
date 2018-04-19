package com.cycapservers.account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**Profiles Entity class for database calls to the Account table
 * @author Jeremy Roghair
 * */
@Entity
@Table(name="profiles")
public class Profiles {
	
	/**Id is the primary key of the profiles table within the database.
	 * This field is auto generated within the database. 
	 * */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id=0;

    /**UserID is the users id that is set when registering a new account.
     * This field cannot be null. 
     * */
    @NotNull
    //@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="UserID")
    private String userID;
    
    /**Role for a specific character class for a user. This field cannot be null.  
     * */
    @NotNull
    @Column(name="Champion")
    private String champion;
    
    /**In-game kills for a user. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Kills")
    private int kills;
    
    /**In-game deaths for a user. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Deaths")
    private int deaths;
    
    /**Total Game Wins for a user. This field cannot be null.  
     * */
    @NotNull
    @Column(name="Gamewins")
    private int gamewins;

    /**Total Game Losses for a user. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Gamelosses")
    private int gamelosses;

    /**Total games played for a user. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Gamesplayed")
    private int gamesplayed;
    
    /**Total flag grabs for a user as a specific role. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Flaggrabs")
    private int flaggrabs;
    
    /**Total flag returns for a user as a specific role. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Flagreturns")
    private int flagreturns;
    
    /**Total flag captures for a user as a specific role. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Flagcaptures")
    private int flagcaptures;
    
    /**Total Experience gained for a user as a specific role. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Experience")
    private int experience; 
  
    /**Current level for a user as a specific role. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Level")
    private int level;    
    
    /**Default Constructor
     * */
    public Profiles(){
    	
    }

    /**Constructor specific profile creation
     * @param userID
     * @param champion
     * @param kills
     * @param deaths
     * @param gamewins
     * @param gamelosses
     * @param gamesplayed
     * @param flaggrabs
     * @param flagreturns
     * @param flagcaptures
     * @param experience
     * @param level 
     * */
    public Profiles(String userID, String champion, int kills, int deaths, int gamewins, int gamelosses, int gamesplayed, int flaggrabs,
    		int flagreturns, int flagcaptures, int experience, int level){
    	this.userID=userID;
    	this.champion=champion; 
    	this.kills=kills;
    	this.deaths=deaths;
    	this.gamewins=gamewins;
    	this.gamelosses=gamelosses;
    	this.gamesplayed=gamesplayed;
    	this.flaggrabs=flaggrabs; 
    	this.flagreturns=flagreturns; 
    	this.flagcaptures=flagcaptures; 
    	this.experience=experience; 
    	this.level = level;
    }
    
    /**Getter method for userid
     * @return userid
     * */
	public String getUserID() {
		return this.userID;
	}

	/**Setter method for userID
	 * @param userID
	 * @return void
	 * */
	public void setUserID(String userID) {
		this.userID = userID;
	}

    /**Getter method for champion
     * @return champion
     * */
	public String getChampion() {
		return this.champion;
	}

	/**Setter method for champion
	 * @param champion
	 * @return void
	 * */
	public void setChampion(String champion) {
		this.champion = champion;
	}

    /**Getter method for kills
     * @return kills
     * */
	public int getKills() {
		return this.kills;
	}

	/**Setter method for kills
	 * @param kills
	 * @return void
	 * */
	public void setKills(int kills) {
		this.kills = kills;
	}

    /**Getter method for deaths
     * @return deaths
     * */
	public int getDeaths() {
		return this.deaths;
	}

	/**Setter method for deaths
	 * @param deaths
	 * @return void
	 * */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

    /**Getter method for Gamewins
     * @return gamewins
     * */
	public int getGamewins() {
		return this.gamewins;
	}

	/**Setter method for GameWins
	 * @param gamewins
	 * @return void
	 * */
	public void setGamewins(int gamewins) {
		this.gamewins = gamewins;
	}

    /**Getter method for gamelosses
     * @return gamelosses
     * */
	public int getGamelosses() {
		return this.gamelosses;
	}

	/**Setter method for gamelosses
	 * @param gamelosses
	 * @return void
	 * */
	public void setGamelosses(int gamelosses) {
		this.gamelosses = gamelosses;
	}

    /**Getter method for gamesplayed
     * @return gamesplayed
     * */
	public int getGamesplayed() {
		return this.gamesplayed;
	}

	/**Setter method for gameplayed
	 * @param gamesplayed
	 * @return void
	 * */
	public void setGamesplayed(int gamesplayed) {
		this.gamesplayed = gamesplayed;
	}

    /**Getter method for flaggrabs
     * @return flaggrabs
     * */
	public int getFlaggrabs() {
		return this.flaggrabs;
	}

	/**Setter method for flaggrabs
	 * @param flaggrabs
	 * @return void
	 * */
	public void setFlaggrabs(int flaggrabs) {
		this.flaggrabs = flaggrabs;
	}

    /**Getter method for flagreturns
     * @return flagreturns
     * */
	public int getFlagreturns() {
		return this.flagreturns;
	}

	/**Setter method for flagreturns
	 * @param flagreturns
	 * @return void
	 * */
	public void setFlagreturns(int flagreturns) {
		this.flagreturns = flagreturns;
	}
	
	
    /**Getter method for flagcaptures
     * @return flagcaptures
     * */
	public int getFlagcaptures() {
		return flagcaptures;
	}

	/**Setter method for flagcaptures
	 * @param flagcaptures
	 * @return void
	 * */
	public void setFlagcaptures(int flagcaptures) {
		this.flagcaptures = flagcaptures;
	}

    /**Getter method for experience
     * @return experience
     * */
	public int getExperience(){
		return this.experience;
	}
	
	/**Setter method for experience
	 * @param experience
	 * @return void
	 * */
	public void setExperience(int experience){
		this.experience=experience;
	}
    /**Getter method for level
     * @return level
     * */
	public int getLevel(){
		return level; 
	}
	/**Setter method for level
	 * @param level
	 * @return void
	 * */
	public void setLevel(int level){
		this.level=level;
	}

   
}
