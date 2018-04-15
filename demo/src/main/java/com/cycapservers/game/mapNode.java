package com.cycapservers.game;

import java.util.HashMap;

public class mapNode {

	int x;
	int y;
	short gridX;
	short gridY;
	double f = Double.POSITIVE_INFINITY;
	double g = 0.0;
	HashMap<String, mapNode> previousNodes = new HashMap<String, mapNode>();
	boolean node_trav;
	boolean corner;

	//constructs the node with given characteristics
	public mapNode(int x, int y, boolean trav, short i, short j) {
		this.x = x;
		this.y = y;
		this.node_trav = trav;
		this.gridX = i;
		this.gridY = j;
	}

	public void set_prev(String ai_id, mapNode prev){
		previousNodes.put(ai_id, prev);
	}
	
	public mapNode get_prev(String ai_id) {
		return previousNodes.get(ai_id);
	}
	
	public void print_prev() {
		// TODO not important
		// System.out.println(this.previous);
	}
	
	public String toString(){
		return "x: " + this.x + " y: " +  this.y + " trave: " + this.node_trav;
	}
	
}
