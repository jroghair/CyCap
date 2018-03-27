package com.cycapservers.game;

import java.util.ArrayList;

public class AI_map_generator {
	
	GameState g;
	ArrayList<ArrayList<mapNode>> map = new ArrayList<ArrayList<mapNode>>(); //2d map of nodes
	
	/**
	 * constructor
	 */
	public AI_map_generator(GameState g){
		this.g = g;
	}//constructor
	
	/**
	 * Creates a map of nodes for the AI player to use in path calculations
	 * @param g is a reference to the current game state
	 * @return map a 2d array list of nodes
	 */
	public  ArrayList<ArrayList<mapNode>> generate_node_array(GameState g){ //passing GameState so it doesn't have to be static
		short index_i = 0;
		short index_j = 0;
		for(int i = 0;i < (Utils.GRID_LENGTH * g.mapGridWidth);i += g.node_pixel_dist){
			ArrayList<mapNode> node_col = new ArrayList<mapNode>();
			for(int j = 0;j < (Utils.GRID_LENGTH * g.mapGridHeight );j += g.node_pixel_dist){
				Entity test_player_ent = new Entity(0, 0, i, j, Utils.GRID_LENGTH, Utils.GRID_LENGTH, 0, 1);//used for traversing
				boolean traversable = true;
				for(int t = 0;t < g.walls.size();t++){
					if(Utils.isColliding(test_player_ent, g.walls.get(t))){
						traversable = false;
						break; //colliding with one wall is enough to know this isn't a possible space
					}
				}
				node_col.add(new mapNode(i, j, traversable, index_i, index_j));
				//System.out.println("added new node i: " + index_i + " j: " + index_j);
				index_j++;
			}
			map.add(node_col);
			index_i++;
			index_j = 0;
		}
		return map;
	}
	
	
	
}