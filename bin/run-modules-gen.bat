@echo off
echo.
echo [��Ϣ] ʹ��Jar��������Modules-Gen���̡�
echo.

cd %~dp0
cd ../xueyi-modules/xueyi-gen/target

set JAVA_OPTS=-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 -jar %JAVA_OPTS% xueyi-modules-gen.jar

cd bin
pause