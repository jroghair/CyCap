package com.cycapservers.account;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**Friend Entity class for database calls to the friend table
 * @author Jeremy Roghair
 * */
@Entity
@Table(name="friend")
public class Friend {
	
	/**Id is the primary key of the friend table within the database.
	 * This field is auto generated within the database. This field cannot be null. 
	 * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    /**PlayerID for specific friends for a user. This field cannot be null. 
     * */
    @NotNull
    @Column(name="PlayerID")
    private String playerID;
    
    /**UserID for specific user. This field cannot be null. 
     * */
    @NotNull
    @Column(name="UserID")
    private String userID;
    
    /**Getter method for UserID
     * @return userID
     * */
	public String getUserID() {
		return this.userID;
	}
	/**Setting method for UserID
	 * @param userID
	 * @return void
	 * */
	public void setUserID(String userID) {
		this.userID = userID;
	}	
   /**Getter method for a strings playerID
    * @return playerID
    * */
	public String getPlayerID() {
		return this.playerID;
	}
	/**Setter method for playersID
	 * @param playerID
	 * @return void
	 * */
	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
}