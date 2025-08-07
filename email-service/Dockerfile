# -------- BUILD STAGE --------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy dependencies files first for cache optimization
COPY pom.xml .
COPY src ./src

# Build with skip tests to speed up (optional: remove -DskipTests in prod)
RUN mvn clean package -DskipTests

# -------- RUNTIME STAGE --------
FROM openjdk:21-jdk-slim AS runner

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Optional: use non-root user in prod
# RUN useradd -ms /bin/bash appuser && chown -R appuser /app
# USER appuser

EXPOSE 8094

ENTRYPOINT ["java", "-jar", "app.jar"]
