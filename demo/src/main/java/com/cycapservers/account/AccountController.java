package com.cycapservers.account;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import com.cycapservers.account.AccountRepository; 

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountsRepository;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/accounts/new")
    public String initCreationForm(Map<String, Object> model) {
    	logger.info("Entered into Controller Layer");
        Account account = new Account();
        model.put("account", account);
        return "accounts/createOrUpdateAccountForm";
    }

    @PostMapping("/accounts/new")
    public String processCreationForm(@Valid Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "accounts/createOrUpdateAccountForm";
        } else {
            accountsRepository.save(account);
            return "redirect:/accounts";
        }
    }
/*
    @GetMapping("/accounts")
    public String getAllAccounts(Map<String, Object> model) {
    	
        logger.info("Entered into Controller /accounts Layer");
        Collection<Account> results = accountsRepository.findAll();
        logger.info("Number of Records Fetched:" + results.size());
        model.put("selections", results);
        return "accounts/accountsList";
    }*/

    @GetMapping("/accounts/{userID}")
    public String findAccountByUserID(@PathVariable("userID") String userID, Map<String, Object> model) {
    	System.out.println(userID);
        logger.info("Entered into Controller /accounts/{userID} Layer");
        Collection<Account> results = accountsRepository.findByUserID(userID);
        logger.info("Number of Records Fetched:" + results.size());
        model.put("selections", results);
        return "accounts/accountsList";
    }

    @GetMapping("/accounts/find")
    public String findAccount(Map<String, Object> model) {
        model.put("account", new Account());
        return "accounts/findAccounts";
    }
    
    
	
}
