FROM maven:3.8.3-openjdk-17 as BUILD

WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true



# Use a suitable base image for Java 17 applications
FROM openjdk:17-alpine

# Set a working directory
# WORKDIR /app
WORKDIR /run

# Copy the project files
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /run/demo-0.0.1-SNAPSHOT.jar 

RUN ls /run

# Expose the application port (assuming it's 8080)
EXPOSE 8080

# Ensure the GeoLite2 City database file is accessible
RUN mkdir -p src/main/resources
COPY src/main/resources/GeoLite2-City.mmdb /app/src/main/resources/GeoLite2-City.mmdb

# Start the application using the built JAR
ENTRYPOINT ["java", "-jar", "/run/demo-0.0.1-SNAPSHOT.jar"]

