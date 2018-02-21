package com.cycapservers.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomepageController {
	
    @GetMapping("/")
    public String homepage() {
        return "homepage";
    }
    /*
    @GetMapping("/fragments/how_to")
    public String howToPlay(){
    	return "how_to";
    }*/
}
