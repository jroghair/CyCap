//global variables
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
var keys_down = []; //keys being pressed
var bullets = []; //all bullets
var player, bullet_p;
var player_size = 40,
  bullet_size = 5; //pixels
var player_speed = 3,
  bullet_speed = 10; //pixels
var mouseX;
var mouseY;
var last_shot_time = 0; //don't change
var time_between_shots = 100; //milliseconds


//this code executes right when the page is loaded
setup(); //only call setup once
setInterval(draw, 1000 / 60); //called 60 times a second


//all functions
function setup() {
  player = new player();
  bullet_p = new bullet_population();
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
  player.move_p();
  bullet_p.move_bullets();
  player.display_p();
  bullet_p.display_bullets();
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

function bullet_object() {
  this.x = player.x;
  this.y = player.y;

  this.x_diff = mouseX - player.x;
  this.y_diff = (mouseY - player.y) * -1;

  this.angle = toDegrees(Math.atan(this.y_diff / this.x_diff));

  this.damage = 15;

  if (this.x_diff < 0 && this.y_diff > 0) {
    this.angle += 180;
  } else if (this.x_diff < 0 && this.y_diff < 0) {
    this.angle += 180;
  } else if (this.x_diff > 0 && this.y_diff < 0) {
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
  this.display_bullets = function() {
    for (var i = 0; i < bullets.length; i++) {
      context.beginPath();
      context.fillStyle = "#000000";
      context.arc(bullets[i].x, bullets[i].y, bullet_size, 0, 2 * Math.PI);
      context.fill();
      context.closePath();
    }
  }

  this.move_bullets = function() {
    for (var i = 0; i < bullets.length; i++) {
      //console.log(bullets[i].x_ratio + ', ' + bullets[i].y_ratio);
      bullets[i].x += bullet_speed * bullets[i].x_ratio;
      bullets[i].y += bullet_speed * bullets[i].y_ratio;
      if (bullets[i].x < 0 || bullets[i].x > 810 || bullets[i].y < 0 || bullets[i].y > 540) {
        bullets.splice(i, 1);
      }


    }
  }
}

function player() {
  this.x = 0;
  this.y = 0;

  this.display_p = function() {
    context.beginPath();
    context.fillStyle = "#59b5c1";
    context.fillRect(this.x - (player_size / 2), this.y - (player_size / 2), player_size, player_size);
    context.closePath();
  }


  this.move_p = function() {
    if (keys_down.includes(87)) {
      this.y -= player_speed;
    }
    if (keys_down.includes(65)) {
      this.x -= player_speed;
    }
    if (keys_down.includes(68)) {
      this.x += player_speed;
    }
    if (keys_down.includes(83)) {
      this.y += player_speed;
    }

    if (keys_down.includes(32)) {
      if (last_shot_time == 0) {
        bullets.push(new bullet_object());
        last_shot_time = Date.now();
      } else if ((Date.now() - last_shot_time) >= time_between_shots) {
        bullets.push(new bullet_object);
        last_shot_time = Date.now();
      }
    }
  }
}
