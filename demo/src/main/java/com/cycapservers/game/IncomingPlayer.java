package com.cycapservers.game;

import org.springframework.web.socket.WebSocketSession;

public class IncomingPlayer {
	
	protected String client_id;
	protected String role;
	protected WebSocketSession session;
	
	public IncomingPlayer(String c, String r, WebSocketSession session) {
		this.client_id = c;
		this.role = r;
		this.session = session;
	}
}