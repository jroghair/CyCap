package com.cycapservers.game;

import java.util.ArrayList;
import java.util.List;

public class InputSnapshot {
	
	protected String password;
	protected Player client;
	
	protected double mapX;
	protected double mapY;
	protected double canvasX;
	protected double canvasY;
	protected boolean mouse_clicked;
	protected boolean lmb_down;
	
	protected List<Integer> keys_down;
	protected List<Integer> keys_pnr;
	
	protected int snapshotNum;
	
	protected double frameTime;
	
	protected long timeStamp; //TODO: for bullets in the future
	
	public InputSnapshot(String data) {
		this.timeStamp = System.currentTimeMillis();
		String[] arr = data.split(":");
		this.password = arr[2];
		
		this.mapX = Double.parseDouble(arr[3]);
		this.mapY = Double.parseDouble(arr[4]);
		this.canvasX = Double.parseDouble(arr[5]);
		this.canvasY = Double.parseDouble(arr[6]);
		this.mouse_clicked = Boolean.parseBoolean(arr[7]);
		this.lmb_down = Boolean.parseBoolean(arr[8]);
		
		this.keys_down = new ArrayList<Integer>();
		String[] temp = arr[9].split(",");
		if(!temp[0].equals("")) {
			for(int i = 0; i < temp.length; i++) {
				this.keys_down.add(Integer.parseInt(temp[i]));
			}
		}
		
		this.keys_pnr = new ArrayList<Integer>();
		temp = arr[10].split(",");
		if(!temp[0].equals("")) {
			for(int i = 0; i < temp.length; i++) {
				this.keys_pnr.add(Integer.parseInt(temp[i]));
			}
		}
		
		this.snapshotNum = Integer.parseInt(arr[11]);
		this.frameTime = Double.parseDouble(arr[12]);
	}
	
	public void setClient(Player p) {
		this.client = p;
	}
}