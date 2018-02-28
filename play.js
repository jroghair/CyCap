//CANVAS INFORMATION
var canvas = document.getElementById("game_canvas");
var context = canvas.getContext("2d");
canvas.height = document.documentElement.clientHeight;
canvas.width = document.documentElement.clientWidth;

let fog_canvas = document.getElementById("fog_canvas");
let fog_context = fog_canvas.getContext("2d");
fog_canvas.height = canvas.height;
fog_canvas.width = canvas.width;

//INPUT INFORMATION
let keys_down = []; //keys being pressed
let keys_pnr = []; //keys that have been pushed and released
let mouse_hand;

//MOVEMENT AND COLLISION CONSTANTS
const UP    = 0b1000;
const DOWN  = 0b0100;
const LEFT  = 0b0010;
const RIGHT = 0b0001;

//WORLD OBJECTS
let bullets = []; //all bullets, including artillery shells
let walls = [];
let part_fx = [];
let guis = [];
let map;
let masks = [];

//FRAME TIME & DELTA_T
let lastFrameTime;
let global_delta_t = 0; //the time that this frame took in SECONDS

let player;
let client_id; //this will eventually come from the server

let player_speed = 120, bullet_speed = 450; //pixels per second
let current_zoom_lvl = 2;
let last_shot_time = 0; //don't change

//TESTING
//stuff for fps testing
let is_fps_running = true;
let fps_frame_times = [];
let rolling_buffer_length = 30;

let speed_test;

//all functions
function setup() {
	
	client_id = prompt("What is your username?");
	
	//KEEP THIS AWAY FROM ALL OF THE OTHER CODE \/\/\/
	drawFogOfWarImages();
	fog_context.putImageData(fog_norm, 0, 0);
	//KEEP THIS SHIT ^^^ AWAY FROM EVERYTHING ELSE
	
	
	requestAnimationFrame(run); //more synchronized method similar to setInterval
	
	//set the global transforms
	gt1 = 1; //x scale
	gt2 = 0; //x skew
	gt3 = 0; //y skew
	gt4 = 1; //y scale
	gt5 = 0; //x trans
	gt6 = 0; //y trans
	
	player = new Player(grid_length, grid_length, player_image, 64, 64, "recruit", "1", client_id);
	gt5 = -1 * ((player.x * gt1) - (canvas.width / 2));
	gt6 = -1 * ((player.y * gt4) - (canvas.height / 2));
	
	map = new TiledBackground(background_tiles);
	placeBorder(bg_width_grids, bg_height_grids, 0, 0);
	wallLine(5, 10, 5, 'x');  //done
	wallLine(9, 3, 7, 'y');   //done
	wallLine(12, 3, 15, 'x'); //done
	wallLine(12, 4, 4, 'y');  //done
	wallLine(12, 10, 8, 'x'); //done
	wallLine(20, 4, 8, 'y');  //done
	wallLine(21, 7, 3, 'x');  //done
	wallLine(26, 4, 4, 'y');  //done
	wallLine(29, 1, 2, 'y');  //done
	wallLine(29, 5, 2, 'y');  //done
	wallLine(29, 7, 2, 'x');  //done
	wallLine(31, 7, 19, 'y'); //done
	wallLine(34, 1, 8, 'y');  //done
	wallLine(38, 5, 2, 'x');  //done
	wallLine(32, 9, 6, 'x');  //done
	wallLine(34, 12, 6, 'x'); //done
	wallLine(34, 13, 4, 'y'); //done
	wallLine(34, 19, 10, 'y');//done
	wallLine(24, 10, 5, 'x'); //done
	wallLine(23, 10, 16, 'y');//done
	wallLine(28, 14, 3, 'x'); //done
	wallLine(26, 17, 3, 'x'); //done
	wallLine(26, 20, 4, 'y'); //done
	wallLine(27, 23, 4, 'x'); //done
	wallLine(23, 26, 9, 'x'); //done
	wallLine(20, 14, 13, 'y');//done
	wallLine(1, 15, 19, 'x'); //done
	wallLine(1, 18, 2, 'x');  //done
	wallLine(5, 18, 3, 'x');  //done
	wallLine(8, 18, 11, 'y'); //done
	wallLine(11, 18, 8, 'y'); //done
	wallLine(12, 18, 6, 'x'); //done
	wallLine(3, 24, 2, 'y');  //done
	wallLine(3, 26, 3, 'x');  //done
	wallLine(11, 26, 5, 'x'); //done
	wallLine(18, 26, 2, 'x'); //done
	
	//AI NODE BUILDING MUST BE AFTER WALLS ARE BUILT
	generateNodes();
	ai_player1 = new AI_player(grid_length, grid_length, enemy_image, 100, 430, "recruit", "1");
	
	guis.push(new GuiElement(health_gui, 50, canvas.height - 50, 100, 100, 0, 8));
	speed_test = new SpeedPotion(256, 256);
	
	//setting up two key listeners to improve movement
	//when a key goes down it is added to a list and when it goes up its taken out
	document.addEventListener("keydown", function(event) {
		if (!keys_down.includes(event.keyCode)) {
			keys_down.push(event.keyCode);
		}
	});
	document.addEventListener("keyup", function(event) {
		keys_down.splice(keys_down.indexOf(event.keyCode), 1);
		keys_pnr.push(event.keyCode);
	});
	document.addEventListener("click", function(event) {
		//place trap at player position
		//places a blast mask just for testing
		bullets.push(new ArtilleryShell(10, 10, player.x, player.y, mouse_hand.mouseX, mouse_hand.mouseY, bullet_image, player.team, blast1_image));
	});
	//mouse listener for coordinates
	mouse_hand = new MouseHandler();
	window.addEventListener('mousemove', function(event){
		this.rect = canvas.getBoundingClientRect();
		mouse_hand.x_pos_rel_canvas = (event.clientX - rect.left);
		mouse_hand.y_pos_rel_canvas = (event.clientY - rect.top);
	}, false);
	
	lastFrameTime = Date.now();
	//connectToServer();
}

