-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id                   bigint              not null                                comment '部门id',
  parent_id                 bigint              default 0                               comment '父部门id',
  dept_code                 varchar(64)         not null                                comment '部门编码',
  dept_name                 varchar(30)         default ''                              comment '部门名称',
  ancestors                 varchar(500)        default ''                              comment '祖级列表',
  leader                    varchar(20)         default ''                              comment '负责人',
  phone                     varchar(11)         default ''                              comment '联系电话',
  email                     varchar(50)         default ''                              comment '邮箱',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (dept_id)
) engine=innodb comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept (dept_id, tenant_id, dept_code, parent_id, ancestors, dept_name, create_by)
values (99, 2, '100', 0, '0', '雪忆科技', 1),
       (100, 1, '100', 0, '0', '雪忆科技', 1),
       (101, 1, '101', 100, '0,100', '深圳总公司', 1),
       (102, 1, '102',  100, '0,100', '长沙分公司', 1),
       (103, 1, '103',  101, '0,100,101', '研发部门', 1),
       (104, 1, '104',  101, '0,100,101', '市场部门', 1),
       (105, 1, '105',  101, '0,100,101', '测试部门', 1),
       (106, 1, '106',  101, '0,100,101', '财务部门', 1),
       (107, 1, '107',  101, '0,100,101', '运维部门', 1),
       (108, 1, '108',  102, '0,100,102', '市场部门', 1),
       (109, 1, '109',  102, '0,100,102', '财务部门', 1);

-- ----------------------------
-- 2、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
  post_id                   bigint              not null                                comment '岗位Id',
  dept_id		            bigint	            not null                                comment '部门Id',
  post_code                 varchar(64)         not null                                comment '岗位编码',
  post_name                 varchar(50)         not null                                comment '岗位名称',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post (post_id, tenant_id, dept_id, post_code, post_name, create_by)
values (1, 1, 100, 'ceo', '董事长', 1),
       (2, 1, 100, 'se', '项目经理', 1),
       (3, 1, 100, 'hr', '人力资源', 1),
       (4, 2, 100, 'hr', '人力资源', 1),
       (5, 1, 100, 'user', '普通员工',1);

-- ----------------------------
-- 3、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id                   bigint	            not null                                comment '用户Id',
  dept_id                   bigint	            not null                                comment '部门Id',
  post_id		            bigint	            not null                                comment '职位Id',
  user_code                 varchar(64)         not null                                comment '用户编码',
  user_name                 varchar(30)         not null                                comment '用户账号',
  nick_name                 varchar(30)         not null                                comment '用户昵称',
  user_type                 varchar(2)          default '01'                            comment '用户类型（00超级管理员 01系统用户）',
  phone                     varchar(11)         default ''                              comment '手机号码',
  email                     varchar(50)         default ''                              comment '用户邮箱',
  sex                       char(1)             default '2'                             comment '用户性别（0男 1女 2保密）',
  avatar                    varchar(100)        default ''                              comment '头像地址',
  password                  varchar(100)        default ''                              comment '密码',
  login_ip                  varchar(128)        default ''                              comment '最后登录IP',
  login_date                datetime                                                    comment '最后登录时间',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (user_id)
) engine=innodb comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user (user_id, tenant_id, dept_id, post_id, user_code, user_name, nick_name,user_type, password, create_by, remark)
values (1, 1, 100, 1, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '系统管理员'),
       (2, 1, 100, 1, '002', 'xy', 'xy', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '管理员'),
       (3, 2, 100, 1, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '系统管理员');

