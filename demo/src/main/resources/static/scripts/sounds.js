function SoundEmitter(src, is_looping, x, y){
	this.x = x;
	this.y = y;
	this.sound = src.cloneNode();
	this.sound.loop = is_looping;
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
		let distance = distanceBetween(gameState.player.x, gameState.player.y, this.x, this.y) / grid_length;
		if(distance > 16){
			return 0.0;
		}
		else{
			return Math.min(0.946*Math.exp(-0.48*distance), 1.0);
		}
	}
	
	this.sound.volume = this.getVolume();
}

function SoundFactory(){
	this.sounds = [];
}

let gunshot1 = document.createElement("audio");
gunshot1.src = "res/sounds/m9_gunshot.mp3";
gunshot1.setAttribute("preload", "auto");
gunshot1.setAttribute("controls", "none");