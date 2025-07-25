### STAGE 1: Build source code
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy POM trước để cache dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy src sau để không làm vỡ cache khi code chưa đổi
COPY src ./src

# Build app
RUN mvn clean package -DskipTests

### STAGE 2: Run the built JAR
FROM eclipse-temurin:21-jdk

WORKDIR /app
EXPOSE 8089
EXPOSE 4049

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
