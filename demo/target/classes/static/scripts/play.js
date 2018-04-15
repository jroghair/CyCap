//CANVAS INFORMATION
let canvas = document.getElementById("game_canvas");
let context = canvas.getContext("2d");
canvas.height = document.documentElement.clientHeight;
canvas.width = document.documentElement.clientWidth;

let fog_canvas = document.getElementById("fog_canvas");
let fog_context = fog_canvas.getContext("2d");
fog_canvas.height = canvas.height;
fog_canvas.width = canvas.width;

let gui_canvas = document.getElementById("gui_canvas");
let gui_context = gui_canvas.getContext("2d");
gui_canvas.height = canvas.height;
gui_canvas.width = canvas.width;

//INPUT INFORMATION
let input_handler;

//MOVEMENT AND COLLISION CONSTANTS
const UP    = 0b1000;
const DOWN  = 0b0100;
const LEFT  = 0b0010;
const RIGHT = 0b0001;

//GAME STATE OBJECTS
let gameState;
let part_fx = [];
let map;
let masks = [];

//NON-SERVER-BASED ITEMS
let canvas_box;
let guis = [];
let respawnCounter;
let gameScoreGUI;

//FRAME TIME & DELTA_T
let lastFrameTime;
let global_delta_t = 0; //the time that this frame took in SECONDS

let current_zoom_lvl = 2;

//////TESTING//////
//stuff for fps testing
let is_fps_running = true;
let fps_frame_times = [];
let rolling_buffer_length = 30;
///////////////////

