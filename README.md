## 平台简介

主要做了如下几块：
* 多租户设计：1、物理隔离 2、逻辑隔离 | 共享多租户、隔离多租户 | 一库一户、一库多户
* 素材管理模块：独立素材模块 | 文件|图片统一管理，配置
* 部门架构管理优化：更完善的组织架构操作与管理逻辑
* 多云台、前端微聚合：前端与前端进行打通，实现系统间互通
* RBAC优化：角色-模块-菜单控制模式 | 优化角色控制逻辑
* 分布式Id：通过雪花Id实现全局Id唯一

基于本系统还有许多内容是希望加入进来的，但限于时间问题，后续慢慢更新进来，如遇bug，请提Issues。  
  
## 月度计划

* 1.认证改造：redis改造oAuth 2.0
* 2.`物理隔离实现(已完成)`
* 3.demo部署
* 4.基础操作视频讲解
* 5.常规优化

走过路过，点个star :kissing_heart: 



## 在线体验
- 普通账户
- xueYi / admin / admin123 


- 超管账户（可增减租户）
- administrator / admin / admin123

演示地址：https://xueyitt.cn`(即将上线)`     
文档地址：https://doc.xueyitt.cn

## 雪忆微服务交流群

QQ群：[![加入QQ群](https://img.shields.io/badge/779343138-blue.svg)](https://jq.qq.com/?_wv=1027&k=zw11JJhj) 点击按钮入群。

* 采用前后端分离的模式，微服务版本前端。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。

## 系统模块

~~~
com.xueyi     
├── xueyi-ui              // 前端框架 [80、81]
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
│       └── xueyi-system                              // 系统模块 [9600]
│               └── authority                         // 权限模块
│               └── dict                              // 字典配置
│               └── material                          // 素材模块
│               └── monitor                           // 监控模块
│               └── notice                            // 公告模块
│               └── organize                          // 组织模块
│               └── role                              // 权限关系
│       └── xueyi-gen                                 // 代码生成 [9400]
│       └── xueyi-job                                 // 定时任务 [9500]
│       └── xueyi-file                                // 文件服务 [9300]
│       └── xueyi-tenant                              // 租管模块 [9700]
├── xueyi-visual          // 图形化管理模块
│       └── xueyi-visual-monitor                      // 监控中心 [9100]
├──pom.xml                // 公共依赖
~~~

## 架构图

<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>


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