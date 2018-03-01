function Weapon(name, ft, bt, damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation){
	this.name = name;
	
	this.fire_type = ft;	//this can be single, burst, auto, spray, artillery, and blast
	this.bullet_type = bt;  //this is the index in the sprite sheet of the bullet used
	this.damage = damage;
	this.fire_rate = rate;
	this.bullet_speed = bullet_speed;
	this.reload_time = reload_time;//reload time
	this.shot_variation = shot_variation; 	//shot_variation
	
	this.mag_size = mag_size; //magazine size
	this.ammo_in_clip = this.mag_size;  //the clip is always full to start
	this.total_ammo = (extra_mags * this.mag_size) + this.ammo_in_clip; //total ammo. this include any ammo in magazine
	
	this.fire = function(startX, startY, mouseX, mouseY){
		
	}
}

function Pistol(damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation){
	this.base = Weapon;
	this.base("Pistol", "single", 2, damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation); 
}