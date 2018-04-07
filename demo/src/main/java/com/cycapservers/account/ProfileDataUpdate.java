package com.cycapservers.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cycapservers.game.PlayerStats;

@Controller
public class ProfileDataUpdate {
	//List<PlayerStats> players; 
	
    @Autowired
    private ProfilesRepository profilesRepository;
    
	public ProfileDataUpdate(){
		
	}
	
	public int dbGetLevel(String userID, String champion){
		Profiles profile = profilesRepository.findByUserIDInfantry(userID /*, champion*/); //need to add in once data pull is modified
		int level = profile.getExperience()/100;
		return level; 
	}
	
	public void dbSaveData(List<PlayerStats> players){
		
		for(PlayerStats x: players){
			Profiles oldProfile = profilesRepository.findByUserIDInfantry(x.getUserID() /*, x.getChampion()*/); //need to update to specific roles
			int kills = oldProfile.getKills() + x.getKills(); 
			int deaths = oldProfile.getDeaths() + x.getDeaths();
			int gamewins = oldProfile.getGameWins() + x.getWins();
			int gamelosses = oldProfile.getGameLosses() + x.getLosses();
			int gamesplayed = oldProfile.getGamesplayed() + 1; //assuming one since this will be called after every game that that class is chosen
			int flaggrabs = oldProfile.getFlaggrabs() + x.getFlag_grabs();
			int flagreturns = oldProfile.getFlagreturns() + x.getFlag_returns(); 
			int flagcaptures = oldProfile.getFlagcaptures() + x.getFlag_captures();
			int experience = oldProfile.getExperience() + x.getExperience();
			
			//create a new profile 
			Profiles profile = new Profiles(x.getUserID(), x.getChampion(), kills, deaths, gamewins, gamelosses, gamesplayed, 
											flaggrabs, flagreturns, flagcaptures, experience);
			
			//delete old profile from database
			profilesRepository.delete(oldProfile);
			
			//save new profile to database
			profilesRepository.save(profile);
		}
		
	}
}
