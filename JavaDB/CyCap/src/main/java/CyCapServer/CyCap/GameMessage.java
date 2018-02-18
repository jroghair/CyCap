package CyCapServer.CyCap;

import org.springframework.data.annotation.Id;

public class GameMessage {

	@Id
	private String username;
	
	private String movement;
	private String bullets;
	private String status;
	
	public GameMessage(){
		
	}
	
	public GameMessage(String movement, String bullets, String status){
		this.movement = movement;
		this.bullets = bullets;
		this.status = status;
	}
	
	public String getMovement(){
		return movement;
	}
	
	public void setMovement(String movement){
		this.movement = movement;
	}
	
	public String getBullets(){
		return bullets;
	}
	
	public void setBullets(String bullets){
		this.bullets = bullets;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	@Override 
	public String toString(){
		return "movement " + movement + 
				": bullets " + bullets +
				": status " + status;
	}
}
