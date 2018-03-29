package com.cycapservers.game;

public class Node {
	
	private double x;
	private double y;
	
	public Node(Node n) {
		this.x = n.x;
		this.y = n.y;
	}
	
	public Node(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}