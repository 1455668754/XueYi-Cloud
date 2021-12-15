#!/bin/sh

# 使用说明，用来提示输入参数
usage() {
	echo "Usage: sh 执行脚本.sh [port|base|modules|monitor|stop|rm]"
	exit 1
}

# 开启所需端口
port(){
	firewall-cmd --add-port=80/tcp --permanent
	firewall-cmd --add-port=81/tcp --permanent
	firewall-cmd --add-port=8080/tcp --permanent
	firewall-cmd --add-port=8848/tcp --permanent
	firewall-cmd --add-port=9848/tcp --permanent
	firewall-cmd --add-port=9849/tcp --permanent
	firewall-cmd --add-port=6379/tcp --permanent
	firewall-cmd --add-port=15672/tcp --permanent
	firewall-cmd --add-port=4369/tcp --permanent
	firewall-cmd --add-port=5672/tcp --permanent
	firewall-cmd --add-port=25672/tcp --permanent
	firewall-cmd --add-port=3306/tcp --permanent
	firewall-cmd --add-port=9100/tcp --permanent
	firewall-cmd --add-port=9200/tcp --permanent
	firewall-cmd --add-port=9300/tcp --permanent
	firewall-cmd --add-port=9400/tcp --permanent
	firewall-cmd --add-port=9500/tcp --permanent
	firewall-cmd --add-port=9600/tcp --permanent
	firewall-cmd --add-port=9700/tcp --permanent
	service firewalld restart
}

# 启动基础环境（必须）
base(){
	docker-compose up -d xueyi-mysql xueyi-redis xueyi-nacos xueyi-rabbit
}

# 启动程序模块（必须）
modules(){
	docker-compose up -d xueyi-gateway xueyi-auth xueyi-modules-system xueyi-modules-tenant xueyi-nginx
}

# 启动程序模块 | 次要 | 根据需求启动
monitor(){
	docker-compose up -d xueyi-modules-file xueyi-modules-gen xueyi-modules-job xueyi-visual-monitor
}

# 关闭所有环境/模块
stop(){
	docker-compose stop
}

# 删除所有环境/模块
rm(){
	docker-compose rm
}

# 根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"port")
	port
;;
"base")
	base
;;
"modules")
	modules
;;
"monitor")
	monitor
;;
"stop")
	stop
;;
"rm")
	rm
;;
*)
	usage
;;
esac
