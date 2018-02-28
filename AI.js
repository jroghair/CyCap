/*
Creator:
  Andrew Hancock
Purpose:
  This file contains all of the computer player functionality. This is where
  node generation happens for the map. Also where path generation happens.
*/

//this is the resolution of the node map. pixels in between nodes.
var node_pixel_dist = 8;
let nodes = [];
let pathLastUpdated = Date.now();

//for the ocmputer player //NEW
function AI_player(width, height, img, x, y, role, team) {
	this.base = Entity;
	this.base(img, 0, x, y, width, height, 0, 1);
	
	this.role = role;
	this.team = team;
	this.hp = 100;
	
	this.has_flag = false;
	this.mov_speed = player_speed;
	
	this.path = getFinalPath(this, player);

	this.update = function() {
		//TODO
	}
	
	this.generateNewPath = function(){
		this.path = getFinalPath(this, player);
	}

}

function generateNodes() {
  //go through all coordinates
  for (var i = 0; i < (grid_length * bg_width_grids); i += node_pixel_dist) {
    var node_col = []; //for storing a column of nodes
    for (var j = 0; j < (grid_length * bg_height_grids); j += node_pixel_dist) {
      //make a test entity for player
      var test_player_ent = new Entity(player_image, 0, i, j, grid_length, grid_length, 0, 1);
      //go through all walls and check collision
      var traverable = true; //whether node can be traveled to or not
      for (var t = 0; t < walls.length; t++) {
        if (isColliding(walls[t], test_player_ent)) {
          traverable = false;
          break;
        }
      }
      node_col.push(new node(i, j, traverable));
    }
    nodes.push(node_col);
  }
  return 'success';
}

function aStarPath(moving_point, target_point) {
  var start_node = nodes[moving_point.x][moving_point.y];
  var goal_node = nodes[target_point.x][target_point.y];
  var open = [];
  open.push(start_node);
  var closed = [];
  var current_node = undefined;
  start_node.g = 0;
  start_node.f = start_node.g + heuristic(start_node, goal_node);
  while (open.length != 0) {
    current_node = getLowestF(open);
    if (current_node === goal_node) {
      var path = constructPath(current_node);
      return path;
    }
    open.splice(getIndex(open, current_node), 1);
    closed.push(current_node);
    var neighbors = getNeighbors(current_node);
    for (var i = 0; i < neighbors.length; i++) {
      var neighbor = neighbors[i];
      if (closed.includes(neighbor) != true) {


        var temp_gscore = current_node.g + 1;
        if (open.includes(neighbor)) {
          if (temp_gscore < neighbor.g) {
            neighbor.g = temp_gscore;
          }
        } else {
          neighbor.g = temp_gscore
          open.push(neighbor);
        }
        neighbor.h = heuristic(neighbor, goal_node);
        neighbor.f = neighbor.g + neighbor.h;
        neighbor.previous = current_node;
      }
    }
  }
}

function getNodeFromList(arr, node) {
  for (var i = 0; i < arr.length; i++) {
    if (arr[i].x == node.x && arr[i].y == node.y) {
      return arr[i];
    }
  }
}

function drawAIPath(){
	if(Date.now() - pathLastUpdated > 500){
		ai_player1.generateNewPath();
		pathLastUpdated = Date.now();
	}
	for(var i = 0; i < ai_player1.path.length; i++){ //THIS CAUSES AN ISSUE
		context.beginPath();
		context.strokeStyle = "red";

		context.setTransform(gt1, gt2, gt3, gt4, gt5, gt6);
		context.transform(1, 0, 0, 1, 0, 0); //set draw position

		context.rect(ai_player1.path[i].x, ai_player1.path[i].y, 2, 2);
		context.lineWidth = 2;
		context.stroke();
		context.closePath(); //so styles dont interfere
	}
}

function node_contains(arr, node) {
  for (var i = 0; i < arr.length; i++) {
    if (arr[i].x == node.x && arr[i].y == node.y) {
      return true;
    }
  }
  return false;
}

function getNeighbors(node) {
  var nindex = getNearestNode(node);
  var neighbors = [];
  if (nodes[nindex.x - 1][nindex.y - 1].trav == true) {
    neighbors.push(nodes[nindex.x - 1][nindex.y - 1]);
  }
  if (nodes[nindex.x - 1][nindex.y].trav == true) {
    neighbors.push(nodes[nindex.x - 1][nindex.y]);
  }
  if (nodes[nindex.x - 1][nindex.y + 1].trav == true) { //THIS ONE IS CAUSING THE ISSUE
    neighbors.push(nodes[nindex.x - 1][nindex.y + 1]);
  }
  if (nodes[nindex.x][nindex.y + 1].trav == true) {
    neighbors.push(nodes[nindex.x][nindex.y + 1]);
  }
  if (nodes[nindex.x + 1][nindex.y + 1].trav == true) {
    neighbors.push(nodes[nindex.x + 1][nindex.y + 1]);
  }
  if (nodes[nindex.x + 1][nindex.y].trav == true) {
    neighbors.push(nodes[nindex.x + 1][nindex.y]);
  }
  if (nodes[nindex.x + 1][nindex.y - 1].trav == true) {
    neighbors.push(nodes[nindex.x + 1][nindex.y - 1]);
  }
  if (nodes[nindex.x][nindex.y - 1].trav == true) {
    neighbors.push(nodes[nindex.x][nindex.y - 1]);
  }
  for (var i = 0; i < neighbors.length; i++) {
    neighbors[i].g = node.g + 1;
  }
  return neighbors;
}

function getIndex(open, node) {
  for (var i = 0; i < open.length; i++) {
    if (open[i].x == node.x && open[i].y == node.y) {
      return i;
    }
  }
}

function constructPath(enode) {
  var path = [];
  var temp = enode;
  path.push(temp);
  while (temp.previous) {
    path.push(temp.previous);
    temp = temp.previous;
  }
  return path;
}

function getLowestF(open) {
  var lowest = open[0];
  for (var i = 0; i < open.length; i++) {
    if (open[i].f < lowest.f) {
      lowest = open[i];
    }
  }
  return lowest;
}

function getNearestNode(ent) {
  this.x = (Math.ceil((ent.x / node_pixel_dist)) * node_pixel_dist);
  this.y = (Math.ceil((ent.y / node_pixel_dist)) * node_pixel_dist);
  var i = 0, j = 0;
  while ((nodes[i][j].y != this.y) || (nodes[i][j].x != this.x)) {
    if (nodes[i][j].x != this.x) {
      i++;
    }
    if (nodes[i][j].y != this.y) {
      j++;
    }
  }
  return (new point(i, j));
}

function getFinalPath(moving_ent, target_ent) {
  this.moving_point = getNearestNode(moving_ent);
  this.target_point = getNearestNode(target_ent);
  return aStarPath(this.moving_point, this.target_point);
}

function heuristic(node1, node2) {
  return Math.sqrt(Math.pow((node1.x - node2.x), 2) + Math.pow((node1.y - node2.y), 2));
}

function node(x, y, trav) {
  this.x = x;
  this.y = y;
  this.previous = undefined;
  this.trav = trav;
  this.f = 0;
  this.g = 1;
  this.h;

  this.print_prev = function() {
    console.log(this.previous);
  }
}

function point(x, y) {
  this.x = x;
  this.y = y;
}
