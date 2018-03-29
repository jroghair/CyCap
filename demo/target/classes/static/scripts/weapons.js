function Weapon(name, ft, bt, damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation, selector_icon){
	this.name = name;
	this.selector_icon = selector_icon;
	
	this.fire_type = ft;	//this can be single, burst, auto, spray, artillery, and blast
	this.bullet_type = bt;  //this is the index in the sprite sheet of the bullet used
	this.damage = damage;
	
	this.fire_rate = rate; //the rate in between shots (even for semi-auto weapons) in ms
	this.lastShot = Date.now();
	this.bullet_speed = bullet_speed;
	this.shot_variation = shot_variation; 	//shot_variation
	
	this.reload_time = reload_time;//reload time
	this.is_reloading = false;
	this.reload_start_time = 0;
	
	this.mag_size = mag_size; //magazine size
	this.ammo_in_clip = this.mag_size;  //the clip is always full to start
	this.extra_ammo = extra_mags * this.mag_size; //total ammo. this include any ammo in magazine
	
	//updates important info about the weapon
	this.update = function(player, snapshot){
		//if not reloading, check to see if it is fired
		if(!this.is_reloading){
			this.checkFire(player, snapshot);
		}
		else{
			//play reloading sound or something
		}
	}
	
	//checks to see if the gun is to be fired, based off of input type and the amount of bullets in the magazine
	this.checkFire = function(player, snapshot){
		switch(this.fire_type){
			
			case "spray":
				//intentional fall-through, spray guns like a shotgun are triggered the same way as single fire guns
				
			case "single":
				if(snapshot.mouse_clicked && ((Date.now() - this.lastShot) >= this.fire_rate)){ //TODO: remove dependency on input_handler
					if(this.ammo_in_clip - 1 != -1){
						this.fire(player, snapshot);
						this.lastShot = Date.now();
					}
					else{
						//play click sound
					}
				}
				break;
				
			case "burst":
				//something
				break;
				
			case "auto":
				break;
				
			case "artillery":
				//something
				break;
				
			case "blast":
				//something
				break;
				
			default:
				console.log("Error: weapon fire_type is not acceptable");
				break;
		}
	}
	
	this.fire = function(player, snapshot){
		return; //does nothing if it is just a weapon, must replace in subclass
	}
	
	this.reload = function(){
		let bullets_to_refill = this.mag_size - this.ammo_in_clip;
		if(this.extra_ammo == 0){
			console.log("I cannot reload");
		}
		else if(this.extra_ammo < bullets_to_refill){
			this.ammo_in_clip += this.extra_ammo;
			this.extra_ammo = 0;
		}
		else{
			this.ammo_in_clip += bullets_to_refill;
			this.extra_ammo -= bullets_to_refill;
		}
	}
}

function Pistol(damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation){
	this.base = Weapon;
	this.base("Pistol", "single", 2, damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation, pistol_icon); 
	
	this.fire = function(player, snapshot){
		this.ammo_in_clip--; //lose one bullet from the clip
		gameState.bullets.push(new Bullet(grid_length * 0.125, grid_length * 0.125, this.bullet_type, player.x, player.y, snapshot.mapX, snapshot.mapY, this.damage, this.bullet_speed, this.shot_variation));
		//make bullet sound
		let sound_test = new SoundEmitter(gunshot1, false, 0, 0, 1.0);
		sound_test.play();
	}
}

function Shotgun(damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation){
	this.base = Weapon;
	this.base("Shotgun", "spray", 3, damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation, shotgun_icon);
	
	this.fire = function(player, snapshot){
		this.ammo_in_clip--; //lose one bullet from the clip
		//make bullet sound
		let sound_test = new SoundEmitter(gunshot1, false, 0, 0, 1.0);
		sound_test.play();
		//get a random number of buckshot pellets between like 5 and 10 or something
		let num_of_pellets = getRandomInRange(5, 10);
		for(let i = 0; i < num_of_pellets; i++){
			gameState.bullets.push(new Bullet(grid_length * 0.125, grid_length * 0.125, this.bullet_type, player.x, player.y, snapshot.mapX, snapshot.mapY, this.damage/num_of_pellets, this.bullet_speed, this.shot_variation));
		}
	}
}