-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id                   bigint	            not null                                comment '角色Id',
  dept_id		            bigint	            default null                            comment '部门Id',
  post_id		            bigint	            default null                            comment '职位Id',
  role_code                 varchar(64)         not null                                comment '角色编码',
  role_name                 varchar(30)         not null                                comment '角色名称',
  role_key                  varchar(100)        not null                                comment '角色权限字符串',
  data_scope                char(1)             default '1'                             comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  menu_check_strictly       tinyint             default 1                               comment '菜单树选择项是否关联显示',
  dept_check_strictly       tinyint             default 1                               comment '部门树选择项是否关联显示',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (role_id)
) engine=innodb comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role (role_id, tenant_id, role_code, role_name, role_key, data_scope, menu_check_strictly, dept_check_strictly, create_by, remark)
values (1, 1, '001', '超级管理员', 'admin', 1, 1, 1, 1, '超级管理员'),
       (2, 1, '002', '管理员', 'common', 2, 2, 1, 1, '普通角色'),
       (3, 2, '001', '超级管理员', 'admin', 1, 1, 1, 1, '超级管理员');

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id                   bigint              not null                                comment '菜单Id',
  parent_id                 bigint              default 0                               comment '父菜单Id',
  menu_name                 varchar(50)         not null                                comment '菜单名称',
  path                      varchar(200)        default ''                              comment '路由地址',
  component                 varchar(255)        default null                            comment '组件路径',
  is_frame                  int(1)              default 1                               comment '是否为外链（0是 1否）',
  is_cache                  int(1)              default 0                               comment '是否缓存（0缓存 1不缓存）',
  menu_type                 char(1)             default ''                              comment '菜单类型（M目录 C菜单 F按钮）',
  visible                   char(1)             default 0                               comment '菜单状态（0显示 1隐藏）',
  perms                     varchar(100)        default null                            comment '权限标识',
  icon                      varchar(100)        default '#'                             comment '菜单图标',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  system_id                 bigint              not null                                comment '系统Id(0主管理系统 otherId子系统)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (menu_id)
) engine=innodb comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values (1, 0, 0, 0, '系统管理', 'system', null, 'M', '', 'system', 1, 1, '系统管理目录'),
       (2, 0, 0, 0, '系统监控', 'monitor', null, 'M', '', 'monitor', 2, 1, '系统监控目录'),
       (3, 0, 0, 0, '系统工具', 'tool', null, 'M', '', 'tool', 3, 1, '系统工具目录');
-- 二级菜单
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values (100, 0, 0, 1, '用户管理', 'user', 'system/user/index', 'C', 'system:user:list', 'user', 1, 1, '用户管理菜单'),
       (101, 0, 0, 1, '角色管理', 'role', 'system/role/index', 'C', 'system:role:list', 'peoples', 1, 1, '角色管理菜单'),
       (102, 0, 0, 1, '菜单管理', 'menu', 'system/menu/index', 'C', 'system:menu:list', 'tree-table', 2, 1, '菜单管理菜单'),
       (103, 0, 0, 1, '部门管理', 'dept', 'system/dept/index', 'C', 'system:dept:list', 'tree', 3, 1, '部门管理菜单'),
       (104, 0, 0, 1, '岗位管理', 'post', 'system/post/index', 'C', 'system:post:list', 'post', 4, 1, '岗位管理菜单'),
       (105, 0, 0, 1, '字典管理', 'dict', 'system/dict/index', 'C', 'system:dict:list', 'dict', 5, 1, '字典管理菜单'),
       (106, 0, 0, 1, '参数设置', 'config', 'system/config/index', 'C', 'system:config:list', 'edit', 6, 1, '参数设置菜单'),
       (107, 0, 0, 1, '通知公告', 'notice', 'system/notice/index', 'C', 'system:notice:list', 'message', 7, 1, '通知公告菜单'),
       (108, 0, 0, 1, '日志管理', 'log', '', 'M', '', 'log', 8, 1, '日志管理菜单'),
       (109, 0, 0, 2, '在线用户', 'online', 'monitor/online/index', 'C', 'monitor:online:list', 'online', 1, 1, '在线用户菜单'),
       (110, 0, 0, 2, '定时任务', 'job', 'monitor/job/index', 'C', 'monitor:job:list', 'job', 2, 1, '定时任务菜单'),
       (111, 0, 0, 2, 'Sentinel控制台', 'http://localhost:8718', '', 'C', 'monitor:sentinel:list', 'sentinel', 3, 1, '流量控制菜单'),
       (112, 0, 0, 2, 'Nacos控制台', 'http://localhost:8848/nacos', '', 'C', 'monitor:nacos:list', 'nacos', 4, 1, '服务治理菜单'),
       (113, 0, 0, 2, 'Admin控制台', 'http://localhost:9100/login', '', 'C', 'monitor:server:list', 'server', 5, 1, '服务监控菜单'),
       (114, 0, 0, 3, '表单构建', 'build', 'tool/build/index', 'C', 'tool:build:list', 'build', 1, 1, '表单构建菜单'),
       (115, 0, 0, 3, '代码生成', 'gen', 'tool/gen/index', 'C', 'tool:gen:list', 'code', 2, 1, '代码生成菜单'),
       (116, 0, 0, 3, '系统接口', 'http://localhost:8080/swagger-ui.html', '', 'C', 'tool:swagger:list', 'swagger', 3, 1, '系统接口菜单');

