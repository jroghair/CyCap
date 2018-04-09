package com.cycapservers.account;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Interface for sending/receiving data from the database for the Account table
 * @author Jeremy Roghair
 * */

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

	/**Pulls account data from the database for a specific user
	 * @param userID users data from the database
	 * @return Account account object representing a single users data in the database
	 * */
    @Query("SELECT DISTINCT account FROM Account account WHERE account.userID =:userID")
	@Transactional(readOnly = true)
    Account findByUserID(@Param("userID") String userID);
    
    
    /*Collection<Account> findByUserID(@Param("userID") String userID);*/
    
    
    //Collection<Account> findAll(Collection<Account> id);
   /* Collection<Account> findAll();

    Collection<Account> findByUserID(@Param("userID") String userID);
*/	

    /**Saves an row of data for a specific user into the database for Account table
     * @param account account entity object to be saved for a specific user in the database
     * @return void
     * */
    Account save(Account account);
	
}
