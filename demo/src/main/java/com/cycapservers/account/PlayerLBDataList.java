package com.cycapservers.account;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
/**This class is used to calculate a list of player statistics for the leaderboard screen.
 * @author Jeremy Roghair
 * */
public class PlayerLBDataList {

	/**List of player profiles with their statistics for the leaderboards
	 * */
    private List<PlayerLBData> PlayerLBDataList;

    /**Getter method for the list PlayerLBDataList, if it is null then it returns an empty List.
     * @return PlayerLBDataList
     * */
    @XmlElement
    public List<PlayerLBData> getPlayerLBDataList() {
        if (PlayerLBDataList == null) {
        	PlayerLBDataList = new ArrayList<PlayerLBData>();
        }
        return PlayerLBDataList;
    }
    
    /**Setter method for PlayerLBDataList
     * @param void
     * */
    public void setPlayerLBDataList(List<PlayerLBData> list) {
    	this.PlayerLBDataList = list; 
    }
}
