package com.cycapservers.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.cycapservers.account.Account;


@Controller
@SessionAttributes("account")
public class HomepageController {

    @GetMapping("/")
    public String homepage() {
        return "homepage";
    }
    
    
}
