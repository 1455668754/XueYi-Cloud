-- ----------------------------
-- 1、数据源表|管理系统数据源信息 | 主库有且只能有一个，用途：主要用于存储公共数据，具体看后续文档或视频
-- ----------------------------
drop table if exists xy_tenant_source;
create table xy_tenant_source (
  source_id		            bigint	            not null                                comment '数据源Id',
  database_type             char(1)	            not null default '0'	                comment '数据库(0从库 1主库)',
  driver_class_name		    varchar(500)	    not null default 'master'	            comment '驱动',
  url	                    varchar(500)	    not null default ''	                    comment '地址',
  username	                varchar(500)	    not null default ''	                    comment '用户名',
  password	                varchar(500)	    not null default ''	                    comment '密码',
  type		                char(1)	            not null default '0'	                comment '读写类型(0读&写 1只读 2只写)',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
primary key (source_id)
) engine=innodb comment = '数据源表';

-- ----------------------------
-- 初始化-数据源表数据 | 这条数据为我的基础库，实际使用时调整成自己的库即可
-- ----------------------------
insert into xy_tenant_source(source_id, database_type, driver_class_name, url, username, password, type)
values (0, '1', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://localhost:3306/xy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8', 'root', 'password', '0');

-- ----------------------------
-- 2、Nacos配置表|管理Nacos配置信息 | 需要控制多数据源的方法写进此表 | 开启租户控制的模块一定要设置成自动配置
-- ----------------------------
drop table if exists xy_tenant_nacos;
create table xy_tenant_nacos (
  data_id		            bigint	            not null                                comment '配置Id',
  name                      varchar(500)	    not null default ''	                    comment '配置名称',
  prefix_str		        text	    	                                            comment '头部配置信息',
  slave_str		            text	    	                                            comment '数据源配置信息',
  suffix_str		        text	    	                                            comment '尾部配置信息',
  type		                char(1)	            not null default '0'	                comment '配置类型(0自动配置 1手动配置)',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
primary key (data_id)
) engine=innodb comment = 'Nacos配置表';

-- ----------------------------
-- 初始化-数据源表数据 | 这条数据为我的基础库，实际使用时调整成自己的库即可
-- ----------------------------
insert into xy_tenant_nacos(data_id, name, type, sort)
values (1, 'application-dev.yml', '1', 0),
       (2, 'xueyi-gateway-dev.yml', '1', 1),
       (3, 'xueyi-auth-dev.yml', '1', 2),
       (4, 'xueyi-monitor-dev.yml', '1', 3),
       (5, 'xueyi-tenant-dev.yml', '1', 4),
       (6, 'xueyi-system-dev.yml', '0', 5),
       (7, 'xueyi-gen-dev.yml', '1', 6),
       (8, 'xueyi-job-dev.yml', '1', 7),
       (9, 'xueyi-file-dev.yml', '1', 8),
       (10, 'sentinel-xueyi-gateway', '1', 9);

-- ----------------------------
-- 3、读写分离关联表  写1-n读
-- ----------------------------
drop table if exists xy_tenant_separation;
create table xy_tenant_separation (
  write_id		            bigint	            not null                                comment '写数据源Id',
  read_id		            bigint	            not null                                comment '读数据源Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
primary key (write_id, read_id)
) engine=innodb comment = '读写分离关联表';

-- ----------------------------
-- 初始化-读写分离关联表数据
-- ----------------------------
insert into xy_tenant_separation(write_id, read_id)
values (0, 0);

-- ----------------------------
-- 4、数据源策略表|管理数据源策略信息
-- ----------------------------
drop table if exists xy_tenant_strategy;
create table xy_tenant_strategy (
  strategy_id		        bigint	            not null                                comment '策略Id',
  name                      varchar(500)	    not null default ''	                    comment '策略名称',
  amount		            int unsigned        not null default 0	                    comment '数据源数量',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
primary key (strategy_id)
) engine=innodb comment = '数据源策略表';

-- ----------------------------
-- 初始化-数据源策略表数据
-- ----------------------------
insert into xy_tenant_strategy(strategy_id, name, amount, sort)
values (0, '默认策略', 1, 0);

-- ----------------------------
-- 5、策略-数据源关联表  策略n-n写数据源 | 数据源为写|读写的类型
-- ----------------------------
drop table if exists xy_tenant_strategy_source;
create table xy_tenant_strategy_source (
  strategy_id		        bigint	            not null                                comment '策略Id',
  source_id		            bigint	            not null                                comment '数据源Id',
  status		            char(1)	            not null default 'N'                    comment '主数据源(Y是 N否)',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
primary key (strategy_id, source_id)
) engine=innodb comment = '策略-数据源关联表';

-- ----------------------------
-- 初始化-策略-数据源关联表数据
-- ----------------------------
insert into xy_tenant_strategy_source(strategy_id, source_id, status)
values (0, 0, 'Y');

-- ----------------------------
-- 6、策略-租户关联表  策略N-N租户
-- ----------------------------
drop table if exists xy_tenant_strategy_tenant;
create table xy_tenant_strategy_tenant (
  strategy_id		        bigint	            not null                                comment '策略Id',
  tenant_id		            bigint	            not null                                comment '租户Id',
  status		            char(1)	            not null default 'N'	                comment '主策略(Y是 N否)',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
primary key (tenant_id)
) engine=innodb comment = '租户表';

-- ----------------------------
-- 初始化-租户表数据
-- ----------------------------
insert into xy_tenant_strategy_tenant (strategy_id, tenant_id, status)
values (0, 0, 'Y'),
       (0, 1, 'Y'),
       (0, 2, 'Y');

-- ----------------------------
-- 2、租户信息表|管理租户账户信息
-- ----------------------------
drop table if exists xy_tenant;
create table xy_tenant (
  tenant_id		            bigint	            not null                                comment '租户Id',
  tenant_name		        varchar(50)	        not null unique	                        comment '租户账号',
  tenant_system_name		varchar(50)	        not null 	                            comment '系统名称',
  tenant_nick		        varchar(50)	        not null 	                            comment '租户名称',
  tenant_logo		        varchar(1000)	    default ''	                            comment '租户logo',
  tenant_name_frequency     tinyint             default 0                               comment '租户账号修改次数',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  primary key (tenant_id),
  unique key (tenant_name)
) engine=innodb comment = '租户信息表';

-- ----------------------------
-- 初始化-租户信息表数据
-- ----------------------------
insert into xy_tenant (tenant_id, tenant_name, tenant_system_name, tenant_nick, tenant_logo)
values (0, 'administrator', '雪忆管理系统', 'xueYi1', 'http://127.0.0.1:9300/statics/2021/04/02/73c90edd-8d51-4fb9-b61e-06a0b4630d5b.jpg'),
       (1, 'xueYi', '雪忆管理系统', 'xueYi1', 'http://127.0.0.1:9300/statics/2021/04/02/73c90edd-8d51-4fb9-b61e-06a0b4630d5b.jpg'),
       (2, 'xueYi2', '雪忆管理系统', 'xueYi2', 'http://127.0.0.1:9300/statics/2021/04/02/73c90edd-8d51-4fb9-b61e-06a0b4630d5b.jpg');

-- ----------------------------
-- 3、素材信息表|管理素材信息
-- ----------------------------
drop table if exists xy_material;
create table xy_material (
  material_id		        bigint	            not null                                comment '素材Id',
  folder_id		            bigint	            not null default 0	                    comment '分类Id',
  material_nick		        varchar(100)	    not null	                            comment '素材昵称',
  material_name		        varchar(100)	    not null	                            comment '素材名称',
  material_original_name	varchar(100)	    not null	                            comment '原图名称',
  material_url		        varchar(500)	    not null 	                            comment '素材地址',
  material_original_url		varchar(500)	    not null 	                            comment '原图地址',
  material_size		        decimal(8,4)	    not null 	                            comment '素材大小',
  type		                char(1)	            not null default '0'	                comment '素材类型(0默认素材 1系统素材)',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (material_id)
) engine=innodb comment = '素材信息表';

-- ----------------------------
-- 4、素材分类表|管理素材信息分类
-- ----------------------------
drop table if exists xy_material_folder;
create table xy_material_folder (
  folder_id		            bigint	            not null                                comment '分类Id',
  parent_id		            bigint	            not null default 0                      comment '父类Id',
  folder_name		        varchar(100)	    not null	                            comment '分类名称',
  ancestors                 varchar(500)        default ''                              comment '祖级列表',
  type		                char(1)	            not null default '0'	                comment '分类类型(0默认文件夹 1系统文件夹)',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (folder_id)
) engine=innodb comment = '素材分类表';

-- ----------------------------
-- 5、子模块表|管理子系统模块
-- ----------------------------
drop table if exists xy_system;
create table xy_system (
  system_id		            bigint	            not null                                comment '系统Id',
  system_name		        varchar(50)	        not null	                            comment '系统名称',
  image_url                 varchar(5000)	    default null 	        	            comment '图片地址',
  type		                char(1)	            not null default '1'	                comment '跳转类型(0内部跳转 1外部跳转)',
  route                     varchar(500)        not null	                            comment '跳转路由',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key (system_id)
) engine=innodb comment = '子系统模块表';

# ----------------------------
# 初始化-租户信息表数据
# ----------------------------
insert into xy_system (system_id, system_name, image_url, route, remark, tenant_id)
values (2 , '系统2' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (3 , '系统3' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (4 , '系统4' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (5 , '系统5' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (6 , '系统6' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (7 , '系统7' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1),
       (8 , '系统8' , '[{"materialId": "1384755423424516096", "materialUrl": "http://127.0.0.1:9300/statics/2021/04/21/5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "materialNick": "5ec82664-b6cd-48b6-92e5-478d16b61428.jpg", "hiddenVisible": false, "materialOriginalUrl": "http://127.0.0.1:9300/statics/2021/04/21/d90c13a0-11b5-4314-ad20-f05c6ff18497.jpg"}]' , '1' , '雪忆多租户系统', 1);

-- ----------------------------
-- 6、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id                   bigint	            not null                                comment '角色Id',
  role_code                 varchar(64)         not null                                comment '角色编码',
  role_name                 varchar(30)         not null                                comment '角色名称',
  role_key                  varchar(100)        not null                                comment '角色权限字符串',
  data_scope                char(1)             default '1'                             comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：本岗位数据权限  6：仅本人数据权限）',
  menu_check_strictly       tinyint             default 1                               comment '菜单树选择项是否关联显示',
  dept_check_strictly       tinyint             default 1                               comment '部门树选择项是否关联显示',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
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
insert into sys_role (role_id, tenant_id, role_code, role_name, role_key, menu_check_strictly, dept_check_strictly, create_by, remark)
values (1, 1, '001', '超级管理员', 'admin', 1, 1, 1, '超级管理员'),
       (2, 1, '002', '管理员', 'common', 2, 1, 1, '普通角色'),
       (3, 2, '001', '超级管理员', 'admin', 1, 1, 1, '超级管理员');

-- ----------------------------
-- 7、角色和系统-菜单关联表  角色N-N系统-菜单
-- ----------------------------
drop table if exists sys_role_system_menu;
create table sys_role_system_menu (
  role_id                   bigint              not null                                comment '角色Id',
  system_menu_id            bigint              not null                                comment '系统-菜单Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key(role_id, system_menu_id)
) engine=innodb comment = '角色和系统-菜单关联表';

insert into sys_role_system_menu value (1,12005,0,1);
-- ----------------------------
-- 8、角色和部门-岗位关联表  角色N-N部门-岗位
-- ----------------------------
drop table if exists sys_role_dept_post;
create table sys_role_dept_post (
  role_id                   bigint              not null                                comment '角色Id',
  dept_post_id              bigint              not null                                comment '部门-岗位Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key(role_id, dept_post_id)
) engine=innodb comment = '角色和部门-岗位关联表';

insert into sys_role_dept_post value (1,107,0,1);
-- ----------------------------
-- 9、部门和角色关联表  部门N-N角色
-- ----------------------------
drop table if exists sys_dept_role;
create table sys_dept_role (
  dept_id                   bigint              not null                                comment '部门id',
  role_id                   bigint              not null                                comment '角色Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key(dept_id, role_id)
) engine=innodb comment = '部门和角色关联表';

-- ----------------------------
-- 10、岗位和角色关联表  岗位N-N角色
-- ----------------------------
drop table if exists sys_post_role;
create table sys_post_role (
  post_id                   bigint              not null                                comment '岗位Id',
  role_id                   bigint              not null                                comment '角色Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key(post_id, role_id)
) engine=innodb comment = '岗位和角色关联表';

-- ----------------------------
-- 11、用户和角色关联表  用户N-N角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id                   bigint              not null                                comment '用户Id',
  role_id                   bigint              not null                                comment '角色Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id(0默认系统 otherId特定租户专属)',
  primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values (1, 1, 0, 1);
insert into sys_user_role values (2, 2, 0, 1);