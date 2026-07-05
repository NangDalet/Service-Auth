FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.war app.jar
COPY application-prod.properties ./config/application-prod.properties

ENV SPRING_PROFILES_ACTIVE=prod
ENV EUREKA_CLIENT_ENABLED=true

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
