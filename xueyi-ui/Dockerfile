# 基础镜像
FROM nginx
# author
MAINTAINER xueyi

# 挂载目录
VOLUME /home/xueyi/projects/xueyi-ui
# 创建目录
RUN mkdir -p /home/xueyi/projects/xueyi-ui
# 指定路径
WORKDIR /home/xueyi/projects/xueyi-ui
# 复制conf文件到路径
COPY ./nginx/conf/nginx.conf /etc/nginx/nginx.conf
# 复制html文件到路径
COPY ./dist /home/xueyi/projects/xueyi-ui