function GameState(role){
	this.game_mode = "ctf";
	
	this.player = new Player(grid_length, grid_length, player_images, 64, 64, role, "1", client_id); //the current player on this client
	this.pw;
	
	this.intp_entities = [];
	this.walls = []; //all of the walls in the game
	this.part_fx = []; //particle effects
	this.map; //the set of tiles and map data
	this.masks = []; //the list of ground masks
	
	this.lastTime = 0;
	this.currentServerTimeStep = 0;
	
	//this takes in the message from the server and builds the game state from that
	this.receiveGameState = function(message){
		
		this.currentServerTimeStep = Date.now() - this.lastTime;
		this.lastTime = Date.now();
		
		let objects = message.split(":");
		
		//set all interpolating entities' updated fields to false
		for(let i = 0; i < this.intp_entities.length; i++){
			this.intp_entities[i].updated = false;
		}
		for(let i = 0; i < this.part_fx.length; i++){
			this.part_fx[i].updated = false;
		}
		
		for(let i = 0; i < objects.length; i++){
			let obj = objects[i].split(",");
			
			//player
			if(obj[0] == "000"){
				if(obj[2] == this.player.client_id){ //is the current player
					input_handler.removeHandledSnapshots(+obj[1]);
					this.player.resetData(+obj[3], +obj[4], +obj[5], +obj[6], +obj[7], +obj[8], +obj[9], +obj[10]);
					this.player.team = +obj[12];
					this.player.updateCurrentWeapon(obj[13]);
					if(obj[14] != "empty"){
						this.player.item_slot = +obj[14];
					}
					else{
						this.player.item_slot = "EMPTY";
					}
					this.player.health = +obj[15];
					this.player.is_invincible = obj[16];
					this.player.speed_boost = +obj[17];
					this.player.damage_boost = +obj[18];
					this.player.visibility = +obj[19];
					
					if(this.player.health <= 0){
						respawnCounter.start();
					}
				}
			}
			else if(obj[0] == "001"){
				if(this.game_mode == "ctf"){
					//TODO: update the game score gui
					gameScoreGUI.update(objects[i]);
				}
			}
			else if(obj[0] == "002"){
				//new sound
				if(obj[3] == "m9_gunshot"){
					let sound_test = new SoundEmitter(gunshot1, false, +obj[1], +obj[2]);
					sound_test.play();
				}
			}
			else if(obj[0] == "003"){
				let found = false;
				for(let i = 0; i < this.part_fx.length; i++){
					if(this.part_fx[i].entity_id == obj[1]){
						found = true;
						console.log(obj);
						this.part_fx[i].updateNewEntity(new Entity(findImageFromCode(+obj[2]), +obj[3], +obj[4], +obj[5], +obj[6], +obj[7], +obj[8], obj[9]), this.currentServerTimeStep);
						this.part_fx[i].updated = true;
						break;
					}
				}
				if(!found){
					this.part_fx.push(new InterpolatingEntity(obj[1], new Entity(findImageFromCode(+obj[2]), +obj[3], +obj[4], +obj[5], +obj[6], +obj[7], +obj[8], obj[9])));
				}
			}
			else if(obj[0] == "012"){
				//add wall
				this.addWall(obj);
			}
			else if(obj[0] == "013"){
				//remove wall
				this.removeWall(obj);
			}
			else if(obj[0] == "020"){
				let found = false;
				for(let i = 0; i < this.intp_entities.length; i++){
					if(this.intp_entities[i].entity_id == obj[1]){
						found = true;
						this.intp_entities[i].updateNewEntity(new Entity(findImageFromCode(+obj[2]), +obj[3], +obj[4], +obj[5], +obj[6], +obj[7], +obj[8], obj[9]), this.currentServerTimeStep);
						this.intp_entities[i].updated = true;
						break;
					}
				}
				if(!found){
					this.intp_entities.push(new InterpolatingEntity(obj[1], new Entity(findImageFromCode(+obj[2]), +obj[3], +obj[4], +obj[5], +obj[6], +obj[7], +obj[8], obj[9])));
				}
			}
		}
		
		//delete the interpolating entities that weren't updated
		for(let i = this.intp_entities.length - 1; i >= 0; i--){
			if(this.intp_entities[i].updated == false){
				this.intp_entities.splice(i, 1);
			}
		}
		for(let i = this.part_fx.length - 1; i >= 0; i--){
			if(this.part_fx[i].updated == false){
				this.part_fx.splice(i, 1);
			}
		}
		
		//apply the ClientPrediction
		for(let i = 0; i < input_handler.clientPredictiveState.length; i++){
			this.player.update(input_handler.clientPredictiveState[i]);
		}
	}
	
	this.addWall = function(wallObject){
		this.walls.push(new Wall(wall_image, +wallObject[2], +wallObject[3]));
	}
	
	this.addWalls = function(wallList){
		for(let i = 0; i < wallList.length; i++){
			let obj = wallList[i].split(",");
			this.addWall(obj);
		}
	}
	
	this.removeWall = function(wallObject){
		for(let i = 0; i < this.walls.length; i++){
			if(+wallObject[2] == this.walls[i].grid_x  && +wallObject[3] == this.walls[i].grid_y){
				this.walls.splice(i, 1);
				return;
			}
		}
	}
	
	this.updateGameState = function(snapshot) {
		this.player.update(snapshot);
		for(let i = 0; i < this.intp_entities.length; i++){
			this.intp_entities[i].update();
		}
		for(let i = 0; i < this.part_fx.length; i++){
			this.part_fx[i].update();
		}
	}
	
	this.drawGameState = function(){
		/*
		this.map.draw();
		for(let i = 0; i < this.masks.length; i++){
			this.masks[i].draw();
		}*/
		for(let i = 0; i < this.walls.length; i++){
			this.walls[i].draw();
		}
		
		
		for(let i = 0; i < this.intp_entities.length; i++){
			this.intp_entities[i].draw();
		}
		this.player.draw();
		
		for(let i = 0; i < this.part_fx.length; i++){
			this.part_fx[i].draw();
		}
	}
}

