//CONSTANTS
const GRAVITY = 9.81;
const SIN_45 = Math.sin(Math.PI/4);
const SIN_30 = 0.5;
const SIN_60 = Math.sin(Math.PI/3);
const ARTILLERY_TIME = 3000; //milliseconds
const TIME_BETWEEN_SHOTS = 80; //milliseconds. this will eventually be dependent on the role of the player, essentially which weapon they are using

const CLOSE_ZOOM_LEVEL = 2.0;
const NORMAL_ZOOM_LEVEL = 1.0;
const FAR_ZOOM_LEVEL = 0.5;
const VISIBILITY = 5;
const FOG_DARKNESS = 100;
const FADE_RING_WIDTH = 40;

/*PLAYER CLASS STATS*/
//Recruit
const RECRUIT_MAX_HP = 100;
const RECRUIT_SPEED = 5;
const RECRUIT_VIS = 5;

//Artillery
const ART_MAX_HP = 100;
const ART_SPEED = 5;
const ART_VIS = 5;

//Scout
const SCOUT_MAX_HP = 100;
const SCOUT_SPEED = 5;
const SCOUT_VIS = 6;

//Tank
const TANK_MAX_HP = 150;
const TANK_SPEED = 3;
const TANK_VIS = 4;
/*PLAYER CLASS STATS END*/

let gt1, gt2, gt3, gt4, gt5, gt6; //GLOBAL TRANSFORMS
let fog_norm, fog_close, fog_far; //Fog of War image data


//draws a complete border given a grid height & height. it starts at an X & Y grid position
function placeBorder(width, height, x, y){
	wallLine(x, y, width, "x");
	wallLine(x, height + y - 1, width, "x");
	wallLine(x, y + 1, height - 2, "y");
	wallLine(width + x - 1, y + 1, height - 2, "y");
}

function getWeightedIndex(list){
	let temp = Math.random();
	let sum = 0;
	for(let i = 0; i < list.length; i++){
		sum += list[i];
		if(temp < sum){
			return i;
		}
	}
	return -1; //this should error
}

function listAverage(list){
	let sum = 0;
	for(let i = 0; i < list.length; i++){
		sum += list[i];
	}
	return (sum/list.length);
}

//takes in a starting grid coordinate, a length of the wall line, and which axis it will follow("x" or "y")
//this will travel in the positive direction of which ever axis you give it
function wallLine(start_x, start_y, length, axis){
	if(axis === "x"){
		for(var i = 0; i < length; i++){
			walls.push(new Wall(wall_image, start_x + i, start_y));
		}
	}
	else if(axis === "y"){
		for(var i = 0; i < length; i++){
			walls.push(new Wall(wall_image, start_x, start_y + i));
		}
	}
	else{
		return;
	}
}

