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

DROP PROCEDURE IF EXISTS select_all_users;
DROP PROCEDURE IF EXISTS update_user;
DROP PROCEDURE IF EXISTS delete_user;
DELIMITER $$
CREATE PROCEDURE select_all_users()
BEGIN
 SELECT id,name,email,country FROM users ORDER BY id;
END$$
CREATE PROCEDURE update_user(IN user_id INT,IN user_name VARCHAR(50),IN user_email VARCHAR(50),IN user_country VARCHAR(50),OUT found_user INT)
BEGIN
 -- Return whether the row exists, even when submitted values are unchanged.
 UPDATE users SET name=user_name,email=user_email,country=user_country WHERE id=user_id;
 SELECT COUNT(*) INTO found_user FROM users WHERE id=user_id;
END$$
CREATE PROCEDURE delete_user(IN user_id INT,OUT deleted_rows INT)
BEGIN
 DELETE FROM users WHERE id=user_id;
 SET deleted_rows=ROW_COUNT();
END$$
DELIMITER ;