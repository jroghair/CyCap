package CyCapServer.CyCap;

import java.util.ArrayList;
import java.util.Scanner;

public class MessageParsing {

	ArrayList<Entity> GameState = new ArrayList<Entity>();
	
	//Might need to take in gamestate so it can be updated.
	public void reciveMessage(String message){
		int before = 0;
		int after = 0;
		int i = 0;
		while(true){
			if(i < message.length()){
				ArrayList<String> s = new ArrayList<String>();
				while(message.charAt(i) != ':'){
					before = i;
					while(message.charAt(i) != ','){
						
					}
					after = i - 1;
					s.add(message.substring(before, after));
					i++;
				}
				if(s.size() == 7){
					Entity temp = new Entity(Double.parseDouble(s.get(0)),
							Double.parseDouble(s.get(1)),
									Double.parseDouble(s.get(2)),
											Double.parseDouble(s.get(3)),
													Double.parseDouble(s.get(4)),
															Double.parseDouble(s.get(5)),
															Double.parseDouble(s.get(6)));
					GameState.add(temp);
				}
				else if(s.size() == 8){
					Player temp = new Player(Double.parseDouble(s.get(0)),
							Double.parseDouble(s.get(1)),
							Double.parseDouble(s.get(2)),
									Double.parseDouble(s.get(3)),
											Double.parseDouble(s.get(4)),
													Double.parseDouble(s.get(5)),
													Double.parseDouble(s.get(6)),
													s.get(7));
					GameState.add(temp);
				}
				i++;
			}
		}
		
	}
}
