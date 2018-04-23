package com.cycapservers.account;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/** Generates an List of a players friends that gets returned to the webpage
 * @author Jeremy Roghair
 * */
public class Friends {
	/**List of players Friends
	 * */
    private List<String> friendsList;

    /**Creates an XML list of friends inorder for it to display on the webpage
     * @return friendsList
     * */
    @XmlElement
    public List<String> getFriendList() {
        if (friendsList == null) {
        	friendsList = new ArrayList<>();
        }
        return friendsList;
    }
    
    /**Setter method for setting friendsList
     * @param list
     * @return void
     * */
    public void setFriendsList(List list) {
    	this.friendsList = list; 
    }
}
