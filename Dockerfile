# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build

# Add metadata
LABEL maintainer="Your Name <your.email@example.com>"
LABEL description="Spring Boot Application"

# Set working directory early to make commands cleaner
WORKDIR /app

# Leverage Docker cache by copying only files required at each stage
COPY pom.xml .  # First copy only pom.xml to cache dependencies
RUN mvn dependency:go-offline --no-transfer-progress  # Reduced cluttered logs

# Copy all other files (source code)
COPY src ./src

# Build the application (skip tests to improve build times)
RUN mvn clean package -DskipTests --no-transfer-progress

# Final stage - Slimmed runtime image
FROM eclipse-temurin:17-jre-slim

# Add metadata
LABEL description="Spring Boot Application"

# Create a dedicated, unprivileged user for security
RUN useradd --create-home --shell /bin/bash spring

# Set a non-root working directory
WORKDIR /app

# Copy the built application JAR file from build stage
COPY --from=build /app/target/*.jar /app/application.jar

# Set ownership of the application folder (security)
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Set environment variables with sane defaults
ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE="default"

# Expose the application port
EXPOSE 8080

# Add health check for Kubernetes / Docker monitoring
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application as non-root user
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar"]