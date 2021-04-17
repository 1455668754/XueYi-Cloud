-- ----------------------------
-- 1、任务表 | 税票系统
-- ----------------------------
drop table if exists tr_task;
create table tr_task (
  task_id                   bigint              not null auto_increment                 comment '任务Id',
  task_type                 bigint              not null                                comment '任务类型Id',
  task_code                 varchar(64)         not null                                comment '任务编码',
  task_name                 varchar(30)         not null                                comment '任务名称',
  task_amount               decimal(12,2)       default 0                               comment '任务金额',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (task_id),
  unique (tenant_id, task_code, del_flag)
) engine=innodb auto_increment=1 comment = '任务表';

-- ----------------------------
-- 2、任务类型表 | 税票系统
-- ----------------------------
drop table if exists tr_task_type;
create table tr_task_type (
  type_id                   bigint              not null auto_increment                 comment '任务类型Id',
  type_name                 varchar(30)         not null                                comment '任务类型名称',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (type_id),
  unique (tenant_id, type_name, del_flag)
) engine=innodb auto_increment=1 comment = '任务类型表';

-- ----------------------------
-- 初始化-任务类型表
-- ----------------------------
insert into tr_task_type (tenant_id, type_name) values (1, '标准任务');

-- ----------------------------
-- 3、自由职业者表 | 税票系统
-- ----------------------------
drop table if exists tr_freelance;
create table tr_freelance (
  freelance_id              bigint              not null auto_increment                 comment '自由职业者Id',
  contract_id		        bigint	            not null                                comment '合同Id',
  name                      varchar(30)         not null                                comment '姓名',
  IDCard                    varchar(30)         not null                                comment '身份证号',
  phone                     varchar(30)         not null                                comment '手机号',
  bank_card                 varchar(30)         default null                            comment '银行卡号',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0未签约 1已发合同，待签约 2已签约）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (freelance_id),
  unique (tenant_id, IDCard, del_flag)
) engine=innodb auto_increment=1 comment = '自由职业者表';

-- ----------------------------
-- 4、合同表 | 税票系统
-- ----------------------------
drop table if exists tr_contract;
create table tr_contract (
  contract_id               bigint              not null auto_increment                 comment '合同Id',
  contract_code             varchar(30)         not null                                comment '合同编码',
  contract_name             varchar(30)         not null                                comment '合同名称',
  contract_content          varchar(65535)      not null                                comment '合同内容',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0未签约 1已发合同，待签约 2已签约）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (contract_id),
  unique (tenant_id, contract_code, del_flag)
) engine=innodb auto_increment=1 comment = '合同表';

-- ----------------------------
-- 5、短信表 | 税票系统
-- ----------------------------
drop table if exists tr_SMS;
create table tr_SMS (
  SMS_id                    bigint              not null auto_increment                 comment '短信Id',
  SMS_name                  varchar(30)         not null                                comment '短信对象',
  SMS_phone                 varchar(30)         not null                                comment '短信手机号',
  SMS_content               varchar(65535)      not null                                comment '短信内容',
  SMS_type                  char(1)             not null                                comment '短信类型',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0正常 1停用）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (SMS_id)
) engine=innodb auto_increment=1 comment = '短信表';

-- ----------------------------
-- 6、职业者-短信关联表 | 税票系统
-- ----------------------------
drop table if exists tr_freelance_SMS;
create table tr_freelance_SMS (
  freelance_id              bigint              not null                                comment '短信Id',
  SMS_id                    bigint              not null                                comment '短信Id',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (freelance_id, SMS_id, tenant_id)
) engine=innodb comment = '职业者-短信关联表';

-- ----------------------------
-- 7、任务-职业者关联表 | 税票系统
-- ----------------------------
drop table if exists tr_task_freelance;
create table tr_task_freelance (
  task_id                   bigint              not null                                comment '任务Id',
  freelance_id		        bigint	            not null                                comment '自由职业者Id',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (task_id, freelance_id, tenant_id)
) engine=innodb auto_increment=1 comment = '任务-职业者关联表';

-- ----------------------------
-- 8、账单表 | 税票系统
-- ----------------------------
drop table if exists tr_bill;
create table tr_bill (
  bill_id                   bigint              not null auto_increment                 comment '账单Id',
  bill_code                 varchar(30)         not null                                comment '账单编码',
  bill_name                 varchar(30)         not null                                comment '账单名称',
  settle_by                 bigint              default null                            comment '结算者',
  settle_time               datetime            default null                            comment '结算时间',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0待支付 1已支付 2已关闭 3支付中 4支付失败）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (bill_id)
) engine=innodb auto_increment=1 comment = '账单表';

-- ----------------------------
-- 9、任务-账单表 | 税票系统
-- ----------------------------
drop table if exists tr_task_bill;
create table tr_task_bill (
  task_id                   bigint              not null                                comment '任务Id',
  bill_id		            bigint	            not null                                comment '账单Id',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (task_id, bill_id, tenant_id)
) engine=innodb comment = '任务-账单表';

-- ----------------------------
-- 10、结算表 | 税票系统 | 银行卡信息
-- ----------------------------
drop table if exists tr_settle;
create table tr_settle (
  settle_id                 bigint              not null auto_increment                 comment '结算Id',
  freelance_id              bigint              not null                                comment '自由职业者Id',
  settle_code               varchar(30)         not null                                comment '账单编码',
  account_bank              varchar(30)         not null                                comment '开户行',
  account_type              char(1)             not null                                comment '卡性质（1借记卡 2贷记卡）',
  settle_amount             decimal(12,2)       default 0                               comment '结算金额',
  settle_by                 bigint              default null                            comment '结算者',
  settle_time               datetime            default null                            comment '结算时间',
  sort                      tinyint             not null default 0                      comment '显示顺序',
  status                    char(1)             not null default '0'                    comment '状态（0待支付 1已支付 2已关闭 3支付中 4支付失败）',
  create_by                 bigint              default null                            comment '创建者',
  create_time               datetime            default current_timestamp               comment '创建时间',
  update_by                 bigint              default null                            comment '更新者',
  update_time               datetime            on update current_timestamp             comment '更新时间',
  remark                    varchar(1000)       default null                            comment '备注',
  del_flag		            tinyint             not null default 0                      comment '删除标志(0正常 1删除)',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (settle_id)
) engine=innodb auto_increment=1 comment = '结算表';

-- ----------------------------
-- 11、账单-结算表 | 税票系统
-- ----------------------------
drop table if exists tr_task_bill;
create table tr_task_bill (
  bill_id		            bigint	            not null                                comment '账单Id',
  settle_id                 bigint              not null                                comment '结算Id',
  tenant_id		            bigint	            not null                                comment '租户Id',
  primary key (bill_id, settle_id, tenant_id)
) engine=innodb comment = '账单-结算表';
