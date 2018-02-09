package com.cycapservers.account;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

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
public class AccountController {

	private static final String VIEWS_ACCOUNT_CREATE_OR_UPDATE_FORM = "acccount/createOrUpdateAccountForm";
	private final AccountRepository account;

	@Autowired
	public AccountController(AccountRepository account) {
		this.account = account;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("userID");
	}

	@GetMapping("/account/new")
	public String initCreationForm(Map<String, Object> model) {
		Account account = new Account();
		model.put("account", account);
		return VIEWS_ACCOUNT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/account/new")
	public String processCreationForm(@Valid Account account, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_ACCOUNT_CREATE_OR_UPDATE_FORM;
		} else {
			this.account.save(account);
			return "redirect:/account/" + account.getUserId();
		}
	}

	@GetMapping("/account/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("account", new Account());
		return "account/findAccount";
	}

	@GetMapping("/account")
	public String processFindForm(Account account, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (account.getUserId() == null) {
			account.setUserId(""); // empty string signifies broadest possible
									// search
		}

		// find owners by last name
		Collection<Account> results = this.account.findByUserID(account.getUserId());
		if (results.isEmpty()) {
			// no owners found
			result.rejectValue("userID", "notFound", "not found");
			return "account/findAccount";
		} else if (results.size() == 1) {
			// 1 owner found
			account = results.iterator().next();
			return "redirect:/account/" + account.getUserId();
		} else {
			// multiple owners found
			model.put("selections", results);
			return "account/accountList";
		}
	}

	@GetMapping("/account/{userId}")
	public ModelAndView showAccount(@PathVariable("userId") String userId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		mav.addObject(this.account.findByUserID(userId));
		return mav;
	}

}
