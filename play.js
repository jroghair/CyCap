"use strict";

//global variables
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
canvas.height = document.documentElement.clientHeight;
canvas.width = document.documentElement.clientWidth;

let keys_down = []; //keys being pressed
let bullets = []; //all bullets, including artillery shells
let walls = [];
let part_fx = [];
let guis = [];
let map;
let masks = [];
let player;
let grid_length = (bg_width_px / bg_width_grids); //this comes from the images.js file
let player_speed = 2, bullet_speed = 5; //pixels. eventually we will want this to be based on grid_length/seconds
let mouseX;
let mouseY;
let last_shot_time = 0; //don't change

//all functions
function setup() {
	requestAnimationFrame(run); //more synchronized method similar to setInterval
	
	//set the global transforms
	gt1 = 1; //x scale
	gt2 = 0; //x skew
	gt3 = 0; //y skew
	gt4 = 1; //y scale
	gt5 = 0; //x trans
	gt6 = 0; //y trans
	
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
	
	guis.push(new GuiElement(health_gui, 25, canvas.height - 25, 50, 50, 0, 8));
	
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
		bullets.push(new ArtilleryShell(10, 10, player.x, player.y, mouseX, mouseY, bullet_image, player.team, blast1_image));
	});
	//mouse listener for coordinates
	window.addEventListener('mousemove', getMousePosition, false);
}

