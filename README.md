## 平台简介

目前主要做了五块：
* 共享多租户；
* 租户独立素材模块（各租户的素材建档，统一管理）；
* 重置角色权限，部门-岗位-用户层级管理，上级禁用时同步禁用所有下级；
* 建立模块组，建立多前端时可以作为通用入口，同时模块组与菜单组关联，如用统一后台多前端模式可以通过模块-菜单方式进行控制， 不同前端显示对应前端模块的菜单；
* 重构权限控制，角色-模块-菜单控制模式，同时角色-部门|角色-岗位|角色-用户多种角色授权方式，岗位继承部门角色，用户继承岗位、部门角色。

基于本系统还有许多内容是我想要加入进来的，但限于时间问题，后续慢慢更新进来，刚发布初期可能存在一些bug，如遇到欢迎提出Issues，看到会进行解决。

关于文档这块，近期有时间会更新上来。


同时也非常感谢ruoyi大佬。

* 采用前后端分离的模式，微服务版本前端。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。

#### 友情链接 [若依/RuoYi-Cloud](https://gitee.com/zhangmrit/ruoyi-cloud) Ant Design版本。

## 系统模块

~~~
com.ruoyi     
├── ruoyi-ui              // 前端框架 [80]
├── ruoyi-gateway         // 网关模块 [8080]
├── ruoyi-auth            // 认证中心 [9200]
├── ruoyi-api             // 接口模块
│       └── ruoyi-api-system                          // 系统接口
├── ruoyi-common          // 通用模块
│       └── ruoyi-common-core                         // 核心模块
│       └── ruoyi-common-datascope                    // 权限范围
│       └── ruoyi-common-datasource                   // 多数据源
│       └── ruoyi-common-log                          // 日志记录
│       └── ruoyi-common-redis                        // 缓存服务
│       └── ruoyi-common-security                     // 安全模块
│       └── ruoyi-common-swagger                      // 系统接口
├── ruoyi-modules         // 业务模块
│       └── ruoyi-system                              // 系统模块 [9201]
│               └── authority                         // 权限模块
│               └── dict                              // 字典配置
│               └── material                          // 素材模块
│               └── monitor                           // 监控模块
│               └── notice                            // 公告模块
│               └── organize                          // 组织模块
│               └── role                              // 权限关系
│       └── ruoyi-gen                                 // 代码生成 [9202]
│       └── ruoyi-job                                 // 定时任务 [9203]
│       └── ruoyi-file                                // 文件服务 [9300]
├── ruoyi-visual          // 图形化管理模块
│       └── ruoyi-visual-monitor                      // 监控中心 [9100]
├──pom.xml                // 公共依赖
~~~

## 架构图

<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 在线构建器：拖动表单元素生成相应的HTML代码。
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 在线体验
- 企业账号/员工账号/密码
- xueYi/admin/admin123

演示地址：http://ruoyi.vip  
文档地址：http://doc.ruoyi.vip

## 演示图

<table>
    <tr>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140445_bdd838af_7382127.png "8.png")</td>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140513_48ff7abd_7382127.png "1.png")</td>
    </tr>
    <tr>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140849_681cc401_7382127.png "11.png")</td>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140859_d9190514_7382127.png "12.png")</td>
    </tr>
    <tr>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140524_ec4af10e_7382127.png "2.png")</td>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140534_98a211b1_7382127.png "3.png")</td>
    </tr>
    <tr>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140545_9c62338d_7382127.png "4.png")</td>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140558_1c729ee4_7382127.png "5.png")</td>
    </tr>
	<tr>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140607_d6697a5a_7382127.png "6.png")</td>
        <td>![输入图片说明](https://images.gitee.com/uploads/images/2021/0501/140619_628675c2_7382127.png "7.png")</td>
    </tr>	 
</table>