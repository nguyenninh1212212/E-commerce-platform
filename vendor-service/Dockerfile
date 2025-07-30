# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Copy pom.xml and download dependencies first (cache-friendly)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-slim

WORKDIR /app

# Copy jar from build stage
COPY --from=build /build/target/vendor-service-0.0.1-SNAPSHOT.jar app.jar

# Expose ports
EXPOSE 8093
EXPOSE 4093

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
