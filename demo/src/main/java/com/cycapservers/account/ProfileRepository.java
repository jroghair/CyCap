package com.cycapservers.account;
import java.util.Collection;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ProfileRepository extends CrudRepository<Profile, String> {

    @Query("SELECT DISTINCT profile FROM Profile profile WHERE profile.userID =:userID")
	@Transactional(readOnly = true)

    Collection<String> findByUserID(@Param("userID") String userID);   
    
	
    Friend save(Profile profile);
    
}