//all functions
function setup() {
	let role = "";
	while(!((role == "scout") || (role == "recruit") || (role == "infantry"))){
		role = prompt("Please choose a class. The acceptable options are \"scout\", \"recruit\", or \"infantry\". Please type carefully."); 
	}
	
	//initialize the game state
	gameState = new GameState(role);

	//Draw fog of war images and put the normal zoom one on
	drawFogOfWarImages(gameState.player.visibility);
	fog_context.putImageData(fog_norm, 0, 0);

	//set the global transforms
	gt1 = 1; //x scale
	gt2 = 0; //x skew
	gt3 = 0; //y skew
	gt4 = 1; //y scale
	gt5 = 0; //x trans
	gt6 = 0; //y trans

	gt5 = -1 * ((gameState.player.x * gt1) - (canvas.width / 2));
	gt6 = -1 * ((gameState.player.y * gt4) - (canvas.height / 2));

	canvas_box = new Entity(background_tiles, 0, gameState.player.x, gameState.player.y, canvas.width, canvas.height, 0, 0); //invisible box to determine whether or not to display an entity
	map = new TiledBackground(background_tiles);
	
	//////GUI ELEMENTS//////
	guis.push(new HealthGUI(30, gui_canvas.height - 20, 200, 20)); //health bar
	guis.push(new WeaponSelectGUI());
	guis.push(new ItemSlotGUI(gui_canvas.width - 45, gui_canvas.height - 45));
	guis.push(new AmmoGUI(30, gui_canvas.height - 50, 20, 200, 5));
	respawnCounter = new  RespawnCounter(gui_canvas.width/2, gui_canvas.height/2, 9900);
	gameScoreGUI = new GameScoreGUI(gui_canvas.width/2, 0, gameState.game_mode);
	////////////////////////

	//////INPUT HANDLING//////
	input_handler = new InputHandler();
	//when a key goes down it is added to a list and when it goes up its taken out
	document.addEventListener("keydown", function(event) {
		if (!input_handler.keys_down.includes(event.keyCode)) {
			input_handler.keys_down.push(event.keyCode);
		}
	});
	document.addEventListener("keyup", function(event) {
		input_handler.keys_down.splice(input_handler.keys_down.indexOf(event.keyCode), 1);
		input_handler.keys_pnr.push(event.keyCode);
	});
	
	//mouse click listener
	document.addEventListener("click", function(event) {
		input_handler.mouse_clicked = true;
	});
	
	//left mouse button listener
	document.addEventListener("mousedown", function(event) {
		if(event.button == 0){
			input_handler.lmb_down = true;
		}
	});
	document.addEventListener("mouseup", function(event) {
		if(event.button == 0){
			input_handler.lmb_down = false;
		}
	});
	window.addEventListener('mousemove', function(event){
		this.rect = canvas.getBoundingClientRect();
		input_handler.canvasX = (event.clientX - rect.left);
		input_handler.canvasY = (event.clientY - rect.top);
	}, false);

	lastFrameTime = Date.now();
	connectToServer(role);
	//loadMapFrom server
	document.getElementById("loading_screen").remove();
}

function run() {
	//debugger; //keeps people from messing with code
	
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

	context.setTransform(1, 0, 0, 1, 0, 0); //reset the transform so the clearRect function works
	context.clearRect(0, 0, canvas.width, canvas.height); //clear the canvas
	gui_context.setTransform(1, 0, 0, 1, 0, 0); //reset the transform so the clearRect function works
	gui_context.clearRect(0, 0, gui_canvas.width, gui_canvas.height); //clear the canvas
	

	//////UPDATE EVERYTHING///////
	input_handler.update();
	sendMessageToServer(input_handler.getSnapshot()); //sends the InputSnapshot to the server
	gameState.updateGameState(input_handler.getMostRecentInput()); //updates player and bullet stuff
	canvas_box.x = gameState.player.x; //update the canvas_box position
	canvas_box.y = gameState.player.y;

	for(let i = masks.length - 1; i >= 0; i--){
		masks[i].update(); //we go through this backwards so that if one is removed, it still checks the others
	}
	for(let i = part_fx.length - 1; i >= 0; i--){
		part_fx[i].update(); //we go through this backwards so that if one is removed, it still checks the others
	}
	for(let i = guis.length - 1; i >= 0; i--){
		guis[i].update();
	}
	respawnCounter.update(null);
	
	
	//TOGGLE THE ZOOM LEVEL V IMPORTANT
	if(input_handler.keys_pnr.includes(90)){
		//switch zoom level!
		ToggleZoom();
	}
	gt5 = -1 * ((gameState.player.x * gt1) - (canvas.width / 2));
	gt6 = -1 * ((gameState.player.y * gt4) - (canvas.height / 2));

	
	//////DRAW EVERYTHING///////
	map.draw();
	for(let i = 0; i < masks.length; i++){
		masks[i].draw();
	}
	
	gameState.drawGameState(); //draws the player and the bullets
	
	for(let i = 0; i < part_fx.length; i++){
		part_fx[i].draw();
	}

	//need to draw rectangles outside of the map that
	//are essentially below the GUI, but above pretty much everything else
	//this keeps the fog of war and particle effects from displaying outside of the map

	//draw GUI
	for(let i = 0; i < guis.length; i++){
		guis[i].draw();
	}
	respawnCounter.draw();
	gameScoreGUI.draw();
	
	//reset the 1 frame inputs
	input_handler.keys_pnr.splice(0, input_handler.keys_pnr.length);
	input_handler.mouse_clicked = false;
	
	requestAnimationFrame(run); //run again please
}

