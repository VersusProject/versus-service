USE versus;
CREATE TABLE `maps`(
`versusId` varchar(100) NOT NULL,
`mediciId` varchar(100) DEFAULT NULL,
`indexId` varchar(100) DEFAULT NULL,
PRIMARY KEY (`versusId`),
UNIQUE KEY `id_UNIQUE` (`versusId`)
  ) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

GRANT ALL ON versus.* TO 'versus'@'localhost' IDENTIFIED BY 'versus';
