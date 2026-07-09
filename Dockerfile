# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests clean package
RUN JAR_FILE=$(ls target/*.jar | grep -v 'original' | head -n 1) && cp "$JAR_FILE" app.jar

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/app.jar /app/app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
