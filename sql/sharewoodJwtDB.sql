DROP DATABASE IF EXISTS sharewood_jwt_users;

CREATE DATABASE sharewood_jwt_users DEFAULT CHARACTER SET 'utf8'
  DEFAULT COLLATE 'utf8_unicode_ci';

USE sharewood_jwt_users;

create table oauth_client_details (
  client_id VARCHAR(30) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
) ENGINE = InnoDB;



CREATE TABLE user (
  userId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(30) NOT NULL,
  hashedPassword BINARY(255) NOT NULL,
  accountNonExpired BOOLEAN NOT NULL,
  accountNonLocked BOOLEAN NOT NULL,
  credentialsNonExpired BOOLEAN NOT NULL,
  enabled BOOLEAN NOT NULL,
  CONSTRAINT user_unique UNIQUE (username)
) ENGINE = InnoDB;

CREATE TABLE user_Authority (
  userId BIGINT UNSIGNED NOT NULL,
  authority VARCHAR(100) NOT NULL,
  UNIQUE KEY user_Authority_User_Authority (userId, authority),
  CONSTRAINT user_Authority_UserId FOREIGN KEY (userId)
    REFERENCES user (userId) ON DELETE CASCADE
) ENGINE = InnoDB;




INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- s1a2t3o4r
  'Carol', '{bcrypt}$2a$10$qC5Nac5EvZ3TtOyZyNyA3uN3WlNIoBDMItrANlktLMtY65B3KON/C',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- a5r6e7p8o
  'Albert', '{bcrypt}$2a$10$bic61t1dz97vze.24wfZq.Hv6x672hRDHwiqTuxqVUwDxbgs7G86K',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- t4e3n2e1t
  'Werner', '{bcrypt}$2a$10$xAIx253rsuE3NvjPmQCT4e5l4cYQ7qFC4pW5cBIIjoZgJhaYbiKNG',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- o8p7e6r5a
  'Alice', '{bcrypt}$2a$10$V45DS2eQ0lXsoVOOyE8.duO/koj8jXl2LvywOtPMlBtaLv9s8GeO6',
  TRUE, TRUE, TRUE, TRUE
);

INSERT INTO user (username, hashedPassword, accountNonExpired,
                           accountNonLocked, credentialsNonExpired, enabled)
VALUES ( -- r1o2t3a4s
  'Richard', '{bcrypt}$2a$10$M179fcSSqce2P3wJz0mj9OxNNuLbTThVYlAtPim4mnvXsfYA0AHfW',
  TRUE, TRUE, TRUE, TRUE
);



INSERT INTO user_Authority (UserId, Authority)
  VALUES (1, 'USER');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (2, 'USER');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (3, 'USER');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (3, 'SQUIRREL');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (4, 'USER');

INSERT INTO user_Authority (UserId, Authority)
  VALUES (5, 'ADMIN');

   
-- Store a single client with authorization code grant 

INSERT INTO oauth_client_details (
  client_id, 
  resource_ids, 
  client_secret, 
  scope, 
  authorized_grant_types, 
  web_server_redirect_uri,
  authorities,
  access_token_validity,
  refresh_token_validity,
  additional_information,
  autoapprove)
VALUES('Fleetwood', 'SHAREWOOD', '{bcrypt}$2a$10$azPaUMBpKewR9l0BcKUIX.k3/CS3vdbmXG1EoxJfbKpdEdmirle6.', 'TRUST', 'authorization_code', null, 'ROLE_CLIENT', '5200', null, '{}', null
);




