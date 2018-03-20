package com.cycapservers.account;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import com.cycapservers.account.Account; 

@Controller
@SessionAttributes("account")
public class AccountController {

    @Autowired
    private AccountRepository accountsRepository;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    @ModelAttribute("account")
    public Account newAccount(){
    	return new Account();
    }

    @GetMapping("/accounts/find")
    public String findAccount(Map<String, Object> model) {
        model.put("account", new Account());
        return "accounts/findAccounts";
    }
    
    @RequestMapping(value = "/accounts/register", method = RequestMethod.GET)
    public ModelAndView register(Model model, HttpServletRequest request){
    	logger.info("Entered into get accounts registration controller Layer");
    	String view = "/accounts/registration";
    	return new ModelAndView(view, "command", model);
    }
    
    @RequestMapping(value = "/accounts/registration", method = RequestMethod.POST)
    public View registration(Model model, @ModelAttribute("account") Account account){
    	logger.info("Entered into post account registration controller Layer");
    	String s1 = account.getEmail();
    	String[] s2 = s1.split("\\@");
    	 //validation checks for email and user name already existing
    	if(s2[0].length()>0 && s2.length==2){
       	 	String[] s3 = s2[1].split("\\.");
       	 	if(s3.length==2 && s3[0].length()>0 && s3[1].length()==3){
       	 		String user = account.getUserID();
       	 		Account acnt = this.accountsRepository.findByUserID(user);
       	 		if(acnt==null){
	       	    	account.setDateOfCreation();
	       	    	this.accountsRepository.save(account);
	       	    	return new RedirectView("/accounts/successfulregistration");
       	 		}
       	 	}
    	}
    	return new RedirectView("/accounts/unsuccessfulregistration");
    }
    
    @GetMapping("/accounts/successfulregistration")
    public String successfulAccountRegistration() {
        return "/accounts/successfulregistration";
    }
    @GetMapping("/accounts/unsuccessfulregistration")
    public String unsuccessfulAccountRegistration() {
        return "/accounts/unsuccessfulregistration";
    }
    
    
    @RequestMapping(value = "accounts/log", method =  RequestMethod.GET)
    public ModelAndView log(Model model, HttpServletRequest request){
    	logger.info("Entered into get accounts login controller Layer");
    	String view = "/accounts/login";
    	return new ModelAndView(view, "command", model);
    }
    
    @RequestMapping(value="/accounts/login", method = RequestMethod.POST)
    public View login(Model model, @ModelAttribute("account") Account account){ 
    	String user = account.getUserID();
    	String pswd = account.getPassword();
    	Account acnt = this.accountsRepository.findByUserID(user);
    	if(acnt!=null && acnt.getUserID().compareTo(user)==0 && acnt.getPassword().compareTo(pswd)==0){
    		return new RedirectView("/accounts/successfullogin");
    	}
    	
    	return new RedirectView("/accounts/unsuccessfullogin");
    }   
    
    @GetMapping("/accounts/successfullogin")
    public String successfulLogin() {
        return "/accounts/successfullogin";
    }
    
    @GetMapping("/accounts/unsuccessfullogin")
    public String unsuccessfulLogin() {
        return "/accounts/unsuccessfullogin";
    }
}
