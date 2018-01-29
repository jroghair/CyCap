//global variables
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
var keys_down = []; //keys being pressed
var bullets = []; //all bullets
var player, bullet_p;
let grid_length = (canvas.width / 30); //the grid map we are using for now is 30x20
var player_speed = 3, bullet_speed = 10; //pixels. eventually we will want this to be based on grid_length/seconds
var mouseX;
var mouseY;
var last_shot_time = 0; //don't change
var time_between_shots = 150; //milliseconds. this will eventually be dependent on the role of the player, essentially which weapon they are using
var player_image = "player.png";
var bullet_image = "enemy.png";
var background_image = "grid_map_30x20.png";


//this code executes right when the page is loaded
setup(); //only call setup once



//all functions
function setup() {
  setInterval(draw, 1000 / 60); //called 60 times a second
  player = new Player(grid_length, grid_length, player_image, canvas.width / 2, canvas.height / 2, "recruit", "1");
  bullet_p = new bullet_population();
  map = new Background(background_image);
  //setting up two key listeners to improve movement
  //when a key goes down it is added to a list and when it goes up its taken out
  document.addEventListener("keydown", function(event) {
    if (!keys_down.includes(event.keyCode)) {
      keys_down.push(event.keyCode);
    }
  });
  document.addEventListener("keyup", function(event) {
    keys_down.splice(keys_down.indexOf(event.keyCode), 1);
  });
  //mouse listener for coordinates
  window.addEventListener('mousemove', getMousePosition, false);
}

function draw() {
  context.beginPath(); //so styles dont interfere
  context.clearRect(0, 0, canvas.width, canvas.height); //clear the canvas
  player.update();
  bullet_p.move_bullets();
  map.draw();
  bullet_p.draw();
  player.draw();
  context.closePath(); //so styles dont interfere
}

function getMousePosition(event) {
  this.rect = canvas.getBoundingClientRect();
  mouseX = event.clientX - rect.left;
  mouseY = event.clientY - rect.top;
}

function toRadians(angle) {
  return (angle * (Math.PI / 180.0));
}

function toDegrees(angle) {
  return (angle * (180.0 / Math.PI));
}

function Entity(width, height, img, x, y){
	this.image = new Image();
	this.image.src = img;
	this.width = width;
	this.height = height;
	//this.speedX = 0; not sure if we need speed right now, so im taking it out
	//this.speedY = 0; 
	this.x = x;
	this.y = y;
	
	this.draw = function(){
		context.drawImage(this.image, this.x, this.y, this.width, this.height);
	}
}

function Bullet(width, height, img, x, y) {
	this.base = Entity;
	this.base(width, height, img, x, y);

	this.x_diff = mouseX - player.x;
	this.y_diff = (mouseY - player.y) * -1;

	this.angle = toDegrees(Math.atan(this.y_diff / this.x_diff));

	if (this.x_diff < 0 && this.y_diff > 0) {
		this.angle += 180;
	}
	else if (this.x_diff < 0 && this.y_diff < 0) {
		this.angle += 180;
	}
	else if (this.x_diff > 0 && this.y_diff < 0) {
		this.angle += 360;
	}	
	//console.log(this.angle);

	this.y_ratio = Math.sin(toRadians(this.angle)) * -1;
	this.x_ratio = Math.cos(toRadians(this.angle));
	//console.log(this.x_ratio + ',' + this.y_ratio);
	this.y_ratio += ((Math.random() - 0.5) * 0.05);
	this.x_ratio += ((Math.random() - 0.5) * 0.05);
	//console.log(this.x_ratio + ',' + this.y_ratio);
}


function bullet_population() {
	this.draw = function() {
		for (var i = 0; i < bullets.length; i++) {
		    bullets[i].draw();
		}	
	}

	this.move_bullets = function() {
		for (var i = 0; i < bullets.length; i++) {
			//console.log(bullets[i].x_ratio + ', ' + bullets[i].y_ratio);
			bullets[i].x += bullet_speed * bullets[i].x_ratio;
			bullets[i].y += bullet_speed * bullets[i].y_ratio;
			if (bullets[i].x < 0 || bullets[i].x > canvas.width || bullets[i].y < 0 || bullets[i].y > canvas.height) {
				bullets.splice(i, 1);
			}
		}
	}
}

function Player(width, height, img, x, y, role, team) {
	this.base = Entity;
	this.base(width, height, img, x, y);
	this.role = role;
	this.team = team;
	//decide health based on role
	this.has_flag = false;
	this.mov_speed = player_speed; //this will eventually be dependent on role 

	this.update = function() {
		if (keys_down.includes(87)) {
			this.y -= this.mov_speed;
		}
		if (keys_down.includes(65)) {
			this.x -= this.mov_speed;
		}
		if (keys_down.includes(68)) {
			this.x += this.mov_speed;
		}
		if (keys_down.includes(83)) {
			this.y += this.mov_speed;
		}

		if (keys_down.includes(32)) {
			if (last_shot_time == 0) {
				bullets.push(new Bullet(grid_length * 0.15, grid_length * 0.15, bullet_image, this.x, this.y));
				last_shot_time = Date.now();
			}
			else if ((Date.now() - last_shot_time) >= time_between_shots) {
				bullets.push(new Bullet(grid_length * 0.15, grid_length * 0.15, bullet_image, this.x, this.y));
				last_shot_time = Date.now();
			}
		}
	}
}

function Background(img){
	this.base = Entity;
	this.base(canvas.width, canvas.height, img, 0, 0);
}