-- This script is a reference for the tables the Report Service depends on.
-- These tables should exist in your main application's database.

CREATE DATABASE IF NOT EXISTS `bookmyshow_main`;

USE `bookmyshow_main`;

-- A simplified 'movies' table
CREATE TABLE IF NOT EXISTS `movies` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT,
  `release_date` DATE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- A simplified 'bookings' table
CREATE TABLE IF NOT EXISTS `bookings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `booking_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `movie_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `number_of_tickets` INT NOT NULL,
  `total_amount` DECIMAL(10, 2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_movie_id_idx` (`movie_id`),
  CONSTRAINT `fk_movie_id` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- You can insert some sample data for testing reports
-- INSERT INTO movies (title) VALUES ('The Grand Adventure'), ('Code Warriors');
-- INSERT INTO bookings (movie_id, user_id, number_of_tickets, total_amount) VALUES (1, 101, 2, 500.00), (1, 102, 4, 1000.00), (2, 103, 3, 650.00);