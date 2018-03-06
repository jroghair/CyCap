
CREATE DATABASE IF NOT EXISTS db309sd1;

ALTER DATABASE db309sd1
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

ALTER TABLE account AUTO_INCREMENT=1;


#GRANT ALL PRIVILEGES ON  db309sd1.* TO root@localhost IDENTIFIED BY 'pc';

drop table db309sd1.account;

create table db309sd1.account(

UserID varchar(255) NOT NULL,
Password varchar(255) NOT NULL,
Creation_Date date NOT NULL,
Email varchar(255) NOT NULL,
id int NOT NULL AUTO_INCREMENT,
primary key (id)
);



insert into db309sd1.account
values ('jroghair', 'hello', '2018-01-31' , 'jroghair@iastate.edu', 1);

#delete from CyCapDB.account where UserID='jroghair';


insert into db309sd1.account
values ('jroghair123', 'hi', '2018-02-18' , 'jroghair2@iastate.edu', 2 );


insert into db309sd1.account
values ('bryan', 'hey', '2018-02-14' , 'bryanf@iastate.edu', 3 );