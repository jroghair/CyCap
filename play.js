"use strict";

//global variables
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
var keys_down = []; //keys being pressed
var bullets = []; //all bullets
var walls = [];
let map;
var player, bullet_p;
let grid_length = (canvas.width / 30); //the grid map we are using for now is 30x20
var player_speed = 2, bullet_speed = 5; //pixels. eventually we will want this to be based on grid_length/seconds
var mouseX;
var mouseY;
var last_shot_time = 0; //don't change
var time_between_shots = 150; //milliseconds. this will eventually be dependent on the role of the player, essentially which weapon they are using

//image filenames
var player_image = "player.png";
var bullet_image = "bullet.png";
var background_image = "grid_map_30x20.png";
var wall_image = "wall.png"


//this code executes right when the page is loaded
setup(); //only call setup once



//all functions
function setup() {
	//setInterval(run, 1000 / 60); //called 60 times a second
	requestAnimationFrame(run); //more synchronized method similar to setInterval
	
	player = new Player(grid_length, grid_length, player_image, canvas.width / 2, canvas.height / 2, "recruit", "1");
	bullet_p = new bullet_population();
	map = new Background(background_image);
	walls.push(new Wall(wall_image, 5, 10));
	walls.push(new Wall(wall_image, 5, 11));
	walls.push(new Wall(wall_image, 5, 12));
	walls.push(new Wall(wall_image, 5, 13));
	walls.push(new Wall(wall_image, 7, 10));
	walls.push(new Wall(wall_image, 7, 11));
	walls.push(new Wall(wall_image, 7, 12));
	walls.push(new Wall(wall_image, 7, 13));
	placeBorder();
	
	//setting up two key listeners to improve movement
	//when a key goes down it is added to a list and when it goes up its taken out
	document.addEventListener("keydown", function(event) {
		if (!keys_down.includes(event.keyCode)) {
			keys_down.push(event.keyCode);
		}
	});
	document.addEventListener("keyup", function(event) {
		keys_down.splice(keys_down.indexOf(event.keyCode), 1);
	});
	document.addEventListener("click", function(event) {
		//place trap at player position
	});
	//mouse listener for coordinates
	window.addEventListener('mousemove', getMousePosition, false);
}

