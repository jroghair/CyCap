package CyCapServer.CyCap;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;


public class GameManager {

	//private volatile ArrayList<Game> games = new ArrayList<Game>();
	
	private String Player1;
	
	private String Player2;
	
	private int Player1X;
	
	private int Player2X;
	
	private int Player1Y;
	
	private int Player2Y;
	
	public GameManager(){
		this.Player1 = null;
		this.Player2 = null;
		
	}
	
	//Ask about sending purpose of message;
	public void getMessage(String message){
		int before = 0;
		int after = 0;
		int i = 0;
		int player = 0;
		while(i < message.length()){
			before = i;
			while(message.charAt(i) != ',' && message.charAt(i) != ':'){
				i++;
			}
			after = i - 1;
			i++;
			if(before == 0){
				if(Player1 == null || Player1.equals(message.substring(before, after))){
					if(Player1 == null){
						Player1 = message.substring(before, after);
					}
					player = 1;
				}
				else if(Player2 == null || Player2.equals(message.substring(before, after))){
					if(Player2 == null){
						Player2 = message.substring(before, after);
					}
					player = 2;
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
		
	}
	
	public String toString(){
		if(Player1 != null && Player2 != null){
			return Player1 + "," + Player1X + "," + Player1Y + ":" + Player2 + "," + Player2X + "," + Player2Y + ":";
		}
		return null;
	}
	
	public void MakeGame(String ids, int playerNum){
		
	}
}
