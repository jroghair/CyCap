package CyCapServer.CyCap;

import java.util.ArrayList;

public class oldMoves {

	private ArrayList<Double[]> pos = new ArrayList<Double[]>();
	
	private final int MaxLength = 40;
	
	public oldMoves(){
		
	}
	
	public oldMoves(double x, double y){
		Double[] e = {x,y};
		this.pos.add(e);
	}
	
	public void add(double x, double y){
		Double[] e = {x,y};
		if(pos.size() < this.MaxLength){
			pos.add(e);
		}
		else{
			pos.remove(0);
			pos.add(e);
		}
	}
	
	public Double[] getXYat(int index){
		return pos.get(index);
	}
	
	public int length(){
		return pos.size();
	}
	
	public void remove(int index){
		if(index >= pos.size() || index < 0){
			return;
		}
		pos.remove(index);
	}
	
	public void removeAllAfter(int index){
		if(index >= pos.size() || index < 0){
			return;
		}
		this.pos.subList(index, pos.size() - 1).clear();
	}
	
	
	
	
}
