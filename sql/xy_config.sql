# DROP DATABASE IF EXISTS `xy-config`;
#
# CREATE DATABASE  `xy-config` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

# SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;
#
# USE `xy-config`;

/******************************************/
/*   表名称 = config_info   */
/******************************************/
drop table if exists `config_info`;
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) DEFAULT NULL,
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL,
  `c_use` varchar(64) DEFAULT NULL,
  `effect` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `c_schema` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

INSERT INTO config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema) VALUES
(1, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:
  main:
    allow-circular-references: true
    allow-bean-definition-overriding: true
  autoconfigure:
    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
  cloud:
    sentinel:
      filter:
        # sentinel 在 springboot 2.6.x 不兼容问题的处理
        enabled: false

# feign 配置
feign:
  sentinel:
    enabled: true
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        connectTimeout: 10000
        readTimeout: 10000
  compression:
    request:
      enabled: true
    response:
      enabled: true

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: ''*''
', 'db868af6c3e2b2bbc45a12787a3c33f6', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '通用配置', 'null', 'null', 'yaml', 'null'),
(2, 'application-secret-dev.yml', 'DEFAULT_GROUP', 'secret:
  # 验证码
  captcha:
    enabled: false
    type: math
  #redis参数信息
  redis:
    host: localhost
    port: 6379
    password:
  #服务状态监控参数信息
  security:
    name: xueyi
    password: xueyi123
    title: 服务状态监控
  # swagger参数信息
  swagger:
    title: 接口文档
    license: Powered By xueyi
    licenseUrl: https://doc.xueyitt.cn
  # datasource主库参数信息
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/xy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: password
  # druid控制台参数信息
  druid:
    stat-view-servlet:
      enabled: true
      loginUsername: admin
      loginPassword: 123456
  # nacos参数信息
  nacos:
    serverAddr: 127.0.0.1:8848', 'c28955605f738593b0f70a6cc3816a11', '2022-02-01 16:11:30', '2022-04-07 07:46:59', null, '192.168.73.204', '', '', '通用参数配置', 'null', 'null', 'yaml', 'null'),
(3, 'application-datasource-dev.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: ${secret.redis.host}
    port: ${secret.redis.port}
    password: ${secret.redis.password}
  datasource:
    druid:
      stat-view-servlet:
        enabled: ${secret.druid.stat-view-servlet.enabled}
        loginUsername: ${secret.druid.stat-view-servlet.loginUsername}
        loginPassword: ${secret.druid.stat-view-servlet.loginPassword}
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        breakAfterAcquireFailure: true
        ConnectionErrorRetryAttempts: 2
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: ${secret.datasource.driver-class-name}
            url: ${secret.datasource.url}
            username: ${secret.datasource.username}
            password: ${secret.datasource.password}
          # 数据源信息会通过master库进行获取并生成，请在主库的xy_tenant_source中配置即可
      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭

# mybatis-plus配置
mybatis-plus:
    global-config:
      # 是否控制台 print mybatis-plus 的 LOGO
      banner: false
    db-config:
      # 字段验证策略之 select
      selectStrategy: FieldStrategy.NOT_EMPTY
      # 字段验证策略之 insert
      insertStrategy: FieldStrategy.NOT_EMPTY
      # 字段验证策略之 update
      updateStrategy: FieldStrategy.NOT_EMPTY
    configuration:
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# seata配置
seata:
  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启
  enabled: false
  # Seata 应用编号，默认为 ${spring.application.name}
  application-id: ${spring.application.name}
  # Seata 事务组编号，用于 TC 集群名
  tx-service-group: ${spring.application.name}-group
  # 关闭自动代理
  enable-auto-data-source-proxy: false
  config:
    type: nacos
    nacos:
      serverAddr: ${secret.nacos.serverAddr}
      group: SEATA_GROUP
      namespace:
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: ${secret.nacos.serverAddr}
      namespace:
', '3e7f0ebc7439a0e1d997404a6b473aa9', '2022-02-01 16:11:30', '2022-04-07 08:59:58', null, '192.168.73.204', '', '', '通用动态多数据源配置', 'null', 'null', 'yaml', 'null'),
(4, 'xueyi-gateway-dev.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: ${secret.redis.host}
    port: ${secret.redis.port}
    password: ${secret.redis.password}
  cloud:
    gateway:
      discovery:
        locator:
          lowerCaseServiceId: true
          enabled: true
      routes:
        # 认证中心
        - id: xueyi-auth
          uri: lb://xueyi-auth
          predicates:
            - Path=/auth/**
          filters:
            # 验证码处理
            - CacheRequestFilter
            - ValidateCodeFilter
            - StripPrefix=1
        # 代码生成
        - id: xueyi-gen
          uri: lb://xueyi-gen
          predicates:
            - Path=/code/**
          filters:
            - StripPrefix=1
        # 定时任务
        - id: xueyi-job
          uri: lb://xueyi-job
          predicates:
            - Path=/schedule/**
          filters:
            - StripPrefix=1
        # 系统模块
        - id: xueyi-system
          uri: lb://xueyi-system
          predicates:
            - Path=/system/**
          filters:
            - StripPrefix=1
        # 租户模块
        - id: xueyi-tenant
          uri: lb://xueyi-tenant
          predicates:
            - Path=/tenant/**
          filters:
            - StripPrefix=1
        # 文件服务
        - id: xueyi-file
          uri: lb://xueyi-file
          predicates:
            - Path=/file/**
          filters:
            - StripPrefix=1

# 安全配置
security:
  # 验证码
  captcha:
    enabled: ${secret.captcha.enabled}
    type: ${secret.captcha.type}
  # 防止XSS攻击
  xss:
    enabled: true
    excludeUrls:
      - /system/notice
  # 不校验白名单
  ignore:
    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /*/v2/api-docs
      - /csrf
', '20c5e882255155e0594f03af0877804a', '2022-02-01 16:11:30', '2022-04-07 07:47:32', null, '192.168.73.204', '', '', '网关模块', 'null', 'null', 'yaml', 'null'),
(5, 'xueyi-auth-dev.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  redis:
    host: ${secret.redis.host}
    port: ${secret.redis.port}
    password: ${secret.redis.password}
', 'b7354e1eb62c2d846d44a796d9ec6930', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '认证中心', 'null', 'null', 'yaml', 'null'),
(6, 'xueyi-monitor-dev.yml', 'DEFAULT_GROUP', '# spring配置
spring:
  security:
    user:
      name: ${secret.security.name}
      password: ${secret.security.password}
  boot:
    admin:
      ui:
        title: ${secret.security.title}
', 'd8997d0707a2fd5d9fc4e8409da38129', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '监控中心', 'null', 'null', 'yaml', 'null'),
(7, 'xueyi-tenant-dev.yml', 'DEFAULT_GROUP', 'xueyi:
  # 租户配置
  tenant:
    # 公共表配置
    common-table:
      - sys_menu
      - sys_module
    # 非租户表配置
    exclude-table:
      - te_tenant
      - te_strategy
      - te_source

# mybatis-plus配置
mybatis-plus:
  # 搜索指定包别名
  typeAliasesPackage: com.xueyi.tenant
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# swagger配置
swagger:
  title: 租户模块${secret.swagger.title}
  license: ${secret.swagger.license}
  licenseUrl: ${secret.swagger.licenseUrl}

#seata配置
seata:
  # 服务配置项
  service:
    # 虚拟组和分组的映射
    vgroup-mapping:
      xueyi-tenant-group: default
', '840ef59c3cea0d055675ac638f90a09d', '2022-02-01 16:11:30', '2022-04-07 07:44:52', null, '192.168.73.204', '', '', '租户管理模块', 'null', 'null', 'yaml', 'null'),
(8, 'xueyi-system-dev.yml', 'DEFAULT_GROUP', 'xueyi:
  # 租户配置
  tenant:
    # 公共表配置
    common-table:
      - sys_menu
      - sys_module
    # 非租户表配置
    exclude-table:
      - te_tenant
      - te_strategy
      - sys_dict_type
      - sys_dict_data
      - sys_config

# mybatis-plus配置
mybatis-plus:
  # 搜索指定包别名
  typeAliasesPackage: com.xueyi.system
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# swagger配置
swagger:
  title: 系统模块${secret.swagger.title}
  license: ${secret.swagger.license}
  licenseUrl: ${secret.swagger.licenseUrl}

#seata配置
seata:
  # 服务配置项
  service:
    # 虚拟组和分组的映射
    vgroup-mapping:
      xueyi-system-group: default', 'cc2fef8a73408605894dd30d36db445e', '2022-02-01 16:11:30', '2022-04-07 07:41:51', null, '192.168.73.204', '', '', '系统模块', 'null', 'null', 'yaml', 'null'),
(9, 'xueyi-gen-dev.yml', 'DEFAULT_GROUP', 'xueyi:
  # 租户配置
  tenant:
    # 非租户表配置
    exclude-table:
      - gen_table
      - gen_table_column

# mybatis-plus配置
mybatis-plus:
  # 搜索指定包别名
  typeAliasesPackage: com.xueyi.gen
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml
  configuration:
    jdbc-type-for-null: ''null''

# swagger配置
swagger:
  title: 代码生成${secret.swagger.title}
  license: ${secret.swagger.license}
  licenseUrl: ${secret.swagger.licenseUrl}

# 代码生成
gen:
  # 作者
  author: xueyi
  # ui路径（空代表生成在后端主路径下，可设置为ui项目地址如：C:\\Users\\xueyi\\MultiSaas-UI）
  uiPath:
  # 自动去除表前缀，默认是true
  autoRemovePre: true
  remove-lists:
      # 表前缀（生成类名不会包含表前缀）
    - prefix: sys_
      # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool
      packageName: com.xueyi.system
      frontPackageName: system
    - prefix: te_
      packageName: com.xueyi.tenant
      frontPackageName: tenant

', '2e6867d025381dacc98efa66d4ff357a', '2022-02-01 16:11:30', '2022-04-07 08:03:05', null, '192.168.73.204', '', '', '代码生成', 'null', 'null', 'yaml', 'null'),
(10, 'xueyi-job-dev.yml', 'DEFAULT_GROUP', '# mybatis-plus配置
mybatis-plus:
  # 搜索指定包别名
  typeAliasesPackage: com.xueyi.job
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapperLocations: classpath:mapper/**/*.xml

# swagger配置
swagger:
  title: 定时任务${secret.swagger.title}
  license: ${secret.swagger.license}
  licenseUrl: ${secret.swagger.licenseUrl}

#seata配置
seata:
  # 服务配置项
  service:
    # 虚拟组和分组的映射
    vgroup-mapping:
      xueyi-job-group: default
', 'ebb507f8a468ef381b4948ae7ecd5017', '2022-02-01 16:11:30', '2022-04-07 08:02:40', null, '192.168.73.204', '', '', '定时任务', 'null', 'null', 'yaml', 'null'),
(11, 'xueyi-file-dev.yml', 'DEFAULT_GROUP', '# 本地文件上传
file:
  domain: http://127.0.0.1:9300
  path: D:/xueyi/uploadPath
  prefix: /statics

# FastDFS配置
fdfs:
  domain: http://8.129.231.12
  soTimeout: 3000
  connectTimeout: 2000
  trackerList: 8.129.231.12:22122

# Minio配置
minio:
  url: http://8.129.231.12:9000
  accessKey: minioadmin
  secretKey: minioadmin
  bucketName: test', 'e507ba4ba82516bd5b9d1bea147bd910', '2022-02-01 16:11:30', '2022-04-07 07:45:53', null, '192.168.73.204', '', '', '文件服务', 'null', 'null', 'yaml', 'null'),
(12, 'sentinel-xueyi-gateway', 'DEFAULT_GROUP', '[
    {
        "resource": "xueyi-auth",
        "count": 500,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xueyi-system",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xueyi-tenant",
        "count": 500,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xueyi-gen",
        "count": 200,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "xueyi-job",
        "count": 300,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    }
]', '9f3a3069261598f74220bc47958ec252', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '限流策略', 'null', 'null', 'json', 'null');


/******************************************/
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text,
  `src_ip` varchar(50) DEFAULT NULL,
  `op_type` char(10) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY,
	`password` varchar(500) NOT NULL,
	`enabled` boolean NOT NULL
);

CREATE TABLE `roles` (
	`username` varchar(50) NOT NULL,
	`role` varchar(50) NOT NULL,
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL,
    `resource` varchar(255) NOT NULL,
    `action` varchar(8) NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
