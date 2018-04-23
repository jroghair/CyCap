package com.cycapservers.system;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.cycapservers.account.Account;
//import org.springframework.web.bind.annotation.GetMapping;

/**Controller for handling the websites homepage
 * @author Jeremy Roghair
 * */
@Controller
@SessionAttributes("account")
public class HomepageController {
	
	/**Handles main page website
	 * @return View main page view
	 * */
    @GetMapping("/")
    public String homepage() {
        return "main_page";
    }
	/**Handles game list web page
	 * @return View list of web games
	 * */    
    @GetMapping("game_list")
    public String gameListPage() {
    	return "game_list";
    }
	/**Handles how to web page
	 * @return View how to view to learn how to play
	 * */
    @GetMapping("/how_to")
    public String how_to() {
        return "how_to";
    }
	/**Handles about web page
	 * @return View about view to learn more about the game
	 * */
    @GetMapping("about")
    public String about_the_team() {
    	return "about";
    }
	/**Handles play web page
	 * @return View  play view to play the game
	 * */
    @GetMapping("/play")
    public String playNow(Model model) {
    	Random rand = new Random();
    	model.addAttribute("user", "guest" + rand.nextInt(1000000));
    	return "play";
    }
}