function run() {
	global_delta_t = (Date.now() - lastFrameTime) / 1000; //set the time of the most recent frame (in seconds)
	lastFrameTime = Date.now();
	
	//For FPS testing purposes
	if(is_fps_running){
		fps_frame_times.push(global_delta_t);
		if(fps_frame_times.length == (rolling_buffer_length + 1)){
			fps_frame_times.splice(0, 1); //remove the oldest element
			let temp = Math.round((1 / listAverage(fps_frame_times)));
			document.getElementById("fps").innerHTML = (client_id + ": " + temp);
		}
	}
	
	context.beginPath(); //so styles dont interfere
	context.setTransform(1, 0, 0, 1, 0, 0); //reset the transform so the clearRect function works
	context.clearRect(0, 0, canvas.width, canvas.height); //clear the canvas
  
	//update everything
	mouse_hand.update();
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
	speed_test.update();
	
	if(keys_pnr.includes(90)){
		//switch zoom level!
		ToggleZoom();
	}
  
	map.draw();
	for(let i = 0; i < masks.length; i++){
		masks[i].draw();
	}
	for(let i = 0; i < walls.length; i++){
		walls[i].draw();
	}
	player.draw();
	ai_player1.draw();
	speed_test.draw();
	for(let i = 0; i < bullets.length; i++){
		bullets[i].draw();
	}
	for(let i = 0; i < part_fx.length; i++){
		part_fx[i].draw();
	}
	//draw fog of war
	
	//need to draw rectangles outside of the map that
	//are essentially below the GUI, but above pretty much everything else
	//this keeps the fog of war and particle effects from displaying outside of the map
	
	//draw GUI
	for(let i = 0; i < guis.length; i++){
		guis[i].draw();
	}
	context.closePath(); //so styles dont interfere
	
	//following is to test coordinates
	//context.resetTransform();
	//context.lineWidth = "1";
	//keep the following code
	//it is for showing traversable v. non traversable nodes
	/*
	for (var i = 0; i < nodes.length; i++) {
		for (var j = 0; j < nodes[i].length; j++) {
			context.beginPath();
			if (nodes[i][j].trav == false) {
				context.strokeStyle = "red";
			} else {
				context.strokeStyle = "green";
			}
			context.rect(nodes[i][j].x, nodes[i][j].y, 2, 2);
			context.stroke();
			context.closePath(); //so styles dont interfere
		}
	}
	*/
	drawAIPath();
	keys_pnr.splice(0, keys_pnr.length);
	requestAnimationFrame(run);
}

