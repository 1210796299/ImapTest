@echo off
chcp 65001
echo  启动HHIMAPServer
java -cp hh-imap.jar com.hh.imap.HHIMAPServer &
pause
