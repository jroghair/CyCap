package com.cycapservers.system;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.bind.annotation.GetMapping;


@Controller
@SessionAttributes("account")
public class HomepageController {
	
    @GetMapping("/")
    public String homepage() {
        return "main_page";
    }
    
    @GetMapping("/main_page")
    public String homepage2() {
        return "main_page";
    }
    
    @GetMapping("/how_to")
    public String how_to() {
        return "how_to";
    }
    
    @GetMapping("about")
    public String about_the_team() {
    	return "about";
    }
    
    @GetMapping("/play")
    public String playNow(Model model) {
    	Random rand = new Random();
    	model.addAttribute("user", "guest" + rand.nextInt(1000000));
    	return "play";
    }
}
