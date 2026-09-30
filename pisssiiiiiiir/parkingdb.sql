CREATE DATABASE  IF NOT EXISTS `parkingdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `parkingdb`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: parkingdb
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `charging_requests`
--

DROP TABLE IF EXISTS `charging_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charging_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `charging_completion_time` datetime(6) DEFAULT NULL,
  `charging_start_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `current_percentage` int DEFAULT NULL,
  `estimated_completion_time` datetime(6) DEFAULT NULL,
  `estimated_start_time` datetime(6) DEFAULT NULL,
  `initial_percentage` int NOT NULL,
  `notification_requested` bit(1) NOT NULL,
  `notification_sent` bit(1) NOT NULL,
  `queue_position` int DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `target_percentage` int NOT NULL,
  `total_energy_kwh` double DEFAULT NULL,
  `mwbot_id` bigint DEFAULT NULL,
  `parking_spot_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4pk40ob613w01enul9jyw9fwk` (`mwbot_id`),
  KEY `FK84jeh8unjy4jh28cguaq86qgj` (`parking_spot_id`),
  KEY `FKp6llgshy8a0oklljqv8vke9on` (`user_id`),
  CONSTRAINT `FK4pk40ob613w01enul9jyw9fwk` FOREIGN KEY (`mwbot_id`) REFERENCES `mwbots` (`id`),
  CONSTRAINT `FK84jeh8unjy4jh28cguaq86qgj` FOREIGN KEY (`parking_spot_id`) REFERENCES `parking_spots` (`id`),
  CONSTRAINT `FKp6llgshy8a0oklljqv8vke9on` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charging_requests`
--

