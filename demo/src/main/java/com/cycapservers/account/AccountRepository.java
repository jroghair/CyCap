package com.cycapservers.account;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

//import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends Repository<Account, Integer> {

	/**
	 * Retrieve {@link account}s from the data store by UserID, returning that
	 * distinct users ID
	 * 
	 * @param userID
	 *            Value to search for
	 * @return a Collection of matching {@link Accounts}s (or an empty
	 *         Collection if none found)
	 */
	//@Query("SELECT DISTINCT userID FROM Account a WHERE a.userID LIKE :userID%")
    @Query("SELECT DISTINCT account FROM Account account WHERE account.userID LIKE :userID%")
	 //@Query("SELECT DISTINCT account FROM Account account WHERE account.userID =:userID")
	@Transactional(readOnly = true)
	Collection<Account> findByUserID(@Param("userID") String userID);
    
	
	
	//@Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id")
    
	@Query("SELECT userID FROM Account a WHERE a.userID =:userID")
    @Transactional(readOnly = true)
    Account findBySpecificUserID(@Param("userID") String userID);


	void save(Account account);

}
