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
  `encrypted_data_key` text COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

INSERT INTO config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type)
VALUES (1, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:\r\n  autoconfigure:\r\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\r\n  mvc:\r\n    pathmatch:\r\n      matching-strategy: ant_path_matcher\r\n\r\n# feign 配置\r\nfeign:\r\n  sentinel:\r\n    enabled: true\r\n  okhttp:\r\n    enabled: true\r\n  httpclient:\r\n    enabled: false\r\n  client:\r\n    config:\r\n      default:\r\n        connectTimeout: 10000\r\n        readTimeout: 10000\r\n  compression:\r\n    request:\r\n      enabled: true\r\n    response:\r\n      enabled: true\r\n\r\n# 暴露监控端点\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: ''*''\r\n', 'db868af6c3e2b2bbc45a12787a3c33f6', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '通用配置', 'null', 'null', 'yaml'),
       (2, 'application-secret-dev.yml', 'DEFAULT_GROUP', 'secret:\r\n  #redis参数信息\r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n    password:\r\n  #服务状态监控参数信息\r\n  security:\r\n    name: xueyi\r\n    password: xueyi123\r\n    title: 服务状态监控\r\n  # swagger参数信息\r\n  swagger:\r\n    title: 接口文档\r\n    license: Powered By xueyi\r\n    licenseUrl: https://doc.xueyitt.cn\r\n  # datasource主库参数信息\r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://localhost:3306/xy-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n    username: root\r\n    password: password\r\n  # druid控制台参数信息\r\n  druid:\r\n    stat-view-servlet:\r\n      enabled: true\r\n      loginUsername: admin\r\n      loginPassword: 123456\r\n  # nacos参数信息\r\n  nacos:\r\n    serverAddr: 127.0.0.1:8848', 'c28955605f738593b0f70a6cc3816a11', '2022-02-01 16:11:30', '2022-04-07 07:46:59', null, '192.168.73.204', '', '', '通用参数配置', 'null', 'null', 'yaml'),
       (3, 'application-datasource-dev.yml', 'DEFAULT_GROUP', '# spring配置\r\nspring: \r\n  redis:\r\n    host: ${secret.redis.host}\r\n    port: ${secret.redis.port}\r\n    password: ${secret.redis.password}\r\n  datasource:\r\n    druid:\r\n      stat-view-servlet:\r\n        enabled: ${secret.druid.stat-view-servlet.enabled}\r\n        loginUsername: ${secret.druid.stat-view-servlet.loginUsername}\r\n        loginPassword: ${secret.druid.stat-view-servlet.loginPassword}\r\n    dynamic:\r\n      druid:\r\n        initial-size: 5\r\n        min-idle: 5\r\n        maxActive: 20\r\n        maxWait: 60000\r\n        timeBetweenEvictionRunsMillis: 60000\r\n        minEvictableIdleTimeMillis: 300000\r\n        validationQuery: SELECT 1 FROM DUAL\r\n        testWhileIdle: true\r\n        testOnBorrow: false\r\n        testOnReturn: false\r\n        poolPreparedStatements: true\r\n        breakAfterAcquireFailure: true\r\n        ConnectionErrorRetryAttempts: 2\r\n        maxPoolPreparedStatementPerConnectionSize: 20\r\n        filters: stat,slf4j\r\n        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000\r\n      datasource:\r\n          # 主库数据源\r\n          master:\r\n            driver-class-name: ${secret.datasource.driver-class-name}\r\n            url: ${secret.datasource.url}\r\n            username: ${secret.datasource.username}\r\n            password: ${secret.datasource.password}\r\n          # 数据源信息会通过master库进行获取并生成，请在主库的xy_tenant_source中配置即可\r\n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\r\n\r\n# mybatis-plus配置\r\nmybatis-plus:\r\n  global-config:\r\n    # 是否控制台 print mybatis-plus 的 LOGO\r\n    banner: false\r\n    db-config:\r\n      # 字段验证策略之 select\r\n      selectStrategy: NOT_EMPTY\r\n      # 字段验证策略之 insert\r\n      insertStrategy: NOT_EMPTY\r\n      # 字段验证策略之 update\r\n      updateStrategy: NOT_EMPTY\r\n  # configuration:\r\n    # log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n\r\n# seata配置\r\nseata:\r\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\r\n  enabled: false\r\n  # Seata 应用编号，默认为 ${spring.application.name}\r\n  application-id: ${spring.application.name}\r\n  # Seata 事务组编号，用于 TC 集群名\r\n  tx-service-group: ${spring.application.name}-group\r\n  # 关闭自动代理\r\n  enable-auto-data-source-proxy: false\r\n  config:\r\n    type: nacos\r\n    nacos:\r\n      serverAddr: ${secret.nacos.serverAddr}\r\n      group: SEATA_GROUP\r\n      namespace:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: ${secret.nacos.serverAddr}\r\n      namespace:\r\n', '3e7f0ebc7439a0e1d997404a6b473aa9', '2022-02-01 16:11:30', '2022-04-07 08:59:58', null, '192.168.73.204', '', '', '通用动态多数据源配置', 'null', 'null', 'yaml'),
       (4, 'xueyi-gateway-dev.yml', 'DEFAULT_GROUP', '# spring配置\r\nspring:\r\n  redis:\r\n    host: ${secret.redis.host}\r\n    port: ${secret.redis.port}\r\n    password: ${secret.redis.password}\r\n  cloud:\r\n    gateway:\r\n      discovery:\r\n        locator:\r\n          lowerCaseServiceId: true\r\n          enabled: true\r\n      routes:\r\n        # 认证中心\r\n        - id: xueyi-auth\r\n          uri: lb://xueyi-auth\r\n          predicates:\r\n            - Path=/auth/**\r\n          filters:\r\n            # 验证码处理\r\n            - CacheRequestFilter\r\n            - ValidateCodeFilter\r\n            - StripPrefix=1\r\n        # 代码生成\r\n        - id: xueyi-gen\r\n          uri: lb://xueyi-gen\r\n          predicates:\r\n            - Path=/code/**\r\n          filters:\r\n            - StripPrefix=1\r\n        # 定时任务\r\n        - id: xueyi-job\r\n          uri: lb://xueyi-job\r\n          predicates:\r\n            - Path=/schedule/**\r\n          filters:\r\n            - StripPrefix=1\r\n        # 系统模块\r\n        - id: xueyi-system\r\n          uri: lb://xueyi-system\r\n          predicates:\r\n            - Path=/system/**\r\n          filters:\r\n            - StripPrefix=1\r\n        # 租户模块\r\n        - id: xueyi-tenant\r\n          uri: lb://xueyi-tenant\r\n          predicates:\r\n            - Path=/tenant/**\r\n          filters:\r\n            - StripPrefix=1\r\n        # 文件服务\r\n        - id: xueyi-file\r\n          uri: lb://xueyi-file\r\n          predicates:\r\n            - Path=/file/**\r\n          filters:\r\n            - StripPrefix=1\r\n\r\n# 安全配置\r\nsecurity:\r\n  # 验证码\r\n  captcha:\r\n    enabled: true\r\n    type: math\r\n  # 防止XSS攻击\r\n  xss:\r\n    enabled: true\r\n    excludeUrls:\r\n      - /system/notice\r\n  # 不校验白名单\r\n  ignore:\r\n    whites:\r\n      - /auth/logout\r\n      - /auth/login\r\n      - /auth/register\r\n      - /*/v2/api-docs\r\n      - /csrf\r\n', '20c5e882255155e0594f03af0877804a', '2022-02-01 16:11:30', '2022-04-07 07:47:32', null, '192.168.73.204', '', '', '网关模块', 'null', 'null', 'yaml'),
       (5, 'xueyi-auth-dev.yml', 'DEFAULT_GROUP', '# spring配置\r\nspring:\r\n  redis:\r\n    host: ${secret.redis.host}\r\n    port: ${secret.redis.port}\r\n    password: ${secret.redis.password}\r\n', 'b7354e1eb62c2d846d44a796d9ec6930', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '认证中心', 'null', 'null', 'yaml'),
       (6, 'xueyi-monitor-dev.yml', 'DEFAULT_GROUP', '# spring配置\r\nspring:\r\n  security:\r\n    user:\r\n      name: ${secret.security.name}\r\n      password: ${secret.security.password}\r\n  boot:\r\n    admin:\r\n      ui:\r\n        title: ${secret.security.title}\r\n', 'd8997d0707a2fd5d9fc4e8409da38129', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '监控中心', 'null', 'null', 'yaml'),
       (7, 'xueyi-tenant-dev.yml', 'DEFAULT_GROUP', 'xueyi:\r\n  # 租户配置\r\n  tenant:\r\n    # 公共表配置\r\n    common-table:\r\n      - sys_menu\r\n      - sys_module\r\n    # 非租户表配置\r\n    exclude-table:\r\n      - te_tenant\r\n      - te_strategy\r\n      - te_source\r\n\r\n# mybatis-plus配置\r\nmybatis-plus:\r\n  # 搜索指定包别名\r\n  typeAliasesPackage: com.xueyi.tenant\r\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n  mapperLocations: classpath:mapper/**/*.xml\r\n\r\n# swagger配置\r\nswagger:\r\n  title: 租户模块${secret.swagger.title}\r\n  license: ${secret.swagger.license}\r\n  licenseUrl: ${secret.swagger.licenseUrl}\r\n\r\n#seata配置\r\nseata:\r\n  # 服务配置项\r\n  service:\r\n    # 虚拟组和分组的映射\r\n    vgroup-mapping:\r\n      xueyi-tenant-group: default\r\n', '840ef59c3cea0d055675ac638f90a09d', '2022-02-01 16:11:30', '2022-04-07 07:44:52', null, '192.168.73.204', '', '', '租户管理模块', 'null', 'null', 'yaml'),
       (8, 'xueyi-system-dev.yml', 'DEFAULT_GROUP', 'xueyi:\r\n  # 租户配置\r\n  tenant:\r\n    # 公共表配置\r\n    common-table:\r\n      - sys_menu\r\n      - sys_module\r\n    # 非租户表配置\r\n    exclude-table:\r\n      - te_tenant\r\n      - te_strategy\r\n      - sys_dict_type\r\n      - sys_dict_data\r\n      - sys_config\r\n\r\n# mybatis-plus配置\r\nmybatis-plus:\r\n  # 搜索指定包别名\r\n  typeAliasesPackage: com.xueyi.system\r\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n  mapperLocations: classpath:mapper/**/*.xml\r\n\r\n# swagger配置\r\nswagger:\r\n  title: 系统模块${secret.swagger.title}\r\n  license: ${secret.swagger.license}\r\n  licenseUrl: ${secret.swagger.licenseUrl}\r\n\r\n#seata配置\r\nseata:\r\n  # 服务配置项\r\n  service:\r\n    # 虚拟组和分组的映射\r\n    vgroup-mapping:\r\n      xueyi-system-group: default', 'cc2fef8a73408605894dd30d36db445e', '2022-02-01 16:11:30', '2022-04-07 07:41:51', null, '192.168.73.204', '', '', '系统模块', 'null', 'null', 'yaml'),
       (9, 'xueyi-gen-dev.yml', 'DEFAULT_GROUP', 'xueyi:\r\n  # 租户配置\r\n  tenant:\r\n    # 非租户表配置\r\n    exclude-table:\r\n      - gen_table\r\n      - gen_table_column\r\n\r\n# mybatis-plus配置\r\nmybatis-plus:\r\n  # 搜索指定包别名\r\n  typeAliasesPackage: com.xueyi.gen\r\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n  mapperLocations: classpath:mapper/**/*.xml\r\n  configuration:\r\n    jdbc-type-for-null: ''null''\r\n\r\n# swagger配置\r\nswagger:\r\n  title: 代码生成${secret.swagger.title}\r\n  license: ${secret.swagger.license}\r\n  licenseUrl: ${secret.swagger.licenseUrl}\r\n\r\n# 代码生成\r\ngen: \r\n  # 作者\r\n  author: xueyi\r\n  # ui路径（空代表生成在后端主路径下，可设置为ui项目地址如：C:\Users\xueyi\MultiSaas-UI）\r\n  uiPath: \r\n  # 自动去除表前缀，默认是true\r\n  autoRemovePre: true\r\n  # 数据库映射\r\n  data-base:\r\n    # 字符串类型\r\n    type-str: ["char", "varchar", "nvarchar", "varchar2"]\r\n    # 文本类型\r\n    type-text: ["tinytext", "text", "mediumtext", "longtext"]\r\n    # 日期类型\r\n    type-date: ["datetime", "time", "date", "timestamp"]\r\n    # 时间类型\r\n    type-time: ["datetime", "time", "date", "timestamp"]\r\n    # 数字类型\r\n    type-number: ["tinyint", "smallint", "mediumint", "int", "number", "integer"]\r\n    # 长数字类型\r\n    type-long: ["bigint"]\r\n    # 浮点类型\r\n    type-float: ["float", "double", "decimal"]\r\n  # 字段配置\r\n  operate:\r\n    # 隐藏详情显示\r\n    not-view: ["id", "createBy", "createTime", "updateBy", "updateTime"]\r\n    # 隐藏新增显示\r\n    not-insert: ["id", "createBy", "createTime", "updateBy", "updateTime"]\r\n    # 隐藏编辑显示\r\n    not-edit: ["id", "createBy", "createTime", "updateBy", "updateTime"]\r\n    # 隐藏列表显示\r\n    not-list: ["id", "createBy", "createTime", "updateBy", "updateTime", "remark"]\r\n    # 隐藏查询显示\r\n    not-query: ["id", "sort", "createBy", "createTime", "updateBy", "updateTime", "remark"]\r\n    # 隐藏导入显示\r\n    not-import: ["id", "createBy", "createTime", "updateBy", "updateTime"]\r\n    # 隐藏导出显示\r\n    not-export: ["id", "sort", "createBy", "updateBy"]\r\n  # 基类配置\r\n  entity:\r\n    # 必定隐藏字段（前后端均隐藏）\r\n    must-hide: ["delFlag", "tenantId", "ancestors"]\r\n    # 后端基类\r\n    back:\r\n      base: ["id", "name", "status", "sort", "remark", "createBy", "createTime", "updateBy", "updateTime", "delFlag"]\r\n      tree: ["parentId", "ancestors"]\r\n      sub: []\r\n      tenant: ["tenantId"]\r\n    # 前端基类\r\n    front:\r\n      base: ["createBy", "createName", "createTime", "updateBy", "updateName", "updateTime", "delFlag"]\r\n      tree: ["parentId", "ancestors"]\r\n      sub: []\r\n      tenant: ["tenantId"]\r\n  # 表前缀（与remove-lists对应）\r\n  dict-type-remove: ["sys_", "te_", "gen_"]\r\n  # 表更替配置\r\n  remove-lists:\r\n      # 表前缀（生成类名不会包含表前缀）\r\n    - prefix: sys_\r\n      # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\r\n      packageName: com.xueyi.system\r\n      frontPackageName: system\r\n    - prefix: te_\r\n      packageName: com.xueyi.tenant\r\n      frontPackageName: tenant\r\n    - prefix: gen_\r\n      packageName: com.xueyi.gen\r\n      frontPackageName: gen\r\n', '2e6867d025381dacc98efa66d4ff357a', '2022-02-01 16:11:30', '2022-04-07 08:03:05', null, '192.168.73.204', '', '', '代码生成', 'null', 'null', 'yaml'),
       (10, 'xueyi-job-dev.yml', 'DEFAULT_GROUP', '# mybatis-plus配置\r\nmybatis-plus:\r\n  # 搜索指定包别名\r\n  typeAliasesPackage: com.xueyi.job\r\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n  mapperLocations: classpath:mapper/**/*.xml\r\n\r\n# swagger配置\r\nswagger:\r\n  title: 定时任务${secret.swagger.title}\r\n  license: ${secret.swagger.license}\r\n  licenseUrl: ${secret.swagger.licenseUrl}\r\n\r\n# seata配置\r\nseata:\r\n  # 服务配置项\r\n  service:\r\n    # 虚拟组和分组的映射\r\n    vgroup-mapping:\r\n      xueyi-job-group: default\r\n', 'ebb507f8a468ef381b4948ae7ecd5017', '2022-02-01 16:11:30', '2022-04-07 08:02:40', null, '192.168.73.204', '', '', '定时任务', 'null', 'null', 'yaml'),
       (11, 'xueyi-file-dev.yml', 'DEFAULT_GROUP', '# 本地文件上传\r\nfile:\r\n  domain: http://127.0.0.1:9300\r\n  path: D:/xueyi/uploadPath\r\n  prefix: /statics\r\n\r\n# FastDFS配置\r\nfdfs:\r\n  domain: http://8.129.231.12\r\n  soTimeout: 3000\r\n  connectTimeout: 2000\r\n  trackerList: 8.129.231.12:22122\r\n\r\n# Minio配置\r\nminio:\r\n  url: http://8.129.231.12:9000\r\n  accessKey: minioadmin\r\n  secretKey: minioadmin\r\n  bucketName: test', 'e507ba4ba82516bd5b9d1bea147bd910', '2022-02-01 16:11:30', '2022-04-07 07:45:53', null, '192.168.73.204', '', '', '文件服务', 'null', 'null', 'yaml'),
       (12, 'sentinel-xueyi-gateway', 'DEFAULT_GROUP', '[\r\n    {\r\n        "resource": "xueyi-auth",\r\n        "count": 500,\r\n        "grade": 1,\r\n        "limitApp": "default",\r\n        "strategy": 0,\r\n        "controlBehavior": 0\r\n    },\r\n	{\r\n        "resource": "xueyi-system",\r\n        "count": 1000,\r\n        "grade": 1,\r\n        "limitApp": "default",\r\n        "strategy": 0,\r\n        "controlBehavior": 0\r\n    },\r\n	{\r\n        "resource": "xueyi-tenant",\r\n        "count": 500,\r\n        "grade": 1,\r\n        "limitApp": "default",\r\n        "strategy": 0,\r\n        "controlBehavior": 0\r\n    },\r\n	{\r\n        "resource": "xueyi-gen",\r\n        "count": 200,\r\n        "grade": 1,\r\n        "limitApp": "default",\r\n        "strategy": 0,\r\n        "controlBehavior": 0\r\n    },\r\n	{\r\n        "resource": "xueyi-job",\r\n        "count": 300,\r\n        "grade": 1,\r\n        "limitApp": "default",\r\n        "strategy": 0,\r\n        "controlBehavior": 0\r\n    }\r\n]', '9f3a3069261598f74220bc47958ec252', '2022-02-01 16:11:30', '2022-02-01 16:11:30', null, '0:0:0:0:0:0:0:1', '', '', '限流策略', 'null', 'null', 'json');

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
  `encrypted_data_key` text COMMENT '秘钥',
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
  `encrypted_data_key` text COMMENT '秘钥',
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
