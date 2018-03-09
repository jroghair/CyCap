function InputHandler(){
	
	this.canvasX = 0;
	this.canvasY = 0;
	this.mapX = 0;
	this.mapY = 0;
	this.mouse_clicked = false;
	this.lmb_down = false;
	
	this.keys_down = []; //keys being pressed
	this.keys_pnr = []; //keys that have been pushed and released
	this.clientPredictiveState = []; //this is a list of all of the past input snapshots that have not been handled by the Server
	this.snapshotNum = 1;
	
	this.update = function(){
		this.mapX = (this.canvasX - gt5) / gt1;
		this.mapY = (this.canvasY - gt6) / gt4;
	}
	
	this.getSnapshot = function(){
		//compile input data
		let snap = new InputSnapshot(this.mapX, this.mapY, this.canvasX, this.canvasY, this.mouse_clicked, this.lmb_down, this.keys_down, this.keys_pnr, this.snapshotNum);
		
		//push the snapshot to the list
		this.clientPredictiveState.push(snap);
		
		//increment shapshotNum++
		this.snapshotNum++;
		
		//return string
		return snap.getData();
	}
	
	this.getMostRecentInput = function(){
		let pos = this.clientPredictiveState.length - 1;
		return this.clientPredictiveState[pos];
	}
	
	//this takes in the highest snapshot number handled by the server and removes everything before that from the prediction
	this.removeHandledSnapshots = function(highestSnap){
		for(let i = 0; i < this.clientPredictiveState.length; i++){
			if(this.clientPredictiveState[i].snapshotNum == highestSnap){
				this.clientPredictiveState.splice(0, i+1);
				break;
			}
		}
	}
}

function InputSnapshot(mapX, mapY, canvasX, canvasY, mouse_clicked, lmb_down, keys_down, keys_pnr, num){
	
	this.mapX = mapX;
	this.mapY = mapY;
	this.canvasX = canvasX;
	this.canvasY = canvasY;
	this.mouse_clicked = mouse_clicked;
	this.lmb_down = lmb_down;
	
	this.keys_down = keys_down; //keys being pressed
	this.keys_pnr = keys_pnr; //keys that have been pushed and released
	
	this.snapshotNum = num;
	this.frameTime = global_delta_t; //the amount of time associated with this frame
	
	this.getData = function(){
		//compile the input information
		
		//password
		let output = pw + ":";
		
		//mous data
		output += this.mapX.toFixed(5) + ":";
		output += this.mapY.toFixed(5) + ":";
		output += this.canvasX.toFixed(5) + ":";
		output += this.canvasY.toFixed(5) + ":";
		output += this.mouse_clicked + ":";
		output += this.lmb_down + ":";
		
		//key data
		output += this.keys_down.join(",") + ":";
		output += this.keys_pnr.join(",") + ":";
		
		//add on the snapshotNum, password, and frameTime(so that the movement data is correct locally and on the server
		//or maybe the frameTime can be based off of the difference in time when received at the server? idk man fuck
		output += this.snapshotNum + ":";
		output += this.frameTime;
		
		//return string
		return output;
	}
}