-- 三级菜单
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values (500, 0, 0, 108, '操作日志', 'operlog', 'system/operlog/index', 'C', 'system:operlog:list', 'form', 1, 1, '操作日志菜单'),
       (501, 0, 0, 108, '登录日志', 'logininfor', 'system/logininfor/index', 'C', 'system:logininfor:list', 'logininfor', 2, 1, '登录日志菜单');
-- 用户管理按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values (1001, 0, 0, 100, '用户查询', '', '', 'F', 'system:user:query', '#', 1, 1, ''),
       (1002, 0, 0, 100, '用户新增', '', '', 'F', 'system:user:add', '#', 2, 1, ''),
       (1003, 0, 0, 100, '用户修改', '', '', 'F', 'system:user:edit', '#', 3, 1, ''),
       (1004, 0, 0, 100, '用户删除', '', '', 'F', 'system:user:remove', '#', 4, 1, ''),
       (1005, 0, 0, 100, '用户导出', '', '', 'F', 'system:user:export', '#', 5, 1, ''),
       (1006, 0, 0, 100, '用户导入', '', '', 'F', 'system:user:import', '#', 6, 1, ''),
       (1007, 0, 0, 100, '重置密码', '', '', 'F', 'system:user:resetPwd', '#', 7, 1, '');
-- 角色管理按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1008, 0, 0, 101, '角色查询', '', '', 'F', 'system:role:query', '#', 1, 1, ''),
                            (1009, 0, 0, 101, '角色新增', '', '', 'F', 'system:role:add', '#', 2, 1, ''),
                            (1010, 0, 0, 101, '角色修改', '', '', 'F', 'system:role:edit', '#', 3, 1, ''),
                            (1011, 0, 0, 101, '角色删除', '', '', 'F', 'system:role:remove', '#', 4, 1, ''),
                            (1012, 0, 0, 101, '角色导出', '', '', 'F', 'system:role:export', '#', 5, 1, '');
-- 菜单管理按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1013, 0, 0, 102, '菜单查询', '', '', 'F', 'system:menu:query', '#', 1, 1, ''),
                            (1014, 0, 0, 102, '菜单新增', '', '', 'F', 'system:menu:add', '#', 2, 1, ''),
                            (1015, 0, 0, 102, '菜单修改', '', '', 'F', 'system:menu:edit', '#', 3, 1, ''),
                            (1016, 0, 0, 102, '菜单删除', '', '', 'F', 'system:menu:remove', '#', 4, 1, '');
-- 部门管理按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1017, 0, 0, 103, '部门查询', '', '', 'F', 'system:dept:query', '#', 1, 1, ''),
                            (1018, 0, 0, 103, '部门新增', '', '', 'F', 'system:dept:add', '#', 2, 1, ''),
                            (1019, 0, 0, 103, '部门修改', '', '', 'F', 'system:dept:edit', '#', 3, 1, ''),
                            (1020, 0, 0, 103, '部门删除', '', '', 'F', 'system:dept:remove', '#', 4, 1, '');
-- 岗位管理按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1021, 0, 0, 104, '岗位查询', '', '', 'F', 'system:post:query', '#', 1, 1, ''),
                            (1022, 0, 0, 104, '岗位新增', '', '', 'F', 'system:post:add', '#', 2, 1, ''),
                            (1023, 0, 0, 104, '岗位修改', '', '', 'F', 'system:post:edit', '#', 3, 1, ''),
                            (1024, 0, 0, 104, '岗位删除', '', '', 'F', 'system:post:remove', '#', 4, 1, ''),
                            (1025, 0, 0, 104, '岗位导出', '', '', 'F', 'system:post:export', '#', 5, 1, '');
-- 字典管理按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1026, 0, 0, 105, '字典查询', '#', '', 'F', 'system:dict:query', '#', 1, 1, ''),
                            (1027, 0, 0, 105, '字典新增', '#', '', 'F', 'system:dict:add', '#', 2, 1, ''),
                            (1028, 0, 0, 105, '字典修改', '#', '', 'F', 'system:dict:edit', '#', 3, 1, ''),
                            (1029, 0, 0, 105, '字典删除', '#', '', 'F', 'system:dict:remove', '#', 4, 1, ''),
                            (1030, 0, 0, 105, '字典导出', '#', '', 'F', 'system:dict:export', '#', 5, 1, '');
