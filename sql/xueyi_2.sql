-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id                   bigint              not null                                comment '部门id',
  parent_id                 bigint              default 0                               comment '父部门id',
  dept_code                 varchar(64)         not null                                comment '部门编码',
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
values (1, 1, 100, 1, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '系统管理员'),
       (2, 1, 100, 1, '002', 'xy', 'xy', '01', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 1, '管理员'),
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
values
       -- 一级菜单
       (10000, 0, 0, 0, '组织管理', 'organize',   null, 'M', '', 'xy_organization', 1, 0, '组织管理目录'),
              -- 二级菜单
              (11000, 0, 0, 10000, '部门管理', 'dept',   'system/organize/dept/index',      'C',  'system:dept:list',      'xy_dept',   1, 0, '部门管理菜单'),
                                   -- 部门管理按钮
                                   (11001, 0, 0, 11000, '部门查询', '', '', 'F', 'system:dept:query',           '#', 1, 0, ''),
                                   (11002, 0, 0, 11000, '部门新增', '', '', 'F', 'system:dept:add',             '#', 2, 0, ''),
                                   (11003, 0, 0, 11000, '部门修改', '', '', 'F', 'system:dept:edit',            '#', 3, 0, ''),
                                   (11004, 0, 0, 11000, '部门删除', '', '', 'F', 'system:dept:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (12000, 0, 0, 10000, '岗位管理', 'post',   'system/organize/post/index',      'C',  'system:post:list',      'xy_post',   2, 0, '岗位管理菜单'),
                                   -- 岗位管理按钮
                                   (12001, 0, 0, 12000, '岗位查询', '', '', 'F', 'system:post:query',           '#', 1, 0, ''),
                                   (12002, 0, 0, 12000, '岗位新增', '', '', 'F', 'system:post:add',             '#', 2, 0, ''),
                                   (12003, 0, 0, 12000, '岗位修改', '', '', 'F', 'system:post:edit',            '#', 3, 0, ''),
                                   (12004, 0, 0, 12000, '岗位删除', '', '', 'F', 'system:post:remove',          '#', 4, 0, ''),
                                   (12005, 0, 0, 12000, '岗位导出', '', '', 'F', 'system:post:export',          '#', 5, 0, ''),
              -- 二级菜单
              (13000, 0, 0, 10000, '用户管理', 'user',   'system/organize/user/index',      'C',  'system:user:list',      'xy_user',   3, 0, '用户管理菜单'),
                                   -- 用户管理按钮
                                   (13001, 0, 0, 13000, '用户查询', '', '', 'F', 'system:user:query',           '#', 1, 0, ''),
                                   (13002, 0, 0, 13000, '用户新增', '', '', 'F', 'system:user:add',             '#', 2, 0, ''),
                                   (13003, 0, 0, 13000, '用户修改', '', '', 'F', 'system:user:edit',            '#', 3, 0, ''),
                                   (13004, 0, 0, 13000, '用户删除', '', '', 'F', 'system:user:remove',          '#', 4, 0, ''),
                                   (13005, 0, 0, 13000, '用户导出', '', '', 'F', 'system:user:export',          '#', 5, 0, ''),
                                   (13006, 0, 0, 13000, '用户导入', '', '', 'F', 'system:user:import',          '#', 6, 0, ''),
                                   (13007, 0, 0, 13000, '重置密码', '', '', 'F', 'system:user:resetPwd',        '#', 7, 0, ''),
       -- 一级菜单
       (20000, 0, 0, 0, '企业账户', 'enterprise', null, 'M', '', 'xy_enterprise',   2, 0, '企业账户目录'),
       -- 一级菜单
       (30000, 0, 0, 0, '系统管理', 'system',     null, 'M', '', 'xy_setting',      3, 0, '系统管理目录'),
              (31000, 0, 0, 30000, '字典管理', 'dict',   'system/system/dict/index',        'C',  'system:dict:list',      'xy_dict',   1, 0, '字典管理菜单'),
                                   -- 字典管理按钮
                                   (31001, 0, 0, 31000, '字典查询', '#', '', 'F', 'system:dict:query',          '#', 1, 0, ''),
                                   (31002, 0, 0, 31000, '字典新增', '#', '', 'F', 'system:dict:add',            '#', 2, 0, ''),
                                   (31003, 0, 0, 31000, '字典修改', '#', '', 'F', 'system:dict:edit',           '#', 3, 0, ''),
                                   (31004, 0, 0, 31000, '字典删除', '#', '', 'F', 'system:dict:remove',         '#', 4, 0, ''),
                                   (31005, 0, 0, 31000, '字典导出', '#', '', 'F', 'system:dict:export',         '#', 5, 0, ''),
              -- 二级菜单
              (32000, 0, 0, 30000, '参数管理', 'config', 'system/system/config/index',      'C',  'system:config:list',    'xy_config', 2, 0, '参数管理菜单'),
                                   -- 参数设置按钮
                                   (32001, 0, 0, 32000, '参数查询', '#', '', 'F', 'system:config:query',        '#', 1, 0, ''),
                                   (32002, 0, 0, 32000, '参数新增', '#', '', 'F', 'system:config:add',          '#', 2, 0, ''),
                                   (32003, 0, 0, 32000, '参数修改', '#', '', 'F', 'system:config:edit',         '#', 3, 0, ''),
                                   (32004, 0, 0, 32000, '参数删除', '#', '', 'F', 'system:config:remove',       '#', 4, 0, ''),
                                   (32005, 0, 0, 32000, '参数导出', '#', '', 'F', 'system:config:export',       '#', 5, 0, ''),
              -- 二级菜单
              (33000, 0, 0, 30000, '通知公告', 'notice', 'system/system/notice/index',      'C',  'system:notice:list',    'xy_message', 3, 0, '通知公告菜单'),
                                   -- 通知公告按钮
                                   (33001, 0, 0, 33000, '公告查询', '#', '', 'F', 'system:notice:query',        '#', 1, 0, ''),
                                   (33002, 0, 0, 33000, '公告新增', '#', '', 'F', 'system:notice:add',          '#', 2, 0, ''),
                                   (33003, 0, 0, 33000, '公告修改', '#', '', 'F', 'system:notice:edit',         '#', 3, 0, ''),
                                   (33004, 0, 0, 33000, '公告删除', '#', '', 'F', 'system:notice:remove',       '#', 4, 0, ''),
              -- 二级菜单
              (34000, 0, 0, 30000, '日志管理', 'log',    '',                                'M',  '',                      'xy_log',    4, 0, '日志管理菜单'),
                     -- 三级菜单
                     (34100, 0, 0, 34000, '操作日志', 'operlog',   'system/system/journal/operlog/index',    'C', 'system:operlog:list',   'xy_log_operation',  1, 0, '操作日志菜单'),
                                   -- 操作日志按钮
                                   (34101, 0, 0, 34100, '操作查询', '#', '', 'F', 'system:operlog:query',       '#', 1, 0, ''),
                                   (34102, 0, 0, 34100, '操作删除', '#', '', 'F', 'system:operlog:remove',      '#', 2, 0, ''),
                                   (34103, 0, 0, 34100, '日志导出', '#', '', 'F', 'system:operlog:export',      '#', 3, 0, ''),
                     -- 三级菜单
                     (34200, 0, 0, 34000, '登录日志', 'loginInfo', 'system/system/journal/loginInfo/index',  'C', 'system:loginInfo:list', 'xy_log_loginInfo',  2, 0, '登录日志菜单'),
                                   -- 登录日志按钮
                                   (34201, 0, 0, 34200, '登录查询', '#', '', 'F', 'system:loginInfo:query',     '#', 1, 0, ''),
                                   (34202, 0, 0, 34200, '登录删除', '#', '', 'F', 'system:loginInfo:remove',    '#', 2, 0, ''),
                                   (34203, 0, 0, 34200, '日志导出', '#', '', 'F', 'system:loginInfo:export',    '#', 3, 0, ''),
       -- 一级菜单
       (40000, 0, 0, 0, '权限管理', 'authority',  null, 'M', '', 'xy_authority',    3, 0, '权限管理目录'),
              -- 二级菜单
              (41000, 0, 0, 40000, '角色管理', 'role',   'system/authority/role/index',     'C', 'system:role:list',      'xy_role',    1, 0, '角色管理菜单'),
                                   -- 角色管理按钮
                                   (41001, 0, 0, 41000, '角色查询', '', '', 'F', 'system:role:query',           '#', 1, 0, ''),
                                   (41002, 0, 0, 41000, '角色授权', '', '', 'F', 'system:role:set',             '#', 2, 0, ''),
                                   (41003, 0, 0, 41000, '角色新增', '', '', 'F', 'system:role:add',             '#', 3, 0, ''),
                                   (41004, 0, 0, 41000, '角色修改', '', '', 'F', 'system:role:edit',            '#', 4, 0, ''),
                                   (41005, 0, 0, 41000, '角色删除', '', '', 'F', 'system:role:remove',          '#', 5, 0, ''),
                                   (41006, 0, 0, 41000, '角色导出', '', '', 'F', 'system:role:export',          '#', 6, 0, ''),
              -- 二级菜单
              (42000, 0, 0, 40000, '菜单管理', 'menu',   'system/authority/menu/index',     'C', 'system:menu:list',      'xy_menu',    2, 0, '菜单管理菜单'),
                                   -- 菜单管理按钮
                                   (42001, 0, 0, 42000, '菜单查询', '', '', 'F', 'system:menu:query',           '#', 1, 0, ''),
                                   (42002, 0, 0, 42000, '菜单新增', '', '', 'F', 'system:menu:add',             '#', 2, 0, ''),
                                   (42003, 0, 0, 42000, '菜单修改', '', '', 'F', 'system:menu:edit',            '#', 3, 0, ''),
                                   (42004, 0, 0, 42000, '菜单删除', '', '', 'F', 'system:menu:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (43000, 0, 0, 40000, '模块管理', 'system', 'system/authority/system/index',   'C', 'system:system:list',    'xy_system',  3, 0, '模块管理菜单'),
                                   -- 模块管理按钮
                                   (43001, 0, 0, 43000, '模块管理查询', '#', '', 'F', 'system:system:query',    '#', 1, 0, ''),
                                   (43002, 0, 0, 43000, '模块管理新增', '#', '', 'F', 'system:system:add',      '#', 1, 0, ''),
                                   (43003, 0, 0, 43000, '模块管理修改', '#', '', 'F', 'system:system:edit',     '#', 1, 0, ''),
                                   (43004, 0, 0, 43000, '模块管理删除', '#', '', 'F', 'system:system:remove',   '#', 1, 0, ''),
       -- 一级菜单
       (50000, 0, 0, 0, '系统监控', 'monitor',    null, 'M', '', 'xy_monitor',         4, 0, '系统监控目录'),
              -- 二级菜单
              (51000, 0, 0, 50000, '在线用户', 'online', 'system/monitor/online/index',     'C', 'monitor:online:list',   'xy_online',  1, 0, '在线用户菜单'),
                                   -- 在线用户按钮
                                   (51001, 0, 0, 51000, '在线查询', '#', '', 'F', 'monitor:online:query',       '#', 1, 0, ''),
                                   (51002, 0, 0, 51000, '批量强退', '#', '', 'F', 'monitor:online:batchLogout', '#', 2, 0, ''),
                                   (51003, 0, 0, 51000, '单条强退', '#', '', 'F', 'monitor:online:forceLogout', '#', 3, 0, ''),

              -- 二级菜单
              (52000, 0, 0, 50000, '定时任务', 'job',    'system/monitor/job/index',        'C', 'monitor:job:list',      'xy_job',     2, 0, '定时任务菜单'),
                                   -- 定时任务按钮
                                   (52001, 0, 0, 52000, '任务查询', '#', '', 'F', 'monitor:job:query',          '#', 1, 0, ''),
                                   (52002, 0, 0, 52000, '任务新增', '#', '', 'F', 'monitor:job:add',            '#', 2, 0, ''),
                                   (52003, 0, 0, 52000, '任务修改', '#', '', 'F', 'monitor:job:edit',           '#', 3, 0, ''),
                                   (52004, 0, 0, 52000, '任务删除', '#', '', 'F', 'monitor:job:remove',         '#', 4, 0, ''),
                                   (52005, 0, 0, 52000, '状态修改', '#', '', 'F', 'monitor:job:changeStatus',   '#', 5, 0, ''),
                                   (52006, 0, 0, 52000, '任务导出', '#', '', 'F', 'monitor:job:export',         '#', 6, 0, ''),
              -- 二级菜单
              (53000, 0, 0, 50000, 'Sentinel控制台', 'http://localhost:8718',      '',      'C', 'monitor:sentinel:list', 'xy_sentinel', 3, 0, '流量控制菜单'),
              -- 二级菜单
              (54000, 0, 0, 50000, 'Nacos控制台',    'http://localhost:8848/nacos', '',     'C', 'monitor:nacos:list',    'xy_nacos',  4, 0, '服务治理菜单'),
              -- 二级菜单
              (55000, 0, 0, 50000, 'Admin控制台',    'http://localhost:9100/login', '',     'C', 'monitor:server:list',   'xy_server', 5, 0, '服务监控菜单'),
       -- 一级菜单
       (60000, 0, 0, 0, '系统工具', 'tool',       null, 'M', '', 'xy_tool',     5, 0, '系统工具目录'),
              -- 二级菜单
              (61000, 0, 0, 60000, '表单构建', 'build',  'tool/build/index',                'C', 'tool:build:list',       'xy_build',   1, 0, '表单构建菜单'),
              -- 二级菜单
              (62000, 0, 0, 60000, '代码生成', 'gen',    'tool/gen/index',                  'C', 'tool:gen:list',         'xy_code',    2, 0, '代码生成菜单'),
                                   -- 代码生成按钮
                                   (62001, 0, 0, 62000, '生成查询', '#', '', 'F', 'tool:gen:query',             '#', 1, 0, ''),
                                   (62002, 0, 0, 62000, '生成修改', '#', '', 'F', 'tool:gen:edit',              '#', 2, 0, ''),
                                   (62003, 0, 0, 62000, '生成删除', '#', '', 'F', 'tool:gen:remove',            '#', 3, 0, ''),
                                   (62004, 0, 0, 62000, '导入代码', '#', '', 'F', 'tool:gen:import',            '#', 4, 0, ''),
                                   (62005, 0, 0, 62000, '预览代码', '#', '', 'F', 'tool:gen:preview',           '#', 5, 0, ''),
                                   (62006, 0, 0, 62000, '生成代码', '#', '', 'F', 'tool:gen:code',              '#', 6, 0, ''),
              -- 二级菜单
              (63000, 0, 0, 60000, '系统接口', 'http://localhost:8080/swagger-ui.html', '', 'C', 'tool:swagger:list',     'xy_swagger', 3, 0, '系统接口菜单');

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

-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id                 bigint(20)          not null                                comment '参数主键',
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