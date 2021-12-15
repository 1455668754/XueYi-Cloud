#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/quartz.sql ./mysql/db
cp ../sql/xueyi_1.sql ./mysql/db
cp ../sql/xueyi_2.sql ./mysql/db
cp ../sql/xy_config.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../xueyi-ui/main/dist/** ./nginx/html/main
cp -r ../xueyi-ui/administrator/dist/** ./nginx/html/administrator


# copy jar
echo "begin copy xueyi-gateway "
cp ../xueyi-gateway/target/xueyi-gateway.jar ./xueyi/gateway/jar

echo "begin copy xueyi-auth "
cp ../xueyi-auth/target/xueyi-auth.jar ./xueyi/auth/jar

echo "begin copy xueyi-visual "
cp ../xueyi-visual/xueyi-monitor/target/xueyi-visual-monitor.jar  ./xueyi/visual/monitor/jar

echo "begin copy xueyi-modules-system "
cp ../xueyi-modules/xueyi-system/target/xueyi-modules-system.jar ./xueyi/modules/system/jar

echo "begin copy xueyi-modules-tenant "
cp ../xueyi-modules/xueyi-tenant/target/xueyi-modules-tenant.jar ./xueyi/modules/tenant/jar

echo "begin copy xueyi-modules-file "
cp ../xueyi-modules/xueyi-file/target/xueyi-modules-file.jar ./xueyi/modules/file/jar

echo "begin copy xueyi-modules-job "
cp ../xueyi-modules/xueyi-job/target/xueyi-modules-job.jar ./xueyi/modules/job/jar

echo "begin copy xueyi-modules-gen "
cp ../xueyi-modules/xueyi-gen/target/xueyi-modules-gen.jar ./xueyi/modules/gen/jar