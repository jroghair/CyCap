package com.cycapservers.system;

import java.awt.Point;
import java.sql.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.cycapservers.account.Account;
//import org.springframework.web.bind.annotation.GetMapping;
import com.cycapservers.account.AccountRepository;
import com.cycapservers.account.AccountsList;
import com.cycapservers.account.CareerTotals;
import com.cycapservers.account.Friend;
import com.cycapservers.account.FriendRepository;
import com.cycapservers.account.Friends;
import com.cycapservers.account.PlayerLBData;
import com.cycapservers.account.PlayerLBDataList;
import com.cycapservers.account.Profiles;
import com.cycapservers.account.ProfilesRepository;
import com.cycapservers.account.RoleLevels;
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
    public String homepage(Model model, @SessionAttribute("account") Account account) {
    	if(account.getUserID() != null) {
	    	model.addAttribute("logged_in", true);
    	}
    	else {
	    	model.addAttribute("logged_in", false);
    	}
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
    
    @GetMapping("/logout")
    public String logout(Model model, @ModelAttribute("account") Account account){
    	account.setUserID(null);
    	return "main_page";
    	
    }
    
    //TODO: fix the login checks on the profile pages
    
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

					Profiles p1 = new Profiles(user, "infantry", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					Profiles p2 = new Profiles(user, "recruit", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					Profiles p3 = new Profiles(user, "scout", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					Profiles p4 = new Profiles(user, "artillery", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					
					this.profilesRepository.save(p1);
					this.profilesRepository.save(p2);
					this.profilesRepository.save(p3);
					this.profilesRepository.save(p4);

					return "accounts/login";
				}
			}
		}
		return "accounts/unsuccessfulregistration";
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
    
    @ModelAttribute(value = "careerTotals")
	public CareerTotals newCareerTotals() {
		return new CareerTotals();
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
	public String profilePage(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles, @ModelAttribute("RoleLevels") RoleLevels roleLevels, @ModelAttribute("CareerTotals") CareerTotals careerTotals) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile controller Layer");

			String name = account.getUserID();
			Account p = accountsRepository.findByUserID(name);
			account = p;
			model.addAttribute("account", account);

			Profiles q = profilesRepository.findByUserID(name, "recruit");
			profiles = q;
			model.addAttribute("profiles", profiles);

			// model.addAttribute("account", account);]
			Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
			Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");
			Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");
			Profiles artillery = profilesRepository.findByUserID(account.getUserID(), "artillery");

			int kills = infantry.getKills() + recruit.getKills() + scout.getKills() + artillery.getKills();
			int deaths = infantry.getDeaths() + recruit.getDeaths() + scout.getDeaths() + artillery.getDeaths();
			int gamewins = infantry.getGamewins() + recruit.getGamewins() + scout.getGamewins() + artillery.getGamewins();
			int gamelosses = infantry.getGamelosses() + recruit.getGamelosses() + scout.getGamelosses()
					+ artillery.getGamelosses();
			int gamesplayed = infantry.getGamesplayed() + recruit.getGamesplayed() + scout.getGamesplayed()
					+ artillery.getGamesplayed();
			int flaggrabs = infantry.getFlaggrabs() + recruit.getFlaggrabs() + scout.getFlaggrabs()
					+ artillery.getFlaggrabs();
			int flagreturns = infantry.getFlagreturns() + recruit.getFlagreturns() + scout.getFlagreturns()
					+ artillery.getFlagreturns();
			int flagcaptures = infantry.getFlagcaptures() + recruit.getFlagcaptures() + scout.getFlagcaptures()
					+ scout.getFlagcaptures();
			int level = infantry.getLevel() + recruit.getLevel() + scout.getLevel() + artillery.getLevel();
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
			// profiles.setExperience(experience);

			model.addAttribute("profiles", profiles);

			CareerTotals career = new CareerTotals(name, kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
					flagreturns, flagcaptures, level);

			careerTotals = career;

			model.addAttribute("careerTotals", careerTotals);

			RoleLevels roleLevels2 = new RoleLevels(account.getUserID(), recruit.getLevel(), scout.getLevel(),
					artillery.getLevel(), infantry.getLevel());

			roleLevels = roleLevels2;

			model.addAttribute("roleLevels", roleLevels);

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
	public String profilePageInfantry(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles, @ModelAttribute("RoleLevels") RoleLevels roleLevels, @ModelAttribute("CareerTotals") CareerTotals careerTotals) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile infantry controller Layer");

			Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
			profiles = infantry;
			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */

			model.addAttribute("profiles", profiles);

			Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");
			Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");
			Profiles artillery = profilesRepository.findByUserID(account.getUserID(), "artillery");

			RoleLevels roleLevels2 = new RoleLevels(account.getUserID(), recruit.getLevel(), scout.getLevel(),
					artillery.getLevel(), infantry.getLevel());

			roleLevels = roleLevels2;

			model.addAttribute("roleLevels", roleLevels);

			CareerTotals career = new CareerTotals(account.getUserID(), infantry.getKills(), infantry.getDeaths(),
					infantry.getGamewins(), infantry.getGamelosses(), infantry.getGamesplayed(), infantry.getFlaggrabs(),
					infantry.getFlagreturns(), infantry.getFlagcaptures(), infantry.getLevel());

			careerTotals = career;

			model.addAttribute("careerTotals", careerTotals);

			return "accounts/profileInfantry";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}

	@ModelAttribute(value = "roleLevels")
	public RoleLevels newRoleLevels() {
		return new RoleLevels();
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
	public String profilePageRecruit(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles, @ModelAttribute("RoleLevels") RoleLevels roleLevels, @ModelAttribute("CareerTotals") CareerTotals careerTotals) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile recruit controller Layer");
			Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");

			Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");
			Profiles artillery = profilesRepository.findByUserID(account.getUserID(), "artillery");
			Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");

			RoleLevels roleLevels2 = new RoleLevels(account.getUserID(), recruit.getLevel(), scout.getLevel(),
					artillery.getLevel(), infantry.getLevel());

			roleLevels = roleLevels2;

			model.addAttribute("roleLevels", roleLevels);
			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */
			profiles = recruit;
			model.addAttribute("profiles", profiles);

			CareerTotals career = new CareerTotals(account.getUserID(), recruit.getKills(), recruit.getDeaths(),
					recruit.getGamewins(), recruit.getGamelosses(), recruit.getGamesplayed(), recruit.getFlaggrabs(),
					recruit.getFlagreturns(), recruit.getFlagcaptures(), recruit.getLevel());

			careerTotals = career;

			model.addAttribute("careerTotals", careerTotals);

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
	public String profilePageScout(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles, @ModelAttribute("RoleLevels") RoleLevels roleLevels, @ModelAttribute("CareerTotals") CareerTotals careerTotals) {
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

			Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
			Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");
			Profiles artillery = profilesRepository.findByUserID(account.getUserID(), "artillery");

			RoleLevels roleLevels2 = new RoleLevels(account.getUserID(), recruit.getLevel(), scout.getLevel(),
					artillery.getLevel(), infantry.getLevel());

			roleLevels = roleLevels2;

			model.addAttribute("roleLevels", roleLevels);

			CareerTotals career = new CareerTotals(account.getUserID(), scout.getKills(), scout.getDeaths(),
					scout.getGamewins(), scout.getGamelosses(), scout.getGamesplayed(), scout.getFlaggrabs(),
					scout.getFlagreturns(), scout.getFlagcaptures(), scout.getLevel());

			careerTotals = career;

			model.addAttribute("careerTotals", careerTotals);

			return "accounts/profileScout";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}

	@GetMapping("/accounts/profileArtillery")
	public String profilePageArtillery(Model model, @SessionAttribute("account") Account account, @ModelAttribute("Profiles") Profiles profiles, @ModelAttribute("RoleLevels") RoleLevels roleLevels, @ModelAttribute("CareerTotals") CareerTotals careerTotals) {
		if(account.getUserID() != null) {
			logger.info("Entered into get Profile Artillery controller Layer");

			Profiles artillery = profilesRepository.findByUserID(account.getUserID(), "artillery");
			profiles = artillery;
			/*
			 * Profiles profiles = new Profiles(account.getUserID(), "Overall",
			 * kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs,
			 * flagreturns, flagcaptures, experience);
			 */

			model.addAttribute("profiles", profiles);
			Profiles infantry = profilesRepository.findByUserID(account.getUserID(), "infantry");
			Profiles recruit = profilesRepository.findByUserID(account.getUserID(), "recruit");
			Profiles scout = profilesRepository.findByUserID(account.getUserID(), "scout");

			RoleLevels roleLevels2 = new RoleLevels(account.getUserID(), recruit.getLevel(), scout.getLevel(),
					artillery.getLevel(), infantry.getLevel());

			roleLevels = roleLevels2;

			model.addAttribute("roleLevels", roleLevels);

			CareerTotals career = new CareerTotals(account.getUserID(), artillery.getKills(), artillery.getDeaths(),
					artillery.getGamewins(), artillery.getGamelosses(), artillery.getGamesplayed(),
					artillery.getFlaggrabs(), artillery.getFlagreturns(), artillery.getFlagcaptures(),
					artillery.getLevel());

			careerTotals = career;

			model.addAttribute("careerTotals", careerTotals);

			return "accounts/profileInfantry";
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
	
	@ModelAttribute(value = "accountsList")
	public AccountsList newAccountsList() {
		return new AccountsList();
	}

	@GetMapping("/accounts/AdminControls")
	public String LeaderBoards(Model model, @SessionAttribute("account") Account account, @ModelAttribute("AccountsList") AccountsList accountsList) {
		if(account.getUserID() != null) {
			logger.info("Entered into get AdminControls controller Layer");

			if (account.getAdministrator() == 0 && account.getDeveloper() == 0)
				return "accounts/access";
			Collection<Account> users = accountsRepository.findAllUsers();

			List<Account> list = new ArrayList<Account>();
			for (Account x : users) {
				list.add(x);
			}

			accountsList.setAccountsList(list);

			model.addAttribute("accountsList", accountsList.getAccountsList());

			return "accounts/admincontrols";
    	}
    	else {
    		logger.info("Entered into get accounts login controller Layer");
        	return "accounts/login";
    	}
	}

	@RequestMapping(value = "/accounts/AdminControls", method = RequestMethod.POST)
	public String AdminControlsPost(@RequestParam("player") String player, HttpServletRequest request, Model model,
			@SessionAttribute("account") Account account, @ModelAttribute("AccountsList") AccountsList accountsList,
			@ModelAttribute("Account") Account playeraccount) {
		logger.info("Entered into get AdminControls POST controller Layer");

		Account oldAccount = accountsRepository.findByUserID(player);
		if (oldAccount.getAdministrator() == 0) {
			playeraccount.setUserID(player);
			playeraccount.setPassword(oldAccount.getPassword());
			playeraccount.setEmail(oldAccount.getEmail());

			Date date = oldAccount.getDateOfCreation();
			playeraccount.setDateOfCreation(date);
			playeraccount.setMember(1);
			playeraccount.setDeveloper(0);
			playeraccount.setAdministrator(1);

			accountsRepository.delete(oldAccount);
			accountsRepository.save(playeraccount);

		} else if (!account.getUserID().equals(player)) {
			playeraccount.setUserID(player);
			playeraccount.setPassword(oldAccount.getPassword());
			playeraccount.setEmail(oldAccount.getEmail());

			Date date = oldAccount.getDateOfCreation();
			playeraccount.setDateOfCreation(date);
			playeraccount.setMember(1);
			playeraccount.setDeveloper(0);
			playeraccount.setAdministrator(0);
			accountsRepository.delete(oldAccount);
			accountsRepository.save(playeraccount);
		}

		if (account.getAdministrator() == 0 && account.getDeveloper() == 0)
			return "accounts/access";
		Collection<Account> users = accountsRepository.findAllUsers();

		List<Account> list = new ArrayList<Account>();
		for (Account x : users) {
			list.add(x);
		}

		accountsList.setAccountsList(list);

		model.addAttribute("accountsList", accountsList.getAccountsList());

		model.addAttribute("playeraccount", playeraccount);

		return "accounts/admincontrols";
	}
}