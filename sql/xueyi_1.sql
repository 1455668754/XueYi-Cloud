-- ----------------------------
-- 1、租户信息表|管理租户账户信息
-- ----------------------------
drop table if exists xy_tenant;
create table xy_tenant (
  tenant_id		            bigint	            not null                                comment '租户Id',
  strategy_id		        bigint	            not null                                comment '策略Id',
  tenant_name		        varchar(50)	        not null unique	                        comment '租户账号',
  tenant_system_name		varchar(50)	        not null 	                            comment '系统名称',
  tenant_nick		        varchar(50)	        not null 	                            comment '租户名称',
  tenant_logo		        varchar(2000)	    default ''	                            comment '租户logo',
  tenant_name_frequency     tinyint             default 0                               comment '租户账号修改次数',
  is_change                 char(1)             not null default 'N'	                comment '系统租户（Y是 N否）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  primary key (tenant_id),
  unique key (tenant_name)
) engine=innodb comment = '租户信息表';

-- ----------------------------
-- 初始化-租户信息表数据
-- ----------------------------
insert into xy_tenant (is_change, strategy_id, tenant_id, tenant_name, tenant_system_name, tenant_nick, tenant_logo)
values ('Y', 0, -1, 'administrator', '租户管理系统', 'xueYi1', 'http://127.0.0.1:9300/statics/2021/06/08/99d4a2dc-4fdf-435a-aeeb-116ee129d55c.jpeg'),
       ('N', 0, 1, 'xueYi', '雪忆管理系统', 'xueYi1', 'http://127.0.0.1:9300/statics/2021/06/08/99d4a2dc-4fdf-435a-aeeb-116ee129d55c.jpeg');

-- ----------------------------
-- 2、数据源策略表|管理数据源策略信息
-- ----------------------------
drop table if exists xy_tenant_strategy;
create table xy_tenant_strategy (
  strategy_id		        bigint	            not null                                comment '策略Id',
  name                      varchar(500)	    not null default ''	                    comment '策略名称',
  tenant_amount		        int unsigned        not null default 0	                    comment '租户数量',
  source_amount		        int unsigned        not null default 0	                    comment '数据源数量',
  is_change                 tinyint             not null default 0	                    comment '可修改（0是 1否）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (strategy_id)
) engine=innodb comment = '数据源策略表';

-- ----------------------------
-- 初始化-数据源策略表数据
-- ----------------------------
insert into xy_tenant_strategy(strategy_id, name, tenant_amount, source_amount, is_change, sort)
values (0, '默认策略', 2, 1, 1, 0);

-- ----------------------------
-- 3、数据源表|管理系统数据源信息 | 主库有且只能有一个，用途：主要用于存储公共数据，具体看后续文档或视频
-- ----------------------------
drop table if exists xy_tenant_source;
create table xy_tenant_source (
  source_id		            bigint	            not null                                comment '数据源Id',
  name		                varchar(50)	        not null                                comment '数据源名称',
  database_type             char(1)	            not null default '0'	                comment '数据源类型（0子数据源 1主数据源）',
  slave		                varchar(500)	    not null default ''	                    comment '数据源编码',
  driver_class_name		    varchar(500)	    not null default ''	                    comment '驱动',
  url	                    varchar(500)	    not null default ''	                    comment '地址',
  url_prepend	            varchar(500)	    not null default ''	                    comment '连接地址',
  url_append	            varchar(500)	    not null default ''	                    comment '连接参数',
  username	                varchar(500)	    not null default ''	                    comment '用户名',
  password	                varchar(500)	    not null default ''	                    comment '密码',
  type		                char(1)	            not null default '0'	                comment '读写类型（0读&写 1只读 2只写）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (source_id)
) engine=innodb comment = '数据源表';

