var p1; //player object
var bullet_pop; //population of bullets object
var enemy_pop; //population of enemies object
var bullet_speed = 15.0;
var bullets = []; //do not modify
var enemies = []; //do not modify
var num_enemies = 5;
var enemy_diameter = 40;
var bullet_diameter = 10;
var last_shot_time = 0; //do not modify
var time_between_shots = 100;

function setup(){
  createCanvas(900,600);
  p1 = new player();
  bullet_pop = new bullet_population();
  enemy_pop = new enemy_population();
  enemy_pop.generate_enemy_population();
  frameRate(60);
}

function draw(){
  background(51);
  p1.move_p();
  bullet_pop.update_b();
  p1.display_p();
  bullet_pop.draw_b();
  enemy_pop.draw_enemies();
  enemy_pop.check_enemy_bullet_collision();
  enemy_pop.draw_enemy_health_bars();
}

function enemy(){
  this.x = random(0, width);
  this.y = random(0, height);
  this.health = 200;
}

function enemy_population(){
  this.draw_enemy_health_bars = function(){
    for(var i = 0; i < enemies.length; i++){
      push();
      fill(0,0,0);
      rectMode(CORNER);
      noStroke();
      rect(enemies[i].x - 22.5, enemies[i].y - 31.5, 45, 7);
      fill(255,0,0);
      rect(enemies[i].x - 22.5, enemies[i].y - 31.5, (45 * (enemies[i].health)/200), 7);
      pop();
    }
  }

  this.generate_enemy_population = function(){
    for(var i = 0; i < num_enemies; i++){
      enemies.push(new enemy());
    }
  }

  this.move_enemy_population = function(){
    for(var i = 0; i < enemies.length; i++){
      enemies[i].x += random(-5, 5);
      enemies[i].y += random(-5, 5);
    }
  }

  this.check_enemy_bullet_collision = function(){
    for(var i = 0; i < enemies.length; i++){
      for(var j = 0; j < bullets.length; j++){
        //first calculate distance between them and then
        //using that if its less than the radius of the enemy
        //plus the radius of the bullet than a collision happens
        var distance = sqrt(pow((enemies[i].x - bullets[j].x), 2) +
         pow((enemies[i].y - bullets[j].y), 2));

        if(distance < ((enemy_diameter / 2) + (bullet_diameter / 2))){
          enemies[i].health -= bullets[j].damage;
          bullets.splice(j, 1);
          if(enemies[i].health <= 0){
            enemies.splice(i, 1);
            break;
          }
        }
      }
    }
  }

  this.draw_enemies = function(){
    for(var i = 0; i < enemies.length; i++){
      strokeWeight(1);
      stroke(0,255,0);
      fill(255,255,0);
      push();
      ellipseMode(CENTER);
      ellipse(enemies[i].x, enemies[i].y, enemy_diameter, enemy_diameter);
      pop();
    }
  }
}

function player(){
  this.x = 0;
  this.y = 0;

  this.display_p = function(){
    push();
    stroke(255,0,0);
    strokeWeight(50);
    fill(0,255,0);
    rectMode(CENTER);
    rect(this.x, this.y, 30, 30);
    pop();
  }

  this.move_p = function(){
    if(keyIsDown(87)){
      this.y -= 3;
    }
    if(keyIsDown(65)){
      this.x -= 3;
    }
    if(keyIsDown(68)){
      this.x += 3;
    }
    if(keyIsDown(83)){
      this.y += 3;
    }
    if(keyIsDown(32)){
      if(last_shot_time == 0){
        bullets.push(new bullet_object);
        last_shot_time = millis();
      }else if((millis() - last_shot_time) >= time_between_shots){
        bullets.push(new bullet_object);
        last_shot_time = millis();
      }
    }
  }
}

function bullet_object(){
  this.x = p1.x;
  this.y = p1.y;

  this.x_diff = mouseX - p1.x;
  this.y_diff = (mouseY - p1.y) * - 1;

  this.angle = degrees(atan(this.y_diff/this.x_diff));

  this.damage = 15;

  if(this.x_diff < 0 && this.y_diff > 0){
    this.angle += 180;
  }else if(this.x_diff < 0 && this.y_diff < 0){
    this.angle += 180;
  }else if(this.x_diff > 0 && this.y_diff < 0){
    this.angle += 360;
  }

  angleMode(DEGREES);
  this.y_ratio = sin(this.angle);
  this.x_ratio = cos(this.angle);
  angleMode(RADIANS);

}

function bullet_population(){
  this.update_b = function(){
    for(var i = 0; i < bullets.length; i++){
      bullets[i].x += (bullets[i].x_ratio * bullet_speed);
      bullets[i].y += (bullets[i].y_ratio * bullet_speed * -1);
      if(bullets[i].x > width || bullets[i].x < 0 ||
       bullets[i].y > height || bullets[i].y < 0){
        bullets.splice(i, 1);
      }
    }
  }

  this.draw_b = function(){
    for(var i = 0; i < bullets.length; i++){
      fill(255,255,0);
      stroke(255,255,0);
      strokeWeight(1);
      push();
      ellipseMode(CENTER);
      ellipse(bullets[i].x, bullets[i].y, bullet_diameter, bullet_diameter);
      pop();
    }
  }
}
