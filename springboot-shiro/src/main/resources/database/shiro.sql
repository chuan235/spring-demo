
-- 角色表
create table `sys_role`(
	`id` int(9) primary key,
	`available` int(1) not null,
	`description` varchar(128) not null,
	`role` varchar(64) not null
);

-- 用户信息表
create table `user_info` (
	`uid` int(9) primary key,
	`username` varchar(32) not null,
	`name` varchar(32) not null,
	`password` varchar(128) not null,
	`salt` varchar(64) not null,
	`state` varchar(1) not null
);

-- 权限表
create table `sys_permission`(
	`id` int(9) primary key,
	`available` int(1) not null,
	`name` varchar(64) not null,
	`parent_id` int(9) not null,
	`parent_ids` varchar(32) not null,
	`permission` varchar(32) not null,
	`resource_type` varchar(64) not null,
	`url`  varchar(128) not null
);

-- 角色权限表
create table `sys_role_permission`(
	`permission_id` int(9) not null,
	`role_id` int(9) not null
);

-- 用户角色表
create table `sys_user_role`(
	`role_id` int(9) not null,
	`uid` int(9) not null
);


INSERT INTO `user_info` (`uid`,`username`,`name`,`password`,`salt`,`state`) VALUES ('1', 'admin', '管理员', 'af3f8a7442df6eda151b5b4dc0cf0d5e', '8d78869f470951332959580424d4bf4f', 1);
INSERT INTO `sys_permission` (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`) VALUES (1,0,'用户管理',0,'0/','userInfo:view','menu','userInfo/userList');
INSERT INTO `sys_permission` (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`) VALUES (2,0,'用户添加',1,'0/1','userInfo:add','button','userInfo/userAdd');
INSERT INTO `sys_permission` (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`) VALUES (3,0,'用户删除',1,'0/1','userInfo:del','button','userInfo/userDel');
INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (1,0,'管理员','admin');
INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (2,0,'VIP会员','vip');
INSERT INTO `sys_role` (`id`,`available`,`description`,`role`) VALUES (3,1,'test','test');
INSERT INTO `sys_role_permission` VALUES ('1', '1');
INSERT INTO `sys_role_permission` (`permission_id`,`role_id`) VALUES (1,1);
INSERT INTO `sys_role_permission` (`permission_id`,`role_id`) VALUES (2,1);
INSERT INTO `sys_role_permission` (`permission_id`,`role_id`) VALUES (3,2);
INSERT INTO `sys_user_role` (`role_id`,`uid`) VALUES (1,1);
