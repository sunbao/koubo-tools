# 使用官方OpenJDK运行环境作为基础镜像
FROM openjdk:8-jre-alpine

# 设置维护者信息
LABEL maintainer="sunbao"

# 设置工作目录
WORKDIR /app

# 直接复制已编译好的JAR文件
# 注意：在构建镜像时需要确保target目录下有script-generator-1.0.0.jar文件
COPY target/script-generator-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行JAR文件
ENTRYPOINT ["java","-jar","/app/app.jar"]