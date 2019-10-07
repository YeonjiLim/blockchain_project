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
-- Table structure for table `auction_bid`
--

DROP TABLE IF EXISTS `auction_bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `auction_bid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auction_participant_id` int(11) DEFAULT NULL,
  `auction_id` int(11) DEFAULT NULL,
  `bid_date` timestamp NOT NULL,
  `bid_price` decimal(15,2) NOT NULL,
  `winning_bid` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`),
  KEY `FK_경매입찰2` (`auction_id`),
  KEY `FK_경매입찰1` (`auction_participant_id`),
  CONSTRAINT `FK_경매입찰1` FOREIGN KEY (`auction_participant_id`) REFERENCES `auction_member` (`id`),
  CONSTRAINT `FK_경매입찰2` FOREIGN KEY (`auction_id`) REFERENCES `auction` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auction_bid`
--

LOCK TABLES `auction_bid` WRITE;
/*!40000 ALTER TABLE `auction_bid` DISABLE KEYS */;
INSERT INTO `auction_bid` VALUES (27,32,67,'2019-10-06 21:46:04',2.00,'Y'),(28,33,68,'2019-10-06 22:20:18',2.00,'N'),(29,33,69,'2019-10-06 22:23:43',2.00,'N'),(30,33,72,'2019-10-06 22:44:36',2.00,'N');
/*!40000 ALTER TABLE `auction_bid` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-07 17:18:20
