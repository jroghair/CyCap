//CONSTANTS
const GRAVITY = 9.81;
const ARTILLERY_TIME = 3000; //milliseconds
const TIME_BETWEEN_SHOTS = 150; //milliseconds. this will eventually be dependent on the role of the player, essentially which weapon they are using

/*PLAYER CLASS STATS*/
//Recruit
const RECRUIT_DMG = 10;
const RECRUIT_MAX_HP = 100;
const RECRUIT_SPEED = 5;

//Artillery
const ART_DMG = 10;
const ART_MAX_HP = 100;
const ART_SPEED = 5;

//Scout
const SCOUT_DMG = 10;
const SCOUT_MAX_HP = 100;
const SCOUT_SPEED = 5;

//Tank
const TANK_DMG = 10;
const TANK_MAX_HP = 150;
const TANK_SPEED = 3;
/*PLAYER CLASS STATS END*/

let gt1, gt2, gt3, gt4, gt5, gt6; //GLOBAL TRANSFORMS


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
	if(num > lower && num < upper){
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