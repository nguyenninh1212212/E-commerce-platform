FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app
EXPOSE 8089
EXPOSE 4049

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
