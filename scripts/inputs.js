function InputHandler(){
	this.mouse = new MouseHandler();
	this.keys_down = []; //keys being pressed
	this.keys_pnr = []; //keys that have been pushed and released
	this.clientPredictiveState = []; //this is a list of all of the past input snapshots that have not been handled by the Server
	this.snapshotNum = 1;
	
	this.getSnapshot = function(){
		//compile the input information
		//add on the snapshotNum
		//increment shapshotNum++
		//return string
	}
}

function MouseHandler(){

	this.x_pos_rel_canvas = 0;
	this.y_pos_rel_canvas = 0;
	this.mapX = 0;
	this.mapY = 0;
	this.mouse_clicked = false;
	//this.lmb_down = false;

	this.update = function(){
		this.mapX = (this.x_pos_rel_canvas - gt5) / gt1;
		this.mapY = (this.y_pos_rel_canvas - gt6) / gt4;
	}
}