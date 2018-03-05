package CyCapServer.CyCap;

public class BasicPlayer {

	private String PlayerName;
	
	private double x;
	
	private double y;
	
	public BasicPlayer(String PlayerName){
		this.PlayerName = PlayerName;
	}
	
	public void updateX(double x){
		this.x = x;
	}
	
	public String getName(){
		return PlayerName;
	}
	
	public void updateY(double y){
		this.y = y;
	}
	
	public String toString(){
		return PlayerName + "," + x + "," + y;
	}
}
