function InputHandler(){
	this.mouse = new MouseHandler();
	this.keys_down = []; //keys being pressed
	this.keys_pnr = []; //keys that have been pushed and released
	this.clientPredictiveState = []; //this is a list of all of the past input snapshots that have not been handled by the Server
	this.snapshotNum = 1;
	
	this.getSnapshot = function(){
		//compile the input information
		let output = pw + ":" + this.mouse.getData() + ":";
		output += this.keys_down.join(",") + ":";
		output += this.keys_pnr.join(",") + ":";
		
		//add on the snapshotNum, password, and frameTime(so that the movement data is correct locally and on the server
		//or maybe the frameTime can be based off of the difference in time when received at the server? idk man fuck
		output += this.snapshotNum + ":";
		output += global_delta_t;
		this.clientPredictiveState.push(output);
		
		//increment shapshotNum++
		this.snapshotNum++;
		
		//return string
		return output;
	}
	
	this.removeHandledSnapshots = function(highestSnap){
		let temp_arr;
		for(let i = 0; i < this.clientPredictiveState.length; i++){
			temp_arr = this.clientPredictiveState[i].split(":");
			if(temp_arr[9] == highestSnap){
				this.clientPredictiveState.splice(0, i+1);
				break;
			}
		}
	}
}

function MouseHandler(){

	this.canvasX = 0;
	this.canvasY = 0;
	this.mapX = 0;
	this.mapY = 0;
	this.mouse_clicked = false;
	this.lmb_down = false;

	this.update = function(){
		this.mapX = (this.canvasX - gt5) / gt1;
		this.mapY = (this.canvasY - gt6) / gt4;
	}
	
	this.getData = function(){
		let places = 5;
		return (this.mapX.toFixed(places) + ":" + this.mapY.toFixed(places) + ":" + this.canvasX.toFixed(places) + ":" + this.canvasY.toFixed(places) + ":" + this.mouse_clicked + ":" + this.lmb_down);
	}
}

function Snapshot(mapX, mapY, canvasX, canvasY){
	
	this.mapX = 0;
	this.mapY = 0;
	this.canvasX = 0;
	this.canvasY = 0;
	this.mouse_clicked = false;
	this.lmb_down = false;
	
	this.keys_down = []; //keys being pressed
	this.keys_pnr = []; //keys that have been pushed and released
	
	this.snapshotNum = 1;
}