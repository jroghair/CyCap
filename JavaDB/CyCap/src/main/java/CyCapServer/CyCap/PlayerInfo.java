package CyCapServer.CyCap;

import java.util.ArrayList;

public class PlayerInfo {

	private String PlayerId;
	
	private ArrayList<Entity> PlayerState = new ArrayList<Entity>();
	
	public PlayerInfo(String Id){
		this.PlayerId = Id;
	}
	
	public PlayerInfo(String Id, ArrayList<Entity> state){
		this.PlayerId = Id;
		this.PlayerState = state;
	}
	
	public String toString(){
		String s = "";
		for(int i = 0; i < PlayerState.size(); i++){
			s += PlayerState.get(i).toString();
		}
		return s;
	}
	
	public void updateGameState(ArrayList<Entity> state){
		this.PlayerState = state;
	}
	
	public String getPlayerId(){
		return PlayerId;
	}
	
}
