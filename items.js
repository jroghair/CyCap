//response_time is the amount of time in milliseconds you have to collide with it before it triggers
function Item(img, sprIdx, x, y, dWidth, dHeight, response_time){
	this.base = Entity;
	//100% opacity, 0 rotation
	this.base(img, sprIdx, x, y, dWidth, dHeight, 0, 1.0);
	this.reponse_time = response_time; //not sure if this is going to be used right now
	this.targetPlayer = null;
	this.grabbed = false;
	
	//grabber is the player who picked up the item
	this.pickup = function(grabber){
		if(!this.grabbed){
			this.targetPlayer = grabber;
			this.grabbed = true;
		}
	}
	
	this.use = function(){
		return; //base item does nothing on use
	}
}

function Flag(x, y, color){
	this.base = Item;
	this.base(flags_ss, color, x, y, grid_length, grid_length, 100);
	
	//grabber is the player who picked up the item
	this.pickup = function(grabber){
		if(!this.grabbed){
			this.targetPlayer = grabber;
			this.grabbed = true;
			this.dWidth = 16;
			this.dHeight = 16;
		}
	}
	
	this.update = function(){
		if(this.grabbed){
			this.x = this.targetPlayer.x;
			this.y = this.targetPlayer.y;
		}
	}
	
	this.use = function(){
		if(this.targetPlayer == null){
			console.log("Error, no target player for item. Cannot use.");
			return;
		}
		else{
			this.targetPlayer = null;
			this.grabbed = false;
			this.dWidth = 32;
			this.dHeight = 32;
		}
		
	}
}

//An animatedItem will cycle through its spritesheet when it runs out
//cycle_time is in milliseconds
function AnimatedItem(img, x, y, dWidth, dHeight, response_time, num_of_frames, cycle_time){
	this.lastFrame = Date.now();
	this.num_of_frames = num_of_frames;
	this.frame_speed = cycle_time/this.num_of_frames; //this is in ms
	this.base = Item;
	this.base(img, 0, x, y, dWidth, dHeight, response_time); //we will start at sprite index 0
	
	this.update = function(){
		this.updateFrame();
	}
	
	this.updateFrame = function(){
		if((Date.now() - this.lastFrame) > this.frame_speed){
			this.lastFrame = Date.now();
			this.sprIdx += 1;
			if(this.sprIdx == this.num_of_frames){
				this.sprIdx = 0; //cycle back to 0 if we reach the end
			}
		}
	}
}

function PowerUpHandler(){
	//how often a power up spawns, weights for which power ups are going to spawn, 
	this.power_ups = [];
	this.power_ups.push(new SpeedPotion(256, 256));
	
	this.updateItems = function(){
		for(let i = 0; i < this.power_ups.length; i++){
			this.power_ups[i].update();
		}
	}
	
	this.drawItems = function(){
		for(let i = 0; i < this.power_ups.length; i++){
			if(!this.power_ups[i].grabbed){
				this.power_ups[i].draw();
			}
		}
	}
	
}

function SpeedPotion(x, y){
	this.base = AnimatedItem;
	this.base(speed_potion_ss, x, y, grid_length, grid_length, 100, 8, 800);
	this.startTime;
	this.boost_amt = 2.0;
	this.duration = 10000; //in milliseconds
	
	this.update = function(){
		this.updateFrame();
		if(this.startTime != undefined && ((Date.now() - this.startTime) > this.duration)){
			this.targetPlayer.speed_boost /= this.boost_amt;
			let temp_index = power_handler.power_ups.indexOf(this);
			power_handler.power_ups.splice(temp_index, 1);
		}
	}
	
	this.use = function(){
		if(this.targetPlayer == null){
			console.log("Error, no target player for item. Cannot use.");
			return;
		}
		else{
			this.targetPlayer.speed_boost *= this.boost_amt;
			this.startTime = Date.now();
		}
		
	}
}