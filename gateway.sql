/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost
 Source Database       : gateway

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : utf-8

 Date: 10/18/2019 11:06:10 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `filter_definition`
-- ----------------------------
DROP TABLE IF EXISTS `filter_definition`;
CREATE TABLE `filter_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL COMMENT '谓词名称',
  `args` varchar(200) NOT NULL COMMENT '谓词 参数',
  `rd_id` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `enabled` int(1) NOT NULL DEFAULT '1' COMMENT '0 不可用 1 可用',
  PRIMARY KEY (`id`),
  KEY `route_predicat` (`rd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
--  Records of `filter_definition`
-- ----------------------------
BEGIN;
INSERT INTO `filter_definition` VALUES ('1', 'StripPrefix', '[{\"argKey\":\"parts\",\"argValue\":\"1\"}]', '1', '2019-10-17 18:17:56', '2019-10-17 18:21:06', '1'), ('2', 'Hystrix', '[{\"argKey\":\"name\",\"argValue\":\"fallbackcmd\"},{\"argKey\":\"fallbackUri\",\"argValue\":\"forward:/fallback?a=128\"}]', '1', '2019-10-17 18:20:07', '2019-10-17 18:20:07', '1'), ('3', 'RequestRateLimiter', '[{\"argKey\":\"keyResolver\",\"argValue\":\"#{@ipAddressKeyResolver}\"},{\"argKey\":\"redis-rate-limiter.replenishRate\",\"argValue\":\"1\"},{\"argKey\":\"redis-rate-limiter.burstCapacity\",\"argValue\":\"1\"}]', '1', '2019-10-17 18:24:04', '2019-10-17 18:24:04', '1'), ('4', 'ModifyRequestParams', '[]', '1', '2019-10-17 18:25:13', '2019-10-17 18:25:13', '1');
COMMIT;

-- ----------------------------
--  Table structure for `ip_black`
-- ----------------------------
DROP TABLE IF EXISTS `ip_black`;
CREATE TABLE `ip_black` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip_addr` varchar(255) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
--  Records of `ip_black`
-- ----------------------------
BEGIN;
INSERT INTO `ip_black` VALUES ('1', '127.0.0.2', '2019-10-18 09:33:32', '2019-10-18 09:33:32');
COMMIT;

-- ----------------------------
--  Table structure for `ip_white`
-- ----------------------------
DROP TABLE IF EXISTS `ip_white`;
CREATE TABLE `ip_white` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip_addr` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='ip 白名单';

-- ----------------------------
--  Records of `ip_white`
-- ----------------------------
BEGIN;
INSERT INTO `ip_white` VALUES ('1', '127.0.0.1', '2019-10-18 09:33:19', '2019-10-18 09:33:19');
COMMIT;

-- ----------------------------
--  Table structure for `predicate_definition`
-- ----------------------------
DROP TABLE IF EXISTS `predicate_definition`;
CREATE TABLE `predicate_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL COMMENT '谓词名称',
  `args` varchar(200) NOT NULL COMMENT '谓词 参数',
  `rd_id` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `enabled` int(1) NOT NULL DEFAULT '1' COMMENT '0 不可用 1 可用',
  PRIMARY KEY (`id`),
  KEY `route_predicat` (`rd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
--  Records of `predicate_definition`
-- ----------------------------
BEGIN;
INSERT INTO `predicate_definition` VALUES ('1', 'Path', '[{\"argKey\":\"pattern\",\"argValue\":\"/service/**\"}]', '1', '2019-10-17 18:17:03', '2019-10-17 18:17:03', '1'), ('2', 'Host', '[{\"argKey\":\"pattern\",\"argValue\":\"127.0.0.1\"}]', '1', '2019-10-17 18:35:39', '2019-10-17 18:36:07', '1');
COMMIT;

-- ----------------------------
--  Table structure for `route_definition`
-- ----------------------------
DROP TABLE IF EXISTS `route_definition`;
CREATE TABLE `route_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rd_id` varchar(30) NOT NULL COMMENT 'RouteDefinition 的id',
  `rd_uri` varchar(100) NOT NULL COMMENT 'RouteDefinition 的uri [比如 lb://fdl ]',
  `rd_order` int(3) NOT NULL COMMENT 'RouteDefinition 的顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `enabled` int(1) NOT NULL DEFAULT '1' COMMENT '0 不可用 1 可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
--  Records of `route_definition`
-- ----------------------------
BEGIN;
INSERT INTO `route_definition` VALUES ('1', '1', 'lb://fdl', '0', '2019-10-17 17:15:01', '2019-10-17 18:14:40', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
