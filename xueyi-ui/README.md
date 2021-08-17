## 开发

```bash
# 克隆项目
git clone https://gitee.com/xueyitiantang/xueyi-cloud.git

# 进入项目目录
cd xueyi-ui

# 安装依赖 | 执行此步则无需执行下一步(上下两条任选一条进行执行)
yarn install && npm-run-all install:*
yarn install; npm run install-all

# 只安装各模块依赖 | 可以运行npm 脚本 install-all
npm-run-all install:*

# 启动服务 | 可以运行npm 脚本 的start-all(上下两条任选一条进行执行)
npm-run-all --parallel start:*
npm run start-all
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建主模块的生产环境
cd main && vue-cli-service build

# 构建租户管理模块的生产环境
cd administrator && vue-cli-service build

# 构建全部模块的生产环境 | 可以运行npm 脚本 的build-all
concurrently "cd administrator && vue-cli-service build" "cd main && vue-cli-service build"
```

### 前端结构

~~~  
xueyi-ui              // 前端框架 [80、81]
├── common                            // 公共组件
│     ├── src                         // 源代码
│          ├── api                    // 全局公用请求
│          ├── assets                 // 主题 字体等静态资源
│          ├── components             // 全局公用组件
│          └── utils                  // 全局公用方法
│     └── package.json                // package.json
│
├── main                              // 默认系统 [80]
│     ├── build                       // 构建相关  
│     ├── bin                         // 执行脚本
│     ├── public                      // 公共文件
│          ├── favicon.ico            // favicon图标
│          └── index.html             // html模板
│     ├── src                         // 源代码
│          ├── api                    // 所有请求
│          ├── directive              // 全局指令
│          ├── layout                 // 布局
│          ├── router                 // 路由
│          ├── store                  // store管理
│          ├── utils                  // 公用方法
│          ├── views                  // view
│          ├── App.vue                // 入口页面
│          ├── main.js                // 入口 加载组件 初始化等
│          ├── permission.js          // 权限管理
│          └── settings.js            // 系统配置
│     ├── .editorconfig               // 编码格式
│     ├── .env.development            // 开发环境配置
│     ├── .env.production             // 生产环境配置
│     ├── .env.staging                // 测试环境配置
│     ├── .eslintignore               // 忽略语法检查
│     ├── .eslintrc.js                // eslint 配置项
│     ├── .gitignore                  // git 忽略项
│     ├── babel.config.js             // babel.config.js
│     ├── package.json                // package.json
│     └── vue.config.js               // vue.config.js
│
├── administrator                     // 租管系统 [81]
│     ├── build                       // 构建相关  
│     ├── bin                         // 执行脚本
│     ├── public                      // 公共文件
│          ├── favicon.ico            // favicon图标
│          └── index.html             // html模板
│     ├── src                         // 源代码
│          ├── api                    // 所有请求
│          ├── directive              // 全局指令
│          ├── layout                 // 布局
│          ├── router                 // 路由
│          ├── store                  // store管理
│          ├── utils                  // 公用方法
│          ├── views                  // view
│          ├── App.vue                // 入口页面
│          ├── main.js                // 入口 加载组件 初始化等
│          ├── permission.js          // 权限管理
│          └── settings.js            // 系统配置
│     ├── .editorconfig               // 编码格式
│     ├── .env.development            // 开发环境配置
│     ├── .env.production             // 生产环境配置
│     ├── .env.staging                // 测试环境配置
│     ├── .eslintignore               // 忽略语法检查
│     ├── .eslintrc.js                // eslint 配置项
│     ├── .gitignore                  // git 忽略项
│     ├── babel.config.js             // babel.config.js
│     ├── package.json                // package.json
│     └── vue.config.js               // vue.config.js
│   
├── .gitignore                        // git 忽略项
└── package.json                      // package.json
~~~
