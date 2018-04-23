package com.cycapservers.game;

import org.springframework.web.socket.WebSocketSession;

public class IncomingPlayer {
	
	protected String client_id;
	protected String role;
	protected WebSocketSession session;
	protected int team;
	
	public IncomingPlayer(String c, String r, WebSocketSession session, int team) {
		this.client_id = c;
		this.role = r;
		this.session = session;
		this.team = team;
	}
}