FROM maven:3.9.6-eclipse-temurin-21 AS build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests
FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /target/product-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8083
EXPOSE 4043

ENTRYPOINT ["java", "-jar", "app.jar"]
