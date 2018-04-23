package com.cycapservers.account;
import java.util.Collection;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/** Interface for sending/receiving data from the database for the Friend table
 * @author Jeremy Roghair
 * */
@Repository
public interface FriendRepository extends CrudRepository<Friend, String> {

	/**Pulls friends from the database for a user
	 * @param userID users data to be extracted
	 * @return Collection<String> collection of a users friends represented as a collection of strings 
	 * */
    @Query("SELECT DISTINCT friend.playerID FROM Friend friend WHERE friend.userID =:userID")
	@Transactional(readOnly = true)
    Collection<String> findByUserID(@Param("userID") String userID);   
    
    /**Saves an row of data for a specific user into the database for Friend table
     * @param friend friend entity object for a specific player to be saved for a specific user in the database
     * @return void
     * */
    Friend save(Friend friend);
    
    /**Deletes a player from the database for a user who has that player as a friend
     * @param PlayerID
     * @return String 
     * */
    @Transactional
    String deleteByPlayerID(String PlayerId);
}
