package CyCapServer.CyCap;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;


public class GameManager {

	//private volatile ArrayList<Game> games = new ArrayList<Game>();
	
	private volatile ArrayList<BasicPlayer> player = new ArrayList<BasicPlayer>();
	
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
					this.player.get(i).updateX(Double.parseDouble(s[1]));
					this.player.get(i).updateY(Double.parseDouble(s[2]));
				}		
			}
		}
		/*
		while(i < message.length()){
			before = i;
			while(message.charAt(i) != ',' && message.charAt(i) != ':'){
				i++;
			}
			after = i - 1;
			i++;
			if(before == 0){
				if(message.substring(before, after).equals("start")){
					before = i;
					while(message.charAt(i) != ','){
						i++;
					}
					after = i - 1;
					i++;
					this.player.add(new BasicPlayer(message.substring(before, after)));
				}
				else{
					for(int j = 0; j < this.player.size(); j++){
						if(this.player.get(j).getName().equals(message.substring(before, after))){
							player = j;
						}
					}
				}
			else if(player == 1){
				if(message.charAt(after + 1) == ':'){
					Player1Y = Integer.parseInt(message.substring(before, after));
				}
				else{
					Player1X = Integer.parseInt(message.substring(before, after));
				}
			}
			else if(player == 2){
				if(message.charAt(after + 1) == ':'){
					Player2Y = Integer.parseInt(message.substring(before, after));
				}
				else{
					Player2X = Integer.parseInt(message.substring(before, after));
				}
			}
		}
		*/
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
