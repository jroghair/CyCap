package CyCapServer.CyCap;

public class Entity {
	
	protected double ImageId;
	
	protected double x;
	
	protected double y;
	
	protected double width;
	
	protected double height;
	
	protected double rotation;
	
	protected double alpha;
	
	public Entity(){
		
	}
	
	public Entity(double d, double e, double f, double g, double h, double i, double j){
		this.ImageId = d;
		this.x = e;
		this.y = f;
		this.width = g;
		this.height = h;
		this.rotation = i;
		this.alpha = j;
	}
	
	public String toString(){
		return ImageId + "," + x + "," + y + "," + width + "," + height + "," + rotation + "," + alpha + ":";
	}
}