-- 参数设置按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1031, 0, 0, 106, '参数查询', '#', '', 'F', 'system:config:query', '#', 1, 1, ''),
                            (1032, 0, 0, 106, '参数新增', '#', '', 'F', 'system:config:add', '#', 2, 1, ''),
                            (1033, 0, 0, 106, '参数修改', '#', '', 'F', 'system:config:edit', '#', 3, 1, ''),
                            (1034, 0, 0, 106, '参数删除', '#', '', 'F', 'system:config:remove', '#', 4, 1, ''),
                            (1035, 0, 0, 106, '参数导出', '#', '', 'F', 'system:config:export', '#', 5, 1, '');
-- 通知公告按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1041, 0, 0, 107, '公告查询', '#', '', 'F', 'system:notice:query', '#', 1, 1, ''),
                            (1042, 0, 0, 107, '公告新增', '#', '', 'F', 'system:notice:add', '#', 2, 1, ''),
                            (1043, 0, 0, 107, '公告修改', '#', '', 'F', 'system:notice:edit', '#', 3, 1, ''),
                            (1044, 0, 0, 107, '公告删除', '#', '', 'F', 'system:notice:remove', '#', 4, 1, '');
-- 操作日志按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1045, 0, 0, 500, '操作查询', '#', '', 'F', 'system:operlog:query', '#', 1, 1, ''),
                            (1046, 0, 0, 500, '操作删除', '#', '', 'F', 'system:operlog:remove', '#', 2, 1, ''),
                            (1047, 0, 0, 500, '日志导出', '#', '', 'F', 'system:operlog:export', '#', 3, 1, '');
-- 登录日志按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1048, 0, 0, 501, '登录查询', '#', '', 'F', 'system:loginInfo:query', '#', 1, 1, ''),
                            (1049, 0, 0, 501, '登录删除', '#', '', 'F', 'system:loginInfo:remove', '#', 2, 1, ''),
                            (1050, 0, 0, 501, '日志导出', '#', '', 'F', 'system:loginInfo:export', '#', 3, 1, '');
-- 在线用户按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1051, 0, 0, 109, '在线查询', '#', '', 'F', 'monitor:online:query', '#', 1, 1, ''),
                            (1052, 0, 0, 109, '批量强退', '#', '', 'F', 'monitor:online:batchLogout', '#', 2, 1, ''),
                            (1053, 0, 0, 109, '单条强退', '#', '', 'F', 'monitor:online:forceLogout', '#', 3, 1, '');
-- 定时任务按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1054, 0, 0, 110, '任务查询', '#', '', 'F', 'monitor:job:query', '#', 1, 1, ''),
                            (1055, 0, 0, 110, '任务新增', '#', '', 'F', 'monitor:job:add', '#', 2, 1, ''),
                            (1056, 0, 0, 110, '任务修改', '#', '', 'F', 'monitor:job:edit', '#', 3, 1, ''),
                            (1057, 0, 0, 110, '任务删除', '#', '', 'F', 'monitor:job:remove', '#', 4, 1, ''),
                            (1058, 0, 0, 110, '状态修改', '#', '', 'F', 'monitor:job:changeStatus', '#', 5, 1, ''),
                            (1059, 0, 0, 110, '任务导出', '#', '', 'F', 'monitor:job:export',  '#', 6, 1, '');
-- 代码生成按钮
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values
                            (1060, 0, 0, 115, '生成查询', '#', '', 'F', 'tool:gen:query', '#', 1, 1, ''),
                            (1061, 0, 0, 115, '生成修改', '#', '', 'F', 'tool:gen:edit', '#', 2, 1, ''),
                            (1062, 0, 0, 115, '生成删除', '#', '', 'F', 'tool:gen:remove', '#', 3, 1, ''),
                            (1063, 0, 0, 115, '导入代码', '#', '', 'F', 'tool:gen:import', '#', 4, 1, ''),
                            (1064, 0, 0, 115, '预览代码', '#', '', 'F', 'tool:gen:preview', '#', 5, 1, ''),
                            (1065, 0, 0, 115, '生成代码', '#', '', 'F', 'tool:gen:code', '#', 6, 1, '');

-- 菜单 SQL
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon, sort, create_by, remark)
values(1100, 0, 0, 1, '模块管理', 'system', 'system/system/index', 'C', 'system:system:list', '#', 1, 1, '模块管理菜单');
#
# -- 按钮 SQL
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, perms, icon,
                      sort, create_by, remark)
