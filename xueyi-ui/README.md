## 开发

```bash
# 克隆项目
git clone https://gitee.com/xueyitiantang/xueyi-cloud.git

# 进入项目目录
cd xueyi-ui

# 安装依赖 | 执行此步则无需执行下一步(上下两条任选一条进行执行)
yarn install && npm-run-all install:*
yarn install ; npm run install-all

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
