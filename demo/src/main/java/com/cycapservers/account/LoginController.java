package com.cycapservers.account;

import java.util.Map;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class LoginController {
	
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
    /*@Autowired
    private AccountRepository accountsRepository;*/
	

	
    @GetMapping("/login")
    public String initCreationForm(Map<String, Object> model) {
        Account account = new Account();
        model.put("account", account);
        return "accounts/login";
    }
    
    @PostMapping("/login")
    public String processCreationForm(/*@PathVariable("userID") String userID,*/  BindingResult result) {
    	 /*if (result.hasErrors()) {
            return "accounts/login";
    	 } 
    	 System.out.println(userID);
 
    	 else {
    		 
        }*/
    	return "accounts/successfullogin";
    }
	
	
	/////////old
	/*
    @GetMapping("/login")
    public String login(Map<String, Object> model) {
    	logger.info("Entered into login Controller Layer");
        Account account = new Account();
        model.put("account", account);
        //return new ModelAndView(view, "command", model);
        return "accounts/login";
    }
    
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String getTest(@Valid Account accountlogin, BindingResult result){
		logger.info("Entered into login Post Controller Layer");
		
		System.out.println(userID);
		System.out.println(password);
		return "accounts/successfullogin";
	}*/
    /*
    @ModelAttribute(value = "accountLogin")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getRanks(Model model, HttpServletRequest request)
    {	logger.info("Entered into AccountLogin Get Layer");
        String view = "/login";
        return new ModelAndView(view, "command", model);
    }*/
    
    /*
    @PostMapping("/login")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return 
        } else {
            this.owners.save(owner);
            return "redirect:/owners/" + owner.getId();
        }
    }*/
     
}