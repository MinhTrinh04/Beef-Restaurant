FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY BuildingBlocks /app/BuildingBlocks
RUN mvn -f /app/BuildingBlocks/pom.xml clean install -DskipTests

COPY BasketService /app/BasketService
RUN mvn -f /app/BasketService/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/BasketService/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
