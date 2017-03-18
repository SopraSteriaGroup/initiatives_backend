--@formatter:off

-- Table user
INSERT INTO user (username, password) VALUES ('jntakpe', 'test');
INSERT INTO user (username, password) VALUES ('cbarillet', 'test');
INSERT INTO user (username, password) VALUES ('rjansem', 'test');
INSERT INTO user (username, password) VALUES ('cegiraud', 'test');
INSERT INTO user (username, password) VALUES ('crinfray', 'test');
INSERT INTO user (username, password) VALUES ('anycz', 'test');
INSERT INTO user (username, password) VALUES ('fleriche', 'test');
INSERT INTO user (username, password) VALUES ('mbouhamyd', 'test');
INSERT INTO user (username, password) VALUES ('nmpacko', 'test');
INSERT INTO user (username, password) VALUES ('ocoulibaly', 'test');
INSERT INTO user (username, password) VALUES ('mmarquez', 'test');
INSERT INTO user (username, password) VALUES ('tchoteau', 'test');
INSERT INTO user (username, password) VALUES ('eparisot', 'test');


-- Table authority
INSERT INTO authority (name) VALUES ('USER');
INSERT INTO authority (name) VALUES ('ADMIN');

-- Table user_authorities
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='jntakpe'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='jntakpe'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='cbarillet'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='cbarillet'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='rjansem'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='rjansem'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='cegiraud'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='cegiraud'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='crinfray'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='crinfray'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='anycz'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='anycz'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fleriche'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='fleriche'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='mbouhamyd'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='mbouhamyd'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='nmpacko'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='nmpacko'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='ocoulibaly'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='ocoulibaly'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='mmarquez'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='mmarquez'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='tchoteau'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='tchoteau'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='eparisot'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='eparisot'), (SELECT id FROM authority WHERE name='ADMIN'));

--@formatter:on