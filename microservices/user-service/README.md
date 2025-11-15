# UuserUservice

## Overview
UuserUservice is a microservice for the Multi-Domain E-Commerce Platform.

## Technology Stack
- **Java**: 17
- **Spring Boot**: 3.2.0
- **Database**: PostgreSQL
- **Cache**: Redis
- **Message Broker**: Apache Kafka
- **Service Discovery**: Eureka
- **API Documentation**: SpringDoc OpenAPI

## Port
- **Development**: 8083
- **Production**: Configured via environment variables

## API Endpoints
Base URL: `http://localhost:8083/api/v1`

### Health Check
- GET `/actuator/health` - Service health status

### Documentation
- Swagger UI: `http://localhost:8083/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8083/v3/api-docs`

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+
- PostgreSQL 15+
- Redis 7+
- Apache Kafka 3.5+

### Environment Variables
```bash
DB_HOST=localhost
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres
REDIS_HOST=localhost
REDIS_PORT=6379
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
EUREKA_SERVER_URL=http://localhost:8761/eureka/
JWT_SECRET=your-secret-key
```

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

### Run with Docker
```bash
docker build -t user-service:latest .
docker run -p 8083:8083 user-service:latest
```

## Testing
```bash
mvn test
mvn verify
```

## Database Migrations
Flyway migrations are located in `src/main/resources/db/migration`

## Monitoring
- Prometheus metrics: `http://localhost:8083/actuator/prometheus`
- Health endpoint: `http://localhost:8083/actuator/health`

## Contributing
Please follow the project's coding standards and create pull requests for any changes.

## License
Copyright © 2024 E-Commerce Platform Team
