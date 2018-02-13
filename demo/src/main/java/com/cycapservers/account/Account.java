package com.cycapservers.account;


import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
@Table(name="Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty
    //@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="UserID")
    private String userID;
    @NotEmpty
    @Column(name="Password")
    private String password;
    @NotEmpty
    @Column(name="Email")
    private String email;
    @NotEmpty
    @Column(name="Creation_Date")
    //private DateTimeFormatter dateOfCreation;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfCreation;  
    
    
	public String getUserID() {
		return this.userID;
	}
    
	public void setUserID(String userID) {
		this.userID = userID;
	}

	 
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateOfCreation() {
		
		return dateOfCreation;
	}
	
	public void setDateOfCreation() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		dtf.format(localDate);
		java.sql.Date dat = java.sql.Date.valueOf(localDate);
		this.dateOfCreation = dat;
	}
	/*
	public void setDateOfCreation() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate)); //2016/11/16
		this.dateOfCreation = dtf;
	}*/

}
