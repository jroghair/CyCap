let innerRad = 1; //in grids
let outerRad = 15; //in grids

function SoundEmitter(src, is_looping, x, y, start_vol){
	this.x = x;
	this.y = y;
	this.sound = src.cloneNode();
	this.sound.volume = start_vol;
	if(is_looping == true){
		this.sound.loop = true;
	}
    this.sound.style.display = "none";
    document.body.appendChild(this.sound);
	
    this.play = function(){
        this.sound.play();
    }
	
    this.stop = function(){
        this.sound.pause();
    }
	
	this.update = function(){
		this.sound.volume = this.getVolume();
	}
	this.getVolume = function(){
		let distance = distanceBetween(gameState.player.x, gameState.player.y, this.x, this.y);
		if(distance > (outerRad * grid_length)){
			return 0.0;
		}
		else if(distance < (innerRad * grid_length)){
			return 1.0;
		}
		else{
			return 1.0 - (distance - (innerRad * grid_length))/((outerRad - innerRad) * grid_length);
		}
	}
}

let gunshot1 = document.createElement("audio");
gunshot1.src = "res/sounds/m9_gunshot.mp3";
gunshot1.setAttribute("preload", "auto");
gunshot1.setAttribute("controls", "none");