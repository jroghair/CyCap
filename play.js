"use strict";

//global variables
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");

var keys_down = []; //keys being pressed
var bullets = []; //all bullets, including artillery shells
var walls = [];
let map;
let masks = [];
var player;
let grid_length = (canvas.width / 30); //the grid map we are using for now is 30x20
var player_speed = 2, bullet_speed = 5; //pixels. eventually we will want this to be based on grid_length/seconds
var mouseX;
var mouseY;
let artillery_time = 3000; //milliseconds
var last_shot_time = 0; //don't change
var time_between_shots = 150; //milliseconds. this will eventually be dependent on the role of the player, essentially which weapon they are using

//image filenames
let player_image = "player.png";
let bullet_image = "bullet.png";
let background_image = "grid_map_30x20.png";
let wall_image = "wall2.png";
let blast1_image = "blast1.png";
let artillery_shell_image = "artillery_shell.png";

//all functions
function setup() {
	//setInterval(run, 1000 / 60); //called 60 times a second
	requestAnimationFrame(run); //more synchronized method similar to setInterval
	
	player = new Player(grid_length, grid_length, player_image, canvas.width / 2, canvas.height / 2, "recruit", "1");
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
		//places a blast mask just for testing
		bullets.push(new ArtilleryShell(10, 10, player.x, player.y, mouseX, mouseY, artillery_shell_image, player.team, blast1_image));
	});
	//mouse listener for coordinates
	window.addEventListener('mousemove', getMousePosition, false);
}

function run() {
	context.beginPath(); //so styles dont interfere
	context.clearRect(0, 0, canvas.width, canvas.height); //clear the canvas
  
	player.update();
	for(let i = bullets.length - 1; i >= 0; i--){
		bullets[i].update(); //we go through this backwards so that if one is removed, it still checks the others
	}
  
	map.draw();
	for(let i = 0; i < masks.length; i++){
		masks[i].draw();
	}
	for(let i = 0; i < walls.length; i++){
		walls[i].draw();
	}
	player.draw();
	for(let i = 0; i < bullets.length; i++){
		bullets[i].draw();
	}
	//draw fog of war
	//draw GUI
  
	context.closePath(); //so styles dont interfere
	requestAnimationFrame(run);
}

//x & y refer to the center of the Entity
function Entity(width, height, img, x, y){
	this.image = new Image();
	this.image.src = img;
	this.width = width;
	this.height = height;
	this.transparency = 1.0; //from 0.0 to 1.0
	this.x = x;
	this.y = y;
	
	this.draw = function(){
		context.setTransform(1, 0, 0, 1, this.x, this.y); // set scale and position
		context.rotate(0); //this is in radians
		context.globalAlpha = this.transparency;
		context.drawImage(this.image, 0, 0, this.image.width, this.image.height, -this.width/2, -this.height/2, this.width, this.height);
		//context.drawImage(this.image, this.x - (this.width/2), this.y - (this.height/2), this.width, this.height);
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
	
	this.update = function(){
		this.x += bullet_speed * this.x_ratio;
		this.y += bullet_speed * this.y_ratio;
		if (this.x < 0 || this.x > canvas.width || this.y < 0 || this.y > canvas.height) {
			let temp_index = bullets.indexOf(this);
			bullets.splice(temp_index, 1);
			return;
		}
			
		for(var j = 0; j < walls.length; j++){
			if (isColliding(this, walls[j]))
			{
				let temp_index = bullets.indexOf(this);
				bullets.splice(temp_index, 1);
				break;
			}
		}
	}
}

function ArtilleryShell(width, height, start_x, start_y, end_x, end_y, img, team, blast_img){
	this.start_time = Date.now();
	this.lastUpdate = this.start_time;
	
	this.start_x = start_x;
	this.start_y = start_y;
	this.end_x = end_x;
	this.end_y = end_y;
	this.start_width = width;//this is the initial width
	this.start_height = height;//this is the initial height
	
	this.team = team;
	this.blast_img = blast_img;
	
	this.x_vel = (this.end_x - this.start_x) / artillery_time; //pixels per millisecond
	this.y_vel = (this.end_y - this.start_y) / artillery_time; //pixels per millisecond
	
	this.base = Entity;
	this.base(width, height, img, this.start_x, this.start_y);
	
	this.update = function(){
		if((Date.now() - this.start_time) < artillery_time){
			let delta_t = Date.now() - this.lastUpdate;
			this.x += (this.x_vel * delta_t);
			this.y += (this.y_vel * delta_t);
			
			let total_time = (Date.now() - this.start_time)/1000; //this is in seconds
			let temp_multiplier = 0.0907*(-9.8*total_time*total_time + 29.4*total_time) + 1
			this.width = this.start_width * temp_multiplier;
			this.height = this.start_height * temp_multiplier;
			
			this.lastUpdate = Date.now();
		}
		else{
			//place mask
			masks.push(new GroundMask(this.blast_img, Math.floor((this.end_x/canvas.width)*30), Math.floor((this.end_y/canvas.height)*20), 3));
			//deal damage
			//remove from draw list
			let temp_index = bullets.indexOf(this);
			bullets.splice(temp_index, 1);
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

//takes in the mask image, the grid coordinates of the center, and the size in grid_lengths
//this should also take in a starting A_value and a fade time in seconds (-1 means never fade)
function GroundMask(img, center_x, center_y, size){
	//this.transparency = starting_a;
	this.grid_x = center_x;
	this.grid_y = center_y;
	this.base = Entity;
	this.base(grid_length * size, grid_length * size, img, (this.grid_x * grid_length) + (grid_length/2), (this.grid_y * grid_length) + (grid_length/2));
	/*
	this.update(){
		//set transparency
	}
	*/
}

function TiledBackground(img){
	
}

//need to make this a subclass of entity
function Background(img){
	this.base = Entity;
	this.base(canvas.width, canvas.height, img, canvas.width/2, canvas.height/2);
}

//this code executes right when the page is loaded
//but it needs to be at the end of the file because it references
//certain functions in util.js that require classes (that exist in this file)
//to have already been defined
setup(); //only call setup once