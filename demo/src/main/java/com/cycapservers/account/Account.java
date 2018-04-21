package com.cycapservers.account;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id = 0;

	@NotNull
	// @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "UserID")
	private String userID;
	@NotNull
	@Column(name = "Password")
	private String password;
	@NotNull
	@Column(name = "Email")
	private String email;
	@NotNull
	@Column(name = "Creation_Date")
	// private DateTimeFormatter dateOfCreation;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOfCreation;

	@NotNull
	@Column(name = "Member")
	private boolean member;

	@NotNull
	@Column(name = "Administrator")
	private boolean administrator;

	@NotNull
	@Column(name = "Developer")
	private boolean developer;

	public Account() {
		this.setDateOfCreation();
		this.member = true;
	}

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

	public boolean isMember() {
		return member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public boolean isDeveloper() {
		return developer;
	}

	public void setDeveloper(boolean developer) {
		this.developer = developer;
	}

}
