/*
Creator:
  Andrew Hancock
Purpose:
  This file contains all of the computer player functionality. This is where
  node generation happens for the map. Also where path generation happens.
*/

//this is the resolution of the node map. pixels in between nodes.
var node_pixel_dist = 16;
let nodes = [];

var closed_node_list = [];
var open_node_list = [];

//for the computer player //NEW
function AI_player(width, height, img, x, y, role, team) {
  this.base = Entity;
  this.base(img, 0, x, y, width, height, 0, 1);
  this.role = role;
  this.team = team;
  this.hp = 100;
  this.has_flag = false;
  this.mov_speed = player_speed;
  this.path = getFinalPath(this, player);
  this.pathLastUpdated = Date.now();

	this.update = function() {
		//TODO
	}

	this.generateNewPath = function() {
		this.path = getFinalPath(this, player);
	}
  
	this.drawAIPath = function(){
		if (Date.now() - this.pathLastUpdated >= 1000) {
			this.generateNewPath();
			this.pathLastUpdated = Date.now();
		}

		for (var i = 0; i < this.path.length; i++) {
			context.beginPath();
			context.strokeStyle = "orange";

			context.setTransform(gt1, gt2, gt3, gt4, gt5, gt6);

			context.rect(this.path[i].x, this.path[i].y, 4, 4);
			context.stroke();
			context.closePath(); //so styles dont interfere
		}
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

/*
current error seems to be with calculations involving g costs possibly
*/

//typically AI_player and then real player
function aStarPath(moving_point, target_point) {
  closed_node_list = [];
  open_node_list = [];
  var start_node = nodes[moving_point.x][moving_point.y];
  var goal_node = nodes[target_point.x][target_point.y];
  //avar test_arr = [];
  //test_arr.push(start_node);test_arr.push(goal_node);console.log(test_arr);

  var current_node;
  start_node.g = 0;
  //start_node.f = heuristic(start_node, goal_node);
  open_node_list.push(start_node);
  while (open_node_list.length != 0) {
    current_node = getLowestF(open_node_list);
    //console.log(current_node);
    //if this is the last node
    if (current_node === goal_node) {
      var path = constructPath(current_node);
      return path;
    }

    open_node_list.splice(getIndex(open_node_list, current_node), 1);
    closed_node_list.push(current_node);

    // if(closed_node_list.length == 1){
    // 	console.log(closed_node_list[0].g);
    // 	console.log(closed_node_list[0]);
    // }

    var neighbors = getNeighbors(current_node);
    for (var i = 0; i < neighbors.length; i++) {
      var neighbor = neighbors[i];

      if (closed_node_list.includes(neighbor) == true) {
        //console.log(neighbor + ' in closed list');
        continue;
      }

      var temp_gscore = 0;
      if (i % 2 == 0) {
        temp_gscore = current_node.g + (1.414 * node_pixel_dist);
      } else {
        temp_gscore = current_node.g + (1.0 * node_pixel_dist);
      }
      if (open_node_list.includes(neighbor) == false) {
        open_node_list.push(neighbor);
      } else if (temp_gscore >= neighbor.g) {
        continue;
      }
      neighbor.previous = current_node;
      neighbor.g = temp_gscore;
      neighbor.f = neighbor.g + heuristic(neighbor, goal_node);
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
    if (i % 2 == 0 && closed_node_list.includes(neighbors[i]) == false && open_node_list.includes(neighbors[i]) == false) {
      neighbors[i].g = node.g + (1.414 * node_pixel_dist);
    } else if (closed_node_list.includes(neighbors[i]) == false && open_node_list.includes(neighbors[i]) == false) {
      neighbors[i].g = node.g + (1.0 * node_pixel_dist);
    }
  }
  return neighbors;
}

function getIndex(open_node_list, node) {
  for (var i = 0; i < open_node_list.length; i++) {
    if (open_node_list[i].x == node.x && open_node_list[i].y == node.y) {
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

function getLowestF(open_node_list) {
  var lowest = open_node_list[0];
  for (var i = 0; i < open_node_list.length; i++) {
    if (open_node_list[i].f < lowest.f) {
      lowest = open_node_list[i];
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

  if (nodes[i][j].trav != false) {
    return (new point(i, j));
  } else {
    this.x = (Math.floor((ent.x / node_pixel_dist)) * node_pixel_dist);
    this.y = (Math.floor((ent.y / node_pixel_dist)) * node_pixel_dist);
    var i = 0,
      j = 0;
    while ((nodes[i][j].y != this.y) || (nodes[i][j].x != this.x)) {
      if (nodes[i][j].x != this.x) {
        i++;
      }
      if (nodes[i][j].y != this.y) {
        j++;
      }
    }
		if(nodes[i][j].trav == true){
			return (new point(i, j));
		}else{
			this.x = (Math.ceil((ent.x / node_pixel_dist)) * node_pixel_dist);
	    this.y = (Math.floor((ent.y / node_pixel_dist)) * node_pixel_dist);
	    var i = 0,
	      j = 0;
	    while ((nodes[i][j].y != this.y) || (nodes[i][j].x != this.x)) {
	      if (nodes[i][j].x != this.x) {
	        i++;
	      }
	      if (nodes[i][j].y != this.y) {
	        j++;
	      }
	    }
			if(nodes[i][j].trav == true){
				return (new point(i, j));
			}else{
				this.x = (Math.floor((ent.x / node_pixel_dist)) * node_pixel_dist);
		    this.y = (Math.ceil((ent.y / node_pixel_dist)) * node_pixel_dist);
		    var i = 0,
		      j = 0;
		    while ((nodes[i][j].y != this.y) || (nodes[i][j].x != this.x)) {
		      if (nodes[i][j].x != this.x) {
		        i++;
		      }
		      if (nodes[i][j].y != this.y) {
		        j++;
		      }
		    }
				if(nodes[i][j].trav == true){
					return (new point(i, j));
				}
			}
		}
  }
}

//sent AI_player, actual player
function getFinalPath(moving_ent, target_ent) {
  this.moving_point = getNearestNode(moving_ent);
  this.target_point = getNearestNode(target_ent);
  return aStarPath(this.moving_point, this.target_point);
}


//just return distance between current point and goal point
function heuristic(node1, node2) {
  return Math.sqrt(Math.pow((node1.x - node2.x), 2) + Math.pow((node1.y - node2.y), 2));
}

function node(x, y, trav) {
  this.x = x;
  this.y = y;
  this.previous = undefined;
  this.trav = trav;
  this.f = Infinity;
  this.g = 0;
  this.h;

  this.print_prev = function() {
    console.log(this.previous);
  }
}

function point(x, y) {
  this.x = x;
  this.y = y;
}