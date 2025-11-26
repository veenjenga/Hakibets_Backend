# Stage 1: Build the JAR with Maven
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B # Cache dependencies
# Copy source code
COPY src ./src
# Build the JAR
RUN mvn clean package -DskipTests # Skip tests for speed

# Stage 2: Run the JAR with OpenJDK
FROM openjdk:17-jre-slim
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
# Expose port 8080 (Spring Boot default)
EXPOSE 8080
# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]