function run() {
	context.beginPath(); //so styles dont interfere
	context.clearRect(0, 0, canvas.width, canvas.height); //clear the canvas
  
	player.update();
	bullet_p.move_bullets();
  
	map.draw();
	for(let i = 0; i < walls.length; i++){
		walls[i].draw();
	}
	bullet_p.draw();
	player.draw();
  
	context.closePath(); //so styles dont interfere
	requestAnimationFrame(run);
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
	var y_collision = isBetween(ent_1.y - (ent_1.height/2), ent_2.y - (ent_2.height/2), ent_2.y + (ent_2.height/2)) || isBetween(ent_1.y + (ent_1.height/2), ent_2.y - (ent_2.height/2), ent_2.y + (ent_2.height/2)) || isBetween(ent_1.y, ent_2.y - (ent_2.height/2), ent_2.y + (ent_2.height/2));
	
	if(isBetween(ent_1.x - (ent_1.width/2), ent_2.x - (ent_2.width/2), ent_2.x + (ent_2.width/2)) && y_collision){
		return true;
	}
	else if(isBetween(ent_1.x + (ent_1.width/2), ent_2.x - (ent_2.width/2), ent_2.x + (ent_2.width/2)) && y_collision){
		return true;
	}
	else if(isBetween(ent_1.x, ent_2.x - (ent_2.width/2), ent_2.x + (ent_2.width/2)) && y_collision){
		return true;
	}
	else{
		return false;
	}
	/* This was the older method of collision detection. it is simpler and could still be used for more basic detection
	if (isBetween(ent_1.x, (ent_2.x -  (ent_2.width/2)), (ent_2.x +  (ent_2.width/2)))
	 && isBetween(ent_1.y, (ent_2.y -  (ent_2.height/2)), (ent_2.y +  (ent_2.height/2)))){
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
  mouseX = event.clientX - rect.left;
  mouseY = event.clientY - rect.top;
}

function toRadians(angle) {
  return (angle * (Math.PI / 180.0));
}

function toDegrees(angle) {
  return (angle * (180.0 / Math.PI));
}

//x & y refer to the center of the Entity
function Entity(width, height, img, x, y){
	this.image = new Image();
	this.image.src = img;
	this.width = width;
	this.height = height;
	//this.speedX = 0; not sure if we need speed right now, so im taking it out
	//this.speedY = 0; 
	this.x = x;
	this.y = y;
	
	this.draw = function(){
		context.drawImage(this.image, this.x - (this.width/2), this.y - (this.height/2), this.width, this.height);
	}
}

function Bullet(width, height, img, x, y, team) {
	this.base = Entity;
	this.base(width, height, img, x, y);

	this.team = team; //this is so we can avoid friendly fire (and maybe reduce the amount of collision checks)
	
	this.x_diff = mouseX - player.x;
	this.y_diff = (mouseY - player.y) * -1;

	this.angle = toDegrees(Math.atan(this.y_diff / this.x_diff));

	if (this.x_diff < 0 && this.y_diff > 0) {
		this.angle += 180;
	}
	else if (this.x_diff < 0 && this.y_diff < 0) {
		this.angle += 180;
	}
	else if (this.x_diff > 0 && this.y_diff < 0) {
		this.angle += 360;
	}	
	//console.log(this.angle);

	this.y_ratio = Math.sin(toRadians(this.angle)) * -1;
	this.x_ratio = Math.cos(toRadians(this.angle));
	//console.log(this.x_ratio + ',' + this.y_ratio);
	this.y_ratio += ((Math.random() - 0.5) * 0.05);
	this.x_ratio += ((Math.random() - 0.5) * 0.05);
	//console.log(this.x_ratio + ',' + this.y_ratio);
}


function bullet_population() {
	this.draw = function() {
		for (var i = 0; i < bullets.length; i++) {
		    bullets[i].draw();
		}	
	}

	this.move_bullets = function() {
		for (var i = 0; i < bullets.length; i++) {
			//console.log(bullets[i].x_ratio + ', ' + bullets[i].y_ratio);
			bullets[i].x += bullet_speed * bullets[i].x_ratio;
			bullets[i].y += bullet_speed * bullets[i].y_ratio;
			if (bullets[i].x < 0 || bullets[i].x > canvas.width || bullets[i].y < 0 || bullets[i].y > canvas.height) {
				bullets.splice(i, 1);
				i--; //this is so that it checks the bullet after one that is removed
				continue; //so that when a bullet leaves the map and is spliced out of the bullet list
				//it does not check if that bullet hit a wall
			}
			
			for(var j = 0; j < walls.length; j++){
				if (isColliding(bullets[i], walls[j]))
				{
					bullets.splice(i, 1);
					i--; //this is so that it checks the bullet after one that is removed
					break; //does not check the other walls if at least one was hit
				}
			}
		}
		
	}
}

function Player(width, height, img, x, y, role, team) {
	this.base = Entity;
	this.base(width, height, img, x, y);
	
	this.role = role;
	this.team = team;
	//decide health based on role
	this.has_flag = false;
	this.mov_speed = player_speed; //this will eventually be dependent on role 
	
	this.last_x = this.x;
	this.last_y = this.y;

	this.update = function() {
		//save last positions in case the new ones are no good
		this.last_x = this.x;
		this.last_y = this.y;
		
		//this section will probably end up on the server
		if (keys_down.includes(87)) {
			this.y -= this.mov_speed;
		}
		if (keys_down.includes(65)) {
			this.x -= this.mov_speed;
		}
		if (keys_down.includes(68)) {
			this.x += this.mov_speed;
		}
		if (keys_down.includes(83)) {
			this.y += this.mov_speed;
		}
		
		//check if you hit a wall after that move
		for(let j = 0; j < walls.length; j++){
				if (isColliding(this, walls[j]))
				{
					this.x = this.last_x;
					this.y = this.last_y;
					break; //does not check the other walls if at least one was hit
				}
			}
		
		//shoot bullets
		if (keys_down.includes(32)) {
			if (last_shot_time == 0) {
				bullets.push(new Bullet(grid_length * 0.15, grid_length * 0.15, bullet_image, this.x, this.y, this.team));
				last_shot_time = Date.now();
			}
			else if ((Date.now() - last_shot_time) >= time_between_shots) {
				bullets.push(new Bullet(grid_length * 0.15, grid_length * 0.15, bullet_image, this.x, this.y, this.team));
				last_shot_time = Date.now();
			}
		}
	}
}

//Grid_x and grid_y are the positions on the grid, with top left grid coordinates being (0,0)
function Wall(img, grid_x, grid_y){
	this.grid_x = grid_x;
	this.grid_y = grid_y;
	this.base = Entity;
	this.base(grid_length, grid_length, img, (this.grid_x * grid_length) + (grid_length/2), (this.grid_y * grid_length) + (grid_length/2));
	
}

function Background(img){
	this.image = new Image();
	this.image.src = img;
	this.width = canvas.width;
	this.height = canvas.height;
	
	this.draw = function(){
		context.drawImage(this.image, 0, 0, this.width, this.height);
	}
}