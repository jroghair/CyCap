package com.cycapservers.account;
import java.util.Collection;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface FriendRepository extends CrudRepository<Friend, String> {

    @Query("SELECT DISTINCT friend.playerID FROM Friend friend WHERE friend.userID =:userID")
	@Transactional(readOnly = true)

    Collection<String> findByUserID(@Param("userID") String userID);   
    
	
    Friend save(Friend friend);
    
    @Transactional
    String deleteByPlayerID(String PlayerId);
}
