package com.cycapservers.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


public class GameManager {
	
	private volatile ArrayList<GameState> games;
	protected volatile List<String> game_ids;
	protected final int GAME_ID_LENGTH = 5;
	
	/**
	 * A list of all of the currently active lobbies
	 */
	private volatile ArrayList<Lobby> lobbies;
	
	//create player afk list that has the players time out after 30 seconds and get deleted
	
	private volatile ArrayList<String> removedPlayer = new ArrayList<String>();
	
	private long time = 0;
	private Timer timer;
	
	private boolean afkPlayers;
	
	static final int TOLERABLE_UPDATE_ERROR = 10; //IN MS
	static final int BULLET_WARNING_LEVEL = 250;
	static final int ADVANCED_BULLET_WARNING_LEVEL = 500;
	
	//Get a method to check last message.
	
	public GameManager(){
		game_ids = new ArrayList<String>();
		timer = new Timer(true);
		games = new ArrayList<GameState>();
		lobbies = new ArrayList<Lobby>();
		String id = Utils.getGoodRandomString(game_ids, GAME_ID_LENGTH);
		games.add(new CaptureTheFlag(id, 0));
		game_ids.add(id);
		//games.get(0).setUpGame();
		timer.scheduleAtFixedRate(games.get(0), 500, 100);
	}
	
	//Ask about sending purpose of message;
	public void getMessage(WebSocketSession session, String message) throws IOException{
		boolean found = false;
		boolean erase = false;
		
		String[] arr = message.split(":");
		if(arr[0].equals("input")) {
			String game_id = arr[1];
			for(GameState g : games) {
				if(g.game_id.equals(game_id)) {
					g.addInputSnap(new InputSnapshot(message));
				}
			}
		}
		else if(arr[0].equals("join")) {
			for(GameState s: games){
				found = s.findIncomingPlayer(arr[1], session); //TODO: the client should not send role, this should be determined already
				if(found){
					break;
				}
			}
			if(!found){
				games.get(0).playerJoin(arr[1], session, "recruit"); //the player can only play as the recruit in the guest game
			}
		}
		
		else if(arr[0].equals("lobby")){
			if(arr[1].equals("playerList")){
				GivePlayerList(session,arr[2]);
			}
			else if(arr[1].equals("role")){
				for(int i  = 0; i < lobbies.size(); i++){
					if(lobbies.get(i).getId().equals(arr[2])){
						lobbies.get(i).ChangePlayerClass(session, arr[3], arr[4]);
					}
				}
			}
			else if(arr[1].equals("join")){
				boolean foundGame = false;
				if(arr[2].equals("Death")){
					for(int i = 0; i < lobbies.size(); i++){
						if(lobbies.get(i).getGame().getClass().equals(TeamDeathMatch.class)){
							if(lobbies.get(i).hasSpace()){
								erase = lobbies.get(i).addPlayer(arr[3],session);
								session.sendMessage(new TextMessage("joined:" + lobbies.get(i).getId()));
								foundGame = true;
								if(erase){
									//lobbies.remove(i);
								}
								break;
							}
						}
					}
					if(!foundGame){
						System.out.println(arr[3]);
						String id = Utils.getGoodRandomString(game_ids, GAME_ID_LENGTH);
						Lobby l = new Lobby(TeamDeathMatch.class, id);
						game_ids.add(id);
						l.addPlayer(arr[3],session);
						session.sendMessage(new TextMessage("joined:" + l.getId()));
						lobbies.add(l);
						games.add(l.getGame());
					}
				}
				else if(arr[2].equals("Capture")){
					for(int i = 0; i < lobbies.size(); i++){
						if(lobbies.get(i).getGame().getClass().equals(CaptureTheFlag.class)){
							if(lobbies.get(i).hasSpace()){
								erase = lobbies.get(i).addPlayer(arr[3], session);
								session.sendMessage(new TextMessage("joined:" + lobbies.get(i).getId()));
								foundGame = true;
								if(erase){
									lobbies.get(i).getGame().setUpGame();
									timer.scheduleAtFixedRate(lobbies.get(i).getGame(), 500, 100);
									lobbies.remove(i);
								}
								break;
							}
						}
					}
					if(!foundGame){
						String id = Utils.getGoodRandomString(game_ids, GAME_ID_LENGTH);
						Lobby l = new Lobby(CaptureTheFlag.class, id);
						game_ids.add(id);
						l.addPlayer(arr[3], session);
						session.sendMessage(new TextMessage("joined:" + l.getId()));
						lobbies.add(l);
						games.add(l.getGame());
						//timer.scheduleAtFixedRate(l.getGame(), 0, 100);
						
					}
				}
				else if(arr[2].equals("FFA")){
					for(int i = 0; i < lobbies.size(); i++){
						if(lobbies.get(i).getGame().getClass().equals(FreeForAll.class)){
							if(lobbies.get(i).hasSpace()){
								erase = lobbies.get(i).addPlayer(arr[3], session);
								session.sendMessage(new TextMessage("joined:" + lobbies.get(i).getId()));
								foundGame = true;
								if(erase){
									lobbies.remove(i);
								}
								break;
							}
						}
					}
					if(!foundGame){
						String id = Utils.getGoodRandomString(game_ids, GAME_ID_LENGTH);
						Lobby l = new Lobby(FreeForAll.class,  id);
						game_ids.add(id);
						l.addPlayer(arr[3], session);
						session.sendMessage(new TextMessage("joined:" + l.getId()));
						lobbies.add(l);
						games.add(l.getGame());
					}
				}
				else{
					//For if a game id is given.
					for(int i = 0; i < lobbies.size(); i++){
						if(lobbies.get(i).getId().equals(arr[2])){
							if(lobbies.get(i).hasSpace()){
								lobbies.get(i).addPlayer(arr[3],session);
								session.sendMessage(new TextMessage("joined:" + games.get(i).game_id));
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gives a list of all of the players in the lobby that is represented by the id. 
	 * @param session
	 * The person who requested the lists websocket session.
	 * @param id
	 * The id of the lobby that the player belongs in.
	 * @throws IOException
	 */
	private void GivePlayerList(WebSocketSession session, String id) throws IOException{
		int game = -1;
		for(int i = 0; i < lobbies.size(); i++){
			if(lobbies.get(i).getId().equals(id)){
				game = i;
				break;
			}
		}
		if(game != -1){
			lobbies.get(game).GivePlayerList(session, id);
			//session.sendMessage(new TextMessage("clean"));
			//for(int i = 0; i < lobby.get(game).getCurrentSize(); i++){
				//session.sendMessage(new TextMessage("player:"+ lobby.get(game).getPlayer(i)));
				//System.out.println(lobby.get(game).getPlayer(i));
			//}
		}
		return;
	}
	
	public boolean playerToRemove(){
		if(afkPlayers){
			afkPlayers = false;
			return true;
		}
		return false;
	}
	
	public void removePlayer(WebSocketSession session) {
		//TODO fix this for multiple games
		for(GameState g : games) {
			g.removePlayer(session);
		}
		for(Lobby lobby : lobbies) {
			/*
			lobby.removePlayer(session);
			if(lobby.getCurrentSize() == 0){
				System.out.println("timer");
				lobby.getGame().setUpGame();
				timer.scheduleAtFixedRate(lobby.getGame(), 500, 100);
				lobby.t.cancel();
				lobbies.remove(lobby);
				break;
			}
			*/
		}
	}
	
	/*
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
				this.removedPlayer.add(this.player.get(i).getName());
				this.player.remove(i);
				this.afkPlayers = true;
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
	*/
}
