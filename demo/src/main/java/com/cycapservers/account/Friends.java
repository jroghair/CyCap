package com.cycapservers.account;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class Friends {
    private List<String> friendsList;

    @XmlElement
    public List<String> getFriendList() {
        if (friendsList == null) {
        	friendsList = new ArrayList<>();
        }
        return friendsList;
    }
    

    public void setFriendsList(List list) {
    	this.friendsList = list; 
    }
}
