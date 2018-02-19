var node_pixel_dist = 5;

function generateNodes() {
  //go through all coordinates
  var time1 = Date.now();
  for (var i = 0; i < canvas.width; i += node_pixel_dist) {
    var node_col = []; //for storing a column of nodes
    for (var j = 0; j < canvas.height; j += node_pixel_dist) {
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
  // console.log('nodal generation time: ' + (Date.now() - time1) + ' ms');
  return 'success';
}

function aStarPath(moving_point, target_point) {
	// console.log('starting path...');
  var start_node = nodes[moving_point.x][moving_point.y];
  var goal_node = nodes[target_point.x][target_point.y];
  // console.log('from: ' + start_node.x + ',' + start_node.y);
  // console.log('to: ' + goal_node.x + ',' + goal_node.y);
  var open = [];
  open.push(start_node);
  var closed = [];
  var current_node = undefined;
  start_node.g = 0;
  start_node.f = start_node.g + heuristic(start_node, goal_node);
  //console.log(start_node.f);
  while (open.length != 0) {
		//start with node with lowest cost function
    //console.log('open list: ' + open);
    current_node = getLowestF(open);
    // console.log('current node: ' + current_node.x + ',' + current_node.y);
    //current_node.print_prev();
    if (current_node === goal_node) {
      // console.log('path is complete.');
      var path = constructPath(current_node);
      return path;
    }
		open.splice(getIndex(open, current_node), 1);
		closed.push(current_node);
		var neighbors = getNeighbors(current_node);
		//console.log(neighbors);
		for(var i = 0;i < neighbors.length;i++){
      var neighbor = neighbors[i];
			//if the node isnt in visited list
			if(closed.includes(neighbor) != true){
				//console.log('doesnt contain');
				//set cost function for the node


        var temp_gscore = current_node.g + 1;
        if(open.includes(neighbor)){
          if(temp_gscore < neighbor.g){
            neighbor.g = temp_gscore;
          }
        }else{
          neighbor.g = temp_gscore
          open.push(neighbor);
        }
        neighbor.h = heuristic(neighbor, goal_node);
        neighbor.f = neighbor.g + neighbor.h;
        neighbor.previous = current_node;
        //console.log('setting: ' + current_node.x + ' as a previous of: ' + neighbor.x);



        /*
				neighbor.f = neighbor.g + heuristic(neighbor, goal_node);
				//if its not in the open list
				if(node_contains(neighbor, open) != true){
					open.push(neighbor); //add to open list and go to next iter
				}else{//if it is in open list get it and set it to openneighbor
					var openneighbor = getNodeFromList(open, neighbor);
					if(neighbor.g < openneighbor.g){
						openneighbor.g = neighbor.g;
						openneighbor.previous = neighbor.previous;//add to the path
					}
				}
        */
			}
		}
  }

	// console.log('success');
}

function getNodeFromList(arr, node){
	for(var i = 0;i < arr.length;i++){
		if(arr[i].x == node.x && arr[i].y == node.y){
			return arr[i];//return node
		}
	}
}

function node_contains(arr, node){
	for(var i = 0;i < arr.length;i++){
		if(arr[i].x == node.x && arr[i].y == node.y){
			return true;
		}
	}
	return false;
}

function getNeighbors(node){
	var nindex = getNearestNode(node);
	var neighbors = [];
  if(nodes[nindex.x-1][nindex.y-1].trav == true){
	   neighbors.push(nodes[nindex.x-1][nindex.y-1]);
  }
  if(nodes[nindex.x-1][nindex.y].trav == true){
	   neighbors.push(nodes[nindex.x-1][nindex.y]);
  }
  if(nodes[nindex.x-1][nindex.y+1].trav == true){
	   neighbors.push(nodes[nindex.x-1][nindex.y+1]);
  }
  if(nodes[nindex.x][nindex.y+1].trav == true){
	   neighbors.push(nodes[nindex.x][nindex.y+1]);
  }
  if(nodes[nindex.x+1][nindex.y+1].trav == true){
	   neighbors.push(nodes[nindex.x+1][nindex.y+1]);
  }
  if(nodes[nindex.x+1][nindex.y].trav == true){
	   neighbors.push(nodes[nindex.x+1][nindex.y]);
  }
  if(nodes[nindex.x+1][nindex.y-1].trav == true){
	   neighbors.push(nodes[nindex.x+1][nindex.y-1]);
  }
  if(nodes[nindex.x][nindex.y-1].trav == true){
	   neighbors.push(nodes[nindex.x][nindex.y-1]);
  }
  //set travel costs to nodes
  for(var i = 0;i < neighbors.length;i++){
    //neighbors[i].previous = node;
    neighbors[i].g = node.g + 1;
  }
	return neighbors;
}

function getIndex(open, node){
	for(var i = 0;i < open.length;i++){
		if(open[i].x == node.x && open[i].y == node.y){
			return i;
		}
	}
}

function constructPath(enode) {
  /*
  var c_node = snode;
	console.log('constructing path coordinates...');
  var path = [];
  for(;;){
    path.push(c_node);
    if(c_node.x == enode.x){
      if(c_node.y == enode.y){
        break;
      }
    }
    c_node = c_node.previous;
    console.log(path);
    if(path.length > 100){
      break;
    }
  }

  */
  var path = [];
  var temp = enode;
  path.push(temp);
  // console.log('here1');
  while(temp.previous){
    path.push(temp.previous);
    // if(temp.previous == undefined){
    //   break;
    // }
    //console.log('pushed previous');
    temp = temp.previous;0

  }
  // console.log('here2');
  // console.log(path);
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
  // this will round to the nearest 5 since thats where nodes are
  this.x = (Math.ceil((ent.x / node_pixel_dist)) * node_pixel_dist);
  //console.log('old x: ' + ent.x + ' new x: ' + this.x);
  this.y = (Math.ceil((ent.y / node_pixel_dist)) * node_pixel_dist);
  //console.log('old y: ' + ent.y + ' new y: ' + this.y);

  //go through all nodes and see if one matches
  //this could be improved
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
  //console.log('found node: ' + nodes[i][j].x + ',' + nodes[i][j].y);
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

  this.print_prev = function(){
    console.log(this.previous);
  }
}

function point(x, y) {
  this.x = x;
  this.y = y;
}
