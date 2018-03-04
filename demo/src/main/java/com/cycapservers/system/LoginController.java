package com.cycapservers.system;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class LoginController {
	
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
    @GetMapping("/login")
    public String login() {
    	logger.info("Entered into Login Controller Layer");
        return "accounts/login";
    }
    /*
    @PostMapping("/login/{login}")
    public String validateLogin(@PathVariable("login") String userID, Map<String, Object> model){
    	
    }*/
    
    
}