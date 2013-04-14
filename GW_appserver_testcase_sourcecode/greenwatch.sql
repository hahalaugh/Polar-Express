/*
Navicat MySQL Data Transfer

Source Server         : JAJ
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : greenwatch

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2013-04-03 20:56:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `community`
-- ----------------------------
DROP TABLE IF EXISTS `community`;
CREATE TABLE `community` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `founder` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of community
-- ----------------------------
INSERT INTO `community` VALUES ('1', 'TCD', 'Trinity College Dublin', 'hahalaugh');
INSERT INTO `community` VALUES ('2', 'UCD', 'University College Dublin', 'hahalaugh');
INSERT INTO `community` VALUES ('3', 'STEPHENS GREEN PARK', 'STEFENS GREEN PARK', 'hahalaugh');

-- ----------------------------
-- Table structure for `image`
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` int(11) NOT NULL,
  `path` text NOT NULL,
  `date` date NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of image
-- ----------------------------

-- ----------------------------
-- Table structure for `land`
-- ----------------------------
DROP TABLE IF EXISTS `land`;
CREATE TABLE `land` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(64) NOT NULL,
  `uploader` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of land
-- ----------------------------
INSERT INTO `land` VALUES ('1', 'Grafton Street Stevens Green', 'Juntao');
INSERT INTO `land` VALUES ('2', 'Nassau Street #66', 'Evangelos');
INSERT INTO `land` VALUES ('3', 'Thomas Street #22 ', 'Ruben');
INSERT INTO `land` VALUES ('4', 'name', 'juntao');

-- ----------------------------
-- Table structure for `member`
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `capacity` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES ('1', 'juntao', 'juntao', '123456', 'geologist', 'juntao@qq.com');
INSERT INTO `member` VALUES ('2', 'martin', 'emms', 'qazwsx', 'agriculturist', 'emms@gmail.cm');
INSERT INTO `member` VALUES ('3', 'ruben', 'ruben', 'ertdffg', 'gardener', 'ruben@tcd.ie');
INSERT INTO `member` VALUES ('4', 'marco', 'marco', 'plmokn', 'amateur', 'marco@gmail.com');

-- ----------------------------
-- Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', 'Location :Kirwan Street 23 Time:3/4/2013 Members Number:4', 'Prunning Trees');
INSERT INTO `task` VALUES ('2', 'Location: Bow Street 4 Time:4/4/2013 17:30 Members number: 3', 'Water carrots');
INSERT INTO `task` VALUES ('3', 'Location: Cabra Drive Time: 3/4/2013', 'Fertilizing Potatoes');
