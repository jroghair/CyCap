package com.cycapservers.system;

import java.awt.Point;
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
import com.cycapservers.account.PlayerLBData;
import com.cycapservers.account.PlayerLBDataList;
import com.cycapservers.account.Profiles;
import com.cycapservers.account.ProfilesRepository;
import com.cycapservers.game.Utils;


@Controller
@SessionAttributes("account")
public class HomepageController {
	
	/**
	 * Autowires AccountsRepository interface for database connection
	 **/
	@Autowired
    private AccountRepository accountsRepository;
    
	/**
	 * Autowires FriendsRepository interface for database connection
	 */
    @Autowired
    private FriendRepository friendsRepository;
    
    /**
	 * Autowires ProfilesRepository interface for database connection
	 **/
	@Autowired
	private ProfilesRepository profilesRepository;

    private final Logger logger = LoggerFactory.getLogger(HomepageController.class);
    
    @ModelAttribute("account")
    public Account newAccount(){
    	return new Account();
    }
    
    /**
	 * Create a profile model
	 * 
	 * @return Profiles model for profiles
	 */
	@ModelAttribute(value = "Profiles")
	public Profiles newProfiles() {
		return new Profiles();
	}
	
    @GetMapping("/")
    public String homepage() {
        return "main_page";
    }
    
