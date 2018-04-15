package com.cycapservers.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.cycapservers.account.Account;
//import org.springframework.web.bind.annotation.GetMapping;
import com.cycapservers.account.AccountRepository;
import com.cycapservers.account.Friend;
import com.cycapservers.account.FriendRepository;
import com.cycapservers.account.Friends;


@Controller
@SessionAttributes("account")
public class HomepageController {
	
	@Autowired
    private AccountRepository accountsRepository;
    
    @Autowired
    private FriendRepository friendsRepository;

    private final Logger logger = LoggerFactory.getLogger(HomepageController.class);
    
    @ModelAttribute("account")
    public Account newAccount(){
    	return new Account();
    }
	
    @GetMapping("/")
    public String homepage() {
        return "main_page";
    }
    
    @GetMapping("game_list")
    public String gameListPage() {
    	return "game_list2";
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
    public String playNow(Model model, @SessionAttribute("account") Account account) {
    	if(account.getUserID() != null) {
	    	model.addAttribute("user", account.getUserID());
	    	return "play";
    	}
    	else {
	    	Random rand = new Random();
	    	model.addAttribute("user", "guest" + rand.nextInt(1000000));
	    	return "play";
    	}
    }
    
    @RequestMapping(value = "/accounts/register", method = RequestMethod.GET)
    public ModelAndView register(Model model, HttpServletRequest request){
    	logger.info("Entered into get accounts registration controller Layer");
    	String view = "accounts/registration";
    	return new ModelAndView(view, "command", model);
    }
    
    @RequestMapping(value = "/accounts/registration", method = RequestMethod.POST)
    public String registration(Model model, @ModelAttribute("account") Account account){
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
	       	    	return "accounts/login";
       	 		}
       	 	}
    	}
    	return "accounts/unsuccessfulregistration";
    }
    
    @GetMapping("/accounts/successfulregistration")
    public String successfulAccountRegistration() {
        return "accounts/successfulregistration";
    }
    @GetMapping("/accounts/unsuccessfulregistration")
    public String unsuccessfulAccountRegistration() {
        return "accounts/unsuccessfulregistration";
    }
    
    
    @RequestMapping(value = "/accounts/log", method =  RequestMethod.GET)
    public ModelAndView log(Model model, HttpServletRequest request){
    	logger.info("Entered into get accounts login controller Layer");
    	String view = "accounts/login";
    	return new ModelAndView(view, "command", model);
    }
    
    @RequestMapping(value="/accounts/login", method = RequestMethod.POST)
    public String login(Model model, @ModelAttribute("account") Account account){ 
    	String user = account.getUserID();
    	String pswd = account.getPassword();
    	Account acnt = this.accountsRepository.findByUserID(user);
    	if(acnt!=null && acnt.getUserID().compareTo(user)==0 && acnt.getPassword().compareTo(pswd)==0){
    		return "accounts/successfullogin";
    	}
    	
    	return "accounts/unsuccessfullogin";
    }   
    
    @GetMapping("/accounts/successfullogin")
    public String successfulLogin() {
        return "accounts/successfullogin";
    }
    
    @GetMapping("/accounts/unsuccessfullogin")
    public String unsuccessfulLogin() {
        return "accounts/unsuccessfullogin";
    }
    
    @ModelAttribute(value = "friends")
    public Friends newFriends(){
    	return new Friends();
    }
    
    @RequestMapping(value = "/accounts/friends", method =  RequestMethod.GET)
    public String friends(Model model, HttpServletRequest request, @SessionAttribute("account") Account account, @ModelAttribute("friends") Friends friends){
    	logger.info("Entered into get friends controller Layer");
    	model.addAttribute("account", account);
    	String s1 = account.getUserID(); 
    	Collection<String> coll = this.friendsRepository.findByUserID(s1);
    	List<String> list;
    	if (coll instanceof List){
    		list = (List<String>)coll;
    	}
    	else{
    	  list = new ArrayList<String>(coll);
    	}
    	friends.setFriendsList(list);
    	model.addAttribute("friendsList", friends);
    	return "accounts/friendsList";
    }
    
    @ModelAttribute(value = "friend")
    public Friend newFriend(){
    	return new Friend();
    }
    
    
    @RequestMapping(value = "/accounts/friendAdd", method =  RequestMethod.POST)
    public View friendAdd(HttpServletRequest request, @SessionAttribute("account") Account account, @ModelAttribute("friend") Friend friend){
    	logger.info("Entered into get friends ADD controller Layer");
    	String f1 = friend.getUserID();
    	
    	if(accountsRepository.findByUserID(f1)!=null){
    		friend.setPlayerID(f1);
        	friend.setUserID(account.getUserID());
        	this.friendsRepository.save(friend);
    	}
    	return new RedirectView("/accounts/friends");
    }
    
    @RequestMapping(value = "/accounts/friendRemove", method =  RequestMethod.POST)
    public View friendRemove(HttpServletRequest request, @SessionAttribute("account") Account account, @ModelAttribute("friend") Friend friend){
    	logger.info("Entered into get friends REMOVE controller Layer");
    
    	String f1 = friend.getUserID();
    	
    	if(accountsRepository.findByUserID(f1)!=null){
    		friend.setPlayerID(f1);
        	friend.setUserID(account.getUserID());
        	this.friendsRepository.deleteByPlayerID(f1);
    	}
    	
    	return new RedirectView("/accounts/friends");
    }

    @RequestMapping(value = "/accounts/chat", method =  RequestMethod.GET)
    public String friendChat2(HttpServletRequest request, @SessionAttribute("account") Account account){
    	logger.info("Entered into get Chat controller Layer");
    	//System.out.println(account.getUserID());
    	
    	return "accounts/chat";
    }
    
    @GetMapping("/accounts/profile")
    public String profilePage(@SessionAttribute("account") Account account) {
    	//model.addAttribute("account", account);
    	return "accounts/profile";
    }
    
    @GetMapping("/Lobby")
    public String Lobby(){
    	return "game_list2";
    }
    
    @GetMapping("/LobbyScreen")
    public String LobbyScreen(@SessionAttribute("account") Account account){
    	return "LobbyScreen";
    }
}