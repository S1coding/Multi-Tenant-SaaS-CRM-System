# Use an official OpenJDK runtime as a base image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY target/Multi-Tenant-SaaS-CRM-System-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (change 8080 if needed)
EXPOSE 8080

# Command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]