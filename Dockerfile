# 1단계: 빌드용 Gradle 이미지
FROM gradle:7.6.0-jdk17 AS builder
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN chmod +x ./gradlew && ./gradlew build --no-daemon

# 2단계: 실행용 JDK 이미지
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
