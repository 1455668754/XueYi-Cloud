# 基础镜像
FROM mysql:8.0.26

# author
MAINTAINER xueyi

# 设置docker默认编码db初始化数据乱码
ENV LANG C.UTF-8

# 执行sql脚本
ADD ./quartz.sql /docker-entrypoint-initdb.d/
ADD ./xueyi_1.sql /docker-entrypoint-initdb.d/
ADD ./xueyi_2.sql /docker-entrypoint-initdb.d/
ADD ./xy_config.sql /docker-entrypoint-initdb.d/
ADD ./xy_seata.sql /docker-entrypoint-initdb.d/

# 增加执行权限，解决启动mysql容器后，不自动执行sql脚本的问题
RUN chmod a+x /docker-entrypoint-initdb.d