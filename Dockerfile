FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY target/*.war app.war

EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.war"]
