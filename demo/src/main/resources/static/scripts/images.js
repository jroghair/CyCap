//////SPRITESHEET DATA//////
let player_images = new Image();
player_images.src = "res/images/player_images.png";
player_images.sprites = [{x:0, y:0, w:128, h:128}, {x:128, y:0, w:128, h:128}, {x:256, y:0, w:128, h:128},
						 {x:0, y:128, w:128, h:128}, {x:128, y:128, w:128, h:128}, {x:256, y:128, w:128, h:128}];

let enemy_image = new Image();
enemy_image.src = "res/images/enemy.png";
enemy_image.sprites = [{x:0, y:0, h:128, w:128}];

let bullet_image = new Image();
bullet_image.src = "res/images/bullets.png";
bullet_image.sprites = [{x:0, y:0, w:128, h:128}, {x:128, y:0, w:128, h:128}, {x:0, y:128, w:128, h:128}, {x:128, y:128, w:128, h:128}];

//MAP INFORMATION
let background_tiles = new Image();
background_tiles.src = "res/images/map_tiles_ss.png";
background_tiles.sprites = [{x:0, y:0, w:64, h:64}, {x:64, y:0, w:64, h:64}, {x:0, y:64, w:64, h:64}, {x:64, y:64, w:64, h:64}];
background_tiles.chances = [0.95, 0.04, 0.007, 0.003];
let grid_length = 32;
let bg_width_grids = 41;
let bg_height_grids = 30;

let wall_image = new Image();
wall_image.src = "res/images/wall2.png";
wall_image.sprites = [{x:0, y:0, w:64, h:64}];

let blast1_image = new Image();
blast1_image.src = "res/images/blast1.png";
blast1_image.sprites = [{x:0, y:0, w:128, h:128}];

let speed_potion_ss = new Image();
speed_potion_ss.src = "res/images/speed_potion.png";
speed_potion_ss.sprites = [{x:0, y:0, w:128, h:128}, {x:128, y:0, w:128, h:128}, {x:256, y:0, w:128, h:128}, {x:384, y:0, w:128, h:128},
						   {x:0, y:128, w:128, h:128}, {x:128, y:128, w:128, h:128}, {x:256, y:128, w:128, h:128}, {x:384, y:128, w:128, h:128}];

let shield_potion = new Image();
shield_potion.src = "res/images/shield_potion.png";
shield_potion.sprites = [{x:0, y:0, h:256, w:256}];

let health_gui = new Image();
health_gui.src = "res/images/health_bar_good.png";
health_gui.sprites = [{x:0, y:0, w:64, h:64}, {x:64, y:0, w:64, h:64}, {x:128, y:0, w:64, h:64}, {x:192, y:0, w:64, h:64},
					  {x:0, y:64, w:64, h:64}, {x:64, y:64, w:64, h:64}, {x:128, y:64, w:64, h:64}, {x:192, y:64, w:64, h:64}];
					  
let item_frame = new Image();
item_frame.src = "res/images/item_slot_frame.png";
item_frame.sprites = [{x:0, y:0, h:64, w:64}];
					  
//WEAPON SELECTION GUI IMAGES
let weapon_select_frame = new Image();
weapon_select_frame.src = "res/images/weapon_select_frame.png";
weapon_select_frame.sprites = [{x:0, y:0, h:800, w:1600}];

let weapon_selection_ring = new Image();
weapon_selection_ring.src = "res/images/gui_selection_ring.png";
weapon_selection_ring.sprites = [{x:0, y:0, h:128, w:128}];

let shotgun_icon = new Image();
shotgun_icon.src = "res/images/shotgun_icon.png";
shotgun_icon.sprites = [{x:0, y:0, h:128, w:128}];

let empty_icon = new Image();
empty_icon.src = "res/images/empty_icon.png";
empty_icon.sprites = [{x:0, y:0, h:128, w:128}];

let pistol_icon = new Image();
pistol_icon.src = "res/images/pistol_icon.png";
pistol_icon.sprites = [{x:0, y:0, h:128, w:128}];

