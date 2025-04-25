# Use Amazon Corretto JDK 23 (Alpine variant for small size)
FROM amazoncorretto:23-alpine

# Set working directory inside container
WORKDIR /app

# Copy the Spring Boot jar built by Gradle
COPY build/libs/myapp-1.0.1.jar /app

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "myapp-1.0.1.jar"]
