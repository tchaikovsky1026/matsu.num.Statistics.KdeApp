@echo off
setlocal

rem ===== setup =====
set ROOT_PATH=%~dp0
set LIB_PATH=%ROOT_PATH%/lib

set MODULE_PATH=%ROOT_PATH%;%LIB_PATH%;
set MODULE_NAME=matsu.num.Statistics.KdeApp
set MAIN_CLASS=matsu.num.statistics.kdeapp.kde1d.Kde1dCliEntryPoint

rem ===== execute =====
java ^
  -p "%MODULE_PATH%" ^
  -m "%MODULE_NAME%/%MAIN_CLASS%" ^
  %*

endlocal
