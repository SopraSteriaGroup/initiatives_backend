--@formatter:off

-- Table user
INSERT INTO user (username, password) VALUES ('jntakpe', 'test');
INSERT INTO user (username, password) VALUES ('cbarillet', 'test');
INSERT INTO user (username, password) VALUES ('sberger', 'test');
INSERT INTO user (username, password) VALUES ('smaitre', 'test');
INSERT INTO user (username, password) VALUES ('ccavelier', 'test');
INSERT INTO user (username, password) VALUES ('tmarchandise', 'test');

-- Table authority
INSERT INTO authority (name) VALUES ('USER');
INSERT INTO authority (name) VALUES ('ADMIN');

-- Table user_authorities
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='jntakpe'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='jntakpe'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='cbarillet'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='cbarillet'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='sberger'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='smaitre'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='smaitre'), (SELECT id FROM authority WHERE name='ADMIN'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='ccavelier'), (SELECT id FROM authority WHERE name='USER'));
INSERT INTO user_authorities(user_id, authority_id) VALUES ((SELECT id FROM user WHERE username='ccavelier'), (SELECT id FROM authority WHERE name='ADMIN'));

--@formatter:on