package CyCapServer.CyCap;

import java.util.ArrayList;

public class Game {
	
	private String gameId;
	
	private int maxPlayers;

	ArrayList<PlayerInfo> GameState = new ArrayList<PlayerInfo>();
	
	public Game(String gameID, String Id1, String Id2){
		this.gameId = gameID;
		GameState.add(new PlayerInfo(Id1));
		GameState.add(new PlayerInfo(Id2));
	}
	
	public void UpdatePlayer(String Id, ArrayList<Entity> e){
		for(int i = 0; i < GameState.size(); i++){
			if(GameState.get(i).getPlayerId().equals(Id)){
				GameState.get(i).updateGameState(e);
			}
		}
	}
	
	public String toString(){
		String s = "";
		for(int i = 0; i < GameState.size(); i++){
			s += GameState.get(i).toString();
		}
		return s;
	}
}
