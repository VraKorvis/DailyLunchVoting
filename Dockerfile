FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=0 /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]