function ToggleZoom(){
	if(current_zoom_lvl == 1){
		current_zoom_lvl = 2;
		gt1 = NORMAL_ZOOM_LEVEL; //setting scaling to normal levels
		gt4 = NORMAL_ZOOM_LEVEL;
		gt5 = -1 * ((gameState.player.x * gt1) - (canvas.width / 2));
		gt6 = -1 * ((gameState.player.y * gt4) - (canvas.height / 2));
		fog_context.putImageData(fog_norm, 0, 0);
	}
	else if(current_zoom_lvl == 2){
		current_zoom_lvl = 3;
		gt1 = FAR_ZOOM_LEVEL; //setting scaling to wide levels
		gt4 = FAR_ZOOM_LEVEL;
		gt5 = -1 * ((gameState.player.x * gt1) - (canvas.width / 2));
		gt6 = -1 * ((gameState.player.y * gt4) - (canvas.height / 2));
		fog_context.putImageData(fog_far, 0, 0);
	}
	else if(current_zoom_lvl == 3){
		current_zoom_lvl = 1;
		gt1 = CLOSE_ZOOM_LEVEL; //setting scaling to zoomed levels
		gt4 = CLOSE_ZOOM_LEVEL;
		gt5 = -1 * ((gameState.player.x * gt1) - (canvas.width / 2));
		gt6 = -1 * ((gameState.player.y * gt4) - (canvas.height / 2));
		fog_context.putImageData(fog_close, 0, 0);
	}
	canvas_box.dWidth = canvas.width / gt1; //update the height and width of the canvas box
	canvas_box.dHeight = canvas.height / gt4;
	canvas_box.updateCollisionRadius(); //update the collision_radius so that culling still works properly with zoom
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
	this.collision_radius = distanceBetween(x, y, (x + (dWidth/2)), (y + (dHeight/2))); //TODO: update this if dWidth or dHeight ever changes!
	this.dWidth = dWidth;
	this.dHeight = dHeight;
	this.r = toRadians(r);
	this.a = a;
	
	this.resetData = function(imgCode, sprIdx, x, y, dWidth, dHeight, r, a){
		this.image = findImageFromCode(imgCode);
		this.sprIdx = sprIdx
		this.sprite = this.image.sprites[this.sprIdx];
		this.x = x;
		this.y = y;
		this.dWidth = dWidth;
		this.dHeight = dHeight;
		this.collision_radius = this.updateCollisionRadius();
		this.r = r;
		this.a = a;
	}
	
	this.updateCollisionRadius = function(){
		this.collision_radius = distanceBetween(this.x, this.y, (this.x + (this.dWidth/2)), (this.y + (this.dHeight/2)));
	}
	
	this.draw = function(){
		if(isColliding(this, canvas_box)){ //this keeps things from drawing if they are outside of the canvas
			this.sprite = this.image.sprites[Math.floor(this.sprIdx)]; //make sure the correct sprite is being displayed
			context.setTransform(gt1, gt2, gt3, gt4, Math.round(gt5 + (this.x * gt1)), Math.round(gt6 + (this.y * gt4))); //we must round the X & Y positions so that it doesn't break the textures
			//context.transform(1, 0, 0, 1, this.x, this.y); //set draw position
			context.rotate(this.r); //this is in radians
			context.globalAlpha = this.a;
			context.drawImage(this.image, this.sprite.x, this.sprite.y, this.sprite.w, this.sprite.h, -this.dWidth/2, -this.dHeight/2, this.dWidth, this.dHeight);
		}
	}
}

