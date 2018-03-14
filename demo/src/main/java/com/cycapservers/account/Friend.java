package com.cycapservers.account;


import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotNull
    @Column(name="PlayerID")
    private String playerID;
    
    @NotNull
    @Column(name="UserID")
    private String userID;
    
    
	public String getUserID() {
		return this.userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}	
 
	public String getPlayerID() {
		return this.playerID;
	}
	
	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
}