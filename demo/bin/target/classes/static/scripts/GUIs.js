function GuiElement(img, x, y, width, height, elemIndex){
	this.base = Entity;
	this.base(img, elemIndex, x, y, width, height, 0, 1);

	this.update = function(){
		return;
	}

	this.draw = function(){
		this.sprite = this.image.sprites[this.sprIdx]; //make sure the correct sprite is being displayed
		gui_context.setTransform(1, 0, 0, 1, this.x, this.y); //set draw position
		gui_context.rotate(this.r); //this is in radians
		gui_context.globalAlpha = this.a;
		gui_context.drawImage(this.image, this.sprite.x, this.sprite.y, this.sprite.w, this.sprite.h, -this.dWidth/2, -this.dHeight/2, this.dWidth, this.dHeight);
	}
}

//x & y are the coordinates for the bottom left corner
function HealthGUI(x, y, width, height){
	this.x = x;
	this.y = y;
	this.health_frame = new GuiElement(item_frame, x + width/2, y - height/2, width, height, 0);
	this.pixelsPerHP = (width - 4)/gameState.player.max_hp;
	this.health_bar = new GuiElement(color_boxes, x + width/2, y - height/2, width - 4, height - 4, 0);
	
	this.update = function(){
		//only need to update the health_bar
		this.pixelsPerHP = (width - 4)/gameState.player.max_hp;
		let newWidth;
		if(gameState.player.health >= 0){
			newWidth = this.pixelsPerHP * gameState.player.health;
		}
		else{
			newWidth = 0;
		}
		this.health_bar.dWidth = newWidth;
		//update it's x position, y position does not need to be
		this.health_bar.x = this.x + newWidth/2 + 2;
		
	}
	
	this.draw = function(){
		this.health_frame.draw();
		this.health_bar.draw();
	}
}

function AmmoGUI(x, y, height, width, barWidth){
	this.bars = []; //in the future i think I will want fun bars for each bullet instead of a single bar
	this.barWidth = barWidth;
	
	this.x = x;
	this.y = y;
	this.ammoFrame = new GuiElement(item_frame, x + width/2, y - height/2, width, height, 0);
	this.pixelsPerBullet = (width - 4)/gameState.player.currentWeapon.mag_size;
	this.ammoBar = new GuiElement(color_boxes, x + width/2, y - height/2, width - 4, height - 4, 3);
	
	this.update = function(){
		this.pixelsPerBullet = (width - 4)/gameState.player.currentWeapon.mag_size;
		let newWidth;
		if(gameState.player.currentWeapon.ammo_in_clip >= 0){
			newWidth = this.pixelsPerBullet * gameState.player.currentWeapon.ammo_in_clip;
		}
		else{
			newWidth = 0;
		}
		this.ammoBar.dWidth = newWidth;
		//update it's x position, y position does not need to be
		this.ammoBar.x = this.x + newWidth/2 + 2;
	}
	
	this.draw = function(){
		this.ammoFrame.draw();
		this.ammoBar.draw();
	}
}

