# Stage 1: Build the application
# 阶段1：构建应用程序
#FROM maven:3.8.4-openjdk-17 AS build
FROM maven:3.8.4-eclipse-temurin-17 AS build
# 使用 Maven 3.8.4 和 OpenJDK 11 的镜像作为构建阶段的基础镜像

WORKDIR /app
# 设置工作目录为 /app，这里将是代码和依赖文件的存放位置

COPY pom.xml .
# 将 Maven 的配置文件 pom.xml 复制到容器中的当前工作目录

COPY src ./src
# 将项目的源码目录 src 复制到容器的 /app/src 目录中

RUN mvn clean package -DskipTests
# 使用 Maven 命令构建项目，跳过测试，生成 JAR 文件

# Stage 2: Run the application
# 阶段2：运行应用程序
#FROM openjdk:17-jre-slim
FROM openjdk:17-slim
# 使用轻量级的 OpenJDK 11 JRE 镜像作为运行阶段的基础镜像

WORKDIR /app
# 设置工作目录为 /app，将应用文件放置在该目录下

# Set non-root user for security
# 创建一个非root用户以提升安全性
RUN adduser --system --group --no-create-home appuser
# 创建一个系统用户 appuser，无主目录，用于运行应用

USER appuser
# 切换到 appuser 用户，防止应用以 root 权限运行，提升安全性

# Copy only the JAR from the build stage
# 从构建阶段复制生成的 JAR 文件
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar app.jar
# 从上一个阶段的 /app/target 目录中复制生成的 JAR 文件到当前阶段的 /app 目录中，命名为 app.jar

# Environment and JVM options
# 配置环境变量和 JVM 启动参数
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
# 设置 JAVA_OPTS 环境变量，指定 JVM 在容器中的优化选项，例如支持容器内存限制，并将最大内存使用量限制为容器内存的 75%

EXPOSE 8222
# 声明容器要监听的端口，这里是 8761 端口，用于外部访问

# Health check
# 配置健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8761/actuator/health || exit 1
# 设置健康检查，每隔 30 秒检查一次，超时时间为 5 秒，启动初期等待 10 秒，重试 3 次
# 使用 curl 请求应用的健康检查端点（例如 Spring Boot 的 /actuator/health），如果失败则返回状态码 1 表示容器不健康

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
# 启动应用，执行 Java 命令，使用 JAVA_OPTS 参数并运行 app.jar 文件