//Draws three F.O.W. images and stores them in 3 variables for later drawing
function drawFogOfWarImages(){
	//create the image data for the three settings
	fog_norm = fog_context.createImageData(fog_canvas.width, fog_canvas.height);
	fog_close = fog_context.createImageData(fog_canvas.width, fog_canvas.height);
	fog_far = fog_context.createImageData(fog_canvas.width, fog_canvas.height);
	let x, y, dist, r_in, r_out, grad_level; //variables used in generation
	
	//draw the normal zoom image first
	for(let i = 0; i < fog_norm.data.length; i+=4){
		x = (i/4) %  fog_canvas.width;
		y = Math.floor((i/4) / fog_canvas.width);
		dist = Math.sqrt(Math.pow(x - (canvas.width/2), 2) + Math.pow(y - (canvas.height/2), 2)); //distance from the middle of the screen to this pixel
		r_in = (((grid_length * VISIBILITY) - (FADE_RING_WIDTH/2)) * NORMAL_ZOOM_LEVEL);//inner radius of fade ring
		r_out = (((grid_length * VISIBILITY) + (FADE_RING_WIDTH/2)) * NORMAL_ZOOM_LEVEL);//outer radius of fade ring
		
		if(dist > r_out){
			fog_norm.data[i+3] = FOG_DARKNESS;
		}
		else if((dist > r_in) && (dist <= r_out)){
			grad_level = ((dist - r_in) / (r_out - r_in)) * FOG_DARKNESS;
			fog_norm.data[i+3] = grad_level;
		}else{
			fog_norm.data[i+3] = 0;
		}
	}
	
	//draw the far zoom
	for(let i = 0; i < fog_far.data.length; i+=4){
		x = (i/4) %  fog_canvas.width;
		y = Math.floor((i/4) / fog_canvas.width);
		dist = Math.sqrt(Math.pow(x - (canvas.width/2), 2) + Math.pow(y - (canvas.height/2), 2)); //distance from the middle of the screen to this pixel
		r_in = (((grid_length * VISIBILITY) - (FADE_RING_WIDTH/2)) * FAR_ZOOM_LEVEL); //inner radius of fade ring
		r_out = (((grid_length * VISIBILITY) + (FADE_RING_WIDTH/2)) * FAR_ZOOM_LEVEL);//outer radius of fade ring
		
		if(dist > r_out){
			fog_far.data[i+3] = FOG_DARKNESS;
		}
		else if((dist > r_in) && (dist <= r_out)){
			grad_level = ((dist - r_in) / (r_out - r_in)) * FOG_DARKNESS;
			fog_far.data[i+3] = grad_level;
		}
		else{
			fog_far.data[i+3] = 0;
		}
	}
	
	//draw the close zoom
	for(let i = 0; i < fog_close.data.length; i+=4){
		x = (i/4) %  fog_canvas.width;
		y = Math.floor((i/4) / fog_canvas.width);
		dist = Math.sqrt(Math.pow(x - (canvas.width/2), 2) + Math.pow(y - (canvas.height/2), 2)); //distance from the middle of the screen to this pixel
		r_in = (((grid_length * VISIBILITY) - (FADE_RING_WIDTH/2)) * CLOSE_ZOOM_LEVEL);//inner radius of fade ring
		r_out = (((grid_length * VISIBILITY) + (FADE_RING_WIDTH/2)) * CLOSE_ZOOM_LEVEL);//outer radius of fade ring
		
		if(dist > r_out){
			fog_close.data[i+3] = FOG_DARKNESS;
		}
		else if((dist > r_in) && (dist <= r_out)){
			grad_level = ((dist - r_in) / (r_out - r_in)) * FOG_DARKNESS;
			fog_close.data[i+3] = grad_level;
		}
		else{
			fog_close.data[i+3] = 0;
		}
	}
}

//returns true if ent_1 is colliding with ent_2
//in the future, I want this to somehow return which walls the player is colliding with, this will help with
//allowing the player to slide along a wall while pushing into it and other smarter collision detection
function isColliding(ent_1, ent_2){
	var y_collision = isBetween(ent_1.y - (ent_1.dHeight/2), ent_2.y - (ent_2.dHeight/2), ent_2.y + (ent_2.dHeight/2)) || isBetween(ent_1.y + (ent_1.dHeight/2), ent_2.y - (ent_2.dHeight/2), ent_2.y + (ent_2.dHeight/2)) || isBetween(ent_1.y, ent_2.y - (ent_2.dHeight/2), ent_2.y + (ent_2.dHeight/2));
	
	if(isBetween(ent_1.x - (ent_1.dWidth/2), ent_2.x - (ent_2.dWidth/2), ent_2.x + (ent_2.dWidth/2)) && y_collision){
		return true;
	}
	else if(isBetween(ent_1.x + (ent_1.dWidth/2), ent_2.x - (ent_2.dWidth/2), ent_2.x + (ent_2.dWidth/2)) && y_collision){
		return true;
	}
	else if(isBetween(ent_1.x, ent_2.x - (ent_2.dWidth/2), ent_2.x + (ent_2.dWidth/2)) && y_collision){
		return true;
	}
	else{
		return false;
	}
	/* This was the older method of collision detection. it is simpler and could still be used for more basic detection
	if (isBetween(ent_1.x, (ent_2.x -  (ent_2.dWidth/2)), (ent_2.x +  (ent_2.dWidth/2)))
	 && isBetween(ent_1.y, (ent_2.y -  (ent_2.dHeight/2)), (ent_2.y +  (ent_2.dHeight/2)))){
		return true;
	}
	else{
		return false;
	}
	*/
}

//returns true if num is between lower and upper, exclusive
function isBetween(num, lower, upper){
	if(num >= lower && num <= upper){
		return true;
	}
	else{
		return false;
	}
}
function MouseHandler(){
	
	this.x_pos_rel_canvas = 0;
	this.y_pos_rel_canvas = 0;
	this.mouseX = 0;
	this.mouseY = 0;
	
	this.update = function(){
		this.mouseX = (this.x_pos_rel_canvas - gt5) / gt1;
		this.mouseY = (this.y_pos_rel_canvas - gt6) / gt4;
	}
}

function toRadians(angle) {
  return (angle * (Math.PI / 180.0));
}

function toDegrees(angle) {
  return (angle * (180.0 / Math.PI));
}