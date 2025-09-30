-- This script is for reference and to set up the database schema for the Offer Service.
-- It will not be run automatically by Spring Boot unless configured to do so.

CREATE DATABASE IF NOT EXISTS `bookmyshow_offers`;

USE `bookmyshow_offers`;

CREATE TABLE IF NOT EXISTS `offers` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `offer_code` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `discount_percentage` DOUBLE NOT NULL,
  `max_discount` DOUBLE NOT NULL,
  `expiry_date` DATE NOT NULL,
  `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_offer_code` (`offer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;