values (1101, 0, 0, 1100, '模块管理查询', '#', '', 'F', 'system:system:query', '#', 1, 1, ''),
       (1102, 0, 0, 1100, '模块管理新增', '#', '', 'F', 'system:system:add', '#', 1, 1, ''),
       (1103, 0, 0, 1100, '模块管理修改', '#', '', 'F', 'system:system:edit', '#', 1, 1, ''),
       (1104, 0, 0, 1100, '模块管理删除', '#', '', 'F', 'system:system:remove', '#', 1, 1, '');

-- ----------------------------
-- 6、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id                   bigint(20)          not null auto_increment                 comment '字典主键',
  dict_name                 varchar(100)        default ''                              comment '字典名称',
  dict_type                 varchar(100)        default ''                              comment '字典类型',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into sys_dict_type (dict_id, dict_name, dict_type, create_by, remark)
values (1, '用户性别', 'sys_user_sex', 0, '用户性别列表'),
       (2, '菜单状态', 'sys_show_hide', 0, '菜单状态列表'),
       (3, '系统开关', 'sys_normal_disable', 0, '系统开关列表'),
       (4, '任务状态', 'sys_job_status', 0, '任务状态列表'),
       (5, '任务分组', 'sys_job_group', 0, '任务分组列表'),
       (6, '系统是否', 'sys_yes_no', 0, '系统是否列表'),
       (7, '通知类型', 'sys_notice_type', 0, '通知类型列表'),
       (8, '通知状态', 'sys_notice_status', 0, '通知状态列表'),
       (9, '操作类型', 'sys_oper_type', 0, '操作类型列表'),
       (10, '系统状态', 'sys_common_status', 0, '登录状态列表'),
       (11, '授权类型', 'sys_grant_type', 0, '授权类型列表'),
       (12, '跳转类型', 'sys_jump_type', 0, '跳转类型列表');


-- ----------------------------
-- 7、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code                 bigint(20)          not null auto_increment                 comment '字典编码',
  dict_sort                 int(4)              default 0                               comment '字典排序',
  dict_label                varchar(100)        default ''                              comment '字典标签',
  dict_value                varchar(100)        default ''                              comment '字典键值',
  dict_type                 varchar(100)        default ''                              comment '字典类型',
  css_class                 varchar(100)        default null                            comment '样式属性（其他样式扩展）',
  list_class                varchar(100)        default null                            comment '表格回显样式',
  is_default                char(1)             default 'N'                             comment '是否默认（Y是 N否）',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

insert into sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default,
                           create_by, remark)
values (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', 0, '性别男'),
       (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', 0, '性别女'),
       (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', 0, '性别未知'),
       (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', 0, '显示菜单'),
       (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', 0, '隐藏菜单'),
       (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', 0, '正常状态'),
       (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', 0, '停用状态'),
       (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', 0, '正常状态'),
       (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', 0, '停用状态'),
       (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', 0, '默认分组'),
       (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', 0, '系统分组'),
       (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 0, '系统默认是'),
       (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 0, '系统默认否'),
       (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', 0, '通知'),
       (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', 0, '公告'),
       (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', 0, '正常状态'),
       (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', 0, '关闭状态'),
       (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', 0, '新增操作'),
       (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', 0, '修改操作'),
       (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', 0, '删除操作'),
       (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', 0, '授权操作'),
       (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', 0, '导出操作'),
       (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', 0, '导入操作'),
       (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', 0, '强退操作'),
       (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', 0, '生成操作'),
       (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', 0, '清空操作'),
       (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', 0, '正常状态'),
       (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', 0, '停用状态'),
       (29, 1, '授权码模式', 'authorization_code', 'sys_grant_type', '', '', 'N', 0, '授权码模式'),
       (30, 2, '密码模式', 'password', 'sys_grant_type', '', '', 'N', 0, '密码模式'),
       (31, 3, '客户端模式', 'client_credentials', 'sys_grant_type', '', '', 'N', 0, '客户端模式'),
       (32, 4, '简化模式', 'implicit', 'sys_grant_type', '', '', 'N', 0, '简化模式'),
       (33, 5, '刷新模式', 'refresh_token', 'sys_grant_type', '', '', 'N', 0, '刷新模式'),
       (34, 1, '内部跳转', '0', 'sys_jump_type', '', '', 'N', 0, '路由内部跳转'),
       (35, 2, '外部跳转', '1', 'sys_jump_type', '', '', 'N', 0, '路由外部跳转');