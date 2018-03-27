package com.cycapservers.game;

import java.awt.Point;
import java.util.ArrayList;

public class AI_utils {

	private AI_player g;
	private GameState a;

	public AI_utils(AI_player g, GameState a) {
		this.g = g;
		this.a = a;
	}

	public Point get_nearest_node(Entity e) {
		int x = (int) (Math.ceil(e.x / a.node_pixel_dist) * a.node_pixel_dist);
		int y = (int) (Math.ceil(e.y / a.node_pixel_dist) * a.node_pixel_dist);
		short i = 0, j = 0;
		while (g.map.get(i).get(j).y != y) {
			j++;
		}
		while (g.map.get(i).get(j).x != x) {
			i++;
		}
		if (g.map.get(i).get(j).node_trav != false) {
			return new Point(i, j);
		} else {
			x = (int) (Math.floor(e.x / a.node_pixel_dist) * a.node_pixel_dist);
			y = (int) (Math.floor(e.y / a.node_pixel_dist) * a.node_pixel_dist);
			i = 0;
			j = 0;
			while (g.map.get(i).get(j).y != y) {
				j++;
			}
			while (g.map.get(i).get(j).x != x) {
				i++;
			}
			if (g.map.get(i).get(j).node_trav != false) {
				return new Point(i, j);
			} else {
				x = (int) (Math.floor(e.x / a.node_pixel_dist) * a.node_pixel_dist);
				y = (int) (Math.ceil(e.y / a.node_pixel_dist) * a.node_pixel_dist);
				i = 0;
				j = 0;
				while (g.map.get(i).get(j).y != y) {
					j++;
				}
				while (g.map.get(i).get(j).x != x) {
					i++;
				}
				if (g.map.get(i).get(j).node_trav != false) {
					return new Point(i, j);
				} else {
					x = (int) (Math.ceil(e.x / a.node_pixel_dist) * a.node_pixel_dist);
					y = (int) (Math.floor(e.y / a.node_pixel_dist) * a.node_pixel_dist);
					i = 0;
					j = 0;
					while (g.map.get(i).get(j).y != y) {
						j++;
					}
					while (g.map.get(i).get(j).x != x) {
						i++;
					}
					if (g.map.get(i).get(j).node_trav != false) {
						return new Point(i, j);
					} else {
						System.out.println("Couldn't find a traversable node near entity");
					}
				}
			}
		}
		return null;
	}

	public ArrayList<mapNode> get_neighbors(mapNode node, ArrayList<mapNode> closed_list,
			ArrayList<mapNode> open_list) {
		ArrayList<mapNode> neighbors = new ArrayList<mapNode>();
		try {
			if (g.map.get(node.i - 1).get(node.j - 1).node_trav == true) {
				neighbors.add(g.map.get(node.i - 1).get(node.j - 1));
				if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
						&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
					neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * a.node_pixel_dist);
				}
				neighbors.get(neighbors.size() - 1).corner = true;
			}
		} catch (Exception e) {
			System.out.println(node.i + " " + node.j);
		}
		if (g.map.get(node.i - 1).get(node.j).node_trav == true) {
			neighbors.add(g.map.get(node.i - 1).get(node.j));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * a.node_pixel_dist);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		if (g.map.get(node.i - 1).get(node.j + 1).node_trav == true) {
			neighbors.add(g.map.get(node.i - 1).get(node.j + 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * a.node_pixel_dist);
			}
			neighbors.get(neighbors.size() - 1).corner = true;
		}
		if (g.map.get(node.i).get(node.j + 1).node_trav == true) {
			neighbors.add(g.map.get(node.i).get(node.j + 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * a.node_pixel_dist);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		// neighbor number 5
		if (g.map.get(node.i + 1).get(node.j + 1).node_trav == true) {
			neighbors.add(g.map.get(node.i + 1).get(node.j + 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * a.node_pixel_dist);
			}
			neighbors.get(neighbors.size() - 1).corner = true;
		}
		if (g.map.get(node.i + 1).get(node.j).node_trav == true) {
			neighbors.add(g.map.get(node.i + 1).get(node.j));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * a.node_pixel_dist);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		if (g.map.get(node.i + 1).get(node.j - 1).node_trav == true) {
			neighbors.add(g.map.get(node.i + 1).get(node.j - 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.414 * a.node_pixel_dist);
			}
			neighbors.get(neighbors.size() - 1).corner = true;
		}
		if (g.map.get(node.i).get(node.j - 1).node_trav == true) {
			neighbors.add(g.map.get(node.i).get(node.j - 1));
			if (closed_list.contains(neighbors.get(neighbors.size() - 1)) == false
					&& open_list.contains(neighbors.get(neighbors.size() - 1)) == false) {
				neighbors.get(neighbors.size() - 1).g = node.g + (1.0 * a.node_pixel_dist);
			}
			neighbors.get(neighbors.size() - 1).corner = false;
		}
		return neighbors;
	}
}
