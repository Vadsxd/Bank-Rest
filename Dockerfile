FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=builder /app/target/*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]