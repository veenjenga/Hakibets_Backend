# Stage 1: Build the JAR with Maven (using maven:3.9.6-eclipse-temurin-17, latest stable)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copy pom.xml first to cache dependencies (faster builds)
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copy source code
COPY src ./src
# Build the JAR, skipping tests for speed
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR with OpenJDK (using eclipse-temurin:17-jre-alpine, lightweight JRE)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
# Expose port 8080 (Spring Boot default)
EXPOSE 8080
# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]