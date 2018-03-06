public final class Utils{
	public final static float GRAVITY = 9.81;
	
	private Utils(){} //prevents the class from being constructed
	
	public static boolean isBetween(int num, int lower, int upper){
		if(num >= lower && num <= upper){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static double distanceBetweenEntities(Entity ent1, Entity ent2){
		return Math.sqrt(Math.pow(ent1.getX() - ent2.getX(), 2) + Math.pow(ent1.getY() - ent2.getY(), 2));
	}
	
	public static boolean isColliding(Entity ent_1, Entity ent_2){
		//TODO: fix the entity variable references
		//QUICK COLLISION DETECTION
		if(distanceBetweenEntities(ent_1, ent_2) >= (ent_1.collision_radius + ent_2.collision_radius)){
			return false;
		}
		
		//ADVANCED COLLISION DETECTION
		var y_collision = isBetween(ent_1.y - (ent_1.dHeight/2), ent_2.y - (ent_2.dHeight/2), ent_2.y + (ent_2.dHeight/2)) || isBetween(ent_1.y + (ent_1.dHeight/2), ent_2.y - (ent_2.dHeight/2), ent_2.y + (ent_2.dHeight/2)) || isBetween(ent_1.y, ent_2.y - (ent_2.dHeight/2), ent_2.y + (ent_2.dHeight/2));

		if(isBetween(ent_1.x - (ent_1.dWidth/2), ent_2.x - (ent_2.dWidth/2), ent_2.x + (ent_2.dWidth/2)) && y_collision){
			return true;
		}
		else if(isBetween(ent_1.x + (ent_1.dWidth/2), ent_2.x - (ent_2.dWidth/2), ent_2.x + (ent_2.dWidth/2)) && y_collision){
			return true;
		}
		else if(isBetween(ent_1.x, ent_2.x - (ent_2.dWidth/2), ent_2.x + (ent_2.dWidth/2)) && y_collision){
			return true;
		}
		else{
			return false;
		}
		
		/* This was the older method of collision detection. it is simpler and could still be used for more basic detection
		if (isBetween(ent_1.x, (ent_2.x -  (ent_2.dWidth/2)), (ent_2.x +  (ent_2.dWidth/2)))
		 && isBetween(ent_1.y, (ent_2.y -  (ent_2.dHeight/2)), (ent_2.y +  (ent_2.dHeight/2)))){
			return true;
		}
		else{
			return false;
		}
		*/
	}	
	
}