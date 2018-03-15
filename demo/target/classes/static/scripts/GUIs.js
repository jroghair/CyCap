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

function HealthGUI(img, x, y, width, height, elemIndex, num_frames){
	this.base = GuiElement;
	this.base(img, x, y, width, height, elemIndex);
	this.num_frames = num_frames;
	
	this.update = function(){
		this.sprIdx = 8 - Math.round(gameState.player.health/gameState.player.max_hp * this.num_frames);
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
			this.item_image.image = gameState.player.item_slot.image;
			this.item_image.sprIdx = gameState.player.item_slot.sprIdx;
		}
	}
	
	this.draw = function(){
		//draw stuff
		this.item_frame.draw();
		this.item_image.draw();
	}
}