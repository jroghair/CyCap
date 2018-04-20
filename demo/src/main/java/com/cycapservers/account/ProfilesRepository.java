package com.cycapservers.account;
import java.util.Collection;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**Interface for sending/receiving data from the database for the Profiles table
 * @author Jeremy Roghair
 * */
@Repository
public interface ProfilesRepository extends CrudRepository<Profiles, String> {

	/**Pulls profile data from the database for a specific user
	 * @param userID users data to select from the database
	 * @param champion role the player has selected in a game to be selected from the database 
	 * @return Profile profile object representing a single users data in the database
	 * */
    @Query("SELECT DISTINCT profiles FROM Profiles profiles WHERE profiles.userID =:userID and profiles.champion=:champion")
	@Transactional(readOnly = true)
    Profiles findByUserID(@Param("userID") String userID, @Param("champion") String champion);   
    //@Param("userID") String userID,
	//and profile.charclass=:charclass
    //profile.userID =:userID 
 
	/**Pulls profile data from the database for a specific user
	 * @return Collection<Profile> profile object representing a single users data in the database
	 * */
    @Query("SELECT DISTINCT profiles FROM Profiles profiles")
   	@Transactional(readOnly = true)
       Collection<Profiles> findByAllProfiles();   
    
    
    /**Saves an row of data for a specific user and role into the database for profile table
     * @param Profile profiles entity object for a specific user and role to be saved in the database
     * @return void
     * */
    Profiles save(Profiles profile);
    /**Deletes a player from the database whose role is specified in profile
     * @param Profile entity profile object for a user
     * @return void 
     * */
    void delete(Profiles profile);
    
}