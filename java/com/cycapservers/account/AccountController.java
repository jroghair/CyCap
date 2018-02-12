package com.cycapservers.account;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cycapservers.account.AccountRepository; 

@Controller
@Repository
public class AccountController {

	private static final String VIEWS_ACCOUNT_CREATE_OR_UPDATE_FORM = "acccounts/createOrUpdateAccountForm";
	private final AccountRepository accounts;
	private final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	public AccountController(AccountRepository account) {
		this.accounts = account;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("userID");
	}

	@GetMapping("/accounts/new")
	public String initCreationForm(Map<String, Object> model) {
		Account account = new Account();
		model.put("account", account);
		return VIEWS_ACCOUNT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/accounts/new")
	public String processCreationForm(@Valid Account account, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_ACCOUNT_CREATE_OR_UPDATE_FORM;
		} else {
			this.accounts.save(account);
			return "redirect:/accounts/" + account.getUserID();
		}
	}

	@GetMapping("/accounts/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("account", new Account());
		return "accounts/findAccounts";
	}

	@GetMapping("/accounts")
	public String processFindForm(Account account, BindingResult result, Map<String, Object> model) {
	
		// allow parameterless GET request for /owners to return all records
		if (account.getUserID() == null) {
			account.setUserID(""); // empty string signifies broadest possible
									// search
		}

		// find users by last name
		Collection<Account> results = this.accounts.findByUserID(account.getUserID());
		if (results.isEmpty()) {
			// no users found
			result.rejectValue("userID", "notFound", "not found");
			return "accounts/findAccounts";
		} else if (results.size() == 1) {
			// 1 user found
			account = results.iterator().next();
			return "redirect:/accounts/" + account.getUserID();
		} else {
			// multiple users found
			model.put("selections", results);
			return "accounts/accountsList";
		}
			
	}

	@GetMapping("/accounts/{userID}")
	public ModelAndView showAccount(@PathVariable("userID") String userID) {
		ModelAndView mav = new ModelAndView("accounts/accountDetails");
		mav.addObject(this.accounts.findBySpecificUserID(userID));
		return mav;
	}

}
