# 基础镜像
FROM  openjdk:8-jre
# author
MAINTAINER xueyi

# 挂载目录
VOLUME /home/xueyi
# 创建目录
RUN mkdir -p /home/xueyi
# 指定路径
WORKDIR /home/xueyi
# 复制jar文件到路径
COPY ./target/xueyi-modules-tenant.jar /home/xueyi/xueyi-modules-tenant.jar

# 启动系统服务
ENTRYPOINT ["java","-jar","xueyi-modules-tenant.jar"]