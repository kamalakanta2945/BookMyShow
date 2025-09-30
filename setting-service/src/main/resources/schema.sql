-- This script is for reference and to set up the database schema for the Setting Service.
-- It will not be run automatically by Spring Boot unless configured to do so.

CREATE DATABASE IF NOT EXISTS `bookmyshow_settings`;

USE `bookmyshow_settings`;

CREATE TABLE IF NOT EXISTS `settings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `setting_key` VARCHAR(100) NOT NULL,
  `setting_value` VARCHAR(512) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_setting_key` (`setting_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;