    @GetMapping("game_list")
    public String gameListPage(@SessionAttribute("account") Account account) {
    	if(account.getUserID() != null) {
    		return "game_list2";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
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
    
    /**
	 * Handles the account registration after a user submits their request to
	 * create a new account
	 * 
	 * @param model
	 *            model for account
	 * @param account
	 *            session attribute for a user, set when a user logs in
	 * @return String html page for logging in
	 */
	@RequestMapping(value = "/accounts/registration", method = RequestMethod.POST)
	public String registration(Model model, @ModelAttribute("account") Account account) {
		logger.info("Entered into post account registration controller Layer");
		String s1 = account.getEmail();
		String[] s2 = s1.split("\\@");
		// validation checks for email and user name already existing
		if (s2[0].length() > 0 && s2.length == 2) {
			String[] s3 = s2[1].split("\\.");
			if (s3.length == 2 && s3[0].length() > 0 && s3[1].length() == 3) {
				String user = account.getUserID();
				Account acnt = this.accountsRepository.findByUserID(user);
				if (acnt == null) {
					account.setDateOfCreation();
					this.accountsRepository.save(account);

					Profiles p1 = new Profiles(user, "infantry", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					Profiles p2 = new Profiles(user, "recruit", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					Profiles p3 = new Profiles(user, "scout", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					//TODO: artillery class!
					this.profilesRepository.save(p1);
					this.profilesRepository.save(p2);
					this.profilesRepository.save(p3);

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
    	if(account.getUserID() != null) {
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
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
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
    	if(account.getUserID() != null) {
    		logger.info("Entered into get Chat controller Layer");
        	return "accounts/chat";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
    }
    
    @GetMapping("/Lobby")
    public String Lobby(@SessionAttribute("account") Account account){
    	if(account.getUserID() != null) {
    		return "game_list2";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
    }
    
    @GetMapping("/LobbyScreen")
    public String LobbyScreen(@SessionAttribute("account") Account account){
    	if(account.getUserID() != null) {
    		return "LobbyScreen";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
    }
    
    /**
	 * Controller for handling Accounts profile requests for an overall player
	 * statistics
	 * 
	 * @param model
	 *            model for webpage
	 * @param account
	 *            session attribute for a players profile, generated after
	 *            logging in
	 * @param profiles
	 *            model attribute for profiles
	 * @return String returns html page for a user profile
	 */
	@GetMapping("/accounts/profile")
	public String profilePage(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile controller Layer");
			// model.addAttribute("account", account);]
			System.out.println(account.getUserID());
			Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
			Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");
			Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");

			int kills = infantry.getKills() + recruit.getKills() + scout.getKills();
			int deaths = infantry.getDeaths() + recruit.getDeaths() + scout.getDeaths();
			int gamewins = infantry.getGamewins() + recruit.getGamewins() + scout.getGamewins();
			int gamelosses = infantry.getGamelosses() + recruit.getGamelosses() + scout.getGamelosses();
			int gamesplayed = infantry.getGamesplayed() + recruit.getGamesplayed() + scout.getGamesplayed();
			int flaggrabs = infantry.getFlaggrabs() + recruit.getFlaggrabs() + scout.getFlaggrabs();
			int flagreturns = infantry.getFlagreturns() + recruit.getFlagreturns() + scout.getFlagreturns();
			int flagcaptures = infantry.getFlagcaptures() + recruit.getFlagcaptures() + scout.getFlagcaptures();
			int experience = infantry.getExperience() + recruit.getExperience() + scout.getExperience();
			// int level = ;

			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */

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

			if(Utils.DEBUG) System.out.println("is profiles nullFINAL?" + profiles == null);

			model.addAttribute("profiles", profiles);

			return "accounts/profile";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}

	/**
	 * Controller for handling Accounts profile requests for a players Infantry
	 * statistics
	 * 
	 * @param model
	 *            model for webpage
	 * @param account
	 *            session attribute for a players profile, generated after
	 *            logging in
	 * @param profiles
	 *            model attribute for profiles
	 * @return String returns html page for a user profile
	 */
	@GetMapping("/accounts/profileInfantry")
	public String profilePageInfantry(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile infantry controller Layer");
			System.out.println(account.getUserID());

			Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
			profiles = infantry;
			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */

			model.addAttribute("profiles", profiles);

			return "accounts/profileInfantry";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}

	/**
	 * Controller for handling Accounts profile requests for a players Recruit
	 * statistics
	 * 
	 * @param model
	 *            model for webpage
	 * @param account
	 *            session attribute for a players profile, generated after
	 *            logging in
	 * @param profiles
	 *            model attribute for profiles
	 * @return String returns html page for a user profile
	 */
	@GetMapping("/accounts/profileRecruit")
	public String profilePageRecruit(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile recruit controller Layer");
			Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");

			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */
			profiles = recruit;
			model.addAttribute("profiles", profiles);

			return "accounts/profileRecruit";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}

	/**
	 * Controller for handling Accounts profile requests for a players Scout
	 * statistics
	 * 
	 * @param model
	 *            model for webpage
	 * @param account
	 *            session attribute for a players profile, generated after
	 *            logging in
	 * @param profiles
	 *            model attribute for profiles
	 * @return View returns html page for a user profile
	 */
	@GetMapping("/accounts/profileScout")
	public String profilePageScout(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile scout controller Layer");
			Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");

			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */
			profiles = scout;
			model.addAttribute("profiles", profiles);

			return "accounts/profileScout";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}

	/*
	 * @ModelAttribute(value = "ProfilesList") public ProfilesList
	 * newProfilesList(){ return new ProfilesList(); }
	 */

	/**
	 * Model for playerLBDataList use primarily for leaderboards
	 * 
	 * @return PlayerLBDataList
	 */
	@ModelAttribute(value = "PlayerLBDataList")
	public PlayerLBDataList newPlayerLBDataList() {
		return new PlayerLBDataList();
	}

	/**
	 * Controller for handling leaderboards requests.
	 * 
	 * @param model
	 *            model for PlayerLBDataList
	 * @param account
	 *            session variable for a user, set at user login
	 * @param PlayerLBDataList
	 *            Model attribute
	 * @return View returns a view of account leaderboards
	 */
	@GetMapping("/accounts/leaderboards")
	public String LeaderBoards(Model model, @SessionAttribute("account") Account account, @ModelAttribute("PlayerLBDataList") PlayerLBDataList playerLBDataList) {
		if(account.getUserID() != null) {
			logger.info("Entered into get leaderboards controller Layer");
			Collection<Profiles> overall = profilesRepository.findByAllProfiles();
			/*
			 * List<Profiles> list; if (overall instanceof List){ list =
			 * (List<Profiles>)overall; } else{ list = new
			 * ArrayList<Profiles>(overall); }
			 */

			List<PlayerLBData> list = new ArrayList<PlayerLBData>();
			for (Profiles x : overall) {
				PlayerLBData p = new PlayerLBData(x.getUserID(), x.getChampion(), x.getLevel(), x.getKills(), x.getDeaths(),
						x.getGamesplayed(), x.getGamewins());
				list.add(p);
			}

			playerLBDataList.setPlayerLBDataList(list);

			model.addAttribute("playerLBDataList", playerLBDataList.getPlayerLBDataList());

			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */
			return "accounts/leaderboards";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}
}