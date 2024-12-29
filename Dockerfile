# Use a base image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the pre-built JAR file into the container
COPY /app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the Spring application
EXPOSE 8080

# Specify the entry point for the application
ENTRYPOINT ["java", "-jar", "app.jar"]