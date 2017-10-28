-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.6.21-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema test
--

CREATE DATABASE IF NOT EXISTS test;
USE test;

--
-- Definition of table `menu`
--

DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` varchar(45) NOT NULL,
  `pid` varchar(45) DEFAULT NULL,
  `mName` varchar(45) NOT NULL,
  `iconCls` varchar(45) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_menu_1` (`pid`),
  CONSTRAINT `FK_menu_1` FOREIGN KEY (`pid`) REFERENCES `menu` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `menu`
--

/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`id`,`pid`,`mName`,`iconCls`,`url`) VALUES 
 ('0',NULL,'首页',NULL,''),
 ('buggl','xtgl','BUG管理',NULL,NULL),
 ('cdgl','xtgl','菜单管理',NULL,NULL),
 ('jsgl','xtgl','角色管理',NULL,NULL),
 ('qxgl','xtgl','权限管理',NULL,NULL),
 ('xtgl','0','系统管理',NULL,''),
 ('yhgl','xtgl','用户管理',NULL,'/admin/yhgl.jsp');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;


--
-- Definition of table `schoolclass`
--

DROP TABLE IF EXISTS `schoolclass`;
CREATE TABLE `schoolclass` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `schoolclass`
--

/*!40000 ALTER TABLE `schoolclass` DISABLE KEYS */;
INSERT INTO `schoolclass` (`id`,`name`) VALUES 
 (1,'sss'),
 (2,'三年二班2ddd'),
 (3,'三年二班dddddd');
/*!40000 ALTER TABLE `schoolclass` ENABLE KEYS */;


--
-- Definition of table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `classid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `students`
--

/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` (`id`,`name`,`classid`) VALUES 
 (1,'dfsfs1',1),
 (10,'dfsfs0',0);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;


--
-- Definition of table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `sex` varchar(45) DEFAULT NULL,
  `age` int(10) unsigned DEFAULT NULL,
  `createdate` date DEFAULT NULL,
  `pwd` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`,`username`,`sex`,`age`,`createdate`,`pwd`) VALUES 
 ('0aaa','User','null',19,'2017-10-28','21232f297a57a5a743894a0e4a801fc3'),
 ('1aaa','User','null',19,'2017-10-28','21232f297a57a5a743894a0e4a801fc3'),
 ('2aaa','User','null',19,'2017-10-28','21232f297a57a5a743894a0e4a801fc3'),
 ('3aaa','User','null',19,'2017-10-28','21232f297a57a5a743894a0e4a801fc3'),
 ('4aaa','User','null',19,'2017-10-28','21232f297a57a5a743894a0e4a801fc3'),
 ('5aaa','User','null',19,'2017-10-28','21232f297a57a5a743894a0e4a801fc3');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
