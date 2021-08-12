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
  sort                      int unsigned        not null default 0                      comment '显示顺序',
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
insert into sys_dept (dept_id, tenant_id, dept_code, parent_id, ancestors, dept_name)
values (-1, -1, '1', 0, '0', '雪忆科技'),
       (100, 1, '100', 0, '0', '雪忆科技'),
       (101, 1, '101', 100, '0,100', '深圳总公司'),
       (102, 1, '102',  100, '0,100', '长沙分公司'),
       (103, 1, '103',  101, '0,100,101', '研发部门'),
       (104, 1, '104',  101, '0,100,101', '市场部门'),
       (105, 1, '105',  101, '0,100,101', '测试部门'),
       (106, 1, '106',  101, '0,100,101', '财务部门'),
       (107, 1, '107',  101, '0,100,101', '运维部门'),
       (108, 1, '108',  102, '0,100,102', '市场部门'),
       (109, 1, '109',  102, '0,100,102', '财务部门');

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
  sort                      int unsigned        not null default 0                      comment '显示顺序',
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
insert into sys_post (post_id, tenant_id, dept_id, post_code, post_name)
values (-1, -1, -1, 'ceo', '超级管理员'),
       (2, 1, 100, 'ceo', '董事长'),
       (3, 1, 100, 'se', '项目经理'),
       (4, 1, 100, 'hr', '人力资源'),
       (5, 1, 100, 'user', '普通员工');

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
  sort                      int unsigned        not null default 0                      comment '显示顺序',
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
insert into sys_user (user_id, tenant_id, dept_id, post_id, user_code, user_name, nick_name,user_type, password, remark)
values (-1, -1, -1, -1, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员'),
       (2, 1, 100, 2, '001', 'admin', 'admin', '00', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员'),
       (3, 1, 100, 2, '002', 'xy', 'xy', '01', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '管理员');

-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id                   bigint	            not null                                comment '角色Id',
  role_code                 varchar(64)         default null                            comment '角色编码',
  role_name                 varchar(30)         not null                                comment '角色名称',
  role_key                  varchar(100)        default null                            comment '角色权限字符串',
  data_scope                char(1)             default '1'                             comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：本岗位数据权限  6：仅本人数据权限）',
  menu_check_strictly       tinyint             default 1                               comment '菜单树选择项是否关联显示',
  dept_check_strictly       tinyint             default 1                               comment '部门树选择项是否关联显示',
  type		                char(1)	            not null default '0'	                comment '角色类型（0常规 1超管衍生 2租户衍生 3部门衍生 4岗位衍生 5用户衍生）',
  derive_id		            bigint	            default null	                        comment '衍生Id',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id（0默认系统 otherId特定租户专属）',
  primary key (role_id)
) engine=innodb comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role (role_id, role_name, type, derive_id, tenant_id)
values (-1, '超管衍生-1', '1', -1, -1),
       (-2, '租户衍生-1', '2', -1, -1),
       (-3, '用户衍生-1', '5', -1, -1),
       (-4, '岗位衍生-1', '4', -1, -1),
       (-5, '部门衍生-1', '3', -1, -1),
       (3, '超管衍生1', '1', 1, 1),
       (4, '租户衍生1', '2', 1, 1),
       (5, '用户衍生2', '5', 2, 1),
       (6, '用户衍生3', '5', 3, 1),
       (7, '岗位衍生2', '4', 2, 1),
       (8, '岗位衍生3', '4', 3, 1),
       (9, '岗位衍生4', '4', 4, 1),
       (10, '岗位衍生5', '4', 5, 1),
       (11, '部门衍生100', '3', 100, 1),
       (12, '部门衍生101', '3', 101, 1),
       (13, '部门衍生102', '3', 102, 1),
       (14, '部门衍生103', '3', 103, 1),
       (15, '部门衍生104', '3', 104, 1),
       (16, '部门衍生105', '3', 105, 1),
       (17, '部门衍生106', '3', 106, 1),
       (18, '部门衍生107', '3', 107, 1),
       (19, '部门衍生108', '3', 108, 1),
       (20, '部门衍生109', '3', 109, 1);

insert into sys_role (role_id, tenant_id, role_code, role_name, role_key, menu_check_strictly, dept_check_strictly, create_by, remark)
values (1, 1, '001', '超级管理员', 'admin', 1, 1, 1, '超级管理员'),
       (2, 1, '002', '管理员', 'common', 2, 1, 1, '普通角色');

-- ----------------------------
-- 5、角色和系统-菜单关联表  角色N-N系统-菜单
-- ----------------------------
drop table if exists sys_role_system_menu;
create table sys_role_system_menu (
  role_id                   bigint              not null                                comment '角色Id',
  system_menu_id            bigint              not null                                comment '系统-菜单Id',
  type		                char(1)	            not null default '0'	                comment '角色类型（0常规 1衍生 2租户）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id（0默认系统 otherId特定租户专属）',
  primary key(role_id, system_menu_id)
) engine=innodb comment = '角色和系统-菜单关联表';

-- ----------------------------
-- 6、角色和部门-岗位关联表  角色N-N部门-岗位
-- ----------------------------
drop table if exists sys_role_dept_post;
create table sys_role_dept_post (
  role_id                   bigint              not null                                comment '角色Id',
  dept_post_id              bigint              not null                                comment '部门-岗位Id',
  type		                char(1)	            not null default '0'	                comment '角色类型（0常规 1衍生）',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id（0默认系统 otherId特定租户专属）',
  primary key(role_id, dept_post_id)
) engine=innodb comment = '角色和部门-岗位关联表';

-- ----------------------------
-- 7、组织和角色关联表  组织N-N角色
-- ----------------------------
drop table if exists sys_organize_role;
create table sys_organize_role (
  id		                bigint	            not null auto_increment                 comment 'id',
  dept_id                   bigint              default null                            comment '部门id',
  post_id                   bigint              default null                            comment '岗位id',
  user_id                   bigint              default null                            comment '用户id',
  derive_dept_id            bigint              default null                            comment '部门衍生id',
  derive_post_id            bigint              default null                            comment '岗位衍生id',
  derive_user_id            bigint              default null                            comment '用户衍生id',
  derive_tenant_id          bigint              default null                            comment '租户衍生id',
  derive_administrator_id   bigint              default null                            comment '超管衍生id',
  role_id                   bigint              not null                                comment '角色Id',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id（0默认系统 otherId特定租户专属）',
  primary key(id)
  ,unique (dept_id, post_id, user_id, derive_dept_id, derive_post_id, derive_user_id, derive_tenant_id, derive_administrator_id, role_id)
) engine=innodb auto_increment=1 comment = '组织和角色关联表';

-- ----------------------------
-- 初始化-组织和角色关联表数据
-- ----------------------------
insert into sys_organize_role ( derive_administrator_id, role_id, tenant_id )
values (-1, -1, -1),
       ( 1,  3,  1);

insert into sys_organize_role ( derive_tenant_id, role_id, tenant_id )
values (-1, -2, -1),
       ( 1,  4,  1);

insert into sys_organize_role ( derive_user_id, role_id, tenant_id )
values (-1, -3, -1),
       ( 2,  5,  1),
       ( 3,  6,  1);

insert into sys_organize_role ( derive_post_id, role_id, tenant_id )
values (-1, -4, -1),
       ( 2,  7,  1),
       ( 3,  8,  1),
       ( 4,  9,  1),
       ( 5, 10,  1);

insert into sys_organize_role ( derive_dept_id, role_id, tenant_id )
values (-1, -5, -1),
       (100, 11,  1),
       (101, 12,  1),
       (102, 13,  1),
       (103, 14,  1),
       (104, 15,  1),
       (105, 16,  1),
       (106, 17,  1),
       (107, 18,  1),
       (108, 19,  1),
       (109, 20,  1);

-- ----------------------------
-- 8、素材信息表|管理素材信息
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
  type		                char(1)	            not null default '0'	                comment '素材类型（0默认素材 1系统素材）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id（0默认系统 otherId特定租户专属）',
  primary key (material_id)
) engine=innodb comment = '素材信息表';

-- ----------------------------
-- 9、素材分类表|管理素材信息分类
-- ----------------------------
drop table if exists xy_material_folder;
create table xy_material_folder (
  folder_id		            bigint	            not null                                comment '分类Id',
  parent_id		            bigint	            not null default 0                      comment '父类Id',
  folder_name		        varchar(100)	    not null	                            comment '分类名称',
  ancestors                 varchar(500)        default ''                              comment '祖级列表',
  type		                char(1)	            not null default '0'	                comment '分类类型（0默认文件夹 1系统文件夹）',
  sort                      int unsigned        not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  del_flag		            tinyint             not null default 0                      comment '删除标志（0正常 1删除）',
  tenant_id		            bigint	            not null                                comment '租户Id（0默认系统 otherId特定租户专属）',
  primary key (folder_id)
) engine=innodb comment = '素材分类表';

-- ----------------------------
-- 10、操作日志记录
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
-- 11、系统访问记录
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
-- 12、通知公告表
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