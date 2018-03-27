package com.cycapservers.game;

import java.awt.Point;
import java.util.ArrayList;

public class AI_path_generator {

	private AI_player ai;
	private GameState g;
	protected ArrayList<mapNode> closed_list;
	protected ArrayList<mapNode> open_list;

	public AI_path_generator(AI_player ai, GameState g) {
		this.ai = ai;
		this.g = g;
		this.open_list = new ArrayList<mapNode>();
		this.closed_list = new ArrayList<mapNode>();
	}

	/**
	 * unifying path finding function
	 * 
	 * @param start
	 *            starting entity (the one that moves)
	 * @param end
	 *            ending entity
	 * @return
	 * @throws Exception
	 */
	public ArrayList<mapNode> get_a_star_path(Entity start, Entity end) {
		long timer = System.currentTimeMillis();
		Point moving_point = ai.AI_util.get_nearest_node(start);
		System.out.println("Amount to find start node: " + (System.currentTimeMillis() - timer));
		timer = System.currentTimeMillis();
		Point ending_point = ai.AI_util.get_nearest_node(end);
		System.out.println("Amount to find end node: " + (System.currentTimeMillis() - timer));
		return A_Star_Path(moving_point, ending_point);
	}

	/**
	 * generates a path using A*
	 * 
	 * @param a
	 *            starting point
	 * @param b
	 *            ending point
	 * @throws Exception
	 */
	private ArrayList<mapNode> A_Star_Path(Point a, Point b) {
		long timer  = System.currentTimeMillis();
		closed_list.clear();
		open_list.clear();
		mapNode start_node = ai.map.get(a.x).get(a.y);
		mapNode goal_node = ai.map.get(b.x).get(b.y);
		mapNode current_node;

		start_node.g = 0;
		open_list.add(start_node);
		while (open_list.size() != 0) {
			current_node = get_lowest_f(open_list);
			if (current_node == goal_node) {
				System.out.println("time to find path: " + (System.currentTimeMillis() - timer));
				return construct_path(current_node);
			}
			open_list.remove(get_node_index(open_list, current_node));
			closed_list.add(current_node);
			ArrayList<mapNode> neighbors = ai.AI_util.get_neighbors(current_node, closed_list, open_list);
			for (int i = 0; i < neighbors.size(); i++) {
				mapNode neighbor = neighbors.get(i);
				if (closed_list.contains(neighbor)) {
					continue;
				}
				double temp_g_score = 0.0;
				if (neighbor.corner == true) {
					temp_g_score = current_node.g + (1.414 * g.node_pixel_dist);
				} else {
					temp_g_score = current_node.g + (1.0 * g.node_pixel_dist);
				}
				if (open_list.contains(neighbor) == false) {
					open_list.add(neighbor);
				} else if (temp_g_score >= neighbor.g) {
					continue;
				}
				neighbor.previous = current_node;
				neighbor.g = temp_g_score;
				neighbor.f = neighbor.g + heuristic(neighbor, goal_node);
			}
		}
		//it couldn't find a path
		System.out.println("couldn't find path");
		return null;
	}

	/**
	 * gets node with lowest f value from list
	 * 
	 * @param a
	 *            list of nodes
	 * @return node with lowest f
	 */
	private mapNode get_lowest_f(ArrayList<mapNode> a) {
		mapNode lowest = a.get(0);
		for (int i = 0; i < a.size(); i++) {
			try {
				if (a.get(i).f < lowest.f) {
					lowest = a.get(i);
				}
			} catch (Exception e) {
				return a.get(0);
			}
		}
		return lowest;
	}

	private int get_node_index(ArrayList<mapNode> a, mapNode b) {
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i) == b) {
				return i;
			}
		}
		System.out.println("get node index issue");
		return 0;
	}

	/**
	 * estimated cost to get to goal from current node
	 * 
	 * @param one
	 *            first node
	 * @param two
	 *            second node
	 * @return calculated cost which in this case is just the distance
	 */
	private double heuristic(mapNode one, mapNode two) {
		return Math.sqrt(Math.pow((one.x - two.x), 2) + Math.pow((one.y - two.y), 2));
	}

	private ArrayList<mapNode> construct_path(mapNode cur) {
		long timer  = System.currentTimeMillis();
		ArrayList<mapNode> path = new ArrayList<mapNode>();
		mapNode temp = cur;
		path.add(temp);
		while (temp.previous != null) {
			path.add(temp.previous);
			// reseting the reference for the next path
			temp.previous = null;
			// get next node in path
			temp = path.get(path.size() - 1);
		}
		System.out.println("time to construct path: " + (System.currentTimeMillis() - timer));
		return path;
	}

}
