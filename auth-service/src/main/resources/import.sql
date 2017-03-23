--@formatter:off

-- Table user
insert into user (username) values ('fakesso#jntakpe');
insert into user (username) values ('fakesso#cbarillet');
insert into user (username) values ('fakesso#rjansem');
insert into user (username) values ('fakesso#cegiraud');
insert into user (username) values ('fakesso#crinfray');
insert into user (username) values ('fakesso#anycz',);
insert into user (username) values ('fakesso#fleriche');
insert into user (username) values ('fakesso#mbouhamyd');
insert into user (username) values ('fakesso#nmpacko');
insert into user (username) values ('fakesso#ocoulibaly');
insert into user (username) values ('fakesso#mmarquez');
insert into user (username) values ('fakesso#tchoteau');
insert into user (username) values ('fakesso#eparizot');


-- Table authority
INSERT INTO authority (name) VALUES ('USER');
INSERT INTO authority (name) VALUES ('ADMIN');

-- Table user_authorities
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#jntakpe'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#jntakpe'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#cbarillet'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#cbarillet'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#rjansem'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#rjansem'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#cegiraud'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#cegiraud'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#crinfray'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#crinfray'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#anycz'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#anycz'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#fleriche'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#fleriche'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#mbouhamyd'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#mbouhamyd'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#nmpacko'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#nmpacko'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#ocoulibaly'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#ocoulibaly'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#mmarquez'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#mmarquez'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#tchoteau'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#tchoteau'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#eparizot'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fakesso#eparizot'), (SELECT id FROM authority WHERE name='ADMIN'));

--@formatter:on