function run() {
	context.beginPath(); //so styles dont interfere
	context.setTransform(1, 0, 0, 1, 0, 0);
	context.clearRect(0, 0, canvas.width, canvas.height); //clear the canvas
  
	player.update();
	for(let i = bullets.length - 1; i >= 0; i--){
		bullets[i].update(); //we go through this backwards so that if one is removed, it still checks the others
	}
	for(let i = masks.length - 1; i >= 0; i--){
		masks[i].update(); //we go through this backwards so that if one is removed, it still checks the others
	}
	for(let i = part_fx.length - 1; i >= 0; i--){
		part_fx[i].update(); //we go through this backwards so that if one is removed, it still checks the others
	}
	for(let i = guis.length - 1; i >= 0; i--){
		guis[i].update();
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
	for(let i = 0; i < part_fx.length; i++){
		part_fx[i].draw();
	}
	//draw fog of war
	//draw GUI
	for(let i = 0; i < guis.length; i++){
		guis[i].draw();
	}
  
	context.closePath(); //so styles dont interfere
	requestAnimationFrame(run);
}

/*
*img is the Image that already has the sprite sheet data
*img.sprites = [{x:0,y:0,w:10,h:10},{x:20,y:0,w:30,h:40},....]
*sprIdx is the index of the data from img
*x & y are the position of sprite center
*dWidth & dHeight are the destination width and height of the drawn image
*r is the rotation in degrees
*a is the alpha value between 0.0 and 1.0
*/
function Entity(img, sprIdx, x, y, dWidth, dHeight, r, a){
	this.image = img; //this already needs to be defined in another file
	this.sprIdx = sprIdx
	this.sprite = this.image.sprites[this.sprIdx];
	this.x = x;
	this.y = y;
	this.dWidth = dWidth;
	this.dHeight = dHeight;
	this.r = toRadians(r);
	this.a = a;
	
	this.draw = function(){
		this.sprite = this.image.sprites[this.sprIdx]; //make sure the correct sprite is being displayed
		//need to include compatability with global transforms
		context.setTransform(gt1, gt2, gt3, gt4, gt5, gt6); //this 100% fucks up the mouse stuff
		context.transform(1, 0, 0, 1, this.x, this.y); //set draw position
		context.rotate(this.r); //this is in radians
		context.globalAlpha = this.a;
		context.drawImage(this.image, this.sprite.x, this.sprite.y, this.sprite.w, this.sprite.h, -this.dWidth/2, -this.dHeight/2, this.dWidth, this.dHeight);
	}
}

function Bullet(width, height, img, x, y, team) {
	this.x_diff = mouseX - x;
	this.y_diff = (mouseY - y) * -1;
	let angle = toDegrees(Math.atan(this.y_diff / this.x_diff));

	if (this.x_diff < 0 && this.y_diff > 0) {
		angle += 180;
	}
	else if (this.x_diff < 0 && this.y_diff < 0) {
		angle += 180;
	}
	else if (this.x_diff > 0 && this.y_diff < 0) {
		angle += 360;
	}	
	
	this.base = Entity;
	//sprIdx 0, transparency 1, angle is calculated based off of stuff
	this.base(img, 0, x, y, width, height, angle - 90, 1);

	this.team = team; //this is so we can avoid friendly fire (and maybe reduce the amount of collision checks)
	
	this.y_ratio = Math.sin(toRadians(angle)) * -1;
	this.x_ratio = Math.cos(toRadians(angle));
	this.y_ratio += ((Math.random() - 0.5) * 0.05);
	this.x_ratio += ((Math.random() - 0.5) * 0.05);
	
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
	
	this.x_vel = (this.end_x - this.start_x) / ARTILLERY_TIME; //pixels per millisecond
	this.y_vel = (this.end_y - this.start_y) / ARTILLERY_TIME; //pixels per millisecond
	this.v_init = GRAVITY * (ARTILLERY_TIME/1000);
	
	this.base = Entity;
	//sprite 1 on the bullet sheet, rotation 0, transparency 1
	//i am setting this to 0 temporarily
	this.base(img, 1, this.start_x, this.start_y, width, height, 0, 1);
	
	this.update = function(){
		if((Date.now() - this.start_time) < ARTILLERY_TIME){
			let delta_t = Date.now() - this.lastUpdate;
			this.x += (this.x_vel * delta_t);
			this.y += (this.y_vel * delta_t);
			
			let total_time = (Date.now() - this.start_time)/1000; //this is in seconds
			
			let temp_multiplier = 0.0907*(-9.8*total_time*total_time + this.v_init*total_time) + 1
			this.dWidth = this.start_width * temp_multiplier;
			this.dHeight = this.start_height * temp_multiplier;
			
			this.lastUpdate = Date.now();
		}
		else{
			//place mask
			masks.push(new GroundMask(this.blast_img, Math.floor((this.end_x/bg_width_px)*bg_width_grids), Math.floor((this.end_y/bg_height_px)*bg_height_grids), 3, 1));
			part_fx.push(new ParticleEffect(boom_ss, this.end_x, this.end_y, grid_length*2, grid_length*2, 74, 3000));
			//TODO: deal damage
			//remove from draw list
			let temp_index = bullets.indexOf(this);
			bullets.splice(temp_index, 1);
		}
	}
}

function Player(width, height, img, x, y, role, team) {
	this.base = Entity;
	//sprite 0, rotate 0, transparency 1
	this.base(img, 0, x, y, width, height, 0, 1);
	
	this.role = role;
	this.team = team;
	
	//decide health based on role
	this.max_hp = 100;
	this.health = 37;
	//^^^^these are temporary!!! FIX THIS
	
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
		if(keys_down.includes(49)){
			this.health -= 1;
		}
		if(keys_down.includes(50)){
			this.health += 1;
		}
		if(keys_down.includes(51)){
			gt1 -= 0.02;
			gt4 -= 0.02;
		}
		else if(keys_down.includes(52)){
			gt1 += 0.02;
			gt4 += 0.02;
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
			else if ((Date.now() - last_shot_time) >= TIME_BETWEEN_SHOTS) {
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
	//we will use wall sprite 0, rotate 0, trans 1
	this.base(img, 0, (this.grid_x * grid_length) + (grid_length/2), (this.grid_y * grid_length) + (grid_length/2), grid_length, grid_length, 0, 1);
}

//takes in the mask image, the grid coordinates of the center, and the size in grid_lengths
//this should also take in a starting A_value and a fade time in seconds (-1 means never fade)
function GroundMask(img, center_x, center_y, size, starting_a){
	this.start_time = Date.now();
	this.life_time = 10; //in seconds
	this.grid_x = center_x;
	this.grid_y = center_y;
	this.base = Entity;
	//sprIdx 0 from groundMasks, 0 rotate
	this.base(img, 0, (this.grid_x * grid_length) + (grid_length/2), (this.grid_y * grid_length) + (grid_length/2), grid_length * size, grid_length * size, 0, starting_a);

	this.update = function(){
		var age = (Date.now() - this.start_time)/1000; //in seconds
		if(age > this.life_time){
			let temp_index = masks.indexOf(this);
			masks.splice(temp_index, 1);
		}
		else if(age > this.life_time/2){
			this.a = 1 - (2/this.life_time)*(age-this.life_time/2)
		}
		else{
			this.a = 1.0;
		}
	}
}

function ParticleEffect(img, x, y, width, height, num_frames, life_time){
	this.life_time = life_time; //in milliseconds
	this.last_frame = Date.now();
	this.frame_speed = life_time/num_frames;
	this.num_frames = num_frames;
	this.base = Entity;
	this.base(img, 0, x, y, width, height, 0, 1);
	
	this.update = function(){
		if(this.sprIdx >= (this.num_frames - 1)){
			let temp_index = part_fx.indexOf(this);
			part_fx.splice(temp_index, 1);
		}	
		else if((Date.now() - this.last_frame) > this.frame_speed){
			this.last_frame = Date.now();
			this.sprIdx++;
		}
	}
}

function GuiElement(img, x, y, width, height, elemIndex, num_frames){
	this.base = Entity;
	this.base(img, elemIndex, x, y, width, height, 0, 1);
	this.num_frames = num_frames;
	
	this.update = function(){
		this.sprIdx = 8 - Math.round(player.health/player.max_hp * this.num_frames);
	}
}

/*
function TiledBackground(img){
	
}*/

//need to make this a subclass of entity
function Background(img){
	this.base = Entity;
	this.base(img, 0, bg_width_px/2, bg_height_px/2, bg_width_px, bg_height_px, 0, 1);
}

//this code executes right when the page is loaded
//but it needs to be at the end of the file because it references
//certain functions in util.js that require classes (that exist in this file)
//to have already been defined
setup(); //only call setup once