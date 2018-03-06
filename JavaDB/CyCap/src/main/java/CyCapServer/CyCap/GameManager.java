package CyCapServer.CyCap;

import java.util.ArrayList;
import java.time.Clock;

import org.springframework.stereotype.Controller;


public class GameManager {

	//private volatile ArrayList<Game> games = new ArrayList<Game>();
	
	private volatile ArrayList<BasicPlayer> player = new ArrayList<BasicPlayer>();
	
	private long time = 0;
	
	//Get a method to check last message.
	
	public GameManager(){
		
	}
	
	//Ask about sending purpose of message;
	public void getMessage(String message){
		int i = 0;
		String[] s = message.split(",");
		if(s[0].equals("start")){
			BasicPlayer temp = new BasicPlayer(s[1]);
			temp.updateX(Double.parseDouble(s[2]));
			temp.updateY(Double.parseDouble(s[3]));
			this.player.add(temp);
		}
		else{
			for(i = 0; i < this.player.size(); i++){
				if(this.player.get(i).getName().equals(s[0])){
					this.player.get(i).updateXY(Double.parseDouble(s[1]), Double.parseDouble(s[2]));
					this.player.get(i).setTime(System.currentTimeMillis());
				}		
			}
			if((System.currentTimeMillis() - time) > 30000){
				this.getAFK();
				time = System.currentTimeMillis();
			}
		}
	}
	
	public void removePlayer(String Id){
		for(int i = 0; i < this.player.size(); i++){
			if(this.player.get(i).getName().equals(Id)){
				this.player.remove(i);
				break;
			}
		}
	}
	
	public void getAFK(){
		long time = System.currentTimeMillis();
		for(int i = 0; i < this.player.size(); i++){
			if((time - this.player.get(i).getTime()) > 5000){
				this.player.remove(i);
			}
		}
	}
	
	public long GetPlayerTime(int i){
		return this.player.get(i).getTime();
	}
	
	
	public int getPlayers(){
		return this.player.size();
	}
	
	public String playerString(int i){
		return this.player.get(i).toString();
	}
	
	public void MakeGame(String ids, int playerNum){
		
	}
}
