# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

COPY src/ src/
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app

# Tối ưu JVM cho máy chủ 1GB RAM
ENV JAVA_OPTS="-Xms128m -Xmx300m -XX:+UseSerialGC"

COPY --from=builder /app/target/*.jar /app/app.jar
RUN mkdir -p /app/uploads

EXPOSE 8080
# Thêm 'exec' để đảm bảo Java nhận được lệnh tắt an toàn từ Docker
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]