package CyCapServer.CyCap;

public class Player extends Entity {
	
	private String team;
	
	public Player(){
		
	}
	
	public Player(double ImageId, double x, double y, double width, double height, double rotation, double alpha, String team){
		this.ImageId = ImageId;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.alpha = alpha;
		this.team = team;
	}
	
	@Override
	public String toString(){
		return ImageId + "," + x + "," + y + "," + width + "," + height + "," + rotation + "," + alpha + "," + team + ":";
	}
	
}
