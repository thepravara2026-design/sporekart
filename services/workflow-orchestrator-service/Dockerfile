FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/workflow-orchestrator-service-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8097
ENTRYPOINT ["java", "-jar", "app.jar"]
