drop table membre_sopridee;
drop table sopridee;
drop table utilisateur;

CREATE TABLE sopridee
(
id bigint primary key,
titre varchar(50) not null,
resume varchar(255),
idCategorie bigint,
idPorteur bigint not null
);

CREATE TABLE utilisateur
(
  id bigint primary key,
  idSso varchar(50) not null,
  email varchar(100) not null,
  code varchar(30)
);

ALTER TABLE sopridee ADD CONSTRAINT sopridee_utilisateurFk FOREIGN KEY (idPorteur) REFERENCES utilisateur (id) MATCH FULL;

CREATE TABLE membre_sopridee
(
idSopridee bigint not null,
idMembre bigint not null
);

ALTER TABLE membre_sopridee ADD CONSTRAINT membre_sopridee_utilisateurFk FOREIGN KEY (idMembre) REFERENCES utilisateur (id) MATCH FULL;
ALTER TABLE membre_sopridee ADD CONSTRAINT membre_sopridee_soprideeFk FOREIGN KEY (idSopridee) REFERENCES sopridee (id) MATCH FULL;
