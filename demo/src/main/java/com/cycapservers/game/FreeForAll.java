package com.cycapservers.game;

import java.util.List;

import org.springframework.web.socket.WebSocketSession;

public class FreeForAll extends GameState {
	
	public FreeForAll(String id, int map_number) {
		super(id);
	}

	@Override
	public void updateGameState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Item> getItemList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toDataString(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playerJoin(String client_id, WebSocketSession session, String role, int team) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add_AI_player(int team, String role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePlayer(WebSocketSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpGame() {
		// TODO Auto-generated method stub
		
	}

}
