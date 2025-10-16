# 使用官方OpenJDK运行环境作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置维护者信息
LABEL maintainer="koubotools"

# 设置工作目录
WORKDIR /app

# 复制Maven构建的JAR文件到容器中
COPY target/script-generator-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行JAR文件
ENTRYPOINT ["java","-jar","/app/app.jar"]