package com.cycapservers.account;

import java.util.ArrayList;
import java.util.List;


import javax.xml.bind.annotation.XmlElement;

public class ProfilesList {

    private List<Profiles> profilesList;


    @XmlElement
    public List<Profiles> getProfilesList() {
        if (profilesList == null) {
        	profilesList = new ArrayList<Profiles>();
        }
        return profilesList;
    }
    

    public void setProfilesList(List<Profiles> list) {
    	this.profilesList = list; 
    }
    
    
}
