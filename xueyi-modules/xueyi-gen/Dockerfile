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
COPY ./target/xueyi-modules-gen.jar /home/xueyi/xueyi-modules-gen.jar

# 启动代码生成服务
ENTRYPOINT ["java","-jar","xueyi-modules-gen.jar"]