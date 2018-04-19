//CONSTANTS
const GRAVITY = 9.81;
const SIN_45 = Math.sin(Math.PI/4);
const SIN_30 = 0.5;
const SIN_60 = Math.sin(Math.PI/3);
const ARTILLERY_TIME = 3000; //milliseconds

const CLOSE_ZOOM_LEVEL = 2.0;
const NORMAL_ZOOM_LEVEL = 1.0;
const FAR_ZOOM_LEVEL = 0.5;
const FOG_DARKNESS = 140;
const FADE_RING_WIDTH = 40;

let gt1, gt2, gt3, gt4, gt5, gt6; //GLOBAL TRANSFORMS
let fog_norm, fog_close, fog_far; //Fog of War image data

let serverSocket = {}; //the web socket to connect to the server with

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

//returns a random int between 0 and max, not including max
function getRandomInt(max){
	return Math.floor(Math.random() * Math.floor(max));
}

//returns a random int between lower and upper, inclusive
function getRandomInRange(lower, upper){
	return getRandomInt(upper - lower + 1) + lower;
}

function distanceBetween(x1, y1, x2, y2){
	return Math.sqrt(Math.pow(x2 -x1, 2) + Math.pow(y2 - y1, 2));
}

function distanceBetweenEntities(ent1, ent2){
	return Math.sqrt(Math.pow(ent1.x - ent2.x, 2) + Math.pow(ent1.y - ent2.y, 2));
}

//Draws three F.O.W. images and stores them in 3 variables for later drawing
function drawFogOfWarImages(visibility){
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
		r_in = (((grid_length * visibility) - (FADE_RING_WIDTH/2)) * NORMAL_ZOOM_LEVEL);//inner radius of fade ring
		r_out = (((grid_length * visibility) + (FADE_RING_WIDTH/2)) * NORMAL_ZOOM_LEVEL);//outer radius of fade ring

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
		r_in = (((grid_length * visibility) - (FADE_RING_WIDTH/2)) * FAR_ZOOM_LEVEL); //inner radius of fade ring
		r_out = (((grid_length * visibility) + (FADE_RING_WIDTH/2)) * FAR_ZOOM_LEVEL);//outer radius of fade ring

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
		r_in = (((grid_length * visibility) - (FADE_RING_WIDTH/2)) * CLOSE_ZOOM_LEVEL);//inner radius of fade ring
		r_out = (((grid_length * visibility) + (FADE_RING_WIDTH/2)) * CLOSE_ZOOM_LEVEL);//outer radius of fade ring

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
function isColliding(ent_1, ent_2){
	
	//QUICK COLLISION DETECTION
	if(distanceBetweenEntities(ent_1, ent_2) >= (ent_1.collision_radius + ent_2.collision_radius)){
		return false;
	}
	
	//ADVANCED COLLISION DETECTION
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

function toRadians(angle) {
  return (angle * (Math.PI / 180.0));
}

function toDegrees(angle) {
  return (angle * (180.0 / Math.PI));
}

function connectToServer(role){
	serverSocket = new WebSocket('ws://' + window.location.host + '/my-websocket-endpoint');
	serverSocket.onopen = function() {
		//do some initial handshaking, sending back and forth information like the password and starting game state, etc
		sendMessageToServer("join:" + gameState.player.client_id + ":" + role);
		requestAnimationFrame(run); //more synchronized method similar to setInterval
	};

	serverSocket.onmessage = message_handler;
}

function sendMessageToServer(msg){
	serverSocket.send(msg);
}

//event listener for when the socket receives a message from the server
//TODO: fix this based on the new model
function message_handler(msg){
	let temp = msg.data.split(":");
	if(temp[0] == "join"){
		gameState.pw = temp[1];
		temp.splice(0, 2)
		gameState.addWalls(temp);
	}
	else{
		gameState.receiveGameState(msg.data);
	}
}