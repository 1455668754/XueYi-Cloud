@echo off
echo.
echo [��Ϣ] ʹ��Jar��������Monitor���̡�
echo.

cd %~dp0
cd ../xueyi-visual/xueyi-monitor/target

set JAVA_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 -jar %JAVA_OPTS% xueyi-visual-monitor.jar

cd bin
pause