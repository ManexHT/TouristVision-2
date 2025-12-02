# ───────────────────────────────────────────────
# Etapa 1: Build con Maven
# ───────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ───────────────────────────────────────────────
# Etapa 2: Runtime (solo JDK)
# ───────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia el .war generado desde el builder
COPY --from=builder /app/target/*.war app.war

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.war"]
