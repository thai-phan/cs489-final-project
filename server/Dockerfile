# syntax=docker/dockerfile:1

FROM gradle:8.7-jdk17 AS builder
WORKDIR /workspace
COPY . .
RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