function InterpolatingEntity(entity_id, ent){
	this.entity_id = entity_id;
	this.last_ent = null;
	this.new_ent = ent;
	
	this.d_sprIdx = 0;
	this.d_x = 0;
	this.d_y = 0;
	this.delta_width = 0;
	this.delta_height = 0;
	this.d_r = 0;
	this.d_a = 0;
	
	this.updated = true;
	
	this.update = function(){
		if(this.last_ent != null){
			this.last_ent.sprIdx += (this.d_sprIdx * global_delta_t);
			this.last_ent.sprite = this.last_ent.image.sprites[Math.floor(this.last_ent.sprIdx)];
			this.last_ent.x += (this.d_x * global_delta_t);
			this.last_ent.y += (this.d_y * global_delta_t);
			this.last_ent.dWidth += (this.delta_width * global_delta_t);
			this.last_ent.dHeight += (this.delta_height * global_delta_t);
			this.last_ent.updateCollisionRadius();
			this.last_ent.r += (this.d_r * global_delta_t);
			this.last_ent.a += (this.d_a * global_delta_t);
		}
	}
	
	this.updateNewEntity = function(ent2, time){
		this.last_ent = this.new_ent;
		this.new_ent = ent2;
		
		this.last_ent.image = this.new_ent.image;
		this.d_sprIdx = (this.new_ent.sprIdx - this.last_ent.sprIdx) / (time/1000);
		this.d_x = (this.new_ent.x - this.last_ent.x) / (time/1000);
		this.d_y = (this.new_ent.y - this.last_ent.y) / (time/1000);
		this.delta_width = (this.new_ent.dWidth - this.last_ent.dWidth) / (time/1000);
		this.delta_height = (this.new_ent.dHeight - this.last_ent.dHeight) / (time/1000);
		this.d_r = (this.new_ent.r - this.last_ent.r) / (time/1000);
		this.d_a = (this.new_ent.a - this.last_ent.a) / (time/1000);
	}
	
	this.draw = function(){
		if(this.last_ent != null){
			this.last_ent.draw();
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
	this.max_hp = 100;
	this.health = 100;
	this.mov_speed = 150; //pixels per second
	this.weapon1 = "EMPTY";
	this.weapon2 = "EMPTY";
	this.weapon3 = "EMPTY";
	this.weapon4 = "EMPTY";
	this.item_slot = "EMPTY";
	this.visibility = 0;

	//variables for the different power-ups and if they are affecting the player
	this.is_invincible = false;
	this.speed_boost = 1.0;
	this.damage_boost = 1.0;

	this.has_flag = false;
	
	this.setRoleData = function(){
		switch(this.role){
			case "recruit":
				this.mov_speed = 140;
				this.max_hp = 100;
				this.health = this.max_hp;
				this.weapon1 = new AutomaticGun("Assault Rifle", 7, 120, 550, 30, 3, 1200, 0.08, ar_icon); //ar
				this.weapon2 = new Shotgun(30, 500, 500, 5, 4, 6000, 0.35); //remington
				this.weapon3 = "EMPTY";
				this.weapon4 = "EMPTY";
				this.currentWeapon = this.weapon1;
				this.visibility = 6;
				break;
				
			case "artillery":
				break;
				
			case "infantry":
				this.mov_speed = 140;
				this.max_hp = 105;
				this.health = this.max_hp;
				this.weapon1 = new AutomaticGun("Machine Gun", 8, 134, 450, 100, 2, 1750, 0.15, mg_icon); //mg
				this.weapon2 = "EMPTY";
				this.weapon3 = new Pistol(11, 100, 400, 8, 2, 200, 0.05); //m1911
				this.weapon4 = "EMPTY";
				this.currentWeapon = this.weapon1;
				this.visibility = 5;
				break;
				
			case "scout":
				this.mov_speed = 180;
				this.max_hp = 75;
				this.health = this.max_hp;
				this.weapon1 = new Shotgun(45, 300, 500, 2, 10, 2000, 0.7); //sawed off
				this.weapon2 = new Pistol(11, 100, 400, 8, 2, 200, 0.05); //m1911
				this.weapon3 = "EMPTY";
				this.weapon4 = "EMPTY";
				this.currentWeapon = this.weapon1;
				this.visibility = 7;
				break;
				
			default:
				break;
		}
	}
	this.setRoleData();
	this.currentWeapon = this.weapon1;
	
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

	//TODO: this needs to take in an inputSnapshot
	this.update = function(snapshot) {
		this.movePlayer(snapshot); //move the player first
		
		this.currentWeapon.update(this, snapshot); //checks to see if the current weapon is to be fired
		
		//WEAPON AND ITEM RELATED KEYPRESSES
		if(snapshot.keys_pnr.includes(49)){
			this.switchWeapon(1);
		}
		else if(snapshot.keys_pnr.includes(50)){
			this.switchWeapon(2);
		}
		else if(snapshot.keys_pnr.includes(51)){
			this.switchWeapon(3);
		}
		else if(snapshot.keys_pnr.includes(52)){
			this.switchWeapon(4);
		}
		if(snapshot.keys_pnr.includes(82)){
			this.currentWeapon.reload();
		}
		if(snapshot.keys_pnr.includes(70)){
			this.useItem();
		}
	}
	
	this.useItem = function(){
		if(this.item_slot == "EMPTY"){
			//play bad sound
			return;
		}
		else{
			this.item_slot = "EMPTY";
		}
	}
	
	this.updateCurrentWeapon = function(weapon_name){
		
	}
	
	this.switchWeapon = function(weapon_num){
		switch(weapon_num){
			case 1:
				if(this.weapon1 != "EMPTY"){
					this.currentWeapon = this.weapon1;
				}
				else{
					//TODO: play cannot switch to that weapon sound
				}
				break;
				
			case 2:
				if(this.weapon2 != "EMPTY"){
					this.currentWeapon = this.weapon2;
				}
				else{
					//TODO: play cannot switch to that weapon sound
				}
				break;
				
			case 3:
				if(this.weapon3 != "EMPTY"){
					this.currentWeapon = this.weapon3;
				}
				else{
					//TODO: play cannot switch to that weapon sound
				}
				break;
				
			case 4:
				if(this.weapon4 != "EMPTY"){
					this.currentWeapon = this.weapon4;
				}
				else{
					//TODO: play cannot switch to that weapon sound
				}
				break;
				
			default:
				console.log("Error: cannot switch to that weapon");
		}
	}
	
	this.movePlayer = function(snapshot){
		let movement_code  = 0b0000; //the binary code for which directions the player moving

		//this section will probably end up on the server
		if (snapshot.keys_down.includes(87)) {
			movement_code |= UP; //trying to move up
		}
		if (snapshot.keys_down.includes(65)) {
			movement_code |= LEFT; //trying to move left
		}
		if (snapshot.keys_down.includes(68)) {
			movement_code |= RIGHT; //trying to move right
		}
		if (snapshot.keys_down.includes(83)) {
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
			delta_y = -1 * this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
			delta_x = -1 * this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
		}
		else if(movement_code == 0b1001){
			delta_y = -1 * this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
			delta_x = this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
		}
		else if(movement_code == 0b0110){
			delta_y = this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
			delta_x = -1 * this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
		}
		else if(movement_code == 0b0101){
			delta_y = this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
			delta_x = this.mov_speed * this.speed_boost * SIN_45 * snapshot.frameTime;
		}
		else if(movement_code == 0b1000){
			delta_y = -1 * this.mov_speed * this.speed_boost * snapshot.frameTime;
		}
		else if(movement_code == 0b0100){
			delta_y = this.mov_speed * this.speed_boost * snapshot.frameTime;
		}
		else if(movement_code == 0b0010){
			delta_x = -1 * this.mov_speed * this.speed_boost * snapshot.frameTime;
		}
		else if(movement_code == 0b0001){
			delta_x = this.mov_speed * this.speed_boost * snapshot.frameTime;
		}

		if(delta_x != 0){
			this.x += delta_x;
			gt5 -= (delta_x * gt1);
			for(let i = 0; i < gameState.walls.length; i++){
				if(isColliding(this, gameState.walls[i]))
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
			for(let i = 0; i < gameState.walls.length; i++){
				if(isColliding(this, gameState.walls[i]))
				{
					//if the player hit a wall, reset the player positions and global transforms
					this.y -= delta_y;
					gt6 += (delta_y * gt4);
					break; //does not check the other walls if at least one was hit
				}
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
//certain functions in other files that require classes that exist in this file
//to have already been defined
if(document.getElementById("loading_screen").complete){
	setup(); //only call setup once
}
else{
	document.getElementById("loading_screen").onload = function(){
		setup();
	}
}