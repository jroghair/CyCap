package com.cycapservers.game;

public class BasicPlayer {

	private String PlayerName;
	
	private double x;
	
	private double y;
	
	private long time;
	
	private String passCode;
	
	
	public BasicPlayer(String PlayerName){
		this.PlayerName = PlayerName;
	}
	
	public void setPassCode(String s){
		this.passCode = s;
	}
	
	public String getPassCode(){
		return this.passCode;
	}
	
	public boolean verifyPassCode(String s){
		if(s.equals(this.passCode)){
			return true;
		}
		return false;
	}
	
	public void updateX(double x){
		this.x = x;
	}
	
	public void updateXY(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public String getName(){
		return PlayerName;
	}
	
	public long getTime(){
		return time;
	}
	
	public void setTime(long time){
		this.time = time;
	}
	
	public void updateY(double y){
		this.y = y;
	}
	
	
	public String toString(){
		return PlayerName + "," + x + "," + y;
	}
}
