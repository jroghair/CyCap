
CREATE DATABASE IF NOT EXISTS db309sd1;

ALTER DATABASE db309sd1
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

ALTER TABLE account AUTO_INCREMENT=1;


#GRANT ALL PRIVILEGES ON  db309sd1.* TO root@localhost IDENTIFIED BY 'pc';

#drop table db309sd1.account;

create table db309sd1.account(
Id int NOT NULL AUTO_INCREMENT,
UserID varchar(255) NOT NULL,
Password varchar(255) NOT NULL,
Creation_Date date NOT NULL,
Email varchar(255) NOT NULL,

primary key (Id)
);


create table db309sd1.friend(
Id int NOT NULL AUTO_INCREMENT,
PlayerID varchar(255) NOT NULL,
UserID varchar(255) NOT NULL,
primary key (Id)
);


create table db309sd1.profiles(
Id int NOT NULL AUTO_INCREMENT,
UserID varchar(255) NOT NULL,
CharClass varchar(255) NOT NULL,
Kills int NOT NULL,
Deaths int NOT NULL,
GameWins int NOT NULL,
GameLosses int NOT NULL,
GamesPlayed int NOT NULL,
FlagGrabs int NOT NULL,
FlagReturns int NOT NULL,
FlagCaptures int NOT NULL,
primary key (Id)
);

insert into db309sd1.profiles
VALUES (1, 'jroghair', 'infantry', 3, 5, 2, 1, 3, 1, 2, 1);



insert into db309sd1.profiles
VALUES (2, 'jroghair', 'scout', 3, 5, 2, 1, 3, 1, 2, 1);



insert into db309sd1.profiles
VALUES (3, 'jroghair', 'recruit', 3, 5, 2, 1, 3, 1, 2, 1);








