FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/logistic-1.0.0.war app.war

EXPOSE 4800

ENTRYPOINT ["java", "-jar", "/app/app.war"]