function ToggleZoom(){
	if(current_zoom_lvl == 1){
		current_zoom_lvl = 2;
		gt1 = NORMAL_ZOOM_LEVEL; //setting scaling to normal levels
		gt4 = NORMAL_ZOOM_LEVEL;
		gt5 = -1 * ((player.x * gt1) - (canvas.width / 2));
		gt6 = -1 * ((player.y * gt4) - (canvas.height / 2));
		fog_context.putImageData(fog_norm, 0, 0);
	}
	else if(current_zoom_lvl == 2){
		current_zoom_lvl = 3;
		gt1 = FAR_ZOOM_LEVEL; //setting scaling to wide levels
		gt4 = FAR_ZOOM_LEVEL;
		gt5 = -1 * ((player.x * gt1) - (canvas.width / 2));
		gt6 = -1 * ((player.y * gt4) - (canvas.height / 2));
		fog_context.putImageData(fog_far, 0, 0);
	}
	else if(current_zoom_lvl == 3){
		current_zoom_lvl = 1;
		gt1 = CLOSE_ZOOM_LEVEL; //setting scaling to zoomed levels
		gt4 = CLOSE_ZOOM_LEVEL;
		gt5 = -1 * ((player.x * gt1) - (canvas.width / 2));
		gt6 = -1 * ((player.y * gt4) - (canvas.height / 2));
		fog_context.putImageData(fog_close, 0, 0);
	}
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
		//if(Math.sqrt(Math.pow(this.x - player.x, 2) + Math.pow(this.y - player.y, 2)) <= (VISIBILITY * grid_length)){ //this keeps things from drawing if they are too far away
		this.sprite = this.image.sprites[this.sprIdx]; //make sure the correct sprite is being displayed
		//need to include compatability with global transforms
		context.setTransform(gt1, gt2, gt3, gt4, Math.round(gt5), Math.round(gt6)); //we must round the X & Y positions so that it doesn't break the textures
		context.transform(1, 0, 0, 1, Math.round(this.x), Math.round(this.y)); //set draw position
		context.rotate(this.r); //this is in radians
		context.globalAlpha = this.a;
		context.drawImage(this.image, this.sprite.x, this.sprite.y, this.sprite.w, this.sprite.h, -this.dWidth/2, -this.dHeight/2, this.dWidth, this.dHeight);
		//}
	}
	
	this.toDataString = function(){
		let output = "000" + ","; //the temporary image code
		output += this.sprIdx + ",";
		output += this.x + ",";
		output += this.y + ",";
		output += this.dWidth + ",";
		output += this.dHeight + ",";
		output += this.r + ","; //in radians!
		output += this.a;
		return output;
	}
}

