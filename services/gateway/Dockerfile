# Stage 1: Build the application
# 阶段1：构建应用程序
FROM maven:3.8.4-eclipse-temurin-17 AS build
# 使用 Maven 3.8.4 和 OpenJDK 17 的镜像作为构建阶段的基础镜像

WORKDIR /app
# 设置工作目录为 /app

COPY pom.xml .
# 复制 Maven 配置文件 pom.xml 到容器中的当前工作目录

COPY src ./src
# 复制项目源码目录 src 到容器的 /app/src 目录

RUN mvn clean package -DskipTests
# 使用 Maven 命令构建项目，跳过测试，生成 JAR 文件

# Stage 2: Run the application
# 阶段2：运行应用程序
FROM openjdk:17-slim
# 使用轻量级的 OpenJDK 17 JRE 镜像作为运行阶段的基础镜像

WORKDIR /app
# 设置工作目录为 /app

# Install curl
# 安装 curl 用于健康检查
USER root
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
# 使用 root 用户安装 curl，然后清理 apt 缓存以减小镜像体积

# Set non-root user for security
# 创建一个非root用户以提升安全性
RUN adduser --system --group --no-create-home appuser
USER appuser
# 切换到 appuser 用户，防止应用以 root 权限运行，提升安全性

# Copy only the JAR from the build stage
# 从构建阶段复制生成的 JAR 文件
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar app.jar
# 复制 JAR 文件到当前阶段的 /app 目录

# Environment and JVM options
# 配置环境变量和 JVM 启动参数
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8222
# 声明容器要监听的端口，这里是 8222 端口

# Health check
# 配置健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8222/actuator/health || exit 1
# 设置健康检查

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
# 启动应用
