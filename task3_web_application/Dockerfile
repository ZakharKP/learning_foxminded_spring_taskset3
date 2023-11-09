FROM maven:3-eclipse-temurin-19 AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:8-jre-alpine
# WORKDIR /app
VOLUME /tmp
COPY --from=builder /app/target/*.jar app.jar
COPY src/main/resources /src/main/resources
ENTRYPOINT ["java","-jar","/app.jar"]