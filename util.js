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


/*  Computer Player
--generate field of nodes that it can or can't travel to
--put them in an array with neigbors
*/
function generateNodes(){
	//go through all coordinates
	var time1 = Date.now();
	for(var i = 0;i < canvas.width;i+=5){
		var node_col = [];//for storing a column of nodes
		for(var j = 0;j < canvas.height;j+=5){
			//make a test entity for player
			var test_player_ent = new Entity(player_image, 0, i, j, grid_length, grid_length, 0, 1);
			//go through all walls and check collision
			var traverable = true; //whether node can be traveled to or not
			for(var t = 0;t < walls.length;t++){
				if(isColliding(walls[t], test_player_ent)){
					traverable = false;
					break;
				}
			}
			node_col.push(new node(i, j, traverable));

		}
		nodes.push(node_col);
	}
	console.log('nodal generation time: ' + (Date.now() - time1) + ' ms');
	return 'success';
}
function node(x, y, trav){
	this.x = x;
	this.y = y;
	this.trav = trav;
}

//this will eventually be taken out, but i am using it for simplicity for now
function placeBorder(){
	wallLine(0, 0, 30, "x");
	wallLine(0, 19, 30, "x");
	wallLine(0, 1, 18, "y");
	wallLine(29, 1, 18, "y");
}

//takes in a starting grid coordinate, a length of the wall line, and which axis it will follow("x" or "y")
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

function getMousePosition(event) {
  this.rect = canvas.getBoundingClientRect();
  mouseX = ((event.clientX - rect.left) - gt5) / gt1;
  mouseY = ((event.clientY - rect.top) - gt6) / gt4;
}

function toRadians(angle) {
  return (angle * (Math.PI / 180.0));
}

function toDegrees(angle) {
  return (angle * (180.0 / Math.PI));
}
