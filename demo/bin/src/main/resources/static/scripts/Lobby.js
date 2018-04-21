var serverSocket;
document.getElementById("name").innerHTML = sessionStorage.getItem("type");
connectToServer();
var gameId = "";
var gameType = sessionStorage.getItem("type");
var players = 1; 

function refresh(){
	players = 0;
	sendMessageToServer("lobby:playerList:" + gameId);
}


function recruit(){
sessionStorage.setItem("class", "recruit");
}

function infantry(){
sessionStorage.setItem("class", "infantry");
}

function scout(){
sessionStorage.setItem("class", "scout");
}

function myFunction(name) {
	console.log(name);
    var p = document.createElement("P");
    var t = document.createTextNode(name);
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
		myFunction(temp[1]);
		players++;
		document.getElementById("players").innerHTML = "Number of Players: " + players;
	}
	else if(temp[0] == "joined"){
		gameId = temp[1];
		console.log(gameId);
	}
	else if(temp[0] == "clean"){
		document.getElementById("play").innerHTML = "";
		players = 0;
	}
	else if(temp[0] == "play"){
	window.location.href = "play";
	}
}