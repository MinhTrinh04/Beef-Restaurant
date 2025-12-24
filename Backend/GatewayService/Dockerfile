FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY GatewayService /app/GatewayService
RUN mvn -f /app/GatewayService/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/GatewayService/target/*.jar app.jar

EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]
