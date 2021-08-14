## 平台简介

主要做了如下几块：
* 多租户Saas：物理隔离&&逻辑隔离 | 共享多租户&&隔离多租户  
* 动态源策略：租户-策略-数据源 | 动态源增减与租户策略配置  
* 素材管理模块：素材集中管理 | 文件&&图片统一管理，配置  
* 定向菜单屏蔽：租管可屏蔽指定租户指定模块或菜单  
* RBAC优化：角色-模块-菜单 | 优化角色控制逻辑  
* 架构管理优化：部门-岗位-用户 | 更完善的组织架构操作与管理逻辑  
* 微聚合前端：前端素材&&组件公用 | 降低系统重复冗余  
* 分布式Id：Snowflake全局唯一标识 | 保证全局唯一性&&信息安全  

基于本系统还有许多内容是希望加入进来的，但限于时间问题，后续慢慢更新进来，如遇bug，请提Issues。

走过路过，点个star :kissing_heart:

## 在线体验
- 普通账户
- xueYi / admin / admin123 


- 超管账户（可增减租户）           
- administrator / admin / admin123

演示地址：https://xueyitt.cn         
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
│       └── common                                    // 公共组件
│       └── main                                      // 默认系统 [80]
│       └── administrator                             // 租管系统 [81]
├── xueyi-gateway         // 网关模块 [8080]
├── xueyi-auth            // 认证中心 [9200]
├── xueyi-api             // 接口模块
│       └── xueyi-api-system                          // 系统接口
├── xueyi-common          // 通用模块
│       └── xueyi-common-core                         // 核心模块
│       └── xueyi-common-datascope                    // 权限范围
│       └── xueyi-common-datasource                   // 多数据源
│       └── xueyi-common-message                      // 消息队列
│       └── xueyi-common-log                          // 日志记录
│       └── xueyi-common-redis                        // 缓存服务
│       └── xueyi-common-security                     // 安全模块
│       └── xueyi-common-swagger                      // 系统接口
├── xueyi-modules         // 业务模块
│       └── xueyi-system                              // 系统模块 [9600]
│               └── authority                         // 权限模块
│               └── dict                              // 参数字典
│               └── material                          // 素材模块
│               └── monitor                           // 监控模块
│               └── notice                            // 公告模块
│               └── organize                          // 组织模块
│               └── role                              // 权限关系
│               └── source                            // 源策略
│       └── xueyi-tenant                              // 租管模块 [9700]
│       └── xueyi-gen                                 // 代码生成 [9400]
│       └── xueyi-job                                 // 定时任务 [9500]
│       └── xueyi-file                                // 文件服务 [9300]
├── xueyi-visual          // 图形化管理模块
│       └── xueyi-visual-monitor                      // 监控中心 [9100]
├──pom.xml                // 公共依赖
~~~

## 架构图

<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>


## 演示图

<table>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151423_c5168169_7382127.jpeg"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140513_48ff7abd_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151634_fee2ab95_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0507/131952_3b892800_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151708_cef1f3ed_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151737_4c4174db_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151824_752e5b07_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151842_48c3407a_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151937_3b66dcc6_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/151949_008cd20b_7382127.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2021/0814/152034_e32f2b9a_7382127.png"/></td>
        <td><img src="https://images.gitee.com/uploads/images/2021/0501/140619_628675c2_7382127.png"/></td>
    </tr>
</table>
