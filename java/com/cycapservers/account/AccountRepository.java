package com.cycapservers.account;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
	@Query("SELECT DISTINCT UserID FROM Account a WHERE a.lastName LIKE = UserID")
	@Transactional(readOnly = true)
	Collection<Account> findByUserID(@Param("userID") String userID);


	void save(Account account);

}
