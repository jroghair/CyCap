package com.cycapservers.game;

import java.util.List;

public final class MapLoader {
	
	private MapLoader() {} //so it can't be constructed
	
	public static void loadPredefinedMap(int num, List<Wall> walls) {
		switch(num) {
			case 0:
				loadMap0(walls);
				break;
			default:
				throw new IllegalArgumentException("Illegal map number. No such predefined map");
		}
	}
	
	private static void loadMap0(List<Wall> walls) {
		Utils.generateWallLine(walls, 5, 10, 5, 'x');  //done
		Utils.generateWallLine(walls, 9, 3, 7, 'y');   //done
		Utils.generateWallLine(walls, 12, 3, 15, 'x'); //done
		Utils.generateWallLine(walls, 12, 4, 4, 'y');  //done
		Utils.generateWallLine(walls, 12, 10, 8, 'x'); //done
		Utils.generateWallLine(walls, 20, 4, 8, 'y');  //done
		Utils.generateWallLine(walls, 21, 7, 3, 'x');  //done
		Utils.generateWallLine(walls, 26, 4, 4, 'y');  //done
		Utils.generateWallLine(walls, 29, 1, 2, 'y');  //done
		Utils.generateWallLine(walls, 29, 5, 2, 'y');  //done
		Utils.generateWallLine(walls, 29, 7, 2, 'x');  //done
		Utils.generateWallLine(walls, 31, 7, 19, 'y'); //done
		Utils.generateWallLine(walls, 34, 1, 8, 'y');  //done
		Utils.generateWallLine(walls, 38, 5, 2, 'x');  //done
		Utils.generateWallLine(walls, 32, 9, 6, 'x');  //done
		Utils.generateWallLine(walls, 34, 12, 6, 'x'); //done
		Utils.generateWallLine(walls, 34, 13, 4, 'y'); //done
		Utils.generateWallLine(walls, 34, 19, 10, 'y');//done
		Utils.generateWallLine(walls, 24, 10, 5, 'x'); //done
		Utils.generateWallLine(walls, 23, 10, 16, 'y');//done
		Utils.generateWallLine(walls, 28, 14, 3, 'x'); //done
		Utils.generateWallLine(walls, 26, 17, 3, 'x'); //done
		Utils.generateWallLine(walls, 26, 20, 4, 'y'); //done
		Utils.generateWallLine(walls, 27, 23, 4, 'x'); //done
		Utils.generateWallLine(walls, 23, 26, 9, 'x'); //done
		Utils.generateWallLine(walls, 20, 14, 13, 'y');//done
		Utils.generateWallLine(walls, 1, 15, 19, 'x'); //done
		Utils.generateWallLine(walls, 1, 18, 2, 'x');  //done
		Utils.generateWallLine(walls, 5, 18, 3, 'x');  //done
		Utils.generateWallLine(walls, 8, 18, 11, 'y'); //done
		Utils.generateWallLine(walls, 11, 18, 8, 'y'); //done
		Utils.generateWallLine(walls, 12, 18, 6, 'x'); //done
		Utils.generateWallLine(walls, 3, 24, 2, 'y');  //done
		Utils.generateWallLine(walls, 3, 26, 3, 'x');  //done
		Utils.generateWallLine(walls, 11, 26, 5, 'x'); //done
		Utils.generateWallLine(walls, 18, 26, 2, 'x'); //done
	}

}
