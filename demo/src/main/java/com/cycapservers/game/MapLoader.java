package com.cycapservers.game;

import java.util.ArrayList;
import java.util.List;

public final class MapLoader {
	
	private MapLoader() {} //so it can't be constructed
	
	public static void loadPredefinedMap(int num, GameState g) {
		switch(num) {
			case 0:
				loadMap0(g);
				break;
			default:
				throw new IllegalArgumentException("Illegal map number. No such predefined map");
		}
	}
	
	private static void loadMap0(GameState g) {
		Utils.generateWallLine(g.walls, 5, 10, 5, 'x');  //done
		Utils.generateWallLine(g.walls, 9, 3, 7, 'y');   //done
		Utils.generateWallLine(g.walls, 12, 3, 15, 'x'); //done
		Utils.generateWallLine(g.walls, 12, 4, 4, 'y');  //done
		Utils.generateWallLine(g.walls, 12, 10, 8, 'x'); //done
		Utils.generateWallLine(g.walls, 20, 4, 8, 'y');  //done
		Utils.generateWallLine(g.walls, 21, 7, 3, 'x');  //done
		Utils.generateWallLine(g.walls, 26, 4, 4, 'y');  //done
		Utils.generateWallLine(g.walls, 29, 1, 2, 'y');  //done
		Utils.generateWallLine(g.walls, 29, 5, 2, 'y');  //done
		Utils.generateWallLine(g.walls, 29, 7, 2, 'x');  //done
		Utils.generateWallLine(g.walls, 31, 7, 19, 'y'); //done
		Utils.generateWallLine(g.walls, 34, 1, 8, 'y');  //done
		Utils.generateWallLine(g.walls, 38, 5, 2, 'x');  //done
		Utils.generateWallLine(g.walls, 32, 9, 6, 'x');  //done
		Utils.generateWallLine(g.walls, 34, 12, 6, 'x'); //done
		Utils.generateWallLine(g.walls, 34, 13, 4, 'y'); //done
		Utils.generateWallLine(g.walls, 34, 19, 10, 'y');//done
		Utils.generateWallLine(g.walls, 24, 10, 5, 'x'); //done
		Utils.generateWallLine(g.walls, 23, 10, 16, 'y');//done
		Utils.generateWallLine(g.walls, 28, 14, 3, 'x'); //done
		Utils.generateWallLine(g.walls, 26, 17, 3, 'x'); //done
		Utils.generateWallLine(g.walls, 26, 20, 4, 'y'); //done
		Utils.generateWallLine(g.walls, 27, 23, 4, 'x'); //done
		Utils.generateWallLine(g.walls, 23, 26, 9, 'x'); //done
		Utils.generateWallLine(g.walls, 20, 14, 13, 'y');//done
		Utils.generateWallLine(g.walls, 1, 15, 19, 'x'); //done
		Utils.generateWallLine(g.walls, 1, 18, 2, 'x');  //done
		Utils.generateWallLine(g.walls, 5, 18, 3, 'x');  //done
		Utils.generateWallLine(g.walls, 8, 18, 11, 'y'); //done
		Utils.generateWallLine(g.walls, 11, 18, 8, 'y'); //done
		Utils.generateWallLine(g.walls, 12, 18, 6, 'x'); //done
		Utils.generateWallLine(g.walls, 3, 24, 2, 'y');  //done
		Utils.generateWallLine(g.walls, 3, 26, 3, 'x');  //done
		Utils.generateWallLine(g.walls, 11, 26, 5, 'x'); //done
		Utils.generateWallLine(g.walls, 18, 26, 2, 'x'); //done
		Utils.placeBorder(g.walls, 41, 30, 0, 0, true);
		g.mapGridHeight = 30;
		g.mapGridWidth = 41;
		//make a list of nodes
		List<PowerUpNode> pu_nodes = new ArrayList<PowerUpNode>();
		pu_nodes.add(new PowerUpNode((short) 7, (short) 8));
		pu_nodes.add(new PowerUpNode((short) 13, (short) 20));
		pu_nodes.add(new PowerUpNode((short) 27, (short) 25));
		pu_nodes.add(new PowerUpNode((short) 33, (short) 6));
		pu_nodes.add(new PowerUpNode((short) 37, (short) 27));
		g.pu_handler.setNodeList(pu_nodes);
	}

}
