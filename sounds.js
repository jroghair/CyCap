function SoundEmiter(src, is_looping, x, y, start_vol){
	this.x = x;
	this.y = y;
	this.sound = document.createElement("audio");
    this.sound.src = src;
    this.sound.setAttribute("preload", "auto");
    this.sound.setAttribute("controls", "none");
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