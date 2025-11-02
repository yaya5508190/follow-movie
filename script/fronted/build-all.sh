#!/bin/bash

echo "====================================="
echo "开始构建所有前端项目"
echo "====================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 构建函数
build_project() {
    local name=$1
    local path=$2
    local command=$3

    echo "[$4] 构建 $name..."
    cd "$path" || exit 1
    npm run "$command"
    if [ $? -ne 0 ]; then
        echo -e "${RED}错误: $name 构建失败${NC}"
        exit 1
    fi
    cd - > /dev/null || exit 1
    echo -e "${GREEN}✓ $name 构建完成${NC}"
    echo ""
}

# 执行构建
build_project "follow-movie-web" "../../follow-movie-web/fronted-web" "build" "1/5"
build_project "q-bittorrent" "../../plugins/media-download/q-bittorrent/fronted-web" "build-with-prefix" "2/5"
build_project "z-space" "../../plugins/pre-auth/z-space/fronted-web" "build-with-prefix" "3/5"
build_project "media-hub" "../../plugins/media-hub/fronted-web" "build-with-prefix" "4/5"
build_project "m-team" "../../plugins/media-fetch/m-team/fronted-web" "build-with-prefix" "5/5"

echo "====================================="
echo -e "${GREEN}✓ 所有前端项目构建完成！${NC}"
echo "====================================="

