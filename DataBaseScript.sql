CREATE DATABASE database coursejdbc;

USE coursejdbc;

CREATE TABLE departement (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nom varchar(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE vendeur (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nom varchar(60) NOT NULL,
  Courriel varchar(100) NOT NULL,
  DateNaissance datetime NOT NULL,
  SalaireBase double NOT NULL,
  DepartementId int(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (DepartementId) REFERENCES departement (id)
);

INSERT INTO departement (Nom) VALUES 
  ('Ordinateurs'),
  ('Electroniques'),
  ('Mode'),
  ('Livres');

INSERT INTO vendeur (Nom, Courriel, DateNaissance, SalaireBase, DepartementId) VALUES 
  ('Bob Brown','bob@gmail.com','1998-04-21 00:00:00',1000,1),
  ('Maria Green','maria@gmail.com','1979-12-31 00:00:00',3500,2),
  ('Alex Grey','alex@gmail.com','1988-01-15 00:00:00',2200,1),
  ('Martha Red','martha@gmail.com','1993-11-30 00:00:00',3000,4),
  ('Donald Blue','donald@gmail.com','2000-01-09 00:00:00',4000,3),
  ('Alex Pink','bob@gmail.com','1997-03-04 00:00:00',3000,2);