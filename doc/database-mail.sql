/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : springboot

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-15 18:06:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mail_account
-- ----------------------------
DROP TABLE IF EXISTS `mail_account`;
CREATE TABLE `mail_account` (
  `id` bigint(36) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '认证密码',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `mail` varchar(255) DEFAULT NULL COMMENT '邮箱地址',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `county` varchar(255) DEFAULT NULL COMMENT '市',
  `city` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL COMMENT '住址',
  `phone` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `protocol_id` bigint(36) DEFAULT NULL COMMENT '邮箱类型ID',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  `type` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` bigint(36) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `updator` bigint(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_account
-- ----------------------------
INSERT INTO `mail_account` VALUES ('1', 'pan19940609', '05a16b1b9f69988d7761dac02e68aa6d6dcb3898', '潘政名', 'pan19940609@163.com', '新疆维吾尔自治区', '阿图什市', '克孜勒苏柯尔克孜自治州', '的玩家地撒娇地爱上降低决赛的骄傲死的', '15995407462', '1', '0', '0', '2018-03-14 17:20:51', '1', '2018-03-15 18:05:09', '1');
INSERT INTO `mail_account` VALUES ('2', '1319650191', 'aa6ad7702083b1cf1b4dcb78b9f9bb3d04dd9d9e', '寰宇', '1319650191@qq.com', '北京市', '东城区', '市辖区', '建管路', '18986734128', '2', '0', '1', '2018-03-15 15:39:14', '1', '2018-03-15 18:02:11', '1');

-- ----------------------------
-- Table structure for mail_message
-- ----------------------------
DROP TABLE IF EXISTS `mail_message`;
CREATE TABLE `mail_message` (
  `id` varchar(255) NOT NULL,
  `account_id` bigint(36) DEFAULT NULL COMMENT '邮箱用户ID',
  `from` varchar(255) DEFAULT NULL COMMENT '发件人地址',
  `to` varchar(255) DEFAULT NULL COMMENT '收件人地址',
  `cc` varchar(255) DEFAULT NULL COMMENT '抄送人地址',
  `bcc` varchar(255) DEFAULT NULL COMMENT '暗抄送人',
  `subject` varchar(255) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '内容',
  `send_date` datetime DEFAULT NULL COMMENT '发送时间',
  `received_date` datetime DEFAULT NULL COMMENT '接收时间',
  `flags` varchar(255) DEFAULT NULL COMMENT '邮件标记',
  `size` bigint(36) DEFAULT NULL COMMENT '邮件大小',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_message
-- ----------------------------

-- ----------------------------
-- Table structure for mail_protocol
-- ----------------------------
DROP TABLE IF EXISTS `mail_protocol`;
CREATE TABLE `mail_protocol` (
  `id` bigint(36) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `link` varchar(255) DEFAULT NULL,
  `suffix` varchar(255) DEFAULT NULL COMMENT '邮箱后缀',
  `smtp` varchar(255) DEFAULT NULL COMMENT 'SMTP协议',
  `pop3` varchar(255) DEFAULT NULL COMMENT 'POP3协议',
  `imap` varchar(255) DEFAULT NULL COMMENT 'IMAP协议',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_protocol
-- ----------------------------
INSERT INTO `mail_protocol` VALUES ('1', '网易163邮箱', null, 'https://mail.163.com/', '@163.com', 'smtp.163.com', 'pop3.163.com', 'imap.163.com', '0');
INSERT INTO `mail_protocol` VALUES ('2', 'QQ邮箱', '这是腾讯旗下的QQ邮箱', 'https://mail.qq.com/', '@qq.com', 'smtp.qq.com', 'pop3.qq.com', 'imap.qq.com', '0');
