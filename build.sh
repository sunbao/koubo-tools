#!/bin/bash

# 构建脚本：编译项目并构建Docker镜像

echo "开始构建项目..."

# 检查是否安装了Maven
if ! command -v mvn &> /dev/null
then
    echo "错误：未找到Maven，请先安装Maven"
    exit 1
fi

# 清理并编译项目
echo "正在编译项目..."
mvn clean package -DskipTests

# 检查编译是否成功
if [ $? -ne 0 ]; then
    echo "项目编译失败"
    exit 1
fi

# 检查JAR文件是否存在
if [ ! -f "target/script-generator-1.0.0.jar" ]; then
    echo "错误：未找到JAR文件 target/script-generator-1.0.0.jar"
    exit 1
fi

echo "项目编译成功"

# 构建Docker镜像
echo "正在构建Docker镜像..."
docker build -t koubo-script-generator . --no-cache

if [ $? -ne 0 ]; then
    echo "Docker镜像构建失败"
    exit 1
fi

echo "Docker镜像构建成功！"
echo "镜像名称: koubo-script-generator"
echo "可以通过以下命令运行容器："
echo "docker run -p 8080:8080 koubo-script-generator"