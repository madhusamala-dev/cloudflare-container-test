# Cloudflare Java 17 Test Application

A lightweight Spring Boot 3.2.5 project running on Java 17, designed to test compatibility with Docker and Cloudflare Containers.

---

## 1. Technology Stack

- **Java:** 17
- **Spring Boot:** 3.2.5
- **Build Tool:** Maven 3.9+
- **Embedded Web Server:** Tomcat
- **Monitoring:** Spring Boot Actuator
- **Containerization:** Multi-stage Dockerfile (`maven:3.9-eclipse-temurin-17` builder, `eclipse-temurin:17-jre` runtime)

---

## 2. Prerequisites & Verification

Ensure Java 17 and Maven are installed and configured:

```bash
java -version
mvn -version
```

---

## 3. Running Automated Tests

Run the test suite using Maven:

```bash
mvn clean test
```

All 5 tests in `HelloControllerTest` will run:
1. Application context initialization check.
2. `GET /hello` endpoint test for HTTP 200, application name, status, and message.
3. Dynamic Java runtime version detection.
4. `GET /health` endpoint test for HTTP 200 with `{"status":"UP"}`.
5. `GET /actuator/health` endpoint test for Spring Boot Actuator health status.

---

## 4. Running the Application Locally

Start the embedded Tomcat server:

```bash
mvn spring-boot:run
```

By default, the application binds to `0.0.0.0:8080`.

---

## 5. Testing the Endpoints

Open your browser and navigate to:
- [http://localhost:8080/hello](http://localhost:8080/hello)
- [http://localhost:8080/health](http://localhost:8080/health)
- [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

Or execute via cURL:

```bash
curl http://localhost:8080/hello
curl http://localhost:8080/health
curl http://localhost:8080/actuator/health
```

### Expected Response (`/hello`)

```json
{
  "message": "Hello World! Spring Boot is running successfully on Java 17.",
  "application": "cloudflare-java17-test",
  "javaVersion": "17.0.10",
  "springBootVersion": "3.2.5",
  "status": "RUNNING"
}
```

### Expected Response (`/health`)

```json
{
  "status": "UP"
}
```

### Expected Response (`/actuator/health`)

```json
{
  "status": "UP"
}
```

---

## 6. Docker Build & Execution

### Build the Docker Image

The multi-stage build packages the application with Maven in the build stage and prepares a lightweight runtime image with `eclipse-temurin:17-jre` running under a non-root user (`appuser`):

```bash
docker build -t cloudflare-java17-test:latest .
```

### Run the Container

```bash
docker run --rm -p 8080:8080 --name cloudflare-test-app cloudflare-java17-test:latest
```

### Test Inside Container

```bash
curl http://localhost:8080/hello
```

### Inspecting Container Logs

To view container logs in real time:

```bash
docker logs -f cloudflare-test-app
```

Or view recent logs:

```bash
docker logs cloudflare-test-app
```

### Stopping the Container

If running in the foreground:
- Press `Ctrl + C`

If running in the background (`-d`):
```bash
docker stop cloudflare-test-app
```

---

## 7. Cloudflare Deployment Preparation

This application has been structured and configured specifically for deployment onto Cloudflare Containers:

1. **Network Binding:**
   - The application binds to `0.0.0.0` (all interfaces) rather than `localhost` via `server.address=0.0.0.0`.
   - The port defaults to `8080` and dynamically adheres to the standard `PORT` environment variable (`server.port=${PORT:8080}`).

2. **Stateless Architecture:**
   - The application does not rely on local file persistence, external databases, or shared state, making it ideal for serverless and ephemeral container environments.

3. **Container Compatibility:**
   - The Dockerfile employs JVM container flags (`-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0`) to respect container memory limits and avoid OOM kills.
   - Runs as a non-privileged user (`appuser`).

4. **Deployment Requirements for Cloudflare:**
   - Deploying to Cloudflare Containers requires building the container image, pushing it to a supported container registry (such as Cloudflare's registry or a public/private registry like Docker Hub or GitHub Container Registry), and configuring the corresponding Cloudflare Worker or Container definition according to Cloudflare's platform documentation.
