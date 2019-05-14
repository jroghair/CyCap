# CyCap
CyCap is a fully functional web based capture the flag game. I originally developed it for a class (COM S 309) at Iowa State
University, along with Bryan Friestad, Ted Miller and Andrew Hancock. The backend is written in java and leverages the spring framework, 
while the front end is primarily written in javascript and uses thymeleaf for rendering the html/css. My primary focus during its development
was on the backend, along side Ted. Bryan and Andrew focused on front end development.  

# Features
There are two main components to the project, the game itself and the interactive website. Initially, players can either choose to create
an account or play as a guest. The game itself consists of two teams (up to 4 players on each team), each fighting to capture one anothers flag. The game happens in real
time and supports multiplayer. Players can choose between four different classes to play, each with its own unique mechanics. Each class uses different weapons such as a grenade launcher, pistol,
shotgun..etc. There exists various in game mechanics such as ammo packs and health/speed potions. Andrew Hancock implemented an impressive
artificial intelligence non-player-computer that could move/shoot at the enemy team as well attempt to capture their flag just as well
as most users.

The interactive website was strictly for players with an existing account. Players could keep track of their friends through a friends list 
and chat with them (implemented using websockets). Players could search for games in real time through our game lobby (websockets) that Ted
and I implemented. After each game users are able to see their latest game data reflected on their profile page and the overall leaderboards
for all players of the game. Leaderboards showed key ingame statistics for players including kills/deaths and k/d ratio, and allowed users to 
sort, filter and search. 

[![CyCap Video](https://img.youtube.com/vi/aA-XCnGD7Pg&t=1s/0.jpg)](https://www.youtube.com/watch?v=aA-XCnGD7Pg&t=1s)

# Reflection
This project was my first attempt at software development and certainly my first exposure to Spring. The code is not perfect, but I certainly
feel like I learned a great deal from the experience and am a better programmer as a result. The project went on to be one of a handful that were
nominated for best project at the end of the semester. I highlight the significance of this only to mention that it was only possible due tothe amazing team mates that I had that semester, Ted, Andrew and Bryan. Each of them are fantastic programmers and engineers on their own,but it was our combined teamwork and hardwork led us to having such a great project. Bryan in particular poured more hours into the project
that any of us and was instrumental in making it the success that it was. I truly enjoyed working on this project with them.
