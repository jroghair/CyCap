package com.cycapservers.account;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    Collection<Account> findAll();

    Collection<Account> findById(@Param("userID") String userID);

    Account save(Account account);
	
}
