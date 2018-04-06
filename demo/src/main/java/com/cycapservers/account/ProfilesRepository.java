package com.cycapservers.account;
import java.util.Collection;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ProfilesRepository extends CrudRepository<Profiles, String> {

    @Query("SELECT DISTINCT profiles FROM Profiles profiles WHERE profiles.userID =:userID")
	@Transactional(readOnly = true)
    Profiles findByUserIDInfantry(@Param("userID") String userID);   
    //@Param("userID") String userID,
	//and profile.charclass=:charclass
    //profile.userID =:userID 
    
    //Friend save(Profile profile);
    
}