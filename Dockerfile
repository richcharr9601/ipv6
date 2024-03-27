# Use a suitable base image for Java 17 applications
FROM openjdk:17-alpine

# Set a working directory
WORKDIR /app

# Copy the project files
COPY . /app

# Expose the application port (assuming it's 8080)
EXPOSE 8080

# Ensure the GeoLite2 City database file is accessible
RUN mkdir -p src/main/resources
COPY src/main/resources/GeoLite2-City.mmdb /app/src/main/resources/GeoLite2-City.mmdb

# Start the application using the built JAR
ENTRYPOINT ["java", "-jar", "target/demouploadanddownload_0.0.1-SNAPSHOT.jar"]

