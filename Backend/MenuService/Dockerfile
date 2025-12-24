# Using a multi-stage build to handle local dependencies
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Install BuildingBlocks (Shared Library) first
COPY BuildingBlocks /app/BuildingBlocks
RUN mvn -f /app/BuildingBlocks/pom.xml clean install -DskipTests

# 2. Build Service
COPY MenuService /app/MenuService
RUN mvn -f /app/MenuService/pom.xml clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/MenuService/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
