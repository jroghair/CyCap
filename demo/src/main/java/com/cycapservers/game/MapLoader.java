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
		
		if(g.getClass().equals(CaptureTheFlag.class) || g.getClass().equals(GuestCaptureTheFlag.class)) {
			loadMap0_CTFElements((CaptureTheFlag) g);
		}
		else if(g.getClass().equals(FreeForAll.class)) {
			loadMap0_FFAElements((FreeForAll) g);
		}
		else if(g.getClass().equals(TeamDeathMatch.class)) {
			loadMap0_TDMElements((TeamDeathMatch) g);
		}
	}
	
	private static void loadMap0_CTFElements(CaptureTheFlag g) {
		//make a list of nodes
		List<PowerUpNode> pu_nodes = new ArrayList<PowerUpNode>();
		pu_nodes.add(new PowerUpNode((short) 7, (short) 8));
		pu_nodes.add(new PowerUpNode((short) 13, (short) 20));
		pu_nodes.add(new PowerUpNode((short) 27, (short) 25));
		pu_nodes.add(new PowerUpNode((short) 33, (short) 6));
		pu_nodes.add(new PowerUpNode((short) 37, (short) 27));
		g.pu_handler.setNodeList(pu_nodes);
		
		g.team1_base = new GridLockedNode((short) 4, (short) 25);
		g.team2_base = new GridLockedNode((short) 38, (short) 2);
		String id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		g.team1_flag = new Flag(g.team1_base, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, id, 1);
		g.usedEntityIds.add(id);
		id = Utils.getGoodRandomString(g.usedEntityIds, g.entity_id_len);
		g.team2_flag = new Flag(g.team2_base, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1.0, id, 2);
		g.usedEntityIds.add(id);
		
		g.spawns.add(new SpawnNode((short) 3, (short) 3, 1));
		g.spawns.add(new SpawnNode((short) 4, (short) 4, 1));
		g.spawns.add(new SpawnNode((short) 5, (short) 5, 1));
		g.spawns.add(new SpawnNode((short) 38, (short) 20, 2));
		g.spawns.add(new SpawnNode((short) 37, (short) 21, 2));
		g.spawns.add(new SpawnNode((short) 38, (short) 22, 2));
	}
	
	private static void loadMap0_FFAElements(FreeForAll g) {
		//make a list of nodes
		List<PowerUpNode> pu_nodes = new ArrayList<PowerUpNode>();
		pu_nodes.add(new PowerUpNode((short) 7, (short) 8));
		pu_nodes.add(new PowerUpNode((short) 13, (short) 20));
		pu_nodes.add(new PowerUpNode((short) 27, (short) 25));
		pu_nodes.add(new PowerUpNode((short) 33, (short) 6));
		pu_nodes.add(new PowerUpNode((short) 37, (short) 27));
		g.pu_handler.setNodeList(pu_nodes);
		
		//TODO sometimes, for some reason, spawning in a corner keeps the player from moving, idk
		/*
		g.spawns.add(new SpawnNode((short) 2, (short) 16, 1));
		g.spawns.add(new SpawnNode((short) 6, (short) 20, 2));
		g.spawns.add(new SpawnNode((short) 6, (short) 28, 3));
		g.spawns.add(new SpawnNode((short) 38, (short) 1, 4));
		g.spawns.add(new SpawnNode((short) 35, (short) 4, 5));
		g.spawns.add(new SpawnNode((short) 39, (short) 10, 6));
		g.spawns.add(new SpawnNode((short) 35, (short) 4, 7));
		g.spawns.add(new SpawnNode((short) 39, (short) 10, 8));
		*/
	}
	
	private static void loadMap0_TDMElements(TeamDeathMatch g) {
		//TODO: change up the powerup and spawn nodes
		//make a list of nodes
		List<PowerUpNode> pu_nodes = new ArrayList<PowerUpNode>();
		pu_nodes.add(new PowerUpNode((short) 7, (short) 8));
		pu_nodes.add(new PowerUpNode((short) 13, (short) 20));
		pu_nodes.add(new PowerUpNode((short) 27, (short) 25));
		pu_nodes.add(new PowerUpNode((short) 33, (short) 6));
		pu_nodes.add(new PowerUpNode((short) 37, (short) 27));
		g.pu_handler.setNodeList(pu_nodes);
		
		//TODO sometimes, for some reason, spawning in a corner keeps the player from moving, idk
		g.spawns.add(new SpawnNode((short) 2, (short) 16, 1));
		g.spawns.add(new SpawnNode((short) 6, (short) 20, 1));
		g.spawns.add(new SpawnNode((short) 6, (short) 28, 1));
		g.spawns.add(new SpawnNode((short) 38, (short) 1, 2));
		g.spawns.add(new SpawnNode((short) 35, (short) 4, 2));
		g.spawns.add(new SpawnNode((short) 39, (short) 10, 2));
	}
}