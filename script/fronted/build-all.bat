@echo off
chcp 65001 >nul
echo =====================================
echo 开始构建所有前端项目
echo =====================================
echo.

echo [1/5] 构建主项目 follow-movie-web...
cd ..\..\follow-movie-web\fronted-web
call npm run build
if %errorlevel% neq 0 (
    echo 错误: follow-movie-web 构建失败
    exit /b %errorlevel%
)
cd ..\..\script\fronted
echo ✓ follow-movie-web 构建完成
echo.

echo [2/5] 构建插件 q-bittorrent...
cd ..\..\plugins\media-download\q-bittorrent\fronted-web
call npm run build-with-prefix
if %errorlevel% neq 0 (
    echo 错误: q-bittorrent 构建失败
    exit /b %errorlevel%
)
cd ..\..\..\..\script\fronted
echo ✓ q-bittorrent 构建完成
echo.

echo [3/5] 构建插件 z-space...
cd ..\..\plugins\pre-auth\z-space\fronted-web
call npm run build-with-prefix
if %errorlevel% neq 0 (
    echo 错误: z-space 构建失败
    exit /b %errorlevel%
)
cd ..\..\..\..\script\fronted
echo ✓ z-space 构建完成
echo.

echo [4/5] 构建插件 media-hub...
cd ..\..\plugins\media-hub\fronted-web
call npm run build-with-prefix
if %errorlevel% neq 0 (
    echo 错误: media-hub 构建失败
    exit /b %errorlevel%
)
cd ..\..\..\script\fronted
echo ✓ media-hub 构建完成
echo.

echo [5/5] 构建插件 m-team...
cd ..\..\plugins\media-fetch\m-team\fronted-web
call npm run build-with-prefix
if %errorlevel% neq 0 (
    echo 错误: m-team 构建失败
    exit /b %errorlevel%
)
cd ..\..\..\..\script\fronted
echo ✓ m-team 构建完成
echo.

echo =====================================
echo ✓ 所有前端项目构建完成！
echo =====================================

