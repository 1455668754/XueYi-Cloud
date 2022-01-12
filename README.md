<div align="center"><h3 align="center">XueYi-Cloud 多租户Saas快速开发平台</h3></div>
<p align="center">     
    <p align="center">
        <a>
            <img src="https://img.shields.io/badge/XueYi%20Cloud-v4.1.3-brightgreen" alt="xueYi-cloud">
        </a>
        <a>
            <img src="https://img.shields.io/badge/Spring%20Cloud-2020-blue" alt="spring-cloud">
        </a>
        <a>
            <img src="https://img.shields.io/badge/element--ui-2.15-brightgreen" alt="element-ui">
        </a>
    </p>
</p>

## 简介
基于SpringBoot | Mybatis | RabbitMQ | Vue | Element UI的多租户SaaS开发框架，已支持消息队列、数据权限、动态源、多租户、数据物理&逻辑双隔离等，为企业级多租户Saas及集团化应用提供快速开发解决方案。

##  Mybatis + vue3版本

**[XueYi-MultiSaas](https://gitee.com/xueyitiantang/XueYi-MultiSaas)**

## 特性
- **多租户Saas**： 物理隔离&&逻辑隔离 --- 共享多租户&&隔离多租户
- **动态多源策略**：租户-策略-数据源 --- 动态源增减与租户策略配置
- **素材管理模块**：素材集中管理 --- 文件&&图片统一管理，配置
- **权限控制优化**：角色-模块-菜单 --- 优化角色控制逻辑
- **租户菜单层级**：租管可动态指定租户可用模块或菜单
- **组织管理优化**：部门-岗位-用户 --- 更完善的组织架构操作与管理逻辑
- **微聚合多前端**：前端素材&&组件公用 --- 降低系统重复冗余
- **分布式主键**：  Snowflake全局唯一标识 --- 保证全局唯一性&&信息安全

star 别忘点上 :kissing_heart:

## 交流
- 请移步右上角  **一键三连** :kissing_heart:
- QQ群：[![加入QQ群](https://img.shields.io/badge/779343138-blue.svg)](https://jq.qq.com/?_wv=1027&k=zw11JJhj)
- 若发现bug，群内滴或Issues，提供复现路径。

## 预览
- **普通账户**
    > 企业账号：xueYi   
    员工账号：admin   
    密码：admin123

- **租管账户**
  > 企业账号：administrator   
  员工账号：admin   
  密码：admin123

- **演示**
    >https://demo.xueyitt.cn         
- **文档**
    >https://doc.xueyitt.cn     
- **视频**
    >https://space.bilibili.com/479745149

## 结构

* 采用前后端分离的模式，微服务版本前端。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。

~~~
com.xueyi     
├── xueyi-ui              // 前端框架 [80、81]
│       ├── common                                    // 公共组件
│       ├── main                                      // 默认系统 [80]
│       └── administrator                             // 租管系统 [81]
├── xueyi-gateway         // 网关模块 [8080]
├── xueyi-auth            // 认证中心 [9200]
├── xueyi-api             // 接口模块
│       └── xueyi-api-system                          // 系统接口
├── xueyi-common          // 通用模块
│       ├── xueyi-common-core                         // 核心模块
│       ├── xueyi-common-datascope                    // 权限范围
│       ├── xueyi-common-datasource                   // 多数据源
│       ├── xueyi-common-message                      // 消息队列
│       ├── xueyi-common-socket                       // 消息推送
│       ├── xueyi-common-log                          // 日志记录
│       ├── xueyi-common-redis                        // 缓存服务
│       ├── xueyi-common-security                     // 安全模块
│       └── xueyi-common-swagger                      // 系统接口
├── xueyi-modules         // 业务模块
│       ├── xueyi-file                                // 文件服务 [9300]
│       ├── xueyi-gen                                 // 代码生成 [9400]
│       ├── xueyi-job                                 // 定时任务 [9500]
│       ├── xueyi-tenant                              // 租管模块 [9700]
│       └── xueyi-system                              // 系统模块 [9600]
│               ├── authority                         // 权限模块
│               ├── cache                             // 缓存加载
│               ├── dict                              // 参数字典
│               ├── material                          // 素材模块
│               ├── monitor                           // 监控模块
│               ├── notice                            // 公告模块
│               ├── organize                          // 组织模块
│               └── role                              // 权限关系
├── xueyi-visual          // 图形化管理模块
│       └── xueyi-visual-monitor                      // 监控中心 [9100]
└── pom.xml                // 公共依赖
~~~

## 架构

<img src="https://images.gitee.com/uploads/images/2021/1108/172436_9deff9ff_7382127.png"/>


## 演示

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

## 开源
**源于开源，回归开源**
* 感谢若依开源的[RuoYi-Cloud](https://gitee.com/y_project/RuoYi-Cloud)
* 感谢小锅盖开源的[dynamic](https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter)

## 商用
- **不限制商用，遵守开源协议即可**
- **商业用途要求如下：**
    > 1.点赞三连   
      2.提供公司名及Logo（允许作者后续在站内进行展示）（QQ群内私发我即可）   
      3.发现bug或者可优化内容时进行issues或PR    
      4.社区生态共建（拉拉星星 :sleeping: ）    
      5.禁恶意针对本项目
