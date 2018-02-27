//response_time is the amount of time in milliseconds you have to collide with it before it triggers
function Item(img, sprIdx, x, y, dWidth, dHeight, response_time){
	this.base = Entity;
	//100% opacity, 0 rotation
	this.base(img, sprIdx, x, y, dWidth, dHeight, 0, 1.0);
	this.reponse_time = response_time;
	this.targetPlayer = null;
	
	//grabber is the player who picked up the item
	this.pickup = function(grabber){
		this.targetPlayer = grabber;
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
	
}

function SpeedPotion(x, y){
	this.base = AnimatedItem;
	this.base(speed_potion_ss, x, y, grid_length, grid_length, 100, 8, 800);
	this.startTime;
	
	this.use = function(){
		if(this.targetPlayer == null){
			console.log("Error, no target player for item. Cannot use.");
			return;
		}
		
		
	}
}