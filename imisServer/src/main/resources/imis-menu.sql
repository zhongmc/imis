
SET FOREIGN_KEY_CHECKS
=0;

-- ----------------------------
-- Table structure for department
-- ----------------------------
/*
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '部门名称',
  `parentId` int(11) DEFAULT NULL,
  `depPath` varchar(255) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `isParent` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;
*/
-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO department
  (id, name, parent_id, full_id)
values(1, '易诚互动', null, '.1');
INSERT INTO department
  (id, name, parent_id, full_id)
values(2, '运营管理部', 1, '.1.2');
INSERT INTO department
  (id, name, parent_id, full_id)
values(21, '财务部', 2, '.1.2.21');
INSERT INTO department
  (id, name, parent_id, full_id)
values(22, '项目管理部', 2, '.1.2.22');
INSERT INTO department
  (id, name, parent_id, full_id)
values(23, '人力资源部', 2, '.1.2.23');
INSERT INTO department
  (id, name, parent_id, full_id)
values(3, '北部大区', 1, '.1.3');
INSERT INTO department
  (id, name, parent_id, full_id)
values(31, '北一部', 3, '.1.3.31');
INSERT INTO department
  (id, name, parent_id, full_id)
values(32, '北二部', 3, '.1.3.32');
INSERT INTO department
  (id, name, parent_id, full_id)
values(33, '北三部', 3, '.1.3.33');
INSERT INTO department
  (id, name, parent_id, full_id)
values(4, '东部大区', 1, '.1.4');
INSERT INTO department
  (id, name, parent_id, full_id)
values(41, '东一部', 4, '.1.4.41');
INSERT INTO department
  (id, name, parent_id, full_id)
values(42, '东二部', 4, '.1.4.42');
INSERT INTO department
  (id, name, parent_id, full_id)
values(43, '东三部', 4, '.1.4.43');
INSERT INTO department
  (id, name, parent_id, full_id)
values(5, '南部大区', 1, '.1.5');
INSERT INTO department
  (id, name, parent_id, full_id)
values(51, '南一部', 5, '.1.5.51');
INSERT INTO department
  (id, name, parent_id, full_id)
values(52, '南二部', 5, '.1.5.52');
INSERT INTO department
  (id, name, parent_id, full_id)
values(53, '南三部', 5, '.1.5.53' );




insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (2, null, '项目管理', '/', '/home', 'pages/Home.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (21, 2, '项目基础信息', '/project/basic/**', '/prj/basic', 'components/project/PrjBasic.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (22, 2, '项目预算', '/project/budget/**', '/prj/budget/:id', 'components/project/PrjBudget.vue', 1);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (23, 2, '项目机会', 'project/chance/**', '/prj/chance', 'components/project/PrjCHance.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (3, null, '客户管理', '/', '/home', 'pages/Home.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (31, 3, '客户基础信息', '/custom/basic/**', '/custom/basic', 'components/custom/CustomMana.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (32, 3, '客户干系人', '/custom/contactor/**', '/custom/contactor', 'components/custom/CustomContactor.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (33, 3, '客户跟踪', '/custom/trace/**', '/custom/trace', 'components/custom/CustomTrace.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (4, null, '部门预算', '/', '/home', 'pages/Home.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (41, 4, '部门预算', '/budget/basic/**', '/budget/basic', 'components/budget/BudgetBasic.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (42, 4, '部门预算汇总', '/budget/collect/**', '/budget/collect', 'components/budget/BudgetCollect.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (5, null, '系统设置', '/', '/home', 'pages/Home.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (51, 5, '基础信息设置', '/system/basic/**', '/sys/basic', 'components/system/SysBasic.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (52, 5, '预算设置', '/system/budget/**', '/sys/budget', 'components/system/SysBudget.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (53, 5, '操作员管理', '/system/user/**', '/sys/user', 'components/system/SysUser.vue', 0);
insert into menu
  (id, parent_id, name, url, path, component, hidden)
values
  (54, 5, '项目数据导入', '/system/project/**', '/sys/prjimport', 'components/system/SysPrjImport.vue', 0);

insert into ynb_user
  (id, dep_id, user_name, nick_name, password, first_password, is_password_change, supervisor, status)
values(1, 1, 'admin', '系统管理员', '$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm', '$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm', 1, 1, 'ACTIVE');
insert into role
  (id, name, name_zh)
values(1, 'ROLE_admin', '系统管理员');

insert into role_menu
  (id, menu_id, role_id)
values(1, 5, 1);
insert into role_menu
  (id, menu_id, role_id)
values(2, 51, 1);
insert into role_menu
  (id, menu_id, role_id)
values(3, 52, 1);
insert into role_menu
  (id, menu_id, role_id)
values(4, 53, 1);

insert into user_role
  (id, user_id, role_id)
values(1, 1, 1);


-- ----------------------------
-- Records of menu
-- ----------------------------

-- ----------------------------
-- Table structure for menu_role
-- ----------------------------
DROP TABLE IF EXISTS `menu_role`;
CREATE TABLE `menu_role`
(
  `id` int
(11) NOT NULL AUTO_INCREMENT,
  `mid` int
(11) DEFAULT NULL,
  `rid` int
(11) DEFAULT NULL,
  PRIMARY KEY
(`id`),
  KEY `mid`
(`mid`),
  KEY `rid`
(`rid`),
  CONSTRAINT `menu_role_ibfk_1` FOREIGN KEY
(`mid`) REFERENCES `menu`
(`id`),
  CONSTRAINT `menu_role_ibfk_2` FOREIGN KEY
(`rid`) REFERENCES `role`
(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_role
-- ----------------------------
INSERT INTO `
menu_role`
VALUES
  ('161', '7', '3');
INSERT INTO `
menu_role`
VALUES
  ('162', '7', '6');
INSERT INTO `
menu_role`
VALUES
  ('163', '9', '6');
INSERT INTO `
menu_role`
VALUES
  ('164', '10', '6');
INSERT INTO `
menu_role`
VALUES
  ('165', '11', '6');
INSERT INTO `
menu_role`
VALUES
  ('166', '12', '6');
INSERT INTO `
menu_role`
VALUES
  ('167', '13', '6');
INSERT INTO `
menu_role`
VALUES
  ('168', '14', '6');
INSERT INTO `
menu_role`
VALUES
  ('169', '15', '6');
INSERT INTO `
menu_role`
VALUES
  ('170', '16', '6');
INSERT INTO `
menu_role`
VALUES
  ('171', '17', '6');
INSERT INTO `
menu_role`
VALUES
  ('172', '18', '6');
INSERT INTO `
menu_role`
VALUES
  ('173', '19', '6');
INSERT INTO `
menu_role`
VALUES
  ('174', '20', '6');
INSERT INTO `
menu_role`
VALUES
  ('175', '21', '6');
INSERT INTO `
menu_role`
VALUES
  ('176', '22', '6');
INSERT INTO `
menu_role`
VALUES
  ('177', '23', '6');
INSERT INTO `
menu_role`
VALUES
  ('178', '25', '6');
INSERT INTO `
menu_role`
VALUES
  ('179', '26', '6');
INSERT INTO `
menu_role`
VALUES
  ('180', '27', '6');
INSERT INTO `
menu_role`
VALUES
  ('181', '28', '6');
INSERT INTO `
menu_role`
VALUES
  ('182', '24', '6');
INSERT INTO `
menu_role`
VALUES
  ('247', '7', '4');
INSERT INTO `
menu_role`
VALUES
  ('248', '8', '4');
INSERT INTO `
menu_role`
VALUES
  ('249', '11', '4');
INSERT INTO `
menu_role`
VALUES
  ('250', '7', '2');
INSERT INTO `
menu_role`
VALUES
  ('251', '8', '2');
INSERT INTO `
menu_role`
VALUES
  ('252', '9', '2');
INSERT INTO `
menu_role`
VALUES
  ('253', '10', '2');
INSERT INTO `
menu_role`
VALUES
  ('254', '12', '2');
INSERT INTO `
menu_role`
VALUES
  ('255', '13', '2');
INSERT INTO `
menu_role`
VALUES
  ('256', '7', '1');
INSERT INTO `
menu_role`
VALUES
  ('257', '8', '1');
INSERT INTO `
menu_role`
VALUES
  ('258', '9', '1');
INSERT INTO `
menu_role`
VALUES
  ('259', '10', '1');
INSERT INTO `
menu_role`
VALUES
  ('260', '11', '1');
INSERT INTO `
menu_role`
VALUES
  ('261', '12', '1');
INSERT INTO `
menu_role`
VALUES
  ('262', '13', '1');
INSERT INTO `
menu_role`
VALUES
  ('263', '14', '1');
INSERT INTO `
menu_role`
VALUES
  ('264', '15', '1');
INSERT INTO `
menu_role`
VALUES
  ('265', '16', '1');
INSERT INTO `
menu_role`
VALUES
  ('266', '17', '1');
INSERT INTO `
menu_role`
VALUES
  ('267', '18', '1');
INSERT INTO `
menu_role`
VALUES
  ('268', '19', '1');
INSERT INTO `
menu_role`
VALUES
  ('269', '20', '1');
INSERT INTO `
menu_role`
VALUES
  ('270', '21', '1');
INSERT INTO `
menu_role`
VALUES
  ('271', '22', '1');
INSERT INTO `
menu_role`
VALUES
  ('272', '23', '1');
INSERT INTO `
menu_role`
VALUES
  ('273', '24', '1');
INSERT INTO `
menu_role`
VALUES
  ('274', '25', '1');
INSERT INTO `
menu_role`
VALUES
  ('275', '26', '1');
INSERT INTO `
menu_role`
VALUES
  ('276', '27', '1');
INSERT INTO `
menu_role`
VALUES
  ('277', '28', '1');

-- ----------------------------
-- Table structure for msgcontent
-- ----------------------------
DROP TABLE IF EXISTS `msgcontent`;
CREATE TABLE `msgcontent`
(
  `id` int
(11) NOT NULL AUTO_INCREMENT,
  `title` varchar
(64) DEFAULT NULL,
  `message` varchar
(255) DEFAULT NULL,
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY
(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of msgcontent
-- ----------------------------
INSERT INTO `
msgcontent`
VALUES
  ('14', '2222222222', '11111111111111111', '2018-02-02 20:46:19');
INSERT INTO `
msgcontent`
VALUES
  ('15', '22222222', '3333333333333333333333', '2018-02-02 21:45:57');
INSERT INTO `
msgcontent`
VALUES
  ('16', '通知标题1', '通知内容1', '2018-02-03 11:41:39');
INSERT INTO `
msgcontent`
VALUES
  ('17', '通知标题2', '通知内容2', '2018-02-03 11:52:37');
INSERT INTO `
msgcontent`
VALUES
  ('18', '通知标题3', '通知内容3', '2018-02-03 12:19:41');


-- ----------------------------
-- Table structure for oplog
-- ----------------------------
DROP TABLE IF EXISTS `oplog`;
CREATE TABLE `oplog`
(
  `id` int
(11) NOT NULL AUTO_INCREMENT,
  `addDate` date DEFAULT NULL COMMENT '添加日期',
  `operate` varchar
(255) DEFAULT NULL COMMENT '操作内容',
  `hrid` int
(11) DEFAULT NULL COMMENT '操作员ID',
  PRIMARY KEY
(`id`),
  KEY `hrid`
(`hrid`),
  CONSTRAINT `oplog_ibfk_1` FOREIGN KEY
(`hrid`) REFERENCES `hr`
(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oplog
-- ----------------------------

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position`
(
  `id` int
(11) NOT NULL AUTO_INCREMENT,
  `name` varchar
(32) DEFAULT NULL COMMENT '职位',
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `enabled` tinyint
(1) DEFAULT '1',
  PRIMARY KEY
(`id`),
  UNIQUE KEY `name`
(`name`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of position
-- ----------------------------
INSERT INTO `
position`
VALUES
  ('29', '技术总监', '2018-01-11 21:13:42', '1');
INSERT INTO `
position`
VALUES
  ('30', '运营总监', '2018-01-11 21:13:48', '1');
INSERT INTO `
position`
VALUES
  ('31', '市场总监', '2018-01-11 21:13:56', '1');
INSERT INTO `
position`
VALUES
  ('32', '总经理', '2018-01-11 21:35:07', '1');
INSERT INTO `
position`
VALUES
  ('33', '研发工程师', '2018-01-14 16:07:11', '1');
INSERT INTO `
position`
VALUES
  ('34', '运维工程师', '2018-01-14 16:11:41', '1');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
  `id` int
(11) NOT NULL AUTO_INCREMENT,
  `name` varchar
(64) DEFAULT NULL,
  `nameZh` varchar
(64) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY
(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for salary
-- ----------------------------
DROP TABLE IF EXISTS `salary`;
CREATE TABLE `salary`
(
  `id` int
(11) NOT NULL AUTO_INCREMENT,
  `basicSalary` int
(11) DEFAULT NULL COMMENT '基本工资',
  `bonus` int
(11) DEFAULT NULL COMMENT '奖金',
  `lunchSalary` int
(11) DEFAULT NULL COMMENT '午餐补助',
  `trafficSalary` int
(11) DEFAULT NULL COMMENT '交通补助',
  `allSalary` int
(11) DEFAULT NULL COMMENT '应发工资',
  `pensionBase` int
(11) DEFAULT NULL COMMENT '养老金基数',
  `pensionPer` float DEFAULT NULL COMMENT '养老金比率',
  `createDate` timestamp NULL DEFAULT NULL COMMENT '启用时间',
  `medicalBase` int
(11) DEFAULT NULL COMMENT '医疗基数',
  `medicalPer` float DEFAULT NULL COMMENT '医疗保险比率',
  `accumulationFundBase` int
(11) DEFAULT NULL COMMENT '公积金基数',
  `accumulationFundPer` float DEFAULT NULL COMMENT '公积金比率',
  `name` varchar
(32) DEFAULT NULL,
  PRIMARY KEY
(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of salary
-- ----------------------------
INSERT INTO `
salary`
VALUES
  ('9', '9000', '4000', '800', '500', null, '2000', '0.07', '2018-01-24 00:00:00', '2000', '0.07', '2000', '0.07', '市场部工资账套');
INSERT INTO `
salary`
VALUES
  ('10', '2000', '2000', '400', '1000', null, '2000', '0.07', '2018-01-01 00:00:00', '2000', '0.07', '2000', '0.07', '人事部工资账套');
INSERT INTO `
salary`
VALUES
  ('13', '10000', '3000', '500', '500', null, '4000', '0.07', '2018-01-25 00:00:00', '4000', '0.07', '4000', '0.07', '运维部工资账套');

-- ----------------------------
-- Table structure for sysmsg
-- ----------------------------
DROP TABLE IF EXISTS `sysmsg`;
CREATE TABLE `sysmsg`
(
  `id` int
(11) NOT NULL AUTO_INCREMENT,
  `mid` int
(11) DEFAULT NULL COMMENT '消息id',
  `type` int
(11) DEFAULT '0' COMMENT '0表示群发消息',
  `hrid` int
(11) DEFAULT NULL COMMENT '这条消息是给谁的',
  `state` int
(11) DEFAULT '0' COMMENT '0 未读 1 已读',
  PRIMARY KEY
(`id`),
  KEY `hrid`
(`hrid`),
  KEY `sysmsg_ibfk_1`
(`mid`),
  CONSTRAINT `sysmsg_ibfk_1` FOREIGN KEY
(`mid`) REFERENCES `msgcontent`
(`id`),
  CONSTRAINT `sysmsg_ibfk_2` FOREIGN KEY
(`hrid`) REFERENCES `hr`
(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sysmsg
-- ----------------------------
INSERT INTO `
sysmsg`
VALUES
  ('57', '14', '0', '3', '1');
INSERT INTO `
sysmsg`
VALUES
  ('58', '14', '0', '5', '1');
INSERT INTO `
sysmsg`
VALUES
  ('59', '14', '0', '10', '1');
INSERT INTO `
sysmsg`
VALUES
  ('60', '14', '0', '11', '0');
INSERT INTO `
sysmsg`
VALUES
  ('61', '14', '0', '12', '0');
INSERT INTO `
sysmsg`
VALUES
  ('62', '15', '0', '3', '1');
INSERT INTO `
sysmsg`
VALUES
  ('63', '15', '0', '5', '1');
INSERT INTO `
sysmsg`
VALUES
  ('64', '15', '0', '10', '1');
INSERT INTO `
sysmsg`
VALUES
  ('65', '15', '0', '11', '0');
INSERT INTO `
sysmsg`
VALUES
  ('66', '15', '0', '12', '0');
INSERT INTO `
sysmsg`
VALUES
  ('67', '16', '0', '3', '1');
INSERT INTO `
sysmsg`
VALUES
  ('68', '16', '0', '5', '1');
INSERT INTO `
sysmsg`
VALUES
  ('69', '16', '0', '10', '1');
INSERT INTO `
sysmsg`
VALUES
  ('70', '16', '0', '11', '0');
INSERT INTO `
sysmsg`
VALUES
  ('71', '16', '0', '12', '0');
INSERT INTO `
sysmsg`
VALUES
  ('72', '17', '0', '3', '1');
INSERT INTO `
sysmsg`
VALUES
  ('73', '17', '0', '5', '1');
INSERT INTO `
sysmsg`
VALUES
  ('74', '17', '0', '10', '1');
INSERT INTO `
sysmsg`
VALUES
  ('75', '17', '0', '11', '0');
INSERT INTO `
sysmsg`
VALUES
  ('76', '17', '0', '12', '0');
INSERT INTO `
sysmsg`
VALUES
  ('77', '18', '0', '3', '1');
INSERT INTO `
sysmsg`
VALUES
  ('78', '18', '0', '5', '0');
INSERT INTO `
sysmsg`
VALUES
  ('79', '18', '0', '10', '0');
INSERT INTO `
sysmsg`
VALUES
  ('80', '18', '0', '11', '0');
INSERT INTO `
sysmsg`
VALUES
  ('81', '18', '0', '12', '0');

-- ----------------------------
-- Procedure structure for addDep
-- ----------------------------
DROP PROCEDURE IF EXISTS `addDep`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addDep`
(in depName varchar
(32),in parentId int,in enabled boolean,out result int,out result2 int)
begin
  declare did int;
declare pDepPath varchar
(64);
insert into department
set name
=depName,parentId=parentId,enabled=enabled;
select row_count()
into result;
select last_insert_id()
into did;
set result2
=did;
select depPath
into pDepPath
from department
where id=parentId;
update department set depPath=concat(pDepPath,'.',did) where id=did;
update department set isParent=true where id=parentId;
end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for deleteDep
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteDep`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteDep`
(in did int,out result int)
begin
  declare ecount int;
declare pid int;
declare pcount int;
select count(*)
into ecount
from employee
where departmentId=did;
if ecount>0 then
set result
=-1;
  else
select parentId
into pid
from department
where id=did;
delete from department where id=did and isParent=false;
select row_count()
into result;
select count(*)
into pcount
from department
where parentId=pid;
if pcount=0 then
update department set isParent=false where id=pid;
end
if;
  end
if;
end
;;
DELIMITER ;
SET FOREIGN_KEY_CHECKS
=1;
