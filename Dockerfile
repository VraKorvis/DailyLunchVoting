FROM eclipse-temurin:21-jdk AS builder

RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/dailylunchvoting-1.0.jar app.jar
EXPOSE 8080

RUN useradd -m appuser && chown -R appuser:appuser /app
USER appuser

CMD ["java", "-jar", "app.jar"]