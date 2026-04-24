# syntax=docker/dockerfile:1

FROM gradle:9.4.1-jdk25 AS builder
WORKDIR /workspace
COPY . .
RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