function Bullet(width, height, img, x, y, team) {
	this.x_diff = mouse_hand.mouseX - x;
	this.y_diff = (mouse_hand.mouseY - y) * -1;
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
		this.x += bullet_speed * this.x_ratio * global_delta_t;
		this.y += bullet_speed * this.y_ratio * global_delta_t;
			
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
			this.x += (this.x_vel * global_delta_t * 1000);
			this.y += (this.y_vel * global_delta_t * 1000);
			
			let total_time = (Date.now() - this.start_time)/1000; //this is in seconds
			
			let temp_multiplier = 0.0907*(-9.8*total_time*total_time + this.v_init*total_time) + 1
			this.dWidth = this.start_width * temp_multiplier;
			this.dHeight = this.start_height * temp_multiplier;
		}
		else{
			//place mask
			masks.push(new GroundMask(this.blast_img, Math.floor(this.end_x/grid_length), Math.floor(this.end_y/grid_length), 3, 1));
			part_fx.push(new ParticleEffect(boom_ss, this.end_x, this.end_y, grid_length*2, grid_length*2, 74, 3000));
			//TODO: deal damage
			//remove from draw list
			let temp_index = bullets.indexOf(this);
			bullets.splice(temp_index, 1);
		}
	}
}

function Player(width, height, img, x, y, role, team, client_id) {
	this.client_id = client_id; //this is the player's specific id. no one else in any match is allowed to have this at the same time
	
	this.base = Entity;
	//sprite 0, rotate 0, transparency 1
	this.base(img, 0, x, y, width, height, 0, 1);
	
	this.role = role;
	this.team = team;
	
	//WEAPONS AND ITEMS
	this.currentWeapon = 1;
	this.weapon1;
	this.weapon2;
	this.weapon3;
	this.weapon4;
	this.item_slot = "EMPTY";
	
	//decide health based on role
	this.max_hp = 100;
	this.health = 37;
	//^^^^these are temporary!!! TODO: FIX THIS
	
	//variables for the different power-ups and if they are affecting the player
	this.is_invincible = false;
	this.speed_boost = 1.0; //2.0 if boosted
	this.damage_boost = 1.0; //1.5 if boosted
	
	this.has_flag = false;
	this.mov_speed = player_speed; //this will eventually be dependent on role
	
	this.die = function(){
		//handle the player dying and respawning
	}
	
	this.takeDamage = function(amount){
		if(!this.is_invincible){
			this.health -= amount;
		}
		if(this.health <= 0){
			this.die(); //idk what this is gonna do yet
		}
	}

	this.update = function() {
		let movement_code  = 0b0000; //the binary code for which directions the player moving
		
		//this section will probably end up on the server
		if (keys_down.includes(87)) {
			movement_code |= UP; //trying to move up
		}
		if (keys_down.includes(65)) {
			movement_code |= LEFT; //trying to move left
		}
		if (keys_down.includes(68)) {
			movement_code |= RIGHT; //trying to move right
		}
		if (keys_down.includes(83)) {
			movement_code |= DOWN; //trying to move down
		}
		
		if((movement_code & (UP | DOWN)) == 0b1100){ //if both up and down are pressed
			movement_code &= ~(UP | DOWN); //clear the up and down bits
		}
		if((movement_code & (LEFT | RIGHT)) == 0b0011){ //if both left and right are pressed
			movement_code &= ~(LEFT | RIGHT); //clear the left and right bits
		}
		
		let delta_x = 0;
		let delta_y = 0;
		if(movement_code == 0b1010){
			delta_y = -1 * this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
			delta_x = -1 * this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
		}
		else if(movement_code == 0b1001){
			delta_y = -1 * this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
			delta_x = this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
		}
		else if(movement_code == 0b0110){
			delta_y = this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
			delta_x = -1 * this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
		}
		else if(movement_code == 0b0101){
			delta_y = this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
			delta_x = this.mov_speed * this.speed_boost * SIN_45 * global_delta_t;
		}
		else if(movement_code == 0b1000){
			delta_y = -1 * this.mov_speed * this.speed_boost * global_delta_t;
		}
		else if(movement_code == 0b0100){
			delta_y = this.mov_speed * this.speed_boost * global_delta_t;
		}
		else if(movement_code == 0b0010){
			delta_x = -1 * this.mov_speed * this.speed_boost * global_delta_t;
		}
		else if(movement_code == 0b0001){
			delta_x = this.mov_speed * this.speed_boost * global_delta_t;
		}
		
		if(delta_x != 0){
			this.x += delta_x;
			gt5 -= (delta_x * gt1);
			for(let i = 0; i < walls.length; i++){
				if(isColliding(this, walls[i]))
				{
					//if the player hit a wall, reset the player positions and global transforms
					this.x -= delta_x;
					gt5 += (delta_x * gt1);
					break; //does not check the other walls if at least one was hit
				}
			}
		}
		if(delta_y != 0){
			this.y += delta_y;
			gt6 -= (delta_y * gt4);
			for(let i = 0; i < walls.length; i++){
				if(isColliding(this, walls[i]))
				{
					//if the player hit a wall, reset the player positions and global transforms
					this.y -= delta_y;
					gt6 += (delta_y * gt4);
					break; //does not check the other walls if at least one was hit
				}
			}
		}
		
		
		if(keys_down.includes(49)){
			this.health -= 1;
		}
		if(keys_down.includes(50)){
			this.health += 1;
		}
		
		//shoot bullets
		if (keys_down.includes(32)) {
			if (last_shot_time == 0) {
				bullets.push(new Bullet(grid_length * 0.15, grid_length * 0.15, bullet_image, this.x, this.y, this.team));
				//make bullet sound
				let sound_test = new SoundEmitter(gunshot1, false, 0, 0, 1.0);
				sound_test.play();
				last_shot_time = Date.now();
			}
			else if ((Date.now() - last_shot_time) >= TIME_BETWEEN_SHOTS) {
				bullets.push(new Bullet(grid_length * 0.15, grid_length * 0.15, bullet_image, this.x, this.y, this.team));
				//make bullet sound
				let sound_test = new SoundEmitter(gunshot1, false, 0, 0, 1.0);
				sound_test.play();
				last_shot_time = Date.now();
			}
		}
	}
	
	this.toDataString = function(){
		/*
		let output = "000" + ","; //the temporary image code
		output += this.sprIdx + ",";
		output += this.x + ",";
		output += this.y + ",";
		output += this.dWidth + ",";
		output += this.dHeight + ",";
		output += this.r + ","; //in radians!
		output += this.a + ",";
		output += this.team;
		return output;
		*/
		let output = this.client_id + ",";
		output += this.x + ",";
		output += this.y;
		return output;
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
	
	this.draw = function(){
		this.sprite = this.image.sprites[this.sprIdx]; //make sure the correct sprite is being displayed
		context.setTransform(1, 0, 0, 1, this.x, this.y); //set draw position
		context.rotate(this.r); //this is in radians
		context.globalAlpha = this.a;
		context.drawImage(this.image, this.sprite.x, this.sprite.y, this.sprite.w, this.sprite.h, -this.dWidth/2, -this.dHeight/2, this.dWidth, this.dHeight);
	}
}

function TiledBackground(img){
	this.img = img;
	this.tile_list = [];
	for(let i = 0; i < bg_width_grids; i++){
		for(let j = 0; j < bg_height_grids; j++){
			this.tile_list.push(new BGTile(img, i, j, getWeightedIndex(this.img.chances)));
		}
	}
	
	this.draw = function(){
		for(let i = 0; i < this.tile_list.length; i++){
			this.tile_list[i].draw();
		}
	}
}

function BGTile(img, grid_x, grid_y, index){
	this.grid_x = grid_x;
	this.grid_y = grid_y;
	this.base = Entity;
	//we will use rotate 0, trans 1
	this.base(img, index, (this.grid_x * grid_length) + (grid_length/2), (this.grid_y * grid_length) + (grid_length/2), grid_length, grid_length, 0, 1);
}

//this code executes right when the page is loaded
//but it needs to be at the end of the file because it references
//certain functions in util.js that require classes (that exist in this file)
//to have already been defined
setup(); //only call setup once