-- ----------------------------
-- 初始化-数据源表数据 | 这条数据为我的基础库，实际使用时调整成自己的库即可
-- ----------------------------
insert into xy_tenant_source(source_id, name, database_type, slave, driver_class_name, url, url_prepend, url_append, username, password, type)
values (0, '默认数据源', '1', 'master', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://localhost:3306/xy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8', 'jdbc:mysql://localhost:3306/xy-cloud', '?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8', 'root', 'password', '0');

-- ----------------------------
-- 4、主从库关联表  写1-n读
-- ----------------------------
drop table if exists xy_tenant_separation;
create table xy_tenant_separation (
  write_id		            bigint	            not null                                comment '写数据源Id',
  read_id		            bigint	            not null                                comment '读数据源Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (read_id)
) engine=innodb comment = '主从库关联表';

-- ----------------------------
-- 初始化-主从库关联表数据
-- ----------------------------
insert into xy_tenant_separation(write_id, read_id)
values (0, 0);

-- ----------------------------
-- 5、策略-数据源关联表  策略n-n写数据源 | 数据源仅为写|读写的类型
-- ----------------------------
drop table if exists xy_tenant_strategy_source;
create table xy_tenant_strategy_source (
  strategy_id		        bigint	            not null                                comment '策略Id',
  source_id		            bigint	            not null                                comment '数据源Id',
  status		            char(1)	            not null default 'N'                    comment '主数据源（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (strategy_id, source_id)
) engine=innodb comment = '策略-数据源关联表';

-- ----------------------------
-- 初始化-策略-数据源关联表数据
-- ----------------------------
insert into xy_tenant_strategy_source(strategy_id, source_id, status)
values (0, 0, 'Y');

-- ----------------------------
-- 6、子模块表|管理子系统模块
-- ----------------------------
drop table if exists xy_system;
create table xy_system (
  system_id		            bigint	            not null                                comment '系统Id',
  system_name		        varchar(50)	        not null	                            comment '系统名称',
  image_url                 varchar(5000)	    default null 	        	            comment '图片地址',
  type		                char(1)	            not null default '1'	                comment '跳转类型（0内部跳转 1外部跳转）',
  is_new		            char(1)	            not null default 'Y'	                comment '新页面（Y是 N否）',
  route                     varchar(500)        not null	                            comment '跳转路由',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id（0默认系统 otherId特定租户专属）',
  primary key (system_id)
) engine=innodb comment = '子系统模块表';

# ----------------------------
# 初始化-租户信息表数据
# ----------------------------
insert into xy_system (system_id, system_name, image_url, route, remark, tenant_id)
values (2, '租户管理系统' , '{"materialId":"1401773330431913984","materialNick":"15d67d32-ca7e-4f41-a33c-1aa6d669109f.jpg","materialUrl":"http://127.0.0.1:9300/statics/2021/06/07/15d67d32-ca7e-4f41-a33c-1aa6d669109f.jpg","materialOriginalUrl":"http://127.0.0.1:9300/statics/2021/06/07/1d72dee2-48d8-446c-a264-61a4f3a121d8.jpg","hiddenVisible":false}' , 'http://localhost:81' , '雪忆租户管理系统', -1),
       (1 , '商城' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , 'http://localhost:82' , '轻松打造在线商城', 0),
       (3 , '系统3' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (4 , '系统4' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (5 , '系统5' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (6 , '系统6' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (7 , '系统7' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (8 , '系统8' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1);

-- ----------------------------
-- 7、菜单权限表
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
  sort                      int unsigned        not null default 0                      comment '显示顺序',
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
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, menu_name, path, component, menu_type, visible, perms, icon, sort, create_by, remark)
values
-- 系统Id 0 租户Id 0(公用)
       -- 一级菜单
       (10100, 0, 0, 0, '组织管理', 'organize',   null, 'M', '0', '', 'xy_organization', 1, 0, '组织管理目录'),
              -- 二级菜单
              (10110, 0, 0, 10100, '部门管理', 'dept',   'system/organize/dept/index',      'C', '0',  'system:dept:list',      'xy_dept',   1, 0, '部门管理菜单'),
                                   -- 部门管理按钮
                                   (10111, 0, 0, 10110, '部门查询', '', '', 'F', '0', 'system:dept:query',           '#', 1, 0, ''),
                                   (10112, 0, 0, 10110, '部门新增', '', '', 'F', '0', 'system:dept:add',             '#', 2, 0, ''),
                                   (10113, 0, 0, 10110, '部门修改', '', '', 'F', '0', 'system:dept:edit',            '#', 3, 0, ''),
                                   (10114, 0, 0, 10110, '部门删除', '', '', 'F', '0', 'system:dept:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (10120, 0, 0, 10100, '岗位管理', 'post',   'system/organize/post/index',      'C', '0',  'system:post:list',      'xy_post',   2, 0, '岗位管理菜单'),
                                   -- 岗位管理按钮
                                   (10121, 0, 0, 10120, '岗位查询', '', '', 'F', '0', 'system:post:query',           '#', 1, 0, ''),
                                   (10122, 0, 0, 10120, '岗位新增', '', '', 'F', '0', 'system:post:add',             '#', 2, 0, ''),
                                   (10123, 0, 0, 10120, '岗位修改', '', '', 'F', '0', 'system:post:edit',            '#', 3, 0, ''),
                                   (10124, 0, 0, 10120, '岗位删除', '', '', 'F', '0', 'system:post:remove',          '#', 4, 0, ''),
                                   (10125, 0, 0, 10120, '岗位导出', '', '', 'F', '0', 'system:post:export',          '#', 5, 0, ''),
              -- 二级菜单
              (10130, 0, 0, 10100, '用户管理', 'user',   'system/organize/user/index',      'C', '0',  'system:user:list',      'xy_user',   3, 0, '用户管理菜单'),
                                   -- 用户管理按钮
                                   (10131, 0, 0, 10130, '用户查询', '', '', 'F', '0', 'system:user:query',           '#', 1, 0, ''),
                                   (10132, 0, 0, 10130, '用户新增', '', '', 'F', '0', 'system:user:add',             '#', 2, 0, ''),
                                   (10133, 0, 0, 10130, '用户修改', '', '', 'F', '0', 'system:user:edit',            '#', 3, 0, ''),
                                   (10134, 0, 0, 10130, '用户删除', '', '', 'F', '0', 'system:user:remove',          '#', 4, 0, ''),
                                   (10135, 0, 0, 10130, '用户导出', '', '', 'F', '0', 'system:user:export',          '#', 5, 0, ''),
                                   (10136, 0, 0, 10130, '用户导入', '', '', 'F', '0', 'system:user:import',          '#', 6, 0, ''),
                                   (10137, 0, 0, 10130, '重置密码', '', '', 'F', '0', 'system:user:resetPwd',        '#', 7, 0, ''),
       -- 一级菜单
       (10200, 0, 0, 0, '企业账户', 'enterprise', null, 'M', '0', '', 'xy_enterprise',   2, 0, '企业账户目录'),
              (10210, 0, 0, 10200, '资料管理', 'dict',   'system/dataSetting/enterprise/profile/index',        'C', '0',  'system:enterprise:list',      'xy_dict',   1, 0, '资料管理菜单'),
                                   -- 字典管理按钮
                                   (10211, 0, 0, 10210, '资料查看权限', '#', '', 'F', '0', 'system:enterprise:list',      '#', 1, 0, ''),
                                   (10212, 0, 0, 10210, '普通操作权限', '#', '', 'F', '0', 'system:enterprise:edit',      '#', 2, 0, ''),
                                   (10213, 0, 0, 10210, '超管操作权限', '#', '', 'F', '0', 'system:enterpriseAdmin:edit', '#', 3, 0, ''),
       -- 一级菜单
       (10300, 0, 0, 0, '系统管理', 'system',     null, 'M', '0', '', 'xy_setting',      3, 0, '系统管理目录'),
              -- 二级菜单
              (10310, 0, 0, 10300, '通知公告', 'notice', 'system/system/notice/index',      'C', '0',  'system:notice:list',    'xy_message', 3, 0, '通知公告菜单'),
                                   -- 通知公告按钮
                                   (10311, 0, 0, 10310, '公告查询', '#', '', 'F', '0', 'system:notice:query',        '#', 1, 0, ''),
                                   (10312, 0, 0, 10310, '公告新增', '#', '', 'F', '0', 'system:notice:add',          '#', 2, 0, ''),
                                   (10313, 0, 0, 10310, '公告修改', '#', '', 'F', '0', 'system:notice:edit',         '#', 3, 0, ''),
                                   (10314, 0, 0, 10310, '公告删除', '#', '', 'F', '0', 'system:notice:remove',       '#', 4, 0, ''),
              -- 二级菜单
              (10320, 0, 0, 10300, '日志管理', 'log',    '',                                'M', '0',  '',                      'xy_log',    4, 0, '日志管理菜单'),
                     -- 三级菜单
                     (10321, 0, 0, 10320, '操作日志', 'operlog',   'system/system/journal/operlog/index',    'C', '0', 'system:operlog:list',   'xy_log_operation',  1, 0, '操作日志菜单'),
                                   -- 操作日志按钮
                                   (10322, 0, 0, 10321, '操作查询', '#', '', 'F', '0', 'system:operlog:query',       '#', 1, 0, ''),
                                   (10323, 0, 0, 10321, '操作删除', '#', '', 'F', '0', 'system:operlog:remove',      '#', 2, 0, ''),
                                   (10324, 0, 0, 10321, '日志导出', '#', '', 'F', '0', 'system:operlog:export',      '#', 3, 0, ''),
                     -- 三级菜单
                     (10325, 0, 0, 10320, '登录日志', 'loginInfo', 'system/system/journal/loginInfo/index',  'C', '0', 'system:loginInfo:list', 'xy_log_loginInfo',  2, 0, '登录日志菜单'),
                                   -- 登录日志按钮
                                   (10326, 0, 0, 10325, '登录查询', '#', '', 'F', '0', 'system:loginInfo:query',     '#', 1, 0, ''),
                                   (10327, 0, 0, 10325, '登录删除', '#', '', 'F', '0', 'system:loginInfo:remove',    '#', 2, 0, ''),
                                   (10328, 0, 0, 10325, '日志导出', '#', '', 'F', '0', 'system:loginInfo:export',    '#', 3, 0, ''),
       -- 一级菜单
       (10400, 0, 0, 0, '权限管理', 'authority',  null, 'M', '0', '', 'xy_authority',    3, 0, '权限管理目录'),
              -- 二级菜单
              (10410, 0, 0, 10400, '角色管理', 'role',   'system/authority/role/index',     'C', '0', 'system:role:list',      'xy_role',    1, 0, '角色管理菜单'),
                                   -- 角色管理按钮
                                   (10411, 0, 0, 10410, '角色查询', '', '', 'F', '0', 'system:role:query',           '#', 1, 0, ''),
                                   (10412, 0, 0, 10410, '角色授权', '', '', 'F', '0', 'system:role:set',             '#', 2, 0, ''),
                                   (10413, 0, 0, 10410, '角色新增', '', '', 'F', '0', 'system:role:add',             '#', 3, 0, ''),
                                   (10414, 0, 0, 10410, '角色修改', '', '', 'F', '0', 'system:role:edit',            '#', 4, 0, ''),
                                   (10415, 0, 0, 10410, '角色删除', '', '', 'F', '0', 'system:role:remove',          '#', 5, 0, ''),
                                   (10416, 0, 0, 10410, '角色导出', '', '', 'F', '0', 'system:role:export',          '#', 6, 0, ''),
              -- 二级菜单
              (10420, 0, 0, 10400, '菜单管理', 'menu',   'system/authority/menu/index',     'C', '0', 'system:menu:list',      'xy_menu',    2, 0, '菜单管理菜单'),
                                   -- 菜单管理按钮
                                   (10421, 0, 0, 10420, '菜单查询', '', '', 'F', '0', 'system:menu:query',           '#', 1, 0, ''),
                                   (10422, 0, 0, 10420, '菜单新增', '', '', 'F', '0', 'system:menu:add',             '#', 2, 0, ''),
                                   (10423, 0, 0, 10420, '菜单修改', '', '', 'F', '0', 'system:menu:edit',            '#', 3, 0, ''),
                                   (10424, 0, 0, 10420, '菜单删除', '', '', 'F', '0', 'system:menu:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (10430, 0, 0, 10400, '模块管理', 'system', 'system/authority/system/index',   'C', '0', 'system:system:list',    'xy_system',  3, 0, '模块管理菜单'),
                                   -- 模块管理按钮
                                   (10431, 0, 0, 10430, '模块管理查询', '#', '', 'F', '0', 'system:system:query',    '#', 1, 0, ''),
                                   (10432, 0, 0, 10430, '模块管理新增', '#', '', 'F', '0', 'system:system:add',      '#', 1, 0, ''),
                                   (10433, 0, 0, 10430, '模块管理修改', '#', '', 'F', '0', 'system:system:edit',     '#', 1, 0, ''),
                                   (10434, 0, 0, 10430, '模块管理删除', '#', '', 'F', '0', 'system:system:remove',   '#', 1, 0, ''),
       -- 一级菜单
       (10500, 0, 0, 0, '系统监控', 'monitor',    null, 'M', '0', '', 'xy_monitor',         4, 0, '系统监控目录'),
              -- 二级菜单
              (10510, 0, 0, 10500, '在线用户', 'online', 'system/monitor/online/index',     'C', '0', 'monitor:online:list',   'xy_online',  1, 0, '在线用户菜单'),
                                   -- 在线用户按钮
                                   (10511, 0, 0, 10510, '在线查询', '#', '', 'F', '0', 'monitor:online:query',       '#', 1, 0, ''),
                                   (10512, 0, 0, 10510, '批量强退', '#', '', 'F', '0', 'monitor:online:batchLogout', '#', 2, 0, ''),
                                   (10513, 0, 0, 10510, '单条强退', '#', '', 'F', '0', 'monitor:online:forceLogout', '#', 3, 0, ''),

              -- 二级菜单
              (10520, 0, 0, 10500, '定时任务', 'job',    'system/monitor/job/index',        'C', '0', 'monitor:job:list',      'xy_job',     2, 0, '定时任务菜单'),
                                   -- 定时任务按钮
                                   (10521, 0, 0, 10520, '任务查询', '#', '', 'F', '0', 'monitor:job:query',          '#', 1, 0, ''),
                                   (10522, 0, 0, 10520, '任务新增', '#', '', 'F', '0', 'monitor:job:add',            '#', 2, 0, ''),
                                   (10523, 0, 0, 10520, '任务修改', '#', '', 'F', '0', 'monitor:job:edit',           '#', 3, 0, ''),
                                   (10524, 0, 0, 10520, '任务删除', '#', '', 'F', '0', 'monitor:job:remove',         '#', 4, 0, ''),
                                   (10525, 0, 0, 10520, '状态修改', '#', '', 'F', '0', 'monitor:job:changeStatus',   '#', 5, 0, ''),
                                   (10526, 0, 0, 10520, '任务导出', '#', '', 'F', '0', 'monitor:job:export',         '#', 6, 0, ''),
       -- 一级菜单
       (10600, 0, 0, 0, '素材管理', null,       null, 'M', '1', '', 'xy_tool',     9, 0, '素材管理目录'),
                                   -- 素材模块按钮
                                   (10601, 0, 0, 10600, '素材查询', '#', '', 'F', '0', 'system:material:query',        '#', 1, 0, ''),
                                   (10602, 0, 0, 10600, '素材新增', '#', '', 'F', '0', 'system:material:add',          '#', 2, 0, ''),
                                   (10603, 0, 0, 10600, '素材编辑', '#', '', 'F', '0', 'system:material:edit',         '#', 3, 0, ''),
                                   (10604, 0, 0, 10600, '素材删除', '#', '', 'F', '0', 'system:material:remove',       '#', 4, 0, ''),

-- 系统Id 2 租户Id -1(超管)
       -- 一级菜单
       (20000, 2, -1, 0, '租户管理', 'tenant',      null, 'M', '0', '', 'xy_tenant',     6, 0, '租户管理目录'),
              -- 二级菜单
              (20010, 2, -1, 20000, '租户管理', 'tenant',    'tenant/tenant/index',  'C', '0', 'tenant:tenant:list',    'xy_tenant',    1, 0, '租户管理菜单'),
                                   -- 租户管理按钮
                                   (20011, 0, -1, 20010, '租户查询', '#', '', 'F', '0', 'tenant:tenant:query',          '#', 1, 0, ''),
                                   (20012, 0, -1, 20010, '租户新增', '#', '', 'F', '0', 'tenant:tenant:add',            '#', 2, 0, ''),
                                   (20013, 0, -1, 20010, '租户修改', '#', '', 'F', '0', 'tenant:tenant:edit',           '#', 3, 0, ''),
                                   (20014, 0, -1, 20010, '租户删除', '#', '', 'F', '0', 'tenant:tenant:remove',         '#', 4, 0, ''),
                     -- 二级菜单
              (20050, 2, -1, 20000, '数据源策略', 'strategy',   'tenant/strategy/index',      'C', '0',  'tenant:strategy:list',      '#',   1, 0, '数据源策略菜单'),
                                   -- 数据源策略按钮
                                   (20051, 2, -1, 20050, '数据源策略查询', '', '', 'F', '0', 'tenant:strategy:query',           '#', 1, 0, ''),
                                   (20052, 2, -1, 20050, '数据源策略新增', '', '', 'F', '0', 'tenant:strategy:add',             '#', 2, 0, ''),
                                   (20053, 2, -1, 20050, '数据源策略修改', '', '', 'F', '0', 'tenant:strategy:edit',            '#', 3, 0, ''),
                                   (20054, 2, -1, 20050, '数据源策略删除', '', '', 'F', '0', 'tenant:strategy:remove',          '#', 4, 0, ''),
                                   (20055, 2, -1, 20050, '数据源策略导出', '', '', 'F', '0', 'tenant:strategy:export',          '#', 5, 0, ''),
              -- 二级菜单
              (20020, 2, -1, 20000, '数据源配置', 'source',   'tenant/source/index',      'C', '0',  'tenant:source:list',      '#',   1, 0, '数据源菜单'),
                                   -- 数据源管理按钮
                                   (20021, 2, -1, 20020, '数据源配置查询', '', '', 'F', '0', 'tenant:source:query',           '#', 1, 0, ''),
                                   (20022, 2, -1, 20020, '数据源配置新增', '', '', 'F', '0', 'tenant:source:add',             '#', 2, 0, ''),
                                   (20023, 2, -1, 20020, '数据源配置修改', '', '', 'F', '0', 'tenant:source:edit',            '#', 3, 0, ''),
                                   (20024, 2, -1, 20020, '数据源配置删除', '', '', 'F', '0', 'tenant:source:remove',          '#', 4, 0, ''),
                                   (20025, 2, -1, 20020, '数据源配置导出', '', '', 'F', '0', 'tenant:source:export',          '#', 5, 0, ''),
              -- 二级菜单
              (20030, 2, -1, 20000, '主从配置', 'separation',   'tenant/separation/index',      'C', '0',  'tenant:separation:list',      '#',   1, 0, '主从配置菜单'),
                                   -- 读写分离按钮 SQL
                                   (20031, 2, -1, 20030, '主从配置查询', '', '', 'F', '0', 'tenant:separation:query',           '#', 1, 0, ''),
                                   (20032, 2, -1, 20030, '主从配置配置', '', '', 'F', '0', 'tenant:separation:edit',            '#', 2, 0, ''),
       -- 一级菜单
       (20100, 2, -1, 0, '系统管理', 'system',     null, 'M', '0', '', 'xy_setting',      3, 0, '系统管理目录'),
              (20110, 2, -1, 20100, '字典管理', 'dict',   'system/system/dict/index',        'C', '0',  'system:dict:list',      'xy_dict',   1, 0, '字典管理菜单'),
                                   -- 字典管理按钮
                                   (20111, 2, -1, 20110, '字典查询', '#', '', 'F', '0', 'system:dict:query',          '#', 1, 0, ''),
                                   (20112, 2, -1, 20110, '字典新增', '#', '', 'F', '0', 'system:dict:add',            '#', 2, 0, ''),
                                   (20113, 2, -1, 20110, '字典修改', '#', '', 'F', '0', 'system:dict:edit',           '#', 3, 0, ''),
                                   (20114, 2, -1, 20110, '字典删除', '#', '', 'F', '0', 'system:dict:remove',         '#', 4, 0, ''),
                                   (20115, 2, -1, 20110, '字典导出', '#', '', 'F', '0', 'system:dict:export',         '#', 5, 0, ''),
              -- 二级菜单
              (20120, 2, -1, 20100, '参数管理', 'config', 'system/system/config/index',      'C', '0',  'system:config:list',    'xy_config', 2, 0, '参数管理菜单'),
                                   -- 参数设置按钮
                                   (20121, 2, -1, 20120, '参数查询', '#', '', 'F', '0', 'system:config:query',        '#', 1, 0, ''),
                                   (20122, 2, -1, 20120, '参数新增', '#', '', 'F', '0', 'system:config:add',          '#', 2, 0, ''),
                                   (20123, 2, -1, 20120, '参数修改', '#', '', 'F', '0', 'system:config:edit',         '#', 3, 0, ''),
                                   (20124, 2, -1, 20120, '参数删除', '#', '', 'F', '0', 'system:config:remove',       '#', 4, 0, ''),
                                   (20125, 2, -1, 20120, '参数导出', '#', '', 'F', '0', 'system:config:export',       '#', 5, 0, ''),
       -- 一级菜单
       (20200, 2, -1, 0, '系统监控', 'monitor',    null, 'M', '0', '', 'xy_monitor',         4, 0, '系统监控目录'),
              -- 二级菜单
              (20210, 2, -1, 20200, 'Sentinel控制台', 'http://localhost:8718',      '',      'C', '0', 'monitor:sentinel:list', 'xy_sentinel', 3, 0, '流量控制菜单'),
              -- 二级菜单
              (20220, 2, -1, 20200, 'Nacos控制台',    'http://localhost:8848/nacos', '',     'C', '0', 'monitor:nacos:list',    'xy_nacos',  4, 0, '服务治理菜单'),
              -- 二级菜单
              (20230, 2, -1, 20200, 'Admin控制台',    'http://localhost:9100/login', '',     'C', '0', 'monitor:server:list',   'xy_server', 5, 0, '服务监控菜单'),
       -- 一级菜单
       (20300, 2, -1, 0, '系统工具', 'tool',       null, 'M', '0', '', 'xy_tool',     5, 0, '系统工具目录'),
              -- 二级菜单
              (20310, 2, -1, 20300, '表单构建', 'build',  'tool/build/index',                'C', '0', 'tool:build:list',       'xy_build',   1, 0, '表单构建菜单'),
              -- 二级菜单
              (20320, 2, -1, 20300, '代码生成', 'gen',    'tool/gen/index',                  'C', '0', 'tool:gen:list',         'xy_code',    2, 0, '代码生成菜单'),
                                   -- 代码生成按钮
                                   (20321, 2, -1, 20320, '生成查询', '#', '', 'F', '0', 'tool:gen:query',             '#', 1, 0, ''),
                                   (20322, 2, -1, 20320, '生成修改', '#', '', 'F', '0', 'tool:gen:edit',              '#', 2, 0, ''),
                                   (20323, 2, -1, 20320, '生成删除', '#', '', 'F', '0', 'tool:gen:remove',            '#', 3, 0, ''),
                                   (20324, 2, -1, 20320, '导入代码', '#', '', 'F', '0', 'tool:gen:import',            '#', 4, 0, ''),
                                   (20325, 2, -1, 20320, '预览代码', '#', '', 'F', '0', 'tool:gen:preview',           '#', 5, 0, ''),
                                   (20326, 2, -1, 20320, '生成代码', '#', '', 'F', '0', 'tool:gen:code',              '#', 6, 0, ''),
              -- 二级菜单
              (20330, 2, -1, 20300, '系统接口', 'http://localhost:8080/swagger-ui/index.html', '', 'C', '0', 'tool:swagger:list',     'xy_swagger', 3, 0, '系统接口菜单');

-- ----------------------------
-- 8、字典类型表
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
       (13, '读写类型', 'sys_tenant_read_type', 0, '读写类型列表'),
       (14, '数据源类型', 'sys_tenant_resource_type', 0, '数据源类型列表'),
       (15, '配置类型', 'sys_tenant_configuration_type', 0, '配置类型列表'),
       (16, '空间范围', 'sys_public_choice', 0, '空间范围列表');


-- ----------------------------
-- 9、字典数据表
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
       (36, 1, '读&写', '0', 'sys_tenant_read_type', '', '', 'N', 0, '读&写'),
       (37, 2, '只读', '1', 'sys_tenant_read_type', '', '', 'N', 0, '只读'),
       (38, 3, '只写', '2', 'sys_tenant_read_type', '', '', 'N', 0, '只写'),
       (39, 1, '子数据源', '0', 'sys_tenant_resource_type', '', '', 'N', 0, '子数据源'),
       (40, 2, '主数据源', '1', 'sys_tenant_resource_type', '', '', 'N', 0, '主数据源'),
       (41, 1, '自动配置', '0', 'sys_tenant_configuration_type', '', '', 'N', 0, '自动配置'),
       (42, 2, '手动配置', '1', 'sys_tenant_configuration_type', '', '', 'N', 0, '手动配置'),
       (43, 0, '私有', '0', 'sys_public_choice', '', '', 'N', 0, '私有'),
       (44, 1, '公共', '1', 'sys_public_choice', '', '', 'N', 0, '公共');

-- ----------------------------
-- 10、参数配置表
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
-- 11、定时任务调度表
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
-- 12、定时任务调度日志表
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
-- 13、代码生成业务表
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
-- 14、代码生成业务表字段
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