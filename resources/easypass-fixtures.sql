-- --------------------------------------------------------
-- Hôte :                        127.0.0.1
-- Version du serveur:           10.1.10-MariaDB - mariadb.org binary distribution
-- SE du serveur:                Win32
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Export de la structure de la base pour easypass
DROP DATABASE IF EXISTS `easypass`;
CREATE DATABASE IF NOT EXISTS `easypass` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `easypass`;


-- Export de la structure de table easypass. categories
DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  KEY `Index 1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Export de données de la table easypass.categories : ~6 rows (environ)
DELETE FROM `categories`;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` (`id`, `name`, `logo`) VALUES
  (1, 'Forums', 'forum1.png'),
  (2, 'Réseaux sociaux', 'social.png'),
  (3, 'Autre', 'fake-path.png'),
  (4, 'Boîte mail', 'mail_2.png'),
  (5, 'Travail', 'Sad-after-having-a-work-load.png'),
  (6, 'Useless', '12670213_1957609551131792_6738362166244948893_n.png');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;


-- Export de la structure de table easypass. groups
DROP TABLE IF EXISTS `groups`;
CREATE TABLE IF NOT EXISTS `groups` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logo` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  KEY `Index 1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Export de données de la table easypass.groups : ~5 rows (environ)
DELETE FROM `groups`;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` (`id`, `name`, `description`, `logo`) VALUES
  (1, 'Site Zoo', 'Administrateur du site du Zoo', 'Monkey-Mia-MJK-Australia-650x433.jpg'),
  (2, 'Useless', 'Groupe useless', '12670213_1957609551131792_6738362166244948893_n.png'),
  (3, 'Page Facebook ESGI', 'Facebook', 'logo.png.jpg'),
  (4, 'Forum Photographes', 'Administrateur du forum de photographies', 'fake-path.jpg'),
  (5, 'Champions de Tennis', 'Les meilleurs joueurs sur un seul site.', 'slide-image-3.jpg');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;


-- Export de la structure de table easypass. owner_password
DROP TABLE IF EXISTS `owner_password`;
CREATE TABLE IF NOT EXISTS `owner_password` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `group_id` int(10) unsigned DEFAULT NULL,
  `password_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `owner_password_user_id_foreign` (`user_id`),
  KEY `owner_password_group_id_foreign` (`group_id`),
  KEY `owner_password_password_foreign` (`password_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Export de données de la table easypass.owner_password : ~6 rows (environ)
DELETE FROM `owner_password`;
/*!40000 ALTER TABLE `owner_password` DISABLE KEYS */;
INSERT INTO `owner_password` (`id`, `user_id`, `group_id`, `password_id`) VALUES
  (1, 3, NULL, 1),
  (2, NULL, 5, 2),
  (3, NULL, 5, 3),
  (4, 3, NULL, 4),
  (5, 3, NULL, 5),
  (6, 2, NULL, 6);
/*!40000 ALTER TABLE `owner_password` ENABLE KEYS */;


-- Export de la structure de table easypass. passwords
DROP TABLE IF EXISTS `passwords`;
CREATE TABLE IF NOT EXISTS `passwords` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `category_id` int(10) NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `informations` varchar(255) COLLATE utf8_unicode_ci DEFAULT '0',
  `urlSite` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `paswords_category_id_foreign` (`category_id`),
  CONSTRAINT `FK_passwords_categories` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Export de données de la table easypass.passwords : ~6 rows (environ)
DELETE FROM `passwords`;
/*!40000 ALTER TABLE `passwords` DISABLE KEYS */;
INSERT INTO `passwords` (`id`, `category_id`, `title`, `informations`, `urlSite`, `password`) VALUES
  (1, 7, 'Accès club de tennis', '', 'Tennis club Moussy', '0Ipa7/jpJ6xlDHxcC9GnLQ=='),
  (2, 7, 'Code d\'accès au gymnase', '', 'Aucun', 'JDiaeMPVCdLOYTOa23p71w=='),
  (3, 7, 'Mot de passe Membre VIP', '', 'www.zone-tennis.com', 'd84WZpBmkUvQZDSzJQb1euZRlUEDDjrp'),
  (4, 3, 'Useless', 'A supprimer !', 'www.useless.fr', 'URIOOHEqs/QpnXKubLpfqg=='),
  (5, 2, 'Facebook', '', 'www.facebook.com', 'MGiwWxw4Fbd3+rplO5ohwg=='),
  (6, 5, 'VIP Imagine Dragons', '', 'www.imagine-dragons.fr', '1A2o2KtPuAGqZYSBXZ6mdhpVHslqMHEf');
/*!40000 ALTER TABLE `passwords` ENABLE KEYS */;


-- Export de la structure de table easypass. users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `lastname` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `admin` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Export de données de la table easypass.users : ~5 rows (environ)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `firstname`, `lastname`, `username`, `password`, `email`, `admin`) VALUES
  (1, 'Adrien', 'Turcey', 'aturcey', '7b902e6ff1db9f560443f2048974fd7d386975b0', 'adrienturcey@gmail.com', 1),
  (2, 'Jonathan', 'Cholet', 'jcholet', '7b902e6ff1db9f560443f2048974fd7d386975b0', 'jcholet@gmail.com', 0),
  (3, 'Jeff', 'Jacquelot', 'jjacquelot', '7b902e6ff1db9f560443f2048974fd7d386975b0', 'jjacquelot@gmail.com', 0),
  (4, 'Use', 'Less', 'useless', '7b902e6ff1db9f560443f2048974fd7d386975b0', 'useless@gmail.com', 0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;


-- Export de la structure de table easypass. user_group
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE IF NOT EXISTS `user_group` (
  `user_id` int(10) DEFAULT NULL,
  `admin` int(1) DEFAULT NULL,
  `group_id` int(10) DEFAULT NULL,
  KEY `group_owner` (`group_id`),
  KEY `user_owner` (`user_id`),
  CONSTRAINT `group_owner` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_owner` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Export de données de la table easypass.user_group : ~20 rows (environ)
DELETE FROM `user_group`;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
INSERT INTO `user_group` (`user_id`, `admin`, `group_id`) VALUES
  (2, 0, 1),
  (5, 0, 1),
  (6, 0, 1),
  (1, 1, 1),
  (3, 0, 2),
  (5, 0, 2),
  (1, 1, 2),
  (3, 0, 3),
  (5, 0, 3),
  (1, 1, 3),
  (2, 0, 4),
  (3, 0, 4),
  (5, 0, 4),
  (6, 0, 4),
  (1, 1, 4),
  (1, 0, 5),
  (2, 0, 5),
  (5, 0, 5),
  (6, 0, 5),
  (3, 1, 5);
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;