# ---------- Stage 1: Build app ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# 1️⃣ Copy pom.xml riêng để cache dependency
COPY pom.xml .

# 2️⃣ Download dependencies trước
RUN mvn dependency:go-offline -B

# 3️⃣ Copy source code sau (thường xuyên thay đổi nên để sau cùng)
COPY src ./src

# 4️⃣ Build app
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run app ----------
FROM openjdk:21-jdk-slim

# 5️⃣ Copy jar từ stage build
COPY --from=build /app/target/*.jar /app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "/app.jar"]
