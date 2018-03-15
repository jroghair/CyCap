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
}

let gunshot1 = document.createElement("audio");
gunshot1.src = "res/sounds/m9_gunshot.mp3";
gunshot1.setAttribute("preload", "auto");
gunshot1.setAttribute("controls", "none");