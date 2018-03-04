package com.cycapservers.account;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

	
    @Query("SELECT DISTINCT account FROM Account account WHERE account.userID LIKE :userID%")
	@Transactional(readOnly = true)
	Collection<Account> findByUserID(@Param("userID") String userID);
    
    
    //Collection<Account> findAll(Collection<Account> id);
   /* Collection<Account> findAll();

    Collection<Account> findByUserID(@Param("userID") String userID);

    Account save(Account account);*/
	
}
