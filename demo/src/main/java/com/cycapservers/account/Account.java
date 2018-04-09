package com.cycapservers.account;


import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;

/**Account Entity class for database calls to the Account table
 * @author Jeremy Roghair
 * */
@Entity
@Table(name="account")
public class Account {
	
	/**Id is the primary key of the account table within the database.
	 * This field is auto generated within the database. 
	 * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id=0;

    /**UserID is the users id that is set when registering a new account. This key is a foreign key on the Friends and Profile tables.
     * This field cannot be null. 
     * */
    @NotNull
    //@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="UserID")
    private String userID;
    
    /**Password is the users password that is set when registering a new account. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Password")
    private String password;
    
    /**Email is the users email that is set when registering a new account. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Email")
    private String email;
    
    /**Creation_Date is the date when a user creates their account when registering an new account. This field cannot be null. 
     * */
    @NotNull
    @Column(name="Creation_Date")
    //private DateTimeFormatter dateOfCreation;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfCreation;
    
    //add account level boolean values for account levels
    //admin true/false, moderator true/false, member true/false
    //moderator deals with player reports, banning players etc
    //developer 
    
    /**Default constructor, sets date of creation automatically. 
     * */
    public Account() {
    	this.setDateOfCreation();
    }
    
    /**Getter Method for UserID
     * @return userID
     * */
	public String getUserID() {
		
		return this.userID;
	}

    /**Setter Method for UserID
     * @param userID
     * @return void
     * */
	public void setUserID(String userID) {
		this.userID = userID;
	}

    /**Getter Method for Password
     * @return password
     * */
	public String getPassword() {
		return this.password;
	}
	
    /**Setter Method for Password
     * @param password
     * @return void
     * */
	public void setPassword(String password) {
		this.password = password;
	}
	
    /**Getter Method for Email
     * @return email
     * */
	public String getEmail() {
		return this.email;
	}
    /**Setter Method for Email
     * @param email
     * @return void
     * */
	public void setEmail(String email) {
		this.email = email;
	}

    /**Getter Method for DateOfCreation
     * @return dateOfCreation
     * */
	public Date getDateOfCreation() {
		
		return dateOfCreation;
	}
    /**Setter Method for DateOfCreation
     * @return void
     * */
	public void setDateOfCreation() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		dtf.format(localDate);
		java.sql.Date dat = java.sql.Date.valueOf(localDate);
		this.dateOfCreation = dat;
	}

}
