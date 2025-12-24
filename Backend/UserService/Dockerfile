FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY BuildingBlocks /app/BuildingBlocks
RUN mvn -f /app/BuildingBlocks/pom.xml clean install -DskipTests

COPY UserService /app/UserService
RUN mvn -f /app/UserService/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/UserService/target/*.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
