package com.cycapservers.account;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;


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
    @Column(name="character")
    private String character;
    
    @NotNull
    @Column(name="kills")
    private int kills;
    

    @NotNull
    @Column(name="deaths")
    private int deaths;
    
    @NotNull
    @Column(name="wins")
    private int wins;

    @NotNull
    @Column(name="losses")
    private int losses;

    
    @NotNull
    @Column(name="gamesplayed")
    private int gamesplayed;
    
    
    public Profile() {
    	
    }
    
   
	public String getCharacter() {
		return character;
	}


	public void setCharacter(String character) {
		this.character = character;
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


	public int getWins() {
		return wins;
	}


	public void setWins(int wins) {
		this.wins = wins;
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


	public String getUserID() {
		
		return this.userID;
	}
    
	public void setUserID(String userID) {
		this.userID = userID;
	}

}