let boom_ss = new Image();
boom_ss.src = "res/images/explosion_ss.png";
//boom_ss.src = "explosion_ss2.png";
boom_ss.sprites = [{x:0, y:0, w:114, h:114}, {x:114, y:0, w:114, h:114}, {x:227, y:0, w:114, h:114},
				   {x:340, y:0, w:114, h:114}, {x:454, y:0, w:114, h:114}, {x:568, y:0, w:114, h:114},
				   {x:682, y:0, w:114, h:114}, {x:796, y:0, w:114, h:114}, {x:910, y:0, w:114, h:114},
				   
				   {x:0, y:114, w:114, h:114}, {x:114, y:114, w:114, h:114}, {x:227, y:114, w:114, h:114},
				   {x:340, y:114, w:114, h:114}, {x:454, y:114, w:114, h:114}, {x:568, y:114, w:114, h:114},
				   {x:682, y:114, w:114, h:114}, {x:796, y:114, w:114, h:114}, {x:910, y:114, w:114, h:114},
				   
				   {x:0, y:227, w:114, h:114}, {x:114, y:227, w:114, h:114}, {x:227, y:227, w:114, h:114},
				   {x:340, y:227, w:114, h:114}, {x:454, y:227, w:114, h:114}, {x:568, y:227, w:114, h:114},
				   {x:682, y:227, w:114, h:114}, {x:796, y:227, w:114, h:114}, {x:910, y:227, w:114, h:114},
				   
				   {x:0, y:340, w:114, h:114}, {x:114, y:340, w:114, h:114}, {x:227, y:340, w:114, h:114},
				   {x:340, y:340, w:114, h:114}, {x:454, y:340, w:114, h:114}, {x:568, y:340, w:114, h:114},
				   {x:682, y:340, w:114, h:114}, {x:796, y:340, w:114, h:114}, {x:910, y:340, w:114, h:114},
				   
				   {x:0, y:454, w:114, h:114}, {x:114, y:454, w:114, h:114}, {x:227, y:454, w:114, h:114},
				   {x:340, y:454, w:114, h:114}, {x:454, y:454, w:114, h:114}, {x:568, y:454, w:114, h:114},
				   {x:682, y:454, w:114, h:114}, {x:796, y:454, w:114, h:114}, {x:910, y:454, w:114, h:114},
				   
				   {x:0, y:568, w:114, h:114}, {x:114, y:568, w:114, h:114}, {x:227, y:568, w:114, h:114},
				   {x:340, y:568, w:114, h:114}, {x:454, y:568, w:114, h:114}, {x:568, y:568, w:114, h:114},
				   {x:682, y:568, w:114, h:114}, {x:796, y:568, w:114, h:114}, {x:910, y:568, w:114, h:114},
				   
				   {x:0, y:682, w:114, h:114}, {x:114, y:682, w:114, h:114}, {x:227, y:682, w:114, h:114},
				   {x:340, y:682, w:114, h:114}, {x:454, y:682, w:114, h:114}, {x:568, y:682, w:114, h:114},
				   {x:682, y:682, w:114, h:114}, {x:796, y:682, w:114, h:114}, {x:910, y:682, w:114, h:114},
				   
				   {x:0, y:796, w:114, h:114}, {x:114, y:796, w:114, h:114}, {x:227, y:796, w:114, h:114},
				   {x:340, y:796, w:114, h:114}, {x:454, y:796, w:114, h:114}, {x:568, y:796, w:114, h:114},
				   {x:682, y:796, w:114, h:114}, {x:796, y:796, w:114, h:114}, {x:910, y:796, w:114, h:114},
				   
				   {x:0, y:910, w:114, h:114}, {x:114, y:910, w:114, h:114}];
				   
let flags_ss = new Image();
flags_ss.src = "res/images/flags.png";
flags_ss.sprites = [{x:0, y:0, w:128, h:128}, {x:128, y:0, w:128, h:128}, {x:256, y:0, w:128, h:128},
                    {x:0, y:128, w:128, h:128}, {x:128, y:128, w:128, h:128}, {x:256, y:128, w:128, h:128}];
					
let image_codes = [{code:0, img:player_images}, {code:1, img:enemy_image}, {code:2, img:bullet_image}];

function findImageFromCode(code){
	for(let i = 0; i < image_codes.length; i++){
		if(image_codes[i].code == code){
			return image_codes[i].img;
		}
	}
}