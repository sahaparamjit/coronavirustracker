# Use an OpenJDK base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/coronavirusapp-0.0.1-SNAPSHOT.jar /app

# Expose the port that your Spring Boot application listens on
EXPOSE 8082

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "coronavirusapp-0.0.1-SNAPSHOT.jar"]

# docker build -t coronavirustracker .
# docker run -p 8082:8082 coronavirustracker
# docker run -d -p 8082:8082 --restart unless-stopped coronavirustracker
