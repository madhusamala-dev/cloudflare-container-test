# -------------------------------------------------------------
# Build Stage
# -------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml first and resolve dependencies for efficient layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Build the executable Spring Boot JAR
RUN mvn clean package -DskipTests

# -------------------------------------------------------------
# Runtime Stage
# -------------------------------------------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create a non-root user and group for security
RUN groupadd -r appgroup && useradd -r -g appgroup -d /app -s /usr/sbin/nologin appuser

# Copy the executable JAR from the build stage using an explicit JAR filename
COPY --from=builder /build/target/cloudflare-java17-test.jar /app/cloudflare-java17-test.jar

# Set ownership of the application directory to the non-root user
RUN chown -R appuser:appgroup /app

# Switch to the non-root user
USER appuser:appgroup

# Expose container port
EXPOSE 8080

# Configure JVM flags to respect container memory limits
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Execute application using java -jar
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/cloudflare-java17-test.jar"]
