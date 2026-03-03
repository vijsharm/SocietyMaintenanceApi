FROM eclipse-temurin:17-jre-alpine

# Create working directory
WORKDIR /app

# Copy pre-built jar from target folder
COPY target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-XX:+UseContainerSupport","-jar","app.jar"]