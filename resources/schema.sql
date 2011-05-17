CREATE DATABASE `versus`;

USE versus;

CREATE TABLE `comparisons` (
  `id` varchar(100) NOT NULL,
  `firstDataset` varchar(200) DEFAULT NULL,
  `secondDataset` varchar(200) DEFAULT NULL,
  `adapterId` varchar(200) DEFAULT NULL,
  `extractorId` varchar(200) DEFAULT NULL,
  `measureId` varchar(200) DEFAULT NULL,
  `status` varchar(200) DEFAULT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

GRANT ALL ON versus.* TO 'versus'@'localhost' IDENTIFIED BY 'versus';

