## 平台简介

主要做了如下六块：
* 多租户设计：简单配置即可转变共享多租户模式；
* 素材管理模块：独立素材模块（文件|图片统一管理，配置，一次上传多处使用，同时删除时进行同步删除，已接管富文本上传）；
* 部门架构管理优化：重置角色权限，部门-岗位-用户层级管理，上级禁用时同步禁用所有下级；
* 多系统云台：建立模块组，建立多前端时可以作为通用入口，同时模块组与菜单组关联，如用统一后台多前端模式可以通过模块-菜单方式进行控制， 不同前端显示对应前端模块的菜单；
* RBAC优化：角色-模块-菜单控制模式，同时角色-部门|角色-岗位|角色-用户多种角色授权方式，岗位继承部门角色，用户继承岗位、部门角色。
* 分布式Id：雪花Id

基于本系统还有许多内容是希望加入进来的，但限于时间问题，后续慢慢更新进来，如遇bug，请提Issues。
基于本系统正在编写的商城项目：https://gitee.com/xueyitiantang/xueyi-mall

走过路过，点个star :kissing_heart: 

* 采用前后端分离的模式，微服务版本前端。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。

## 系统模块

~~~
com.xueyi     
├── xueyi-ui              // 前端框架 [80]
├── xueyi-gateway         // 网关模块 [8080]
├── xueyi-auth            // 认证中心 [9200]
├── xueyi-api             // 接口模块
│       └── xueyi-api-system                          // 系统接口
├── xueyi-common          // 通用模块
│       └── xueyi-common-core                         // 核心模块
│       └── xueyi-common-datascope                    // 权限范围
│       └── xueyi-common-datasource                   // 多数据源
│       └── xueyi-common-log                          // 日志记录
│       └── xueyi-common-redis                        // 缓存服务
│       └── xueyi-common-security                     // 安全模块
│       └── xueyi-common-swagger                      // 系统接口
├── xueyi-modules         // 业务模块
│       └── xueyi-system                              // 系统模块 [9201]
│               └── authority                         // 权限模块
│               └── dict                              // 字典配置
│               └── material                          // 素材模块
│               └── monitor                           // 监控模块
│               └── notice                            // 公告模块
│               └── organize                          // 组织模块
│               └── role                              // 权限关系
│       └── xueyi-gen                                 // 代码生成 [9202]
│       └── xueyi-job                                 // 定时任务 [9203]
│       └── xueyi-file                                // 文件服务 [9300]
├── xueyi-visual          // 图形化管理模块
│       └── xueyi-visual-monitor                      // 监控中心 [9100]
├──pom.xml                // 公共依赖
~~~

## 架构图

<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>

## 在线体验
- 企业账号/员工账号/密码
- xueYi/admin/admin123

演示地址：(即将上线)        
文档地址：https://doc.xueyitt.cn/

## 演示图

<table>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/142108_5c0567fd_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140513_48ff7abd_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0507/132106_c1b9451d_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0507/131952_3b892800_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140524_ec4af10e_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140534_98a211b1_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140545_9c62338d_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140558_1c729ee4_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140607_d6697a5a_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140619_628675c2_7382127.png"/></td>
    </tr>
</table>