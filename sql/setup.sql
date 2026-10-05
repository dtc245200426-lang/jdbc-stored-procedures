CREATE DATABASE IF NOT EXISTS demo CHARACTER SET utf8mb4;
USE demo;
CREATE TABLE IF NOT EXISTS users (
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(50) NOT NULL,
 email VARCHAR(50) NOT NULL,
 country VARCHAR(50) NOT NULL
);
DROP PROCEDURE IF EXISTS get_user_by_id;
DROP PROCEDURE IF EXISTS insert_user;
DELIMITER $$
CREATE PROCEDURE get_user_by_id(IN user_id INT)
BEGIN
 SELECT users.name, users.email, users.country FROM users WHERE users.id=user_id;
END$$
CREATE PROCEDURE insert_user(IN user_name VARCHAR(50),IN user_email VARCHAR(50),IN user_country VARCHAR(50))
BEGIN
 INSERT INTO users(name,email,country) VALUES(user_name,user_email,user_country);
END$$
DELIMITER ;
