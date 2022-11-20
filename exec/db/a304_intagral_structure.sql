-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: k7a304.p.ssafy.io    Database: intagral
-- ------------------------------------------------------
-- Server version	5.7.40

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
-- Table structure for table `classification_target`
--

DROP TABLE IF EXISTS `classification_target`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classification_target` (
  `cls_target_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `target_name` varchar(45) NOT NULL,
  `target_name_kor` varchar(45) NOT NULL,
  `reg_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`cls_target_id`),
  UNIQUE KEY `target_name_UNIQUE` (`target_name`),
  UNIQUE KEY `target_name_kor_UNIQUE` (`target_name_kor`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hashtag`
--

DROP TABLE IF EXISTS `hashtag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hashtag` (
  `hashtag_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(45) NOT NULL,
  `search_cnt` int(11) NOT NULL DEFAULT '0',
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`hashtag_id`),
  UNIQUE KEY `content_UNIQUE` (`content`)
) ENGINE=InnoDB AUTO_INCREMENT=302 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hashtag_follow`
--

DROP TABLE IF EXISTS `hashtag_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hashtag_follow` (
  `hashtag_follow_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `hashtag_id` int(10) unsigned NOT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`hashtag_follow_id`),
  UNIQUE KEY `user_id` (`user_id`,`hashtag_id`),
  KEY `fk_hashtag_follow_user1_idx` (`user_id`),
  KEY `fk_hashtag_follow_hashtag1_idx` (`hashtag_id`),
  CONSTRAINT `fk_hashtag_follow_hashtag1` FOREIGN KEY (`hashtag_id`) REFERENCES `hashtag` (`hashtag_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_hashtag_follow_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hashtag_preset`
--

DROP TABLE IF EXISTS `hashtag_preset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hashtag_preset` (
  `hashtag_preset_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `hashtag_id` int(10) unsigned NOT NULL,
  `cls_target_id` int(10) unsigned NOT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`hashtag_preset_id`),
  UNIQUE KEY `user_id` (`user_id`,`hashtag_id`,`cls_target_id`),
  KEY `user_preset_idx` (`user_id`),
  KEY `user_hashtag_idx` (`hashtag_id`),
  KEY `target_preset_idx` (`cls_target_id`),
  CONSTRAINT `hashtag_preset` FOREIGN KEY (`hashtag_id`) REFERENCES `hashtag` (`hashtag_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `target_preset` FOREIGN KEY (`cls_target_id`) REFERENCES `classification_target` (`cls_target_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_preset` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1126 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `post_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `post_date` datetime NOT NULL,
  `img_path` varchar(150) DEFAULT NULL,
  `like_cnt` int(11) NOT NULL DEFAULT '0',
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `fk_post_user1_idx` (`user_id`),
  CONSTRAINT `fk_post_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=442 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post_hashtag`
--

DROP TABLE IF EXISTS `post_hashtag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_hashtag` (
  `post_hashtag_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` int(10) unsigned NOT NULL,
  `hashtag_id` int(10) unsigned NOT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`post_hashtag_id`),
  UNIQUE KEY `post_id` (`post_id`,`hashtag_id`),
  KEY `fk_post_hashtag_post1_idx` (`post_id`),
  KEY `fk_post_hashtag_hashtag1_idx` (`hashtag_id`),
  CONSTRAINT `fk_post_hashtag_hashtag1` FOREIGN KEY (`hashtag_id`) REFERENCES `hashtag` (`hashtag_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_hashtag_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=889 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_like` (
  `post_like_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`post_like_id`),
  UNIQUE KEY `post_id` (`post_id`,`user_id`),
  KEY `fk_post_like_post1_idx` (`post_id`),
  KEY `fk_post_like_user1_idx` (`user_id`),
  CONSTRAINT `fk_post_like_post1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_like_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8mb4 COMMENT='0';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `profile_img_path` varchar(150) DEFAULT NULL,
  `intro` varchar(100) DEFAULT NULL,
  `access_token` varchar(150) DEFAULT NULL,
  `refresh_token` varchar(60) DEFAULT NULL,
  `auth_token` varchar(300) DEFAULT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `oauth_platform` varchar(2) DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `nickname_UNIQUE` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_follow`
--

DROP TABLE IF EXISTS `user_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_follow` (
  `user_follow_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id_from` int(10) unsigned NOT NULL,
  `user_id_to` int(10) unsigned NOT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  `mdf_date` timestamp NULL DEFAULT NULL,
  `reg_user` int(10) unsigned DEFAULT NULL,
  `mdf_user` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`user_follow_id`),
  UNIQUE KEY `user_id_from` (`user_id_from`,`user_id_to`),
  KEY `fk_follow_user1_idx` (`user_id_from`),
  KEY `fk_follow_user2_idx` (`user_id_to`),
  CONSTRAINT `fk_follow_user1` FOREIGN KEY (`user_id_from`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_follow_user2` FOREIGN KEY (`user_id_to`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=213 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-20 15:30:45
