package com.cycapservers.game;

/**
 * 
 * @author ted
 * A subClass for GameState That will add additional features for capture the flag game mode. 
 *
 */
public class Capture extends GameState {

	/**
	 * The Basic constructor for the capture the flag game type.
	 */
	public Capture(){
		super();
		this.maxPlayers = 8;
	}
}
