package com.cycapservers.account;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
@Entity
@Table(name="Account")
public class Account {
    @Id
    @NotEmpty
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String userID;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    @NotEmpty
    private DateTimeFormatter dateOfCreation;
    
    @Column(name="UserID")
	public String getUserId() {
		return userID;
	}
    
	public void setUserId(String userID) {
		this.userID = userID;
	}

	 @Column(name="Password")
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="Email")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="Creation_Date")
	public DateTimeFormatter getDateOfCreation() {
		
		return dateOfCreation;
	}
	
	public void setDateOfCreation() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate)); //2016/11/16
		this.dateOfCreation = dtf;
	}

}
