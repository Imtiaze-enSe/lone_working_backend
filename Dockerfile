# Stage 1: Build the application
FROM maven:3.9.2-amazoncorretto-17 AS build

WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM amazoncorretto:17

WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/loneworking-0.0.1-SNAPSHOT.jar ./loneworking.jar

# Expose the port on which the app runs
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "loneworking.jar"]
