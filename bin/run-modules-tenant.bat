@echo off
echo.
echo [��Ϣ] ʹ��Jar��������Modules-System���̡�
echo.

cd %~dp0
cd ../xueyi-modules/xueyi-tenant/target

set JAVA_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 -jar %JAVA_OPTS% xueyi-modules-tenant.jar

cd bin
pause