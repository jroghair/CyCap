
CREATE DATABASE IF NOT EXISTS db309sd1;

ALTER DATABASE db309sd1
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

ALTER TABLE account AUTO_INCREMENT=1;


#GRANT ALL PRIVILEGES ON  db309sd1.* TO root@localhost IDENTIFIED BY 'pc';

drop table db309sd1.account;

create table db309sd1.account(
Id int NOT NULL AUTO_INCREMENT,
UserID varchar(255) NOT NULL,
Password varchar(255) NOT NULL,
Creation_Date date NOT NULL,
Email varchar(255) NOT NULL,

primary key (Id)
);

CREATE TABLE `db309sd1`.`friend` (
  `Id` INT NOT NULL AUTO_INCREMENT,
  `PlayerID` VARCHAR(45) NOT NULL,
  `UserID` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Id`));
