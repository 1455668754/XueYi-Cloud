-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id                   bigint              not null                                comment '部门id',
  parent_id                 bigint              default 0                               comment '父部门id',
  dept_code                 varchar(64)         default null                            comment '部门编码',
  dept_name                 varchar(30)         default ''                              comment '部门名称',
  ancestors                 varchar(10000)      default ''                              comment '祖级列表',
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
  post_code                 varchar(64)         default null                            comment '岗位编码',
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
  user_code                 varchar(64)         default null                            comment '用户编码',
  user_name                 varchar(30)         not null                                comment '用户账号',
  nick_name                 varchar(30)         not null                                comment '用户昵称',
  user_type                 varchar(2)          default '01'                            comment '用户类型（00超级管理员 01系统用户）',
  phone                     varchar(11)         default ''                              comment '手机号码',
  email                     varchar(50)         default ''                              comment '用户邮箱',
  sex                       char(1)             default '2'                             comment '用户性别（0男 1女 2保密）',
  avatar                    varchar(100)        default ''                              comment '头像地址',
  profile                   varchar(100)        default '这个人很懒，暂未留下什么'          comment '个人简介',
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
values (1, -1, 0, 0, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '系统管理员'),
       (2, 1, 100, 1, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '系统管理员'),
       (3, 1, 100, 1, '002', 'xy', 'xy', '01', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '管理员'),
       (4, 2, 100, 1, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '系统管理员');

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
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, visible, perms, icon, sort, create_by, remark)
values
       -- 一级菜单
       (10000, 0, 0, 0, '组织管理', 'organize',   null, 'M', '0', '', 'xy_organization', 1, 0, '组织管理目录'),
              -- 二级菜单
              (11000, 0, 0, 10000, '部门管理', 'dept',   'system/organize/dept/index',      'C', '0',  'system:dept:list',      'xy_dept',   1, 0, '部门管理菜单'),
                                   -- 部门管理按钮
                                   (11001, 0, 0, 11000, '部门查询', '', '', 'F', '0', 'system:dept:query',           '#', 1, 0, ''),
                                   (11002, 0, 0, 11000, '部门新增', '', '', 'F', '0', 'system:dept:add',             '#', 2, 0, ''),
                                   (11003, 0, 0, 11000, '部门修改', '', '', 'F', '0', 'system:dept:edit',            '#', 3, 0, ''),
                                   (11004, 0, 0, 11000, '部门删除', '', '', 'F', '0', 'system:dept:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (12000, 0, 0, 10000, '岗位管理', 'post',   'system/organize/post/index',      'C', '0',  'system:post:list',      'xy_post',   2, 0, '岗位管理菜单'),
                                   -- 岗位管理按钮
                                   (12001, 0, 0, 12000, '岗位查询', '', '', 'F', '0', 'system:post:query',           '#', 1, 0, ''),
                                   (12002, 0, 0, 12000, '岗位新增', '', '', 'F', '0', 'system:post:add',             '#', 2, 0, ''),
                                   (12003, 0, 0, 12000, '岗位修改', '', '', 'F', '0', 'system:post:edit',            '#', 3, 0, ''),
                                   (12004, 0, 0, 12000, '岗位删除', '', '', 'F', '0', 'system:post:remove',          '#', 4, 0, ''),
                                   (12005, 0, 0, 12000, '岗位导出', '', '', 'F', '0', 'system:post:export',          '#', 5, 0, ''),
              -- 二级菜单
              (13000, 0, 0, 10000, '用户管理', 'user',   'system/organize/user/index',      'C', '0',  'system:user:list',      'xy_user',   3, 0, '用户管理菜单'),
                                   -- 用户管理按钮
                                   (13001, 0, 0, 13000, '用户查询', '', '', 'F', '0', 'system:user:query',           '#', 1, 0, ''),
                                   (13002, 0, 0, 13000, '用户新增', '', '', 'F', '0', 'system:user:add',             '#', 2, 0, ''),
                                   (13003, 0, 0, 13000, '用户修改', '', '', 'F', '0', 'system:user:edit',            '#', 3, 0, ''),
                                   (13004, 0, 0, 13000, '用户删除', '', '', 'F', '0', 'system:user:remove',          '#', 4, 0, ''),
                                   (13005, 0, 0, 13000, '用户导出', '', '', 'F', '0', 'system:user:export',          '#', 5, 0, ''),
                                   (13006, 0, 0, 13000, '用户导入', '', '', 'F', '0', 'system:user:import',          '#', 6, 0, ''),
                                   (13007, 0, 0, 13000, '重置密码', '', '', 'F', '0', 'system:user:resetPwd',        '#', 7, 0, ''),
       -- 一级菜单
       (20000, 0, 0, 0, '企业账户', 'enterprise', null, 'M', '0', '', 'xy_enterprise',   2, 0, '企业账户目录'),
              (21000, 0, 0, 20000, '资料管理', 'dict',   'system/dataSetting/enterprise/profile/index',        'C', '0',  'system:enterprise:list',      'xy_dict',   1, 0, '资料管理菜单'),
                                   -- 字典管理按钮
                                   (21001, 0, 0, 21000, '资料查看权限', '#', '', 'F', '0', 'system:enterprise:list',      '#', 1, 0, ''),
                                   (21002, 0, 0, 21000, '普通操作权限', '#', '', 'F', '0', 'system:enterprise:edit',      '#', 2, 0, ''),
                                   (21003, 0, 0, 21000, '超管操作权限', '#', '', 'F', '0', 'system:enterpriseAdmin:edit', '#', 3, 0, ''),
       -- 一级菜单
       (30000, 0, 0, 0, '系统管理', 'system',     null, 'M', '0', '', 'xy_setting',      3, 0, '系统管理目录'),
              (31000, 0, 0, 30000, '字典管理', 'dict',   'system/system/dict/index',        'C', '0',  'system:dict:list',      'xy_dict',   1, 0, '字典管理菜单'),
                                   -- 字典管理按钮
                                   (31001, 0, 0, 31000, '字典查询', '#', '', 'F', '0', 'system:dict:query',          '#', 1, 0, ''),
                                   (31002, 0, 0, 31000, '字典新增', '#', '', 'F', '0', 'system:dict:add',            '#', 2, 0, ''),
                                   (31003, 0, 0, 31000, '字典修改', '#', '', 'F', '0', 'system:dict:edit',           '#', 3, 0, ''),
                                   (31004, 0, 0, 31000, '字典删除', '#', '', 'F', '0', 'system:dict:remove',         '#', 4, 0, ''),
                                   (31005, 0, 0, 31000, '字典导出', '#', '', 'F', '0', 'system:dict:export',         '#', 5, 0, ''),
              -- 二级菜单
              (32000, 0, 0, 30000, '参数管理', 'config', 'system/system/config/index',      'C', '0',  'system:config:list',    'xy_config', 2, 0, '参数管理菜单'),
                                   -- 参数设置按钮
                                   (32001, 0, 0, 32000, '参数查询', '#', '', 'F', '0', 'system:config:query',        '#', 1, 0, ''),
                                   (32002, 0, 0, 32000, '参数新增', '#', '', 'F', '0', 'system:config:add',          '#', 2, 0, ''),
                                   (32003, 0, 0, 32000, '参数修改', '#', '', 'F', '0', 'system:config:edit',         '#', 3, 0, ''),
                                   (32004, 0, 0, 32000, '参数删除', '#', '', 'F', '0', 'system:config:remove',       '#', 4, 0, ''),
                                   (32005, 0, 0, 32000, '参数导出', '#', '', 'F', '0', 'system:config:export',       '#', 5, 0, ''),
              -- 二级菜单
              (33000, 0, 0, 30000, '通知公告', 'notice', 'system/system/notice/index',      'C', '0',  'system:notice:list',    'xy_message', 3, 0, '通知公告菜单'),
                                   -- 通知公告按钮
                                   (33001, 0, 0, 33000, '公告查询', '#', '', 'F', '0', 'system:notice:query',        '#', 1, 0, ''),
                                   (33002, 0, 0, 33000, '公告新增', '#', '', 'F', '0', 'system:notice:add',          '#', 2, 0, ''),
                                   (33003, 0, 0, 33000, '公告修改', '#', '', 'F', '0', 'system:notice:edit',         '#', 3, 0, ''),
                                   (33004, 0, 0, 33000, '公告删除', '#', '', 'F', '0', 'system:notice:remove',       '#', 4, 0, ''),
              -- 二级菜单
              (34000, 0, 0, 30000, '日志管理', 'log',    '',                                'M', '0',  '',                      'xy_log',    4, 0, '日志管理菜单'),
                     -- 三级菜单
                     (34100, 0, 0, 34000, '操作日志', 'operlog',   'system/system/journal/operlog/index',    'C', '0', 'system:operlog:list',   'xy_log_operation',  1, 0, '操作日志菜单'),
                                   -- 操作日志按钮
                                   (34101, 0, 0, 34100, '操作查询', '#', '', 'F', '0', 'system:operlog:query',       '#', 1, 0, ''),
                                   (34102, 0, 0, 34100, '操作删除', '#', '', 'F', '0', 'system:operlog:remove',      '#', 2, 0, ''),
                                   (34103, 0, 0, 34100, '日志导出', '#', '', 'F', '0', 'system:operlog:export',      '#', 3, 0, ''),
                     -- 三级菜单
                     (34200, 0, 0, 34000, '登录日志', 'loginInfo', 'system/system/journal/loginInfo/index',  'C', '0', 'system:loginInfo:list', 'xy_log_loginInfo',  2, 0, '登录日志菜单'),
                                   -- 登录日志按钮
                                   (34201, 0, 0, 34200, '登录查询', '#', '', 'F', '0', 'system:loginInfo:query',     '#', 1, 0, ''),
                                   (34202, 0, 0, 34200, '登录删除', '#', '', 'F', '0', 'system:loginInfo:remove',    '#', 2, 0, ''),
                                   (34203, 0, 0, 34200, '日志导出', '#', '', 'F', '0', 'system:loginInfo:export',    '#', 3, 0, ''),
       -- 一级菜单
       (40000, 0, 0, 0, '权限管理', 'authority',  null, 'M', '0', '', 'xy_authority',    3, 0, '权限管理目录'),
              -- 二级菜单
              (41000, 0, 0, 40000, '角色管理', 'role',   'system/authority/role/index',     'C', '0', 'system:role:list',      'xy_role',    1, 0, '角色管理菜单'),
                                   -- 角色管理按钮
                                   (41001, 0, 0, 41000, '角色查询', '', '', 'F', '0', 'system:role:query',           '#', 1, 0, ''),
                                   (41002, 0, 0, 41000, '角色授权', '', '', 'F', '0', 'system:role:set',             '#', 2, 0, ''),
                                   (41003, 0, 0, 41000, '角色新增', '', '', 'F', '0', 'system:role:add',             '#', 3, 0, ''),
                                   (41004, 0, 0, 41000, '角色修改', '', '', 'F', '0', 'system:role:edit',            '#', 4, 0, ''),
                                   (41005, 0, 0, 41000, '角色删除', '', '', 'F', '0', 'system:role:remove',          '#', 5, 0, ''),
                                   (41006, 0, 0, 41000, '角色导出', '', '', 'F', '0', 'system:role:export',          '#', 6, 0, ''),
              -- 二级菜单
              (42000, 0, 0, 40000, '菜单管理', 'menu',   'system/authority/menu/index',     'C', '0', 'system:menu:list',      'xy_menu',    2, 0, '菜单管理菜单'),
                                   -- 菜单管理按钮
                                   (42001, 0, 0, 42000, '菜单查询', '', '', 'F', '0', 'system:menu:query',           '#', 1, 0, ''),
                                   (42002, 0, 0, 42000, '菜单新增', '', '', 'F', '0', 'system:menu:add',             '#', 2, 0, ''),
                                   (42003, 0, 0, 42000, '菜单修改', '', '', 'F', '0', 'system:menu:edit',            '#', 3, 0, ''),
                                   (42004, 0, 0, 42000, '菜单删除', '', '', 'F', '0', 'system:menu:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (43000, 0, 0, 40000, '模块管理', 'system', 'system/authority/system/index',   'C', '0', 'system:system:list',    'xy_system',  3, 0, '模块管理菜单'),
                                   -- 模块管理按钮
                                   (43001, 0, 0, 43000, '模块管理查询', '#', '', 'F', '0', 'system:system:query',    '#', 1, 0, ''),
                                   (43002, 0, 0, 43000, '模块管理新增', '#', '', 'F', '0', 'system:system:add',      '#', 1, 0, ''),
                                   (43003, 0, 0, 43000, '模块管理修改', '#', '', 'F', '0', 'system:system:edit',     '#', 1, 0, ''),
                                   (43004, 0, 0, 43000, '模块管理删除', '#', '', 'F', '0', 'system:system:remove',   '#', 1, 0, ''),
       -- 一级菜单
       (50000, 0, 0, 0, '系统监控', 'monitor',    null, 'M', '0', '', 'xy_monitor',         4, 0, '系统监控目录'),
              -- 二级菜单
              (51000, 0, 0, 50000, '在线用户', 'online', 'system/monitor/online/index',     'C', '0', 'monitor:online:list',   'xy_online',  1, 0, '在线用户菜单'),
                                   -- 在线用户按钮
                                   (51001, 0, 0, 51000, '在线查询', '#', '', 'F', '0', 'monitor:online:query',       '#', 1, 0, ''),
                                   (51002, 0, 0, 51000, '批量强退', '#', '', 'F', '0', 'monitor:online:batchLogout', '#', 2, 0, ''),
                                   (51003, 0, 0, 51000, '单条强退', '#', '', 'F', '0', 'monitor:online:forceLogout', '#', 3, 0, ''),

              -- 二级菜单
              (52000, 0, 0, 50000, '定时任务', 'job',    'system/monitor/job/index',        'C', '0', 'monitor:job:list',      'xy_job',     2, 0, '定时任务菜单'),
                                   -- 定时任务按钮
                                   (52001, 0, 0, 52000, '任务查询', '#', '', 'F', '0', 'monitor:job:query',          '#', 1, 0, ''),
                                   (52002, 0, 0, 52000, '任务新增', '#', '', 'F', '0', 'monitor:job:add',            '#', 2, 0, ''),
                                   (52003, 0, 0, 52000, '任务修改', '#', '', 'F', '0', 'monitor:job:edit',           '#', 3, 0, ''),
                                   (52004, 0, 0, 52000, '任务删除', '#', '', 'F', '0', 'monitor:job:remove',         '#', 4, 0, ''),
                                   (52005, 0, 0, 52000, '状态修改', '#', '', 'F', '0', 'monitor:job:changeStatus',   '#', 5, 0, ''),
                                   (52006, 0, 0, 52000, '任务导出', '#', '', 'F', '0', 'monitor:job:export',         '#', 6, 0, ''),
              -- 二级菜单
              (53000, 0, 0, 50000, 'Sentinel控制台', 'http://localhost:8718',      '',      'C', '0', 'monitor:sentinel:list', 'xy_sentinel', 3, 0, '流量控制菜单'),
              -- 二级菜单
              (54000, 0, 0, 50000, 'Nacos控制台',    'http://localhost:8848/nacos', '',     'C', '0', 'monitor:nacos:list',    'xy_nacos',  4, 0, '服务治理菜单'),
              -- 二级菜单
              (55000, 0, 0, 50000, 'Admin控制台',    'http://localhost:9100/login', '',     'C', '0', 'monitor:server:list',   'xy_server', 5, 0, '服务监控菜单'),
       -- 一级菜单
       (60000, 0, 0, 0, '系统工具', 'tool',       null, 'M', '0', '', 'xy_tool',     5, 0, '系统工具目录'),
              -- 二级菜单
              (61000, 0, 0, 60000, '表单构建', 'build',  'tool/build/index',                'C', '0', 'tool:build:list',       'xy_build',   1, 0, '表单构建菜单'),
              -- 二级菜单
              (62000, 0, 0, 60000, '代码生成', 'gen',    'tool/gen/index',                  'C', '0', 'tool:gen:list',         'xy_code',    2, 0, '代码生成菜单'),
                                   -- 代码生成按钮
                                   (62001, 0, 0, 62000, '生成查询', '#', '', 'F', '0', 'tool:gen:query',             '#', 1, 0, ''),
                                   (62002, 0, 0, 62000, '生成修改', '#', '', 'F', '0', 'tool:gen:edit',              '#', 2, 0, ''),
                                   (62003, 0, 0, 62000, '生成删除', '#', '', 'F', '0', 'tool:gen:remove',            '#', 3, 0, ''),
                                   (62004, 0, 0, 62000, '导入代码', '#', '', 'F', '0', 'tool:gen:import',            '#', 4, 0, ''),
                                   (62005, 0, 0, 62000, '预览代码', '#', '', 'F', '0', 'tool:gen:preview',           '#', 5, 0, ''),
                                   (62006, 0, 0, 62000, '生成代码', '#', '', 'F', '0', 'tool:gen:code',              '#', 6, 0, ''),
              -- 二级菜单
              (63000, 0, 0, 60000, '系统接口', 'http://localhost:8080/swagger-ui.html', '', 'C', '0', 'tool:swagger:list',     'xy_swagger', 3, 0, '系统接口菜单'),
       -- 一级菜单
       (70000, 0, -1, 0, '租户管理', 'tool',       null, 'M', '0', '', 'xy_tool',     6, 0, '租户管理目录'),
              -- 二级菜单
              (71000, 0, -1, 70000, '租户管理', 'tenant',    'system/tenant/tenant/index',  'C', '0', 'system:tenant:list',    'xy_code',    2, 0, '租户管理菜单'),
                                   -- 代码生成按钮
                                   (71001, 0, -1, 71000, '租户查询', '#', '', 'F', '0', 'system:tenant:query',          '#', 1, 0, ''),
                                   (71002, 0, -1, 71000, '租户新增', '#', '', 'F', '0', 'system:tenant:add',            '#', 2, 0, ''),
                                   (71003, 0, -1, 71000, '租户修改', '#', '', 'F', '0', 'system:tenant:edit',           '#', 3, 0, ''),
                                   (71004, 0, -1, 71000, '租户删除', '#', '', 'F', '0', 'system:tenant:remove',         '#', 4, 0, ''),
       -- 一级菜单
       (90000, 0, 0, 0, '素材管理', null,       null, 'M', '1', '', 'xy_tool',     9, 0, '素材管理目录'),
                                   -- 素材模块按钮
                                   (91001, 0, 0, 90000, '素材查询', '#', '', 'F', '0', 'system:material:query',        '#', 1, 0, ''),
                                   (91002, 0, 0, 90000, '素材新增', '#', '', 'F', '0', 'system:material:add',          '#', 2, 0, ''),
                                   (91003, 0, 0, 90000, '素材编辑', '#', '', 'F', '0', 'system:material:edit',         '#', 3, 0, ''),
                                   (91004, 0, 0, 90000, '素材删除', '#', '', 'F', '0', 'system:material:remove',       '#', 4, 0, '');

-- ----------------------------
-- 6、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id                   bigint              not null auto_increment                 comment '字典主键',
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
       (12, '跳转类型', 'sys_jump_type', 0, '跳转类型列表'),
       (13, '参数类型', 'sys_parameter_type', 0, '参数类型列表'),
       (14, '商城类型', 'sys_mall_type', 0, '商城类型列表'),
       (15, '商城版本', 'sys_mall_edition', 0, '商城版本列表'),
       (16, '展示状态', 'sys_view_type', 0, '展示状态列表'),
       (17, '字段类型', 'sys_field_type', 0, '字段类型列表'),
       (18, '后台展示状态', 'sys_mall_view_type', 0, '后台展示状态列表');


-- ----------------------------
-- 7、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code                 bigint              not null auto_increment                 comment '字典编码',
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
       (35, 2, '外部跳转', '1', 'sys_jump_type', '', '', 'N', 0, '路由外部跳转'),
       (36, 1, '私有库', '0', 'sys_parameter_type', '', '', 'N', 0, '私有库'),
       (37, 2, '公共库', '1', 'sys_parameter_type', '', '', 'N', 0, '公共库'),
       (38, 1, '一站一店版', '0', 'sys_mall_type', '', '', 'N', 0, '一站一店版'),
       (39, 2, '一站多店版', '1', 'sys_mall_type', '', '', 'N', 0, '一站多店版'),
       (40, 3, '多站一店版', '2', 'sys_mall_type', '', '', 'N', 0, '多站一店版'),
       (41, 4, '多站多店版', '3', 'sys_mall_type', '', '', 'N', 0, '多站多店版'),
       (42, 5, '多商户版', '4', 'sys_mall_type', '', '', 'N', 0, '多商户版'),
       (43, 1, '入门版', '0', 'sys_mall_edition', '', '', 'N', 0, '入门版'),
       (44, 2, '标准版', '1', 'sys_mall_edition', '', '', 'N', 0, '标准版'),
       (45, 3, '企业版', '2', 'sys_mall_edition', '', '', 'N', 0, '企业版'),
       (46, 4, '旗舰版', '3', 'sys_mall_edition', '', '', 'N', 0, '旗舰版'),
       (47, 1, '所有人可见', '0', 'sys_view_type', '', '', 'N', 0, '所有人可见'),
       (48, 2, '仅登录可见', '1', 'sys_view_type', '', '', 'N', 0, '仅登录可见'),
       (49, 3, '仅会员可见', '2', 'sys_view_type', '', '', 'N', 0, '仅会员可见'),
       (50, 1, '选择型', '0', 'sys_field_type', '', '', 'N', 0, '选择型'),
       (51, 2, '文字输入型', '1', 'sys_field_type', '', '', 'N', 0, '文字输入型'),
       (52, 3, '数值输入型', '2', 'sys_field_type', '', '', 'N', 0, '数值输入型'),
       (53, 1, '正常', '0', 'sys_mall_view_type', '', '', 'N', 0, '正常'),
       (54, 2, '仅后台可见', '1', 'sys_mall_view_type', '', '', 'N', 0, '仅后台可见');

-- ----------------------------
-- 8、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id                 bigint              not null                                comment '参数主键',
  config_name               varchar(100)        default ''                              comment '参数名称',
  config_key                varchar(100)        default ''                              comment '参数键名',
  config_value              varchar(500)        default ''                              comment '参数键值',
  config_type               char(1)             default 'N'                             comment '系统内置（Y是 N否）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (config_id)
) engine=innodb comment = '参数配置表';

insert into sys_config (config_id, config_name, config_key, config_value, config_type, remark, tenant_id)
values (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName',     'skin-blue',     'Y', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', 0),
       (2, '用户管理-账号初始密码',     'sys.user.initPassword',  '123456',        'Y', '初始化密码 123456', 0),
       (3, '主框架页-侧边栏主题',       'sys.index.sideTheme',    'theme-dark',    'Y', '深色主题theme-dark，浅色主题theme-light', 0);

-- ----------------------------
-- 9、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id                    bigint              not null                                comment '任务Id',
  job_name                  varchar(64)         default ''                              comment '任务名称',
  job_group                 varchar(64)         default 'DEFAULT'                       comment '任务组名',
  invoke_target             varchar(500)        not null                                comment '调用目标字符串',
  cron_expression           varchar(255)        default ''                              comment 'cron执行表达式',
  misfire_policy            varchar(20)         default '3'                             comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent                char(1)             default '1'                             comment '是否并发执行（0允许 1禁止）',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1暂停）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

insert into sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, tenant_id)
values (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', 0),
       (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', 0),
       (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', 0);

-- ----------------------------
-- 10、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id                bigint              not null auto_increment                 comment '任务日志Id',
  job_name                  varchar(64)         not null                                comment '任务名称',
  job_group                 varchar(64)         not null                                comment '任务组名',
  invoke_target             varchar(500)        not null                                comment '调用目标字符串',
  job_message               varchar(500)                                                comment '日志信息',
  status                    char(1)             not null default '0'                    comment '执行状态（0正常 1失败）',
  exception_info            varchar(2000)       default ''                              comment '异常信息',
  create_time               datetime            default current_timestamp               comment '创建时间',
  del_time                  datetime            on update current_timestamp             comment '删除时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';

-- ----------------------------
-- 11、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  oper_id                   bigint              not null auto_increment                 comment '日志主键',
  title                     varchar(50)         default ''                              comment '模块标题',
  business_type             int(2)              default 0                               comment '业务类型（0其它 1新增 2修改 3删除）',
  method                    varchar(100)        default ''                              comment '方法名称',
  request_method            varchar(10)         default ''                              comment '请求方式',
  operator_type             int(1)              default 0                               comment '操作类别（0其它 1后台用户 2手机端用户）',
  user_id                   bigint              not null                                comment '操作人员',
  oper_url                  varchar(255)        default ''                              comment '请求URL',
  oper_ip                   varchar(128)        default ''                              comment '主机地址',
  oper_location             varchar(255)        default ''                              comment '操作地点',
  oper_param                varchar(2000)       default ''                              comment '请求参数',
  json_result               varchar(2000)       default ''                              comment '返回参数',
  status                    int(1)              default 0                               comment '操作状态（0正常 1异常）',
  error_msg                 varchar(2000)       default ''                              comment '错误消息',
  oper_time                 datetime            default current_timestamp               comment '操作时间',
  del_time                  datetime            on update current_timestamp             comment '删除时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (oper_id)
) engine=innodb auto_increment=100 comment = '操作日志记录';

-- ----------------------------
-- 12、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id                   bigint              not null auto_increment                 comment '访问Id',
  enterprise_name           varchar(50)         default ''                              comment '企业账号',
  user_name                 varchar(50)         default ''                              comment '用户账号',
  user_id                   bigint              default null                            comment '用户Id',
  ipaddr                    varchar(128)        default ''                              comment '登录IP地址',
  status                    char(1)             default '0'                             comment '登录状态（0成功 1失败）',
  msg                       varchar(255)        default ''                              comment '提示信息',
  access_time               datetime            default current_timestamp               comment '访问时间',
  del_time                  datetime            on update current_timestamp             comment '删除时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (info_id)
) engine=innodb auto_increment=100 comment = '系统访问记录';

-- ----------------------------
-- 13、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  notice_id                 bigint              not null                                comment '公告Id',
  notice_title              varchar(50)         not null                                comment '公告标题',
  notice_type               char(1)             not null                                comment '公告类型（1通知 2公告）',
  notice_content            longblob            default null                            comment '公告内容',
  status                    char(1)             default '0'                             comment '公告状态（0正常 1关闭）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (notice_id)
) engine=innodb comment = '通知公告表';

-- ----------------------------
-- 14、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id          bigint(20)      not null auto_increment    comment '编号',
  table_name        varchar(200)    default ''                 comment '表名称',
  table_comment     varchar(500)    default ''                 comment '表描述',
  sub_table_name    varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name varchar(64)     default null               comment '子表关联的外键名',
  class_name        varchar(100)    default ''                 comment '实体类名称',
  tpl_category      varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  package_name      varchar(100)                               comment '生成包路径',
  module_name       varchar(30)                                comment '生成模块名',
  business_name     varchar(30)                                comment '生成业务名',
  function_name     varchar(50)                                comment '生成功能名',
  function_author   varchar(50)                                comment '生成功能作者',
  gen_type          char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options           varchar(1000)                              comment '其它生成选项',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 15、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          varchar(64)                                comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';