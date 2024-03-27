FROM maven:3.5.3-jdk-8-alpine as BUILD

WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true

RUN ls /app


# Use a suitable base image for Java 17 applications
FROM openjdk:17-alpine

# Set a working directory
# WORKDIR /app
WORKDIR /run

# Copy the project files
COPY . /run


# Expose the application port (assuming it's 8080)
EXPOSE 8080

# Ensure the GeoLite2 City database file is accessible
RUN mkdir -p src/main/resources
COPY src/main/resources/GeoLite2-City.mmdb /app/src/main/resources/GeoLite2-City.mmdb

# Start the application using the built JAR
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