LOCK TABLES `charging_requests` WRITE;
/*!40000 ALTER TABLE `charging_requests` DISABLE KEYS */;
INSERT INTO `charging_requests` VALUES (1,'2025-05-20 14:57:59.463978','2025-05-20 14:55:52.297404','2025-05-20 14:55:52.261168',70,'2025-05-20 16:55:52.298406',NULL,15,_binary '',_binary '',1,'COMPLETED',70,22,NULL,2,2),(2,'2025-05-20 15:16:46.036437','2025-05-20 15:15:27.099859','2025-05-20 15:15:27.054712',80,'2025-05-20 19:00:27.099859',NULL,25,_binary '',_binary '',1,'COMPLETED',80,41.25,NULL,1,3),(3,'2025-05-20 15:18:32.693599','2025-05-20 15:17:50.247493','2025-05-20 15:17:50.206810',100,'2025-05-20 16:39:50.247493',NULL,80,_binary '',_binary '',1,'COMPLETED',100,15,NULL,1,3);
/*!40000 ALTER TABLE `charging_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mwbots`
--

DROP TABLE IF EXISTS `mwbots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mwbots` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `battery_level` int NOT NULL,
  `bot_id` varchar(255) NOT NULL,
  `charging_rate_kw` double NOT NULL,
  `current_location` varchar(255) DEFAULT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `maintenance_required` bit(1) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `current_charging_request_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o6gi7dslyv0y8dewdlu02wt3i` (`bot_id`),
  KEY `FKa89no3of2wayxwc8lx5tb31bw` (`current_charging_request_id`),
  CONSTRAINT `FKa89no3of2wayxwc8lx5tb31bw` FOREIGN KEY (`current_charging_request_id`) REFERENCES `charging_requests` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mwbots`
--

LOCK TABLES `mwbots` WRITE;
/*!40000 ALTER TABLE `mwbots` DISABLE KEYS */;
INSERT INTO `mwbots` VALUES (1,85,'MWB001',11,NULL,NULL,'2025-05-20 15:18:32.718190',_binary '\0','AVAILABLE',3);
/*!40000 ALTER TABLE `mwbots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_spots`
--

DROP TABLE IF EXISTS `parking_spots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_spots` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `spot_number` varchar(255) NOT NULL,
  `type` enum('standard','charging') NOT NULL,
  `availability` tinyint(1) DEFAULT '1',
  `location` varchar(255) NOT NULL,
  `charging_available` bit(1) NOT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `occupied` bit(1) NOT NULL,
  `occupied_since` datetime(6) DEFAULT NULL,
  `reserved` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `spot_number` (`spot_number`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_spots`
--

LOCK TABLES `parking_spots` WRITE;
/*!40000 ALTER TABLE `parking_spots` DISABLE KEYS */;
INSERT INTO `parking_spots` VALUES (1,'A1','standard',1,'Primo piano, Area A',_binary '','2025-05-20 15:19:05.124663',_binary '\0',NULL,_binary ''),(2,'A2','standard',1,'Primo piano, Area A',_binary '','2025-05-20 14:59:24.855042',_binary '\0',NULL,_binary '\0'),(3,'B1','standard',1,'Primo piano, Area A',_binary '','2025-05-20 14:52:21.482618',_binary '\0',NULL,_binary '\0');
/*!40000 ALTER TABLE `parking_spots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `card_last_four` varchar(255) DEFAULT NULL,
  `currency` varchar(255) NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `charging_request_id` bigint DEFAULT NULL,
  `reservation_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKib3tjonsp5epw76q647o7nfx` (`charging_request_id`),
  KEY `FKp8yh4sjt3u0g6aru1oxfh3o14` (`reservation_id`),
  KEY `FKj94hgy9v5fw1munb90tar2eje` (`user_id`),
  CONSTRAINT `FKib3tjonsp5epw76q647o7nfx` FOREIGN KEY (`charging_request_id`) REFERENCES `charging_requests` (`id`),
  CONSTRAINT `FKj94hgy9v5fw1munb90tar2eje` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKp8yh4sjt3u0g6aru1oxfh3o14` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,3,NULL,'EUR','2025-05-20 14:59:55.275132','CREDIT_CARD','COMPLETED','TXN-84E328D8','PARKING_ONLY',NULL,NULL,2),(2,3,'1111','EUR','2025-05-20 15:19:29.188739','CREDIT_CARD','COMPLETED','TXN-CBC5B2BE','PARKING_ONLY',NULL,NULL,3),(3,22.6875,'1111','EUR','2025-05-20 15:21:08.362673','CREDIT_CARD','COMPLETED','TXN-E364C166','PARKING_AND_CHARGING',3,NULL,3);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `start_time` datetime(6) NOT NULL,
  `end_time` datetime(6) NOT NULL,
  `user_id` bigint NOT NULL,
  `parking_spot_id` bigint NOT NULL,
  `cancelled_at` datetime(6) DEFAULT NULL,
  `charging_required` bit(1) NOT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_no_overlap` (`parking_spot_id`,`start_time`,`end_time`),
  KEY `idx_user_reservations` (`user_id`),
  CONSTRAINT `fk_reservation_spot` FOREIGN KEY (`parking_spot_id`) REFERENCES `parking_spots` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reservation_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (1,'2025-05-20 16:00:00.000000','2025-05-20 18:00:00.000000',3,1,NULL,_binary '',NULL,NULL,'CONFIRMED','2025-05-20 15:14:25.938798');
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `battery_capacity_kw` double DEFAULT NULL,
  `car_license_plate` varchar(255) DEFAULT NULL,
  `car_model` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `credit_card_cvv` varchar(255) DEFAULT NULL,
  `credit_card_expiry` varchar(255) DEFAULT NULL,
  `credit_card_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  CONSTRAINT `chk_role` CHECK ((`role` in (_utf8mb4'BASE_USER',_utf8mb4'PREMIUM_USER',_utf8mb4'ADMIN')))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'$2a$10$Q3wwEDExhXMKcM026EoMhO.tGmloRZTmzYi3b1uDO63hkSVFmqzGu','ADMIN','admin1',NULL,NULL,NULL,'2025-05-20 14:50:30.736403',NULL,NULL,NULL,'marco@parkingsystem.com','Marco Amministratore','2025-05-20 14:50:30.736403'),(2,'$2a$10$tMwYJK7HS5P7QXo3DORnm.LJgsUmB3FgtWXxtG1ACTc0yoZ/WLB5W','BASE_USER','luca_user',40,'AB123CD','Nissan Leaf','2025-05-20 14:53:26.402488',NULL,NULL,NULL,'luca@example.com','Luca Rossi','2025-05-20 14:53:26.402488'),(3,'$2a$10$nq42wRCtDupl3uxEt.aPxu1v1m4dXT6hlBUzddSNLylfuXB4rpCg.','PREMIUM_USER','sara_premium',75,'XY789ZW','Tesla Model 3','2025-05-20 15:00:47.747584','123','12/26','4111111111111111','sara@example.com','Sara Bianchi','2025-05-20 15:00:47.747584'),(4,'$2a$10$y1BMK4fJJTzrXLGKQSSZLOUp3dFW1.uY3dObonwu1z2Uc0j9gKhO.','ADMIN','dylan',15,'XSDAFA','audi','2025-05-20 16:06:31.109831',NULL,NULL,NULL,'dylan@gmail.com','dylantest','2025-05-20 16:06:31.109831'),(5,'$2a$10$dQjnTBJtdPJY7ygY2r/mNep4oJhoalc946UVeCWGSXoqP5uHWNGia','ADMIN','admin',15,'XSDAFA','audi','2025-05-20 16:08:29.572082',NULL,NULL,NULL,'admin@example.com','Administrator','2025-05-20 16:08:29.572082');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'parkingdb'
--

--
-- Dumping routines for database 'parkingdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-20 16:18:26
