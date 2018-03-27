package com.cycapservers.game;

public class mapNode {

	int x;
	int y;
	short i;
	short j;
	double f = Double.POSITIVE_INFINITY;
	double g = 0.0;
	mapNode previous = null;
	boolean node_trav;
	boolean corner; //don't remember what this is even used for

	//constructs the node with given characteristics
	public mapNode(int x, int y, boolean trav, short i, short j) {
		this.x = x;
		this.y = y;
		this.node_trav = trav;
		this.i = i;
		this.j = j;
	}

	public void set_prev(mapNode prev){
		this.previous = prev;
	}
	
	public void print_prev() {
		// TODO not important
		// System.out.println(this.previous);
	}
}
