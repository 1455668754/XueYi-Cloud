-- ----------------------------
-- 1、租户信息表
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
  is_lessor                 char(1)             not null default 'N'	                comment '超管租户（Y是 N否）',
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
insert into xy_tenant (tenant_id, is_lessor, is_change, strategy_id,  tenant_name, tenant_system_name, tenant_nick, tenant_logo)
values (-1, 'Y', 'Y', 1, 'administrator', '租户管理系统', 'xueYi1', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg'),
       ( 1, 'N', 'N', 1, 'xueYi', '雪忆管理系统', 'xueYi1', 'https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg');

-- ----------------------------
-- 2、策略信息表
-- ----------------------------
drop table if exists xy_tenant_strategy;
create table xy_tenant_strategy (
  strategy_id		        bigint	            not null                                comment '策略Id',
  name                      varchar(500)	    not null default ''	                    comment '策略名称',
  tenant_amount		        int unsigned        not null default 0	                    comment '租户数量',
  source_amount		        int unsigned        not null default 0	                    comment '数据源数量',
  is_change                 char(1)             not null default 'N'	                comment '系统策略（Y是 N否）',
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
-- 初始化-策略信息表数据
-- ----------------------------
insert into xy_tenant_strategy(strategy_id, name, tenant_amount, source_amount, is_change, sort)
values (1, '默认注册策略', 2, 1, 'Y', 1);

-- ----------------------------
-- 3、数据源表|管理系统数据源信息 | 主库有且只能有一个，用途：主要用于存储公共数据，具体看后续文档或视频
-- ----------------------------
drop table if exists xy_tenant_source;
create table xy_tenant_source (
  source_id		            bigint	            not null                                comment '数据源Id',
  name		                varchar(50)	        not null                                comment '数据源名称',
  database_type             char(1)	            not null default '0'	                comment '数据源类型（0子数据源 1主数据源）',
  is_change                 char(1)             not null default 'N'	                comment '系统主源（Y是 N否）',
  slave		                varchar(500)	    not null default ''	                    comment '数据源编码',
  driver_class_name		    varchar(500)	    not null default ''	                    comment '驱动',
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
insert into xy_tenant_source(source_id, name, database_type, is_change, slave, driver_class_name, url_prepend, url_append, username, password, type)
values (1, '注册数据源', '0', 'Y', 'slave', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://localhost:3306/xy-cloud', '?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8', 'root', 'password', '0');

-- ----------------------------
-- 4、主从库关联表  写1-n读
-- ----------------------------
drop table if exists xy_tenant_separation;
create table xy_tenant_separation (
  write_id		            bigint	            not null                                comment '写源Id',
  write_slave		        varchar(50)	        not null	                            comment '写源编码',
  read_id		            bigint	            not null                                comment '读源Id',
  read_slave		        varchar(50)	        not null	                            comment '读源编码',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (read_id)
) engine=innodb comment = '主从库关联表';

-- ----------------------------
-- 初始化-主从库关联表数据
-- ----------------------------
insert into xy_tenant_separation(write_id, write_slave, read_id, read_slave)
values (1,'slave', 1, 'slave');

-- ----------------------------
-- 5、策略-数据源关联表  策略n-n写数据源 | 数据源仅为写|读写的类型
-- ----------------------------
drop table if exists xy_tenant_strategy_source;
create table xy_tenant_strategy_source (
  strategy_id		        bigint	            not null                                comment '策略Id',
  source_id		            bigint	            not null                                comment '数据源Id',
  is_main		            char(1)	            not null default 'N'                    comment '主数据源（Y是 N否）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
primary key (strategy_id, source_id)
) engine=innodb comment = '策略-数据源关联表';

-- ----------------------------
-- 初始化-策略-数据源关联表数据
-- ----------------------------
insert into xy_tenant_strategy_source(strategy_id, source_id, is_main)
values (1, 1, 'Y');

-- ----------------------------
-- 6、模块信息表|管理子系统
-- ----------------------------
drop table if exists xy_system;
create table xy_system (
  system_id		            bigint	            not null                                comment '模块Id',
  name		                varchar(50)	        not null	                            comment '模块名称',
  image_url                 varchar(5000)	    default null 	        	            comment '图片地址',
  route                     varchar(500)        not null	                            comment '跳转路由|链接',
  is_common                 char(1)             not null default 'N'	                comment '公共模块（Y是 N否）',
  is_change                 char(1)             not null default 'N'	                comment '系统模块（Y是 N否）',
  type		                char(1)	            not null default '1'	                comment '跳转类型（0内部跳转 1外部跳转）',
  is_new		            char(1)	            not null default 'Y'	                comment '跳转新页（Y是 N否）',
  visible		            char(1)	            not null default 'Y'	                comment '页面展示（Y是 N否）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (system_id)
) engine=innodb comment = '模块信息表';

# ----------------------------
# 初始化-模块信息表数据
# ----------------------------
insert into xy_system (system_id, name, is_common, is_change, visible, image_url, route, remark, tenant_id)
values (0, '默认系统' ,    'Y', 'Y', 'N', '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}', '', '默认系统', 0),
       (2, '租户管理系统' , 'N', 'Y', 'Y', '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141601_d68e92a4_7382127.jpeg","hiddenVisible":false}', 'http://localhost:81' , '雪忆租户管理系统', -1);

insert into xy_system (system_id, name, image_url, route, remark, tenant_id)
values (1 , '商城' , '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}' , '1' , '轻松打造在线商城', 0),
       (3 , '系统3' , '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}' , '1' , '雪忆多租户系统', 1),
       (4 , '系统4' , '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}' , '1' , '雪忆多租户系统', 1),
       (5 , '系统5' , '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}' , '1' , '雪忆多租户系统', 1),
       (6 , '系统6' , '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}' , '1' , '雪忆多租户系统', 1),
       (7 , '系统7' , '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}' , '1' , '雪忆多租户系统', 1),
       (8 , '系统8' , '{"materialId":"1","materialNick":"1.jpg","materialUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","materialOriginalUrl":"https://images.gitee.com/uploads/images/2021/1101/141155_f3dfce1d_7382127.jpeg","hiddenVisible":false}' , '1' , '雪忆多租户系统', 1);

-- ----------------------------
-- 7、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id                   bigint              not null                                comment '菜单Id',
  parent_id                 bigint              default 0                               comment '父菜单Id',
  name                      varchar(50)         not null                                comment '菜单名称',
  path                      varchar(200)        default ''                              comment '路由地址',
  component                 varchar(255)        default null                            comment '组件路径',
  query                     varchar(255)        default null                            comment '路由参数',
  is_common                 char(1)             not null default 'N'	                comment '公共菜单（Y是 N否）',
  is_change                 char(1)             not null default 'N'	                comment '系统菜单（Y是 N否）',
  is_frame                  char(1)             not null default 'N'                    comment '是否为外链（Y是 N否）',
  is_cache                  char(1)             not null default 'Y'                    comment '是否缓存（Y缓存 N不缓存）',
  menu_type                 char(1)             default ''                              comment '菜单类型（M目录 C菜单 F按钮）',
  visible                   char(1)             not null default 'Y'                    comment '菜单状态（Y显示 N隐藏）',
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
  system_id                 bigint              not null                                comment '系统Id',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (menu_id)
) engine=innodb comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
insert into sys_menu (menu_id, system_id, tenant_id, parent_id, name, path, component, query, is_common, is_change, is_frame, is_cache, menu_type, visible, perms, icon, sort, create_by, remark)
values
-- 系统Id 0 租户Id 0(公用)
       -- 一级菜单
       (10100, 0, 0, 0, '组织管理', 'organize',   null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_organization', 1, 0, '组织管理目录'),
              -- 二级菜单
              (10110, 0, 0, 10100, '部门管理', 'dept',   'system/organize/dept/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:dept:list',      'xy_dept',   1, 0, '部门管理菜单'),
                                   -- 部门管理按钮
                                   (10111, 0, 0, 10110, '部门查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:query',           '#', 1, 0, ''),
                                   (10112, 0, 0, 10110, '部门新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:add',             '#', 2, 0, ''),
                                   (10113, 0, 0, 10110, '部门修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:edit',            '#', 3, 0, ''),
                                   (10114, 0, 0, 10110, '部门删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:dept:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (10120, 0, 0, 10100, '岗位管理', 'post',   'system/organize/post/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:post:list',      'xy_post',   2, 0, '岗位管理菜单'),
                                   -- 岗位管理按钮
                                   (10121, 0, 0, 10120, '岗位查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:query',           '#', 1, 0, ''),
                                   (10122, 0, 0, 10120, '岗位新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:add',             '#', 2, 0, ''),
                                   (10123, 0, 0, 10120, '岗位修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:edit',            '#', 3, 0, ''),
                                   (10124, 0, 0, 10120, '岗位删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:remove',          '#', 4, 0, ''),
                                   (10125, 0, 0, 10120, '岗位导出', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:post:export',          '#', 5, 0, ''),
              -- 二级菜单
              (10130, 0, 0, 10100, '用户管理', 'user',   'system/organize/user/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:user:list',      'xy_user',   3, 0, '用户管理菜单'),
                                   -- 用户管理按钮
                                   (10131, 0, 0, 10130, '用户查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:query',           '#', 1, 0, ''),
                                   (10132, 0, 0, 10130, '用户新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:add',             '#', 2, 0, ''),
                                   (10133, 0, 0, 10130, '用户修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:edit',            '#', 3, 0, ''),
                                   (10134, 0, 0, 10130, '用户删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:remove',          '#', 4, 0, ''),
                                   (10135, 0, 0, 10130, '用户导出', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:export',          '#', 5, 0, ''),
                                   (10136, 0, 0, 10130, '用户导入', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:import',          '#', 6, 0, ''),
                                   (10137, 0, 0, 10130, '重置密码', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:user:resetPwd',        '#', 7, 0, ''),
       -- 一级菜单
       (10200, 0, 0, 0, '企业账户', 'enterprise', null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_enterprise',   2, 0, '企业账户目录'),
              (10210, 0, 0, 10200, '资料管理', 'dict',   'system/dataSetting/enterprise/profile/index',        '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:enterprise:list',      'xy_dict',   1, 0, '资料管理菜单'),
                                   -- 字典管理按钮
                                   (10211, 0, 0, 10210, '资料查看权限', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:enterprise:list',      '#', 1, 0, ''),
                                   (10212, 0, 0, 10210, '普通操作权限', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:enterprise:edit',      '#', 2, 0, ''),
                                   (10213, 0, 0, 10210, '超管操作权限', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:enterpriseAdmin:edit', '#', 3, 0, ''),
       -- 一级菜单
       (10300, 0, 0, 0, '系统管理', 'system',     null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_setting',      3, 0, '系统管理目录'),
              -- 二级菜单
              (10310, 0, 0, 10300, '通知公告', 'notice', 'system/system/notice/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y',  'system:notice:list',    'xy_message', 3, 0, '通知公告菜单'),
                                   -- 通知公告按钮
                                   (10311, 0, 0, 10310, '公告查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:query',        '#', 1, 0, ''),
                                   (10312, 0, 0, 10310, '公告新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:add',          '#', 2, 0, ''),
                                   (10313, 0, 0, 10310, '公告修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:edit',         '#', 3, 0, ''),
                                   (10314, 0, 0, 10310, '公告删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:notice:remove',       '#', 4, 0, ''),
              -- 二级菜单
              (10320, 0, 0, 10300, '日志管理', 'log',    '',                                '', 'Y', 'N', 'N', 'N', 'M', 'Y',  '',                      'xy_log',    4, 0, '日志管理菜单'),
                     -- 三级菜单
                     (10321, 0, 0, 10320, '操作日志', 'operlog',   'system/system/journal/operlog/index',    '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:operlog:list',   'xy_log_operation',  1, 0, '操作日志菜单'),
                                   -- 操作日志按钮
                                   (10322, 0, 0, 10321, '操作查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:operlog:query',       '#', 1, 0, ''),
                                   (10323, 0, 0, 10321, '操作删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:operlog:remove',      '#', 2, 0, ''),
                                   (10324, 0, 0, 10321, '日志导出', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:operlog:export',      '#', 3, 0, ''),
                     -- 三级菜单
                     (10325, 0, 0, 10320, '登录日志', 'loginInfo', 'system/system/journal/loginInfo/index',  '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:loginInfo:list', 'xy_log_loginInfo',  2, 0, '登录日志菜单'),
                                   -- 登录日志按钮
                                   (10326, 0, 0, 10325, '登录查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:loginInfo:query',     '#', 1, 0, ''),
                                   (10327, 0, 0, 10325, '登录删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:loginInfo:remove',    '#', 2, 0, ''),
                                   (10328, 0, 0, 10325, '日志导出', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:loginInfo:export',    '#', 3, 0, ''),
       -- 一级菜单
       (10400, 0, 0, 0, '权限管理', 'authority',  null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_authority',    3, 0, '权限管理目录'),
              -- 二级菜单
              (10410, 0, 0, 10400, '角色管理', 'role',   'system/authority/role/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:role:list',      'xy_role',    1, 0, '角色管理菜单'),
                                   -- 角色管理按钮
                                   (10411, 0, 0, 10410, '角色查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:query',           '#', 1, 0, ''),
                                   (10412, 0, 0, 10410, '角色授权', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:set',             '#', 2, 0, ''),
                                   (10413, 0, 0, 10410, '角色新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:add',             '#', 3, 0, ''),
                                   (10414, 0, 0, 10410, '角色修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:edit',            '#', 4, 0, ''),
                                   (10415, 0, 0, 10410, '角色删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:remove',          '#', 5, 0, ''),
                                   (10416, 0, 0, 10410, '角色导出', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:role:export',          '#', 6, 0, ''),
              -- 二级菜单
              (10420, 0, 0, 10400, '菜单管理', 'menu',   'system/authority/menu/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:menu:list',      'xy_menu',    2, 0, '菜单管理菜单'),
                                   -- 菜单管理按钮
                                   (10421, 0, 0, 10420, '菜单查询', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:query',           '#', 1, 0, ''),
                                   (10422, 0, 0, 10420, '菜单新增', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:add',             '#', 2, 0, ''),
                                   (10423, 0, 0, 10420, '菜单修改', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:edit',            '#', 3, 0, ''),
                                   (10424, 0, 0, 10420, '菜单删除', '', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:menu:remove',          '#', 4, 0, ''),
              -- 二级菜单
              (10430, 0, 0, 10400, '模块管理', 'system', 'system/authority/system/index',   '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'system:system:list',    'xy_system',  3, 0, '模块管理菜单'),
                                   -- 模块管理按钮
                                   (10431, 0, 0, 10430, '模块查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:query',    '#', 1, 0, ''),
                                   (10432, 0, 0, 10430, '模块新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:add',      '#', 2, 0, ''),
                                   (10433, 0, 0, 10430, '模块修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:edit',     '#', 3, 0, ''),
                                   (10434, 0, 0, 10430, '模块删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:system:remove',   '#', 4, 0, ''),
       -- 一级菜单
       (10500, 0, 0, 0, '系统监控', 'monitor',    null, '', 'Y', 'N', 'N', 'N', 'M', 'Y', '', 'xy_monitor',         4, 0, '系统监控目录'),
              -- 二级菜单
              (10510, 0, 0, 10500, '在线用户', 'online', 'system/monitor/online/index',     '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'monitor:online:list',   'xy_online',  1, 0, '在线用户菜单'),
                                   -- 在线用户按钮
                                   (10511, 0, 0, 10510, '在线查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:online:query',       '#', 1, 0, ''),
                                   (10512, 0, 0, 10510, '批量强退', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:online:batchLogout', '#', 2, 0, ''),
                                   (10513, 0, 0, 10510, '单条强退', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:online:forceLogout', '#', 3, 0, ''),

              -- 二级菜单
              (10520, 0, 0, 10500, '定时任务', 'job',    'system/monitor/job/index',        '', 'Y', 'N', 'N', 'N', 'C', 'Y', 'monitor:job:list',      'xy_job',     2, 0, '定时任务菜单'),
                                   -- 定时任务按钮
                                   (10521, 0, 0, 10520, '任务查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:query',          '#', 1, 0, ''),
                                   (10522, 0, 0, 10520, '任务新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:add',            '#', 2, 0, ''),
                                   (10523, 0, 0, 10520, '任务修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:edit',           '#', 3, 0, ''),
                                   (10524, 0, 0, 10520, '任务删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:remove',         '#', 4, 0, ''),
                                   (10525, 0, 0, 10520, '状态修改', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:changeStatus',   '#', 5, 0, ''),
                                   (10526, 0, 0, 10520, '任务导出', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'monitor:job:export',         '#', 6, 0, ''),
       -- 一级菜单
       (10600, 0, 0, 0, '素材管理', null,       null, '', 'Y', 'N', 'N', 'N', 'M', 'N', '', 'xy_tool',     9, 0, '素材管理目录'),
                                   -- 素材模块按钮
                                   (10601, 0, 0, 10600, '素材查询', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:query',        '#', 1, 0, ''),
                                   (10602, 0, 0, 10600, '素材新增', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:add',          '#', 2, 0, ''),
                                   (10603, 0, 0, 10600, '素材编辑', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:edit',         '#', 3, 0, ''),
                                   (10604, 0, 0, 10600, '素材删除', '#', '', '', 'Y', 'N', 'N', 'N', 'F', 'N', 'system:material:remove',       '#', 4, 0, ''),

-- 系统Id 2 租户Id -1(超管)
       -- 一级菜单
       (20000, 2, -1, 0, '租户管理', 'tenant',      null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_tenant',     6, 0, '租户管理目录'),
              -- 二级菜单
              (20010, 2, -1, 20000, '租户管理', 'tenant',    'tenant/tenant/index',  '', 'N', 'N', 'N', 'N', 'C', 'Y', 'tenant:tenant:list',    'xy_tenant',    1, 0, '租户管理菜单'),
                                   -- 租户管理按钮
                                   (20011, 2, -1, 20010, '租户查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:query',          '#', 1, 0, ''),
                                   (20012, 2, -1, 20010, '租户新增', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:add',            '#', 2, 0, ''),
                                   (20013, 2, -1, 20010, '租户修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:edit',           '#', 3, 0, ''),
                                   (20014, 2, -1, 20010, '菜单配置', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:role',           '#', 4, 0, ''),
                                   (20015, 2, -1, 20010, '租户删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:tenant:remove',         '#', 5, 0, ''),
                     -- 二级菜单
              (20050, 2, -1, 20000, '策略管理', 'strategy',   'tenant/strategy/index',     '', 'N', 'N', 'N', 'N', 'C', 'Y',  'tenant:strategy:list',      'xy_strategy',   1, 0, '数据源策略菜单'),
                                   -- 数据源策略按钮
                                   (20051, 2, -1, 20050, '策略查询', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:query',           '#', 1, 0, ''),
                                   (20052, 2, -1, 20050, '策略新增', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:add',             '#', 2, 0, ''),
                                   (20053, 2, -1, 20050, '策略修改', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:edit',            '#', 3, 0, ''),
                                   (20054, 2, -1, 20050, '策略删除', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:remove',          '#', 4, 0, ''),
                                   (20055, 2, -1, 20050, '策略导出', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:strategy:export',          '#', 5, 0, ''),
              -- 二级菜单
              (20020, 2, -1, 20000, '数据源管理', 'source',   'tenant/source/index',     '', 'N', 'N', 'N', 'N', 'C', 'Y',  'tenant:source:list',      'xy_source',   1, 0, '数据源菜单'),
                                   -- 数据源管理按钮
                                   (20021, 2, -1, 20020, '数据源查询', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:query',           '#', 1, 0, ''),
                                   (20022, 2, -1, 20020, '数据源新增', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:add',             '#', 2, 0, ''),
                                   (20023, 2, -1, 20020, '数据源修改', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:edit',            '#', 3, 0, ''),
                                   (20024, 2, -1, 20020, '数据源配置', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:separation:edit',        '#', 4, 0, ''),
                                   (20025, 2, -1, 20020, '数据源删除', '', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tenant:source:remove',          '#', 5, 0, ''),
       -- 一级菜单
       (20100, 2, -1, 0, '系统管理', 'system',     null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_setting',      3, 0, '系统管理目录'),
              (20110, 2, -1, 20100, '字典管理', 'dict',   'system/system/dict/index',        '', 'N', 'N', 'N', 'N', 'C', 'Y',  'system:dict:list',      'xy_dict',   1, 0, '字典管理菜单'),
                                   -- 字典管理按钮
                                   (20111, 2, -1, 20110, '字典查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:query',          '#', 1, 0, ''),
                                   (20112, 2, -1, 20110, '字典新增', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:add',            '#', 2, 0, ''),
                                   (20113, 2, -1, 20110, '字典修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:edit',           '#', 3, 0, ''),
                                   (20114, 2, -1, 20110, '字典删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:remove',         '#', 4, 0, ''),
                                   (20115, 2, -1, 20110, '字典导出', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:dict:export',         '#', 5, 0, ''),
              -- 二级菜单
              (20120, 2, -1, 20100, '参数管理', 'config', 'system/system/config/index',     '', 'N', 'N', 'N', 'N', 'C', 'Y',  'system:config:list',    'xy_config', 2, 0, '参数管理菜单'),
                                   -- 参数设置按钮
                                   (20121, 2, -1, 20120, '参数查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:query',        '#', 1, 0, ''),
                                   (20122, 2, -1, 20120, '参数新增', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:add',          '#', 2, 0, ''),
                                   (20123, 2, -1, 20120, '参数修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:edit',         '#', 3, 0, ''),
                                   (20124, 2, -1, 20120, '参数删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:remove',       '#', 4, 0, ''),
                                   (20125, 2, -1, 20120, '参数导出', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'system:config:export',       '#', 5, 0, ''),
       -- 一级菜单
       (20200, 2, -1, 0, '系统监控', 'monitor',    null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_monitor',         4, 0, '系统监控目录'),
              -- 二级菜单
              (20210, 2, -1, 20200, 'Sentinel控制台', 'http://localhost:8718',      '',     '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:sentinel:list', 'xy_sentinel', 1, 0, '流量控制菜单'),
              -- 二级菜单
              (20220, 2, -1, 20200, 'Nacos控制台',    'http://localhost:8848/nacos', '',     '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:nacos:list',    'xy_nacos',    2, 0, '服务治理菜单'),
              -- 二级菜单
              (20230, 2, -1, 20200, 'Admin控制台',    'http://localhost:9100/login', '',     '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:server:list',   'xy_server',   3, 0, '服务监控菜单'),
              -- 二级菜单
              (20240, 2, -1, 20200, 'rabbit控制台',   'http://localhost:15672/#/', '',       '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'monitor:rabbitmq:list',  'xy_rabbit',  4, 0, '消息队列菜单'),
       -- 一级菜单
       (20300, 2, -1, 0, '系统工具', 'tool',       null, '', 'N', 'N', 'N', 'N', 'M', 'Y', '', 'xy_tool',     5, 0, '系统工具目录'),
              -- 二级菜单
              (20310, 2, -1, 20300, '表单构建', 'build',  'tool/build/index',                '', 'N', 'N', 'N', 'N', 'C', 'Y', 'tool:build:list',       'xy_build',   1, 0, '表单构建菜单'),
              -- 二级菜单
              (20320, 2, -1, 20300, '代码生成', 'gen',    'tool/gen/index',                  '', 'N', 'N', 'N', 'N', 'C', 'Y', 'tool:gen:list',         'xy_code',    2, 0, '代码生成菜单'),
                                   -- 代码生成按钮
                                   (20321, 2, -1, 20320, '生成查询', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:query',             '#', 1, 0, ''),
                                   (20322, 2, -1, 20320, '生成修改', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:edit',              '#', 2, 0, ''),
                                   (20323, 2, -1, 20320, '生成删除', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:remove',            '#', 3, 0, ''),
                                   (20324, 2, -1, 20320, '导入代码', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:import',            '#', 4, 0, ''),
                                   (20325, 2, -1, 20320, '预览代码', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:preview',           '#', 5, 0, ''),
                                   (20326, 2, -1, 20320, '生成代码', '#', '', '', 'N', 'N', 'N', 'N', 'F', 'N', 'tool:gen:code',              '#', 6, 0, ''),
              -- 二级菜单
              (20330, 2, -1, 20300, '系统接口', 'http://localhost:8080/swagger-ui/index.html', '', '', 'N', 'N', 'Y', 'N', 'C', 'Y', 'tool:swagger:list',     'xy_swagger', 3, 0, '系统接口菜单');

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
       (13, '读写类型', 'sys_source_type', 0, '读写类型列表'),
       (14, '数据源类型', 'sys_database_type', 0, '数据源类型列表'),
       (15, '配置类型', 'sys_tenant_configuration_type', 0, '配置类型列表'),
       (16, '公共是否', 'sys_common_type', 0, '公共是否列表'),
       (17, '数据范围', 'sys_data_scope', 0, '公数据范围列表');

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
       (36, 1, '读&写', '0', 'sys_source_type', '', 'primary', 'Y', 0, '读&写'),
       (37, 2, '只读', '1', 'sys_source_type', '', 'success', 'N', 0, '只读'),
       (38, 3, '只写', '2', 'sys_source_type', '', 'warning', 'N', 0, '只写'),
       (39, 1, '子数据源', '0', 'sys_database_type', '', 'success', 'Y', 0, '子数据源'),
       (40, 2, '主数据源', '1', 'sys_database_type', '', 'danger', 'N', 0, '主数据源'),
       (41, 1, '自动配置', '0', 'sys_tenant_configuration_type', '', '', 'N', 0, '自动配置'),
       (42, 2, '手动配置', '1', 'sys_tenant_configuration_type', '', '', 'N', 0, '手动配置'),
       (43, 0, '公共', 'Y', 'sys_common_type', '', '', 'N', 0, '公共'),
       (44, 1, '私有', 'N', 'sys_common_type', '', '', 'N', 0, '私有'),
       (45, 0, '全部数据权限', '1', 'sys_data_scope', '', '', 'Y', 0, '全部数据权限'),
       (46, 1, '自定数据权限', '2', 'sys_data_scope', '', '', 'N', 0, '自定数据权限'),
       (47, 2, '本部门数据权限', '3', 'sys_data_scope', '', '', 'N', 0, '本部门数据权限'),
       (48, 3, '本部门及以下数据权限', '4', 'sys_data_scope', '', '', 'N', 0, '本部门及以下数据权限'),
       (49, 4, '本岗位数据权限', '5', 'sys_data_scope', '', '', 'N', 0, '本岗位数据权限'),
       (50, 5, '仅本人数据权限', '6', 'sys_data_scope', '', '', 'N', 0, '仅本人数据权限');

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
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (config_id)
) engine=innodb comment = '参数配置表';

insert into sys_config (config_id, config_name, config_key, config_value, config_type, remark, tenant_id)
values (1, '主框架页-默认皮肤样式名称',           'sys.index.skinName',               'skin-blue',       'Y',    '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', 0),
       (2, '用户管理-账号初始密码',              'sys.user.initPassword',            '123456',          'Y',    '初始化密码 123456', 0),
       (3, '主框架页-侧边栏主题',                'sys.index.sideTheme',              'theme-dark',      'Y',    '深色主题theme-dark，浅色主题theme-light', 0),
       (4, '账号自助-是否开启租户注册功能',        'sys.account.registerTenant',       'false',           'Y',    '是否开启注册租户功能（true开启，false关闭）', -1);

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
  tenant_id		            bigint	            not null                                comment '租户Id',
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
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';

-- ----------------------------
-- 13、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id                  bigint(20)          not null auto_increment    comment '编号',
  table_name                varchar(200)        default ''                 comment '表名称',
  table_comment             varchar(500)        default ''                 comment '表描述',
  sub_table_name            varchar(64)         default null               comment '关联子表的表名',
  sub_table_fk_name         varchar(64)         default null               comment '子表关联的外键名',
  class_name                varchar(100)        default ''                 comment '实体类名称',
  prefix                    varchar(100)        default ''                 comment '前缀名称',
  tpl_category              varchar(200)        default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  package_name              varchar(100)                                   comment '生成包路径',
  module_name               varchar(30)                                    comment '生成模块名',
  business_name             varchar(30)                                    comment '生成业务名',
  function_name             varchar(50)                                    comment '生成功能名',
  function_author           varchar(50)                                    comment '生成功能作者',
  gen_type                  char(1)             default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path                  varchar(200)        default '/'                comment '生成路径（不填默认项目路径）',
  options                   varchar(1000)                                  comment '其它生成选项',
  create_by                 varchar(64)         default ''                 comment '创建者',
  create_time 	            datetime                                       comment '创建时间',
  update_by                 varchar(64)         default ''                 comment '更新者',
  update_time               datetime                                       comment '更新时间',
  remark                    varchar(500)        default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 14、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id                 bigint(20)          not null auto_increment    comment '编号',
  table_id                  varchar(64)                                    comment '归属表编号',
  column_name               varchar(200)                                   comment '列名称',
  column_comment            varchar(500)                                   comment '列描述',
  column_type               varchar(100)                                   comment '列类型',
  java_type                 varchar(500)                                   comment 'JAVA类型',
  java_field                varchar(200)                                   comment 'JAVA字段名',
  is_pk                     char(1)                                        comment '是否主键（1是）',
  is_increment              char(1)                                        comment '是否自增（1是）',
  is_required               char(1)                                        comment '是否必填（1是）',
  is_insert                 char(1)                                        comment '是否为插入字段（1是）',
  is_edit                   char(1)                                        comment '是否编辑字段（1是）',
  is_list                   char(1)                                        comment '是否列表字段（1是）',
  is_query                  char(1)                                        comment '是否查询字段（1是）',
  query_type                varchar(200)        default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type                 varchar(200)                                   comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type                 varchar(200)        default ''                 comment '字典类型',
  sort                      int                                            comment '排序',
  create_by                 varchar(64)         default ''                 comment '创建者',
  create_time 	            datetime                                       comment '创建时间',
  update_by                 varchar(64)         default ''                 comment '更新者',
  update_time               datetime                                       comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';