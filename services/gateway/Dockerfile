# Stage 1: Build the application
# 阶段1：构建应用程序
FROM maven:3.8.4-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# 阶段2：运行应用程序
FROM openjdk:17-slim
WORKDIR /app

# Install curl and wget, then install dockerize
USER root
RUN apt-get update && apt-get install -y curl wget && rm -rf /var/lib/apt/lists/*
RUN wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.6.1.tar.gz \
    && rm dockerize-linux-amd64-v0.6.1.tar.gz

# Set non-root user for security
RUN adduser --system --group --no-create-home appuser
USER appuser

# Copy only the JAR from the build stage
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar app.jar

# Environment and JVM options
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8222

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8222/actuator/health || exit 1

# Start application with dockerize waiting for config-server
CMD ["dockerize", "-wait", "http://config-server:8888/actuator/health", "-timeout", "30s", "sh", "-c", "java $JAVA_OPTS -jar app.jar"]
