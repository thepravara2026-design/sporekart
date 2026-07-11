FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/search-service-*.jar app.jar
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "/app/app.jar"]