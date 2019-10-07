-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: 54.180.148.38    Database: haribo
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hash` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nonce` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `block_hash` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `block_number` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `transaction_index` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `from_hash` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_hash` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gas_price` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gas` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `input` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `creates` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `public_key` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `raw` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `r` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `v` int(11) DEFAULT NULL,
  `save_date` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-07 17:18:19