function WeaponSelectGUI(){
	this.gui_frame = new GuiElement(weapon_select_frame, 800, 400, 1600, 800, 0);
	if(gameState.player.weapon1 != "EMPTY"){
		this.weapon1_icon = new GuiElement(gameState.player.weapon1.selector_icon, 94, 74, 111, 111, 0);
	}
	else{
		this.weapon1_icon = new GuiElement(empty_icon, 94, 74, 111, 111, 0);
	}
	
	if(gameState.player.weapon2 != "EMPTY"){
		this.weapon2_icon = new GuiElement(gameState.player.weapon2.selector_icon, 223, 74, 111, 111, 0);
	}
	else{
		this.weapon2_icon = new GuiElement(empty_icon, 223, 74, 111, 111, 0);
	}
	
	if(gameState.player.weapon3 != "EMPTY"){
		this.weapon3_icon = new GuiElement(gameState.player.weapon3.selector_icon, 352, 74, 111, 111, 0);
	}
	else{
		this.weapon3_icon = new GuiElement(empty_icon, 352, 74, 111, 111, 0);
	}
	
	if(gameState.player.weapon4 != "EMPTY"){
		this.weapon4_icon = new GuiElement(gameState.player.weapon4.selector_icon, 481, 74, 111, 111, 0);
	}
	else{
		this.weapon4_icon = new GuiElement(empty_icon, 481, 74, 111, 111, 0);
	}
	
	this.selection_ring = new GuiElement(weapon_selection_ring, 94, 74, 128, 128, 0);
	
	this.update = function(){
		this.gui_frame.update();
		if(gameState.player.currentWeapon == gameState.player.weapon1){
			this.selection_ring.x = 94;
		}
		else if(gameState.player.currentWeapon == gameState.player.weapon2){
			this.selection_ring.x = 223;
		}
		else if(gameState.player.currentWeapon == gameState.player.weapon3){
			this.selection_ring.x = 352;
		}
		else if(gameState.player.currentWeapon == gameState.player.weapon4){
			this.selection_ring.x = 481;
		}
	}

	this.draw = function(){
		this.gui_frame.draw();
		this.weapon1_icon.draw();
		this.weapon2_icon.draw();
		this.weapon3_icon.draw();
		this.weapon4_icon.draw();
		this.selection_ring.draw();
	}
}

function ItemSlotGUI(x, y){
	this.item_frame = new GuiElement(item_frame, x, y, 70, 70, 0);
	this.item_image = new GuiElement(item_frame, x, y, 64, 64, 0);
	
	this.update = function(){
		if(gameState.player.item_slot == "EMPTY"){
			this.item_image.a = 0.0; //make invisible
		}
		else{
			this.item_image.a = 1.0; //make visible
			this.item_image.image = findImageFromCode(gameState.player.item_slot);
		}
	}
	
	this.draw = function(){
		//draw stuff
		this.item_frame.draw();
		this.item_image.draw();
	}
}

function TextGUI(txt, font, x, y, a){
	this.txt = txt;
	this.font = font;
	this.x = x;
	this.y = y;
	this.a = a;
	this.active = true;
	
	this.update = function(txt){
		this.txt = txt;
	}
	
	this.draw = function(){
		if(this.active){
			gui_context.setTransform(1, 0, 0, 1, 0, 0); //set draw position
			gui_context.globalAlpha = this.a;
			gui_context.font = this.font;
			gui_context.fillText(this.txt, this.x, this.y);
		}
	}
}

function GameScoreGUI(x, y, type){
	this.base = TextGUI;
	this.base("Red: 0  |  Blue: 0", "25px Arial", x - 100, y + 20, 1.0);
	this.game_type = type;
	if(this.game_type == "ctf"){
		this.txt = "Red: 0  |  Blue: 0";
	}
	
	this.update = function(txt){
		let data = txt.split(",");
		if(this.game_type == "ctf"){
			this.txt = "Red: " + data[1] + "  |  Blue: " + data[2];
		}
	}
	
}

function RespawnCounter(x, y, length){
	this.base = TextGUI;
	this.base("", "25px Arial", x - 100, y + 11, 1.0);
	this.length = length; //this is in ms
	this.startTime = 0;
	this.active = false;
	
	this.start = function(){
		if(!this.active){
			this.a = 1.0
			this.active = true;
			this.startTime = Date.now();
			let time = (this.length - (Date.now() - this.startTime))/1000;
			this.txt = "Respawning In: " + time.toFixed(2);
		}
	}
	
	this.update = function(txt){
		if((Date.now() - this.startTime) >= this.length){
			if(this.active && gameState.player.health > 0){
				this.stop();
			}
		}
		else{
			let time = (this.length - (Date.now() - this.startTime))/1000;
			this.txt = "Respawning In: " + time.toFixed(2);
		}
	}
	
	this.stop = function(){
		this.active = false;
		this.a = 0.0;
	}
}