
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
Member TINYINT(1) NOT NULL,
Administrator TINYINT(1) NOT NULL,
Developer TINYINT(1) NOT NULL,
primary key (Id)
);


create table db309sd1.friend(
Id int NOT NULL AUTO_INCREMENT,
PlayerID varchar(255) NOT NULL,
UserID varchar(255) NOT NULL,
primary key (Id)
);


drop table db309sd1.profiles;

create table db309sd1.profiles(
Id int NOT NULL AUTO_INCREMENT,
UserID varchar(255) NOT NULL,
Champion varchar(255) NOT NULL,
Kills int NOT NULL,
Deaths int NOT NULL,
Gamewins int NOT NULL,
Gamelosses int NOT NULL,
Gamesplayed int NOT NULL,
Flaggrabs int NOT NULL,
Flagreturns int NOT NULL,
Flagcaptures int NOT NULL,
Experience int NOT NULL,
Level int NOT NULL,
Scoutunlocked TINYINT(1) NOT NULL,
Artilleryunlocked TINYINT(1) NOT NULL,
Infantryunlocked TINYINT(1) NOT NULL,
primary key (Id)
);


ALTER TABLE db309sd1.profiles AUTO_INCREMENT=1;


insert into db309sd1.profiles
VALUES (1, 'jroghair', 'infantry', 3, 5, 2, 1, 3, 1, 2, 1, 57,1);



insert into db309sd1.profiles
VALUES (2, 'jroghair', 'scout', 2, 6, 3, 2, 4, 1, 2, 1, 67,2);



insert into db309sd1.profiles
VALUES (3, 'jroghair', 'recruit', 3, 6, 5, 2, 4, 4, 2, 2, 13,8);




insert into db309sd1.profiles
VALUES (4, 'ted', 'infantry', 3, 5, 2, 1, 3, 1, 2, 1, 57,1);



insert into db309sd1.profiles
VALUES (5, 'ted', 'scout', 20, 8, 4, 2, 7, 8, 3, 2, 13,4);



insert into db309sd1.profiles
VALUES (6, 'ted', 'recruit', 56, 40, 4, 2, 4, 4, 2, 2, 13,20);


insert into db309sd1.profiles
VALUES (7, 'bryan', 'infantry', 34, 15, 2, 1, 3, 1, 2, 1, 15,9);



insert into db309sd1.profiles
VALUES (19, 'jr', 'scout', 2, 6, 3, 2, 4, 1, 2, 1, 67,2);



insert into db309sd1.profiles
VALUES (20, 'jr', 'recruit', 3, 6, 5, 2, 4, 7, 8, 3, 2,54);




insert into db309sd1.account
VALUES (21, 'jr', 'recruit', 3, 6, 5, 2, 4, 7, 8, 3, 2,54);




UPDATE db309sd1.account
SET Administrator = 1
WHERE id=16;
