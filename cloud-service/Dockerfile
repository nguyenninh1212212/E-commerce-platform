FROM maven:3.9.6-eclipse-temurin-21 AS build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests
FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /target/cloud-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8081

ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
