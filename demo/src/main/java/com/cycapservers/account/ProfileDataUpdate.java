package com.cycapservers.account;

import java.awt.Point;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cycapservers.game.PlayerStats;
import com.cycapservers.game.Utils;

/**
 * Static Controller class that allows the database to be updated for user
 * profiles before and after a game.
 * 
 * @author Jeremy Roghair
 */
@Controller
public class ProfileDataUpdate {
	// List<PlayerStats> players;
	/**
	 * Autowires ProfilesRepository interface for database connection
	 **/
	private static ProfilesRepository profilesRepo;

	@Autowired
	private ProfilesRepository profilesRepository;

	@PostConstruct
	private void initStaticRepo() {
		profilesRepo = profilesRepository;
	}

	/**
	 * Default constructor
	 */
	public ProfileDataUpdate() {

	}

	/**
	 * Allows the game to know a users experience and level at the start of a
	 * game
	 * 
	 * @param userID
	 *            userID for specific players
	 * @param champion
	 *            role choosen by a player at the start of a game. Either
	 *            infantry, recruit or scout
	 * @return Point contains experience and level of a player for the specific
	 *         role
	 */

	// need to return level as well
	public static Point dbGetLevel(String userID, String champion) {
		if (Utils.DEBUG) System.out.print("Client ID: " + userID); // are either of these// null?
		if (Utils.DEBUG) System.out.println("\tClient Role: " + champion);
		Profiles profile = profilesRepo.findByUserID(userID, champion); 
		// creates a modified point using inner class MPoint in order to store
		// more data easily for each player
		Point point = new Point(profile.getLevel(), profile.getExperience());
		return point;
	}

	public static boolean dbCheckLock(String userID, String champion) {
		Profiles profile = profilesRepo.findByUserID(userID, champion);
		if(champion.toLowerCase().equals("recruit"))
			return true;
		else if (champion.toLowerCase().equals("scout"))
			return profile.getScoutunlocked() == 1;
		else if (champion.toLowerCase().equals("artillery"))
			return profile.getArtilleryunlocked() == 1;
		else if (champion.toLowerCase().equals("infantry"))
			return profile.getInfantryunlocked() == 1;
		else
			throw new IllegalArgumentException("illegal parameter: no such role/champion");
	}

	/**
	 * Saves and deletes data to the database, used primarily after a game is
	 * complete
	 * 
	 * @param playerstats
	 *            contains all the statistics of a player at the end of a game
	 * @return void
	 */
	// instead of taking in players just take in a single player
	public static void dbSaveData(PlayerStats player) {
		if (Utils.DEBUG)
			System.out.print("Client ID: " + player.getUserID()); // are
																	// either
																	// of
																	// these
																	// null?
		if (Utils.DEBUG)
			System.out.println("\tClient Role: " + player.getChampion());
		Profiles oldProfile = profilesRepo.findByUserID(player.getUserID(), player.getChampion()); // need
																									// to
																									// update
																									// to
																									// specific
																									// roles
		int kills = oldProfile.getKills() + player.getKills();
		int deaths = oldProfile.getDeaths() + player.getDeaths();
		int gamewins = oldProfile.getGamewins() + player.getWins();
		int gamelosses = oldProfile.getGamelosses() + player.getLosses();
		int gamesplayed = oldProfile.getGamesplayed() + player.getGamesplayed(); // assuming
																					// one
																					// since
																					// this
																					// will
																					// be
																					// called
																					// after
																					// every
																					// game
																					// that
																					// that
																					// class
																					// is
																					// chosen
		int flaggrabs = oldProfile.getFlaggrabs() + player.getFlag_grabs();
		int flagreturns = oldProfile.getFlagreturns() + player.getFlag_returns();
		int flagcaptures = oldProfile.getFlagcaptures() + player.getFlag_captures();
		int experience = player.getExperience();
		int level = player.getLevel();

		Profiles profile;

		// create a new profile
		if (level >= 5) {
			profile = new Profiles(player.getUserID(), player.getChampion(), kills, deaths, gamewins, gamelosses,
					gamesplayed, flaggrabs, flagreturns, flagcaptures, experience, level, 1, 1, 1);
		} else {
			profile = new Profiles(player.getUserID(), player.getChampion(), kills, deaths, gamewins, gamelosses,
					gamesplayed, flaggrabs, flagreturns, flagcaptures, experience, level, 0, 0, 0);
		}
		// delete old profile from database
		profilesRepo.delete(oldProfile);

		// save new profile to database
		profilesRepo.save(profile);
	}

}
