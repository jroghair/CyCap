package com.cycapservers.account;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class AccountsList {

	private List<Account> accountsList;

	@XmlElement
	public List<Account> getAccountsList() {
		if (accountsList == null) {
			accountsList = new ArrayList<Account>();
		}
		return accountsList;
	}

	public void setAccountsList(List list) {
		this.accountsList = list;
	}
}