function AutomaticGun(name, damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation, selector_icon){
	this.base = Weapon;
	this.base(name, "auto", 1, damage, rate, bullet_speed, mag_size, extra_mags, reload_time, shot_variation, selector_icon);
	
	this.checkFire = function(player, snapshot){
		if(snapshot.lmb_down && ((Date.now() - this.lastShot) >= this.fire_rate)){ //TODO: remove dependency on input_handler
			if(this.ammo_in_clip - 1 != -1){
				this.fire(player, snapshot);
				this.lastShot = Date.now();
			}
			else{
				//play click sound
			}
		}
	}
	
	this.fire = function(player, snapshot){
		this.ammo_in_clip--; //lose one bullet from the clip
		gameState.bullets.push(new Bullet(grid_length * 0.125, grid_length * 0.125, this.bullet_type, player.x, player.y, snapshot.mapX, snapshot.mapY, this.damage, this.bullet_speed, this.shot_variation));
		//make bullet sound
		let sound_test = new SoundEmitter(gunshot1, false, 0, 0, 1.0);
		sound_test.play();
	}
}

//UNUSED WEAPONS
let smg = new AutomaticGun("SMG", 5, 100, 600, 40, 4, 500, 0.1, smg_icon);

/////DIFFERENT TYPES OF AMMUNITION/////
function Bullet(width, height, sprIdx, startX, startY, endX, endY, damage, speed, shot_variation) {
	this.x_diff = endX - startX;
	this.y_diff = (endY - startY) * -1;
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
	//bullet_image, transparency 1, angle is calculated based off of stuff
	this.base(bullet_image, sprIdx, startX, startY, width, height, angle - 90, 1);

	//this.team = player.team; //this is so we can avoid friendly fire (and maybe reduce the amount of collision checks)
	this.damage = damage;
	this.speed = speed;
	this.shot_variation = shot_variation;

	this.y_ratio = Math.sin(toRadians(angle)) * -1;
	this.x_ratio = Math.cos(toRadians(angle));
	this.y_ratio += ((Math.random() - 0.5) * this.shot_variation);
	this.x_ratio += ((Math.random() - 0.5) * this.shot_variation);

	this.update = function(snapshot){
		this.x += this.speed * this.x_ratio * snapshot.frameTime;
		this.y += this.speed * this.y_ratio * snapshot.frameTime;

		for(var j = 0; j < walls.length; j++){
			if(isColliding(this, walls[j]))
			{
				let temp_index = gameState.bullets.indexOf(this);
				gameState.bullets.splice(temp_index, 1);
				break;
			}
			
		}
	}
}

function ArtilleryShell(width, height, start_x, start_y, end_x, end_y, img, team, blast_img){
	this.start_time = Date.now();
	this.blast_img = blast_img;
	this.start_x = start_x;
	this.start_y = start_y;
	this.end_x = end_x;
	this.end_y = end_y;
	this.start_width = width;//this is the initial width
	this.start_height = height;//this is the initial height

	this.x_vel = 1000 * (this.end_x - this.start_x) / ARTILLERY_TIME; //pixels per second
	this.y_vel = 1000 * (this.end_y - this.start_y) / ARTILLERY_TIME; //pixels per second
	this.v_init = GRAVITY * (ARTILLERY_TIME/1000);

	
	this.base = Entity;
	//sprite 1 on the bullet sheet, rotation 0, transparency 1
	this.base(img, 1, this.start_x, this.start_y, width, height, 0, 1);

	this.update = function(snapshot){
		if((Date.now() - this.start_time) < ARTILLERY_TIME){
			this.x += (this.x_vel * snapshot.frameTime);
			this.y += (this.y_vel * snapshot.frametime);

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
			let temp_index = gameState.bullets.indexOf(this);
			gameState.bullets.splice(temp_index, 1);
		}
	}
}