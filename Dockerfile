# 使用官方的 OpenJDK 21 基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 将项目的 JAR 文件复制到工作目录
COPY target/wise-transaction-*.jar app.jar

# 暴露应用程序使用的端口，根据 application.yml 文件，这里是 8088
EXPOSE 8088

# 定义 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/heapdump.hprof"

# 定义启动命令，将 JVM 参数传递给 Java 命令
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
