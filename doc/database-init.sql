/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : springboot

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-01-16 17:26:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for core_annex
-- ----------------------------
DROP TABLE IF EXISTS `core_annex`;
CREATE TABLE `core_annex` (
  `id` varchar(36) NOT NULL COMMENT '编号',
  `object_id` varchar(36) DEFAULT NULL COMMENT '对象ID',
  `object_type` varchar(32) DEFAULT NULL COMMENT '对象类型',
  `path` varchar(512) DEFAULT NULL COMMENT '存储路径',
  `name` varchar(128) DEFAULT NULL COMMENT '文件名称',
  `type` varchar(16) DEFAULT NULL COMMENT '文件类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
  `fastdfs_url` varchar(225) DEFAULT NULL COMMENT 'FastDFS路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件';

-- ----------------------------
-- Records of core_annex
-- ----------------------------

-- ----------------------------
-- Table structure for core_area
-- ----------------------------
DROP TABLE IF EXISTS `core_area`;
CREATE TABLE `core_area` (
  `id` varchar(36) NOT NULL COMMENT '编号',
  `code` varchar(8) DEFAULT NULL COMMENT '代码',
  `name` varchar(16) DEFAULT NULL COMMENT '名称',
  `full_name` varchar(32) DEFAULT NULL COMMENT '全称',
  `grade` tinyint(1) DEFAULT NULL COMMENT '层级',
  `type` varchar(1) DEFAULT NULL COMMENT '类型(1:省,2:直辖市,3:地级市,4:县级市,5:县,6:区)',
  `root_id` varchar(36) DEFAULT NULL COMMENT '根ID',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地区';

-- ----------------------------
-- Records of core_area
-- ----------------------------

-- ----------------------------
-- Table structure for core_config
-- ----------------------------
DROP TABLE IF EXISTS `core_config`;
CREATE TABLE `core_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(128) DEFAULT NULL COMMENT '键',
  `value` text COMMENT '值',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型(0:Input输入框,1:Textarea文本框,2:Radio单选框,3:Checkbox多选框,4:Select单选框,5:Select多选框)',
  `params` text COMMENT '参数',
  `editable` tinyint(1) DEFAULT NULL COMMENT '是否可修改 (0:不可修改,1:可修改)',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置';

-- ----------------------------
-- Records of core_config
-- ----------------------------

-- ----------------------------
-- Table structure for core_data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `core_data_dictionary`;
CREATE TABLE `core_data_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type_id` bigint(20) DEFAULT NULL COMMENT '类型',
  `code` varchar(64) DEFAULT NULL COMMENT '代码',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `priority` int(11) DEFAULT NULL COMMENT '序号',
  `grade` smallint(6) DEFAULT NULL COMMENT '层级',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父ID',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';

-- ----------------------------
-- Records of core_data_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for core_data_type
-- ----------------------------
DROP TABLE IF EXISTS `core_data_type`;
CREATE TABLE `core_data_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(64) DEFAULT NULL COMMENT '代码',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `grade` smallint(6) DEFAULT NULL COMMENT '层级',
  `editable` tinyint(1) DEFAULT NULL COMMENT '是否可修改 (0:不可修改,1:可修改)',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据类型';

-- ----------------------------
-- Records of core_data_type
-- ----------------------------

-- ----------------------------
-- Table structure for core_group_role
-- ----------------------------
DROP TABLE IF EXISTS `core_group_role`;
CREATE TABLE `core_group_role` (
  `group_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组和角色';

-- ----------------------------
-- Records of core_group_role
-- ----------------------------

-- ----------------------------
-- Table structure for core_log
-- ----------------------------
DROP TABLE IF EXISTS `core_log`;
CREATE TABLE `core_log` (
  `id` varchar(36) NOT NULL COMMENT '编号',
  `description` varchar(128) DEFAULT NULL COMMENT '操作描述',
  `username` varchar(64) DEFAULT NULL COMMENT '操作用户',
  `log_time` datetime DEFAULT NULL COMMENT '操作时间',
  `spend_time` int(11) DEFAULT NULL COMMENT '消耗时间',
  `ip` varchar(16) DEFAULT NULL COMMENT 'IP地址',
  `url` varchar(256) DEFAULT NULL COMMENT 'URL',
  `method` varchar(8) DEFAULT NULL COMMENT '请求类型',
  `user_agent` varchar(256) DEFAULT NULL COMMENT '用户标识',
  `parameter` text COMMENT '请求参数',
  `result` text COMMENT '响应结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- ----------------------------
-- Records of core_log
-- ----------------------------

-- ----------------------------
-- Table structure for core_membership
-- ----------------------------
DROP TABLE IF EXISTS `core_membership`;
CREATE TABLE `core_membership` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户和用户组';

-- ----------------------------
-- Records of core_membership
-- ----------------------------

-- ----------------------------
-- Table structure for core_permission
-- ----------------------------
DROP TABLE IF EXISTS `core_permission`;
CREATE TABLE `core_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(64) NOT NULL COMMENT '代码',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `priority` int(11) DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限';

-- ----------------------------
-- Records of core_permission
-- ----------------------------

-- ----------------------------
-- Table structure for core_role
-- ----------------------------
DROP TABLE IF EXISTS `core_role`;
CREATE TABLE `core_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(64) NOT NULL COMMENT '代码',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of core_role
-- ----------------------------
INSERT INTO `core_role` VALUES ('1', 'admin', '系统管理员');
INSERT INTO `core_role` VALUES ('2', 'manager', '管理人员');

-- ----------------------------
-- Table structure for core_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `core_role_permission`;
CREATE TABLE `core_role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of core_role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for core_token
-- ----------------------------
DROP TABLE IF EXISTS `core_token`;
CREATE TABLE `core_token` (
  `id` varchar(36) NOT NULL COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `terminal` varchar(16) DEFAULT NULL COMMENT '终端',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态(0:有效,1:失效)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='访问令牌';

-- ----------------------------
-- Records of core_token
-- ----------------------------

-- ----------------------------
-- Table structure for core_user
-- ----------------------------
DROP TABLE IF EXISTS `core_user`;
CREATE TABLE `core_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `login_name` varchar(64) NOT NULL COMMENT '账号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(64) DEFAULT NULL COMMENT '散列',
  `name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `question` varchar(255) DEFAULT NULL COMMENT '问题',
  `answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别(0:未知,1:男,2:女)',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `open_id` varchar(32) DEFAULT NULL COMMENT '微信身份ID',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `register_ip` varchar(16) DEFAULT NULL COMMENT '注册IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(16) DEFAULT NULL COMMENT '最后登录IP',
  `login_count` int(11) DEFAULT NULL COMMENT '登录次数',
  `status` varchar(1) DEFAULT NULL COMMENT '状态(I:未激活,A:正常,E:过期,L:锁定,T:终止)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_name` (`login_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Records of core_user
-- ----------------------------
INSERT INTO `core_user` VALUES ('1', 'admin', 'a94d5cd0079cfc8db030e1107de1addd1903a01b', '', '管理员', null, null, '1', null, '18115841350', '84760025@qq.com', null, null, null, '', '2018-01-05 09:29:20', '0:0:0:0:0:0:0:1', '31', 'A');

-- ----------------------------
-- Table structure for core_user_group
-- ----------------------------
DROP TABLE IF EXISTS `core_user_group`;
CREATE TABLE `core_user_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(64) DEFAULT NULL COMMENT '代码',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型(0:公司,1:部门,2:小组,3:其他)',
  `grade` tinyint(1) DEFAULT NULL COMMENT '层级',
  `director` bigint(20) DEFAULT NULL COMMENT '负责人',
  `path` varchar(512) DEFAULT NULL COMMENT '路径',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父ID',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志(0:正常,1:停用)',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构';

-- ----------------------------
-- Records of core_user_group
-- ----------------------------

-- ----------------------------
-- Table structure for core_user_role
-- ----------------------------
DROP TABLE IF EXISTS `core_user_role`;
CREATE TABLE `core_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户和角色';

-- ----------------------------
-- Records of core_user_role
-- ----------------------------
INSERT INTO `core_user_role` VALUES ('1', '1');
