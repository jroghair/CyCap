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
		Utils.generateWallLine(g, 5, 10, 5, 'x');  //done
		Utils.generateWallLine(g, 9, 3, 7, 'y');   //done
		Utils.generateWallLine(g, 12, 3, 15, 'x'); //done
		Utils.generateWallLine(g, 12, 4, 4, 'y');  //done
		Utils.generateWallLine(g, 12, 10, 8, 'x'); //done
		Utils.generateWallLine(g, 20, 4, 8, 'y');  //done
		Utils.generateWallLine(g, 21, 7, 3, 'x');  //done
		Utils.generateWallLine(g, 26, 4, 4, 'y');  //done
		Utils.generateWallLine(g, 29, 1, 2, 'y');  //done
		Utils.generateWallLine(g, 29, 5, 2, 'y');  //done
		Utils.generateWallLine(g, 29, 7, 2, 'x');  //done
		Utils.generateWallLine(g, 31, 7, 19, 'y'); //done
		Utils.generateWallLine(g, 34, 1, 8, 'y');  //done
		Utils.generateWallLine(g, 38, 5, 2, 'x');  //done
		Utils.generateWallLine(g, 32, 9, 6, 'x');  //done
		Utils.generateWallLine(g, 34, 12, 6, 'x'); //done
		Utils.generateWallLine(g, 34, 13, 4, 'y'); //done
		Utils.generateWallLine(g, 34, 19, 10, 'y');//done
		Utils.generateWallLine(g, 24, 10, 5, 'x'); //done
		Utils.generateWallLine(g, 23, 10, 16, 'y');//done
		Utils.generateWallLine(g, 28, 14, 3, 'x'); //done
		Utils.generateWallLine(g, 26, 17, 3, 'x'); //done
		Utils.generateWallLine(g, 26, 20, 4, 'y'); //done
		Utils.generateWallLine(g, 27, 23, 4, 'x'); //done
		Utils.generateWallLine(g, 23, 26, 9, 'x'); //done
		Utils.generateWallLine(g, 20, 14, 13, 'y');//done
		Utils.generateWallLine(g, 1, 15, 19, 'x'); //done
		Utils.generateWallLine(g, 1, 18, 2, 'x');  //done
		Utils.generateWallLine(g, 5, 18, 3, 'x');  //done
		Utils.generateWallLine(g, 8, 18, 11, 'y'); //done
		Utils.generateWallLine(g, 11, 18, 8, 'y'); //done
		Utils.generateWallLine(g, 12, 18, 6, 'x'); //done
		Utils.generateWallLine(g, 3, 24, 2, 'y');  //done
		Utils.generateWallLine(g, 3, 26, 3, 'x');  //done
		Utils.generateWallLine(g, 11, 26, 5, 'x'); //done
		Utils.generateWallLine(g, 18, 26, 2, 'x'); //done
		Utils.placeBorder(g, 41, 30, 0, 0, true);
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
		
		g.team1_base = new GridLockedNode((short) 6, (short) 24);
		g.team2_base = new GridLockedNode((short) 38, (short) 3);
		String id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		g.team1_flag = new Flag(g.team1_base, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, id, 1);
		g.usedEntityIds.add(id);
		id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		g.team2_flag = new Flag(g.team2_base, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, id, 2);
		g.usedEntityIds.add(id);
		
		//TODO sometimes, for some reason, spawning in a corner keeps the player from moving, idk
		g.spawns.add(new SpawnNode((short) 3, (short) 3, 1));
		g.spawns.add(new SpawnNode((short) 4, (short) 4, 1));
		g.spawns.add(new SpawnNode((short) 5, (short) 5, 1));
		g.spawns.add(new SpawnNode((short) 4, (short) 5, 2));
		g.spawns.add(new SpawnNode((short) 5, (short) 4, 2));
		g.spawns.add(new SpawnNode((short) 6, (short) 6, 2));
		
//		for(SpawnNode s : g.spawns){
//			System.out.println("added node x: " + s.getX() + " node y: " + s.getY());
//		}
	}

}
