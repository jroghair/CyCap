package com.cycapservers.account;

import java.util.ArrayList;

import java.util.Collection;

import java.util.List;


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


/**Account Controller class handles website navigation for registering an game play, account, logging in to an existing account, player profiles and lobby navigation
 * @author Jeremy Roghair
 * */

@Controller
@SessionAttributes("account")
public class AccountController {
	
	/**Autowires AccountsRepository interface for database connection
	 * **/
    @Autowired
    private AccountRepository accountsRepository;
    
    /**Autowires FriendsRepository interface for database connection
     * */
    @Autowired
    private FriendRepository friendsRepository;

    /**Autowires ProfilesRepository interface for database connection
     * **/
    @Autowired
    private ProfilesRepository profilesRepository;
    
    /**Creates a logger that generates messages in the applications log
     * */
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);
    
    /**Creates a model attribute for the account
     * @return Account
     * */
    @ModelAttribute("account")
    public Account newAccount(){
    	return new Account();
    }
    
    /**Handles account registrater calls for creating a new account
     * @param model model for account
     * @param request webpage server request
     * @return ModelAndView view for user account registration
     * */
    @RequestMapping(value = "/accounts/register", method = RequestMethod.GET)
    public ModelAndView register(Model model, HttpServletRequest request){
    	logger.info("Entered into get accounts registration controller Layer");
    	String view = "accounts/registration";
    	return new ModelAndView(view, "command", model);
    }
    /**Handles the account registration after a user submits their request to create a new account
     *@param model model for account
     *@param account session attribute for a user, set when a user logs in
     *@return String html page for logging in
     * */
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
    
    /**Controller for handling successful registration requests
     * @return String html page for a successful registration that takes you back to login screen
     * */
    @GetMapping("/accounts/successfulregistration")
    public String successfulAccountRegistration() {
        return "accounts/successfulregistration";
    }

    /**Controller for handling unsuccessful registration requests
     * @return String html page that takes you back to account registration 
     * */
    @GetMapping("/accounts/unsuccessfulregistration")
    public String unsuccessfulAccountRegistration() {
        return "accounts/unsuccessfulregistration";
    }
    
    /**Controller for login requests prior to a user entering their credentials
     * @param model model for account
     * @param request webpage server requests
     * @return ModelAndView view for account login
     * */
    @RequestMapping(value = "/accounts/log", method =  RequestMethod.GET)
    public ModelAndView log(Model model, HttpServletRequest request){
    	logger.info("Entered into get accounts login controller Layer");
    	String view = "accounts/login";
    	return new ModelAndView(view, "command", model);
    }
    
    /**Controller for handling login requests after a user enters their credentials and submits 
     * @param model model for account
     * @param account session attribute for user, set when a user logs in
     * @return String view for a successful/unsuccessful login
     * */
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
    /**Controller for handling successful login 
     * @return String view for successful login
     * */
    @GetMapping("/accounts/successfullogin")
    public String successfulLogin() {
        return "accounts/successfullogin";
    }
    
    /**Controller for handling unsuccessful login
     * @return String view for unsuccessful login
     * */
    @GetMapping("/accounts/unsuccessfullogin")
    public String unsuccessfulLogin() {
        return "accounts/unsuccessfullogin";
    }
    /**Generates model for users friends
     * @return Friends model for friends
     * */
    @ModelAttribute(value = "friends")
    public Friends newFriends(){
    	return new Friends();
    }
    
    /**Controller for handling "GET" mapping request for users friends based on account session gathered from login
     * @param model model request for 
     * @param request webpage server request
     * @param account session attribute for userid, set when user logs in
     * @param friends
     * */
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
    
    /**Generates a model for Friend 
     * @return Friend generates a model attribute for Friend
     * */
    @ModelAttribute(value = "friend")
    public Friend newFriend(){
    	return new Friend();
    }
    
    /**Controller for handling POST requests for adding a Friend
     * @param request webpage server requests
     * @param account session attribute for a players profile, generated after logging in
     * @param Friend model attribute from Friends model already generated
     * @return View returns a View containing a users Friends
     * */
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
    /**Controller for handling POST requests for removing a friend 
     * @param request webpage server requests
     * @param account session attribute for a players profile, generated after logging in 
     * @param friend  friend model attribute
     * @return View updated view with desired friend removed
     * */
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
    /**Controller for handling GET requests for chatting with a user
     * @param request webpage server request
     * @param account session attribute for a players profile, generated after logging in 
     * @return String html page for user chat
     * */
    @RequestMapping(value = "/accounts/chat", method =  RequestMethod.GET)
    public String friendChat2(HttpServletRequest request, @SessionAttribute("account") Account account){
    	logger.info("Entered into get Chat controller Layer");
    	//System.out.println(account.getUserID());
    	
    	return "accounts/chat";
    }
    
    /**Create a profile model 
     * @return Profiles model for profiles
     * */
    @ModelAttribute(value = "Profiles")
    public Profiles newProfiles(){
    	return new Profiles();
    }
    
    /**Controller for handling Accounts profile requests for an overall player statistics
     * @param model model for webpage
     * @param account session attribute for a players profile, generated after logging in 
     * @param profiles model attribute for profiles
     * @return String returns html page for a user profile
     * */
    @GetMapping("/accounts/profile")
    public String profilePage(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
    	logger.info("Entered into get Profile controller Layer");
    	//model.addAttribute("account", account);]
    	System.out.println(account.getUserID());
    	Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
    	Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");
    	Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");
    	
    	System.out.println("is profiles null?" + infantry==null);
    	System.out.println("is profiles null?" + recruit==null);
    	System.out.println("is profiles null?" + scout==null);
    	
    	int kills = infantry.getKills() + recruit.getKills() + scout.getKills();
    	int deaths = infantry.getDeaths() + recruit.getDeaths() + scout.getDeaths();
    	int gamewins = infantry.getGamewins() + recruit.getGamewins() + scout.getGamewins();
    	int gamelosses = infantry.getGamelosses() + recruit.getGamelosses() + scout.getGamelosses();
    	int gamesplayed = infantry.getGamesplayed() + recruit.getGamesplayed() + scout.getGamesplayed(); 
    	int flaggrabs = infantry.getFlaggrabs() + recruit.getFlaggrabs() + scout.getFlaggrabs(); 
    	int flagreturns = infantry.getFlagreturns() + recruit.getFlagreturns() + scout.getFlagreturns(); 
    	int flagcaptures = infantry.getFlagcaptures() + recruit.getFlagcaptures() + scout.getFlagcaptures(); 
    	int experience = infantry.getExperience() + recruit.getExperience() + scout.getExperience(); 
    	int level = experience/100; 
    	
    	/*Profiles profiles = new Profiles(account.getUserID(), "Overall", kills, deaths, gamewins, gamelosses, gamesplayed,
    									flaggrabs, flagreturns, flagcaptures, experience);*/
    	
    	profiles.setUserID(account.getUserID());
    	profiles.setChampion("Overall");
    	profiles.setKills(kills);
    	profiles.setDeaths(deaths);
    	profiles.setGamewins(gamewins);
    	profiles.setGamelosses(gamelosses);
    	profiles.setGamesplayed(gamesplayed);
    	profiles.setFlaggrabs(flaggrabs);
    	profiles.setFlagreturns(flagreturns);
    	profiles.setFlagcaptures(flagcaptures);
    	profiles.setExperience(experience);
    	
    	System.out.println("is profiles nullFINAL?" + profiles==null);
    	
    	model.addAttribute("profiles", profiles);
    	/*
    	List<Integer> list = new LinkedList<Integer>();
    	list.add(experience);
    	list.add(flagcaptures);
    	list.add(flagreturns);
    	list.add(flaggrabs);
    	list.add(gamesplayed);
    	list.add(gamelosses);
    	list.add(gamewins);
    	list.add(deaths);
    	list.add(kills); 

    	profilesList.setProfilesList(list);
    	System.out.println(list.size());
    	
    	model.addAttribute("profilesList", profilesList);*/
    	
    	//account.getUserID(),
    	System.out.println("infantry deaths: " + infantry.getDeaths());
    	return "accounts/profile";
    }   
    
    /**Controller for handling Accounts profile requests for a players Infantry statistics
     * @param model model for webpage
     * @param account session attribute for a players profile, generated after logging in 
     * @param profiles model attribute for profiles
     * @return String returns html page for a user profile
     * */
    @GetMapping("/accounts/profileInfantry")
    public String profilePageInfantry(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
    	logger.info("Entered into get Profile infantry controller Layer");
    	System.out.println(account.getUserID());
    	
    	Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
    	profiles=infantry;
    	/*Profiles profiles = new Profiles(account.getUserID(), "Overall", kills, deaths, gamewins, gamelosses, gamesplayed,
    									flaggrabs, flagreturns, flagcaptures, experience);*/
    	
    	model.addAttribute("profiles", profiles);
    	
    	return "accounts/profileInfantry";
    }   
 
    /**Controller for handling Accounts profile requests for a players Recruit statistics
     * @param model model for webpage
     * @param account session attribute for a players profile, generated after logging in 
     * @param profiles model attribute for profiles
     * @return String returns html page for a user profile
     * */
    @GetMapping("/accounts/profileRecruit")
    public String profilePageRecruit(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
    	logger.info("Entered into get Profile recruit controller Layer");
    	Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");
    	
    	/*Profiles profiles = new Profiles(account.getUserID(), "Overall", kills, deaths, gamewins, gamelosses, gamesplayed,
    									flaggrabs, flagreturns, flagcaptures, experience);*/
    	profiles=recruit;
    	model.addAttribute("profiles", profiles);
    	
    	return "accounts/profileRecruit";
    }     
 
    /**Controller for handling Accounts profile requests for a players Scout statistics
     * @param model model for webpage
     * @param account session attribute for a players profile, generated after logging in 
     * @param profiles model attribute for profiles
     * @return String returns html page for a user profile
     * */
    @GetMapping("/accounts/profileScout")
    public String profilePageScout(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
    	logger.info("Entered into get Profile scout controller Layer");
    	Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");
    	
    	/*Profiles profiles = new Profiles(account.getUserID(), "Overall", kills, deaths, gamewins, gamelosses, gamesplayed,
    									flaggrabs, flagreturns, flagcaptures, experience);*/
    	profiles=scout; 
    	model.addAttribute("profiles", profiles);
    	
    	return "accounts/profileScout";
    }  
}














