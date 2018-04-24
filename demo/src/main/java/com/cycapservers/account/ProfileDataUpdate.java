package com.cycapservers.account;

import java.awt.Point;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cycapservers.game.PlayerStats;

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
		Profiles profile = profilesRepo.findByUserID(userID, champion);
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
		Profiles oldProfile = profilesRepo.findByUserID(player.getUserID(), player.getChampion());

		int kills = oldProfile.getKills() + player.getKills();
		int deaths = oldProfile.getDeaths() + player.getDeaths();
		int gamewins = oldProfile.getGamewins() + player.getWins();
		int gamelosses = oldProfile.getGamelosses() + player.getLosses();
		int gamesplayed = oldProfile.getGamesplayed() + player.getGamesplayed();

		int flaggrabs = oldProfile.getFlaggrabs() + player.getFlag_grabs();
		int flagreturns = oldProfile.getFlagreturns() + player.getFlag_returns();
		int flagcaptures = oldProfile.getFlagcaptures() + player.getFlag_captures();
		int experience = player.getExperience();
		int level = player.getLevel();

		Profiles profile = new Profiles();

		Profiles recruit = profilesRepo.findByUserID(player.getUserID(), "recruit");

		if (recruit.getLevel() >= 5 && recruit.getScoutunlocked() != 1) {
			Profiles scout = profilesRepo.findByUserID(player.getUserID(), "scout");
			Profiles artillery = profilesRepo.findByUserID(player.getUserID(), "artillery");
			Profiles infantry = profilesRepo.findByUserID(player.getUserID(), "infantry");

			Profiles pr = new Profiles(recruit.getUserID(), recruit.getChampion(), recruit.getKills(),
					recruit.getDeaths(), recruit.getGamewins(), recruit.getGamelosses(), recruit.getGamesplayed(),
					recruit.getFlaggrabs(), recruit.getFlagreturns(), recruit.getFlagcaptures(),
					recruit.getExperience(), recruit.getLevel(), 1, 1, 1);

			Profiles ps = new Profiles(scout.getUserID(), scout.getChampion(), scout.getKills(), scout.getDeaths(),
					scout.getGamewins(), scout.getGamelosses(), scout.getGamesplayed(), scout.getFlaggrabs(),
					scout.getFlagreturns(), scout.getFlagcaptures(), scout.getExperience(), scout.getLevel(), 1, 1, 1);

			Profiles pa = new Profiles(artillery.getUserID(), artillery.getChampion(), artillery.getKills(),
					artillery.getDeaths(), artillery.getGamewins(), artillery.getGamelosses(),
					artillery.getGamesplayed(), artillery.getFlaggrabs(), artillery.getFlagreturns(),
					artillery.getFlagcaptures(), artillery.getExperience(), artillery.getLevel(), 1, 1, 1);

			Profiles pi = new Profiles(infantry.getUserID(), infantry.getChampion(), infantry.getKills(),
					infantry.getDeaths(), infantry.getGamewins(), infantry.getGamelosses(), infantry.getGamesplayed(),
					infantry.getFlaggrabs(), infantry.getFlagreturns(), infantry.getFlagcaptures(),
					infantry.getExperience(), infantry.getLevel(), 1, 1, 1);

			profilesRepo.delete(recruit);
			profilesRepo.delete(scout);
			profilesRepo.delete(artillery);
			profilesRepo.delete(infantry);

			profilesRepo.save(pr);
			profilesRepo.save(ps);
			profilesRepo.save(pa);
			profilesRepo.save(pi);

			profile = new Profiles(player.getUserID(), player.getChampion(), kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs, flagreturns, flagcaptures, experience, level, 1, 1, 1);
		}
		else if (recruit.getLevel() < 5) {
			profile = new Profiles(player.getUserID(), player.getChampion(), kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs, flagreturns, flagcaptures, experience, level, 0, 0, 0);
		}
		else if (recruit.getLevel() > 5) {
			profile = new Profiles(player.getUserID(), player.getChampion(), kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs, flagreturns, flagcaptures, experience, level, 1, 1, 1);
		}
		else {
			profile = new Profiles(player.getUserID(), player.getChampion(), kills, deaths, gamewins, gamelosses, gamesplayed, flaggrabs, flagreturns, flagcaptures, experience, level, 1, 1, 1);
		}

		// delete old profile from database
		profilesRepo.delete(oldProfile);

		// save new profile to database
		profilesRepo.save(profile);
	}

}
