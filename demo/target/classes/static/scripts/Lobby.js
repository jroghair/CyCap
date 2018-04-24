var serverSocket;
document.getElementById("name").innerHTML = sessionStorage.getItem("type");
connectToServer();
var gameId = "";
var gameType = sessionStorage.getItem("type");
var players = 1; 
var x;

function refresh(){
	players = 0;
	sendMessageToServer("lobby:playerList:" + gameId);
}


function recruit(){
sendMessageToServer("lobby:role:" + gameId + ":" + client_id + ":recruit");
}

function infantry(){
sendMessageToServer("lobby:role:" + gameId + ":" + client_id + ":infantry");
}

function scout(){
sendMessageToServer("lobby:role:" + gameId + ":" + client_id + ":scout");
}

function artillery(){
sendMessageToServer("lobby:role:" + gameId + ":" + client_id + ":artillery");
}

function changeTimer(ms){
	clearInterval(x);
	x = setInterval(function() {
		ms = ms - 1000;

		var minutes = Math.floor(ms / 60000);
		var seconds = Math.floor((ms % 60000) / 1000);
		if(seconds < 10){
			document.getElementById("time").innerHTML = "Time Until Start - " + minutes + ":0" + seconds;  
		}
		else{
		// Display the result in the element with id="demo"
		document.getElementById("time").innerHTML = "Time Until Start - " + minutes + ":" + seconds;
		}
	}, 1000);
}

function updatePlayerList(name, role, team) {
	//console.log(name);
    var p = document.createElement("P");
	if(team == 1){
		p.style.backgroundColor = "red";
	}
	else if(team == 2){
		p.style.backgroundColor = "blue";
	}
	else if(team == 3){
		p.style.backgroundColor = "green";
	}
	else if(team == 4){
		p.style.backgroundColor = "yellow";
	}
	else if(team == 5){
		p.style.backgroundColor = "purple";
	}
	else if(team == 6){
		p.style.backgroundColor = "pink";
	}
	else if(team == 7){
		p.style.backgroundColor = "orange";
	}
	else if(team == 8){
		p.style.backgroundColor = "LightSkyBlue";
	}
    var t = document.createTextNode(name + " - " + role);
    p.appendChild(t);
    document.getElementById("play").appendChild(p);
}

function connectToServer(){
	//document.getElementById("name").innerHTML = sessionStorage("type");
	serverSocket = new WebSocket('ws://' + window.location.host + '/my-websocket-endpoint');
	serverSocket.onopen = function() {
		//do some initial handshaking, sending back and forth information like the password and starting game state, etc
		sendMessageToServer("lobby:join:" + gameType + ":" + client_id);
	};

	serverSocket.onmessage = message_handler;
}

function sendMessageToServer(msg){
	serverSocket.send(msg);
}

//event listener for when the socket receives a message from the server
//TODO: fix this based on the new model
function message_handler(msg){
	//var i = 1;
	let temp = msg.data.split(":");
	if(temp[0] == "player"){
		document.getElementById("play").innerHTML = "";
		players = 0;
		for(let i = 1; i < temp.length; i+=3){
			updatePlayerList(temp[i], temp[i + 1], temp[i + 2]);
			players++;
		}
		document.getElementById("players").innerHTML = "Number of Players: " + players;
	}
	else if(temp[0] == "joined"){
		gameId = temp[1];
		console.log(gameId);
	}
	else if(temp[0] == "play"){
		window.location.href = "play";
	}
	else if(temp[0] == "role"){
		if(temp[1] != "no"){
			document.getElementById("role").innerHTML = "Role: " + temp[1];
		}
	}
	else if(temp[0] == "time"){
		changeTimer(parseInt(temp[1]));
	}
}