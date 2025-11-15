# Multi-Domain E-Commerce Platform

## Overview
A comprehensive, scalable microservices-based e-commerce platform supporting company-wise separate domains with customized features, themes, and product catalogs.

## Architecture

### 🏗️ Total Microservices: 63 (54 Implemented, 9 Remaining)

#### Core Business Microservices (25)
1. **company-service** (8081) - Company registration, profile management, domain configuration
2. **product-service** (8082) - Product catalog, inventory management
3. **user-service** (8083) - User authentication (JWT), profile management
4. **vendor-service** (8084) - Multi-vendor support, commission management
5. **shopper-service** (8085) - Shopper profiles, verification, metrics
6. **cart-service** (8086) - Shopping cart, session management
7. **order-service** (8087) - Order processing, status tracking
8. **order-history-service** (8088) - Historical data, analytics
9. **payment-service** (8089) - Multi-gateway payments, refunds
10. **inventory-service** (8090) - Stock management, multi-warehouse
11. **pricing-service** (8091) - Dynamic pricing, discounts
12. **review-service** (8092) - Product reviews, ratings
13. **comment-service** (8093) - Product/shopper comments
14. **recommendation-service** (8094) - AI recommendations
15. **notification-service** (8095) - Email, SMS, push notifications
16. **shopper-notification-service** (8096) - Order assignments
17. **search-service** (8097) - Advanced search, autocomplete
18. **search-history-service** (8098) - Search tracking, analytics
19. **browsing-history-service** (8099) - User behavior tracking
20. **wishlist-service** (8100) - Wishlists, favorites
21. **coupon-service** (8101) - Discount coupons, validation
22. **loyalty-service** (8102) - Loyalty programs, rewards
23. **order-assignment-service** (8103) - Intelligent routing
24. **localization-service** (8104) - Multi-language support
25. **currency-service** (8105) - Multi-currency, exchange rates

#### Customer Experience Microservices (5)
26. **recommendation-service** (8040) - AI-powered product recommendations
27. **search-service** (8043) - Advanced search with Elasticsearch
28. **review-service** (8041) - Customer reviews and ratings
29. **wishlist-service** (8055) - Wishlist management with price tracking
30. **gift-card-service** (8023) - Gift card purchase, redemption, and management

#### Domain-Specific Microservices (10)
31. **domain-management-service** (8106) - Dynamic domains, templates, SSL
32. **furniture-service** (8107) - 3D models, room planning
33. **automobile-service** (8108) - Vehicle specs, financing
34. **electronics-service** (8109) - Technical specs, warranty
35. **fashion-service** (8110) - Size charts, style trends
36. **grocery-service** (8111) - Expiration tracking, nutrition
37. **book-service** (8112) - ISBN, authors, publishers
38. **sports-service** (8113) - Equipment specifications
39. **health-beauty-service** (8114) - Ingredients, skin matching
40. **toys-games-service** (8115) - Age appropriateness, safety

#### Infrastructure Microservices (12)
41. **media-service** (8116) - Image upload, processing
42. **cdn-service** (8117) - Content delivery network
43. **storage-service** (8118) - Distributed file storage
44. **image-processing-service** (8119) - Resizing, compression
45. **backup-service** (8120) - Automated backups
46. **cache-service** (8121) - Redis distributed caching
47. **analytics-service** (8122) - Behavior tracking, sales analytics
48. **logging-service** (8123) - Centralized logging (ELK)
49. **monitoring-service** (8124) - APM, health checks
50. **security-service** (8125) - Fraud detection, rate limiting
51. **email-service** (8126) - Transactional emails
52. **sms-service** (8127) - SMS notifications, OTP

#### Support & Admin Microservices (11)
53. **admin-service** (8128) - Admin dashboard, system monitoring
54. **shopper-dashboard-service** (8129) - Shopper performance
55. **customer-service** (8130) - Support tickets, live chat
56. **shipping-service** (8131) - Shipping calculations, tracking
57. **tax-service** (8132) - Tax calculations, reports
58. **audit-service** (8133) - System logs, audit trails
59. **configuration-service** (8134) - Feature flags, A/B testing
60. **reporting-service** (8135) - Business reports, dashboards
61. **communication-service** (8136) - Real-time chat, video calls
62. **geolocation-service** (8137) - Store locator, delivery zones
63. **api-gateway** (8080) - Request routing, authentication

## Technology Stack

### Backend
- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0 (Eureka, OpenFeign, Config, LoadBalancer)
- **Database**: PostgreSQL 15+
- **Cache**: Redis 7+
- **Message Broker**: Apache Kafka 3.5+
- **Search**: Elasticsearch 8+
- **Security**: Spring Security, JWT (JJWT 0.12.3)
- **API Documentation**: SpringDoc OpenAPI 2.2.0
- **Database Migration**: Flyway
- **Monitoring**: Prometheus, Micrometer
- **Utilities**: Lombok, MapStruct

### Infrastructure
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Service Discovery**: Eureka
- **Configuration**: Spring Cloud Config
- **API Gateway**: Spring Cloud Gateway
- **Circuit Breaker**: Resilience4j
- **Monitoring**: Prometheus + Grafana
- **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **Tracing**: OpenTelemetry + Jaeger

## Project Structure

```
ecommerce-platform/
├── microservices/                # 58 independent Spring Boot services
│   ├── company-service/
│   ├── product-service/
│   ├── user-service/
│   └── ... (55 more services)
│
├── shared-libraries/             # Shared dependencies
│   ├── common-library/          # Common utilities, DTOs, exceptions
│   ├── security-library/        # JWT, tenant context, security
│   ├── event-library/           # Kafka events, publishers, consumers
│   └── client-library/          # Feign clients for inter-service communication
│
├── infrastructure/               # Infrastructure services
│   ├── eureka-server/           # Service discovery
│   ├── config-server/           # Centralized configuration
│   ├── kubernetes/              # K8s manifests
│   ├── terraform/               # Infrastructure as code
│   └── docker-compose/          # Local development
│
├── frontend-apps/                # Frontend applications
│   ├── customer-web/            # Customer web app (React)
│   ├── company-dashboard/       # Company dashboard (React)
│   ├── admin-portal/            # Admin panel (React)
│   └── shopper-app/             # Shopper app (React)
│
├── docs/                         # Documentation
│   ├── architecture/
│   ├── deployment/
│   └── api/
│
├── .github/
│   ├── workflows/               # CI/CD pipelines
│   └── copilot/
│       ├── instructions.md      # Development guidelines
│       └── architecture.md      # Architecture documentation
│
├── pom.xml                       # Parent POM
├── docker-compose.yml            # Local development setup
└── README.md                     # This file
```

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 15+
- Redis 7+
- Apache Kafka 3.5+
- Kubernetes (optional, for production)

### Quick Start with Docker Compose

1. **Clone the repository**
```bash
git clone <repository-url>
cd ecommerce-platform
```

2. **Start infrastructure services**
```bash
docker-compose up -d postgres redis kafka zookeeper elasticsearch
```

3. **Build all microservices**
```bash
mvn clean install
```

4. **Start Eureka Server**
```bash
cd infrastructure/eureka-server
mvn spring-boot:run
```

5. **Start API Gateway**
```bash
cd microservices/api-gateway
mvn spring-boot:run
```

6. **Start any microservice**
```bash
cd microservices/company-service
mvn spring-boot:run
```

### Running Individual Services

Each microservice can be run independently:

```bash
cd microservices/<service-name>
mvn spring-boot:run
```

Or using Docker:

```bash
cd microservices/<service-name>
docker build -t <service-name>:latest .
docker run -p <port>:<port> <service-name>:latest
```

## Development

### Building a Microservice

```bash
cd microservices/<service-name>
mvn clean install
```

### Running Tests

```bash
cd microservices/<service-name>
mvn test
```

### Code Quality

```bash
mvn clean verify
mvn sonar:sonar  # SonarQube analysis
```

## Configuration

### Environment Variables

Each microservice requires the following environment variables:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Service Discovery
EUREKA_SERVER_URL=http://localhost:8761/eureka/

# Security
JWT_SECRET=your-256-bit-secret-key

# Spring Profile
SPRING_PROFILES_ACTIVE=dev
```

### Database Setup

Each microservice has its own database. Create them using:

```sql
CREATE DATABASE companyservice_db;
CREATE DATABASE productservice_db;
CREATE DATABASE userservice_db;
-- ... (55 more databases)
```

Or use the provided initialization script:

```bash
./infrastructure/scripts/init-databases.sh
```

## Deployment

### Docker Deployment

Build and push all services:

```bash
./infrastructure/scripts/build-all-services.sh
./infrastructure/scripts/push-all-services.sh
```

### Kubernetes Deployment

Deploy to Kubernetes cluster:

```bash
kubectl apply -f infrastructure/kubernetes/namespace.yml
kubectl apply -f infrastructure/kubernetes/configmaps/
kubectl apply -f infrastructure/kubernetes/secrets/
kubectl apply -f infrastructure/kubernetes/deployments/
kubectl apply -f infrastructure/kubernetes/services/
kubectl apply -f infrastructure/kubernetes/ingress/
```

### Terraform (AWS/GCP/Azure)

```bash
cd infrastructure/terraform
terraform init
terraform plan
terraform apply
```

## Monitoring & Observability

### Prometheus Metrics
- Each service exposes metrics at: `http://localhost:<port>/actuator/prometheus`

### Health Checks
- Liveness: `http://localhost:<port>/actuator/health/liveness`
- Readiness: `http://localhost:<port>/actuator/health/readiness`

### Grafana Dashboards
- Access: `http://localhost:3000`
- Default credentials: admin/admin

### Logging
- Centralized logging with ELK Stack
- Kibana: `http://localhost:5601`

### Distributed Tracing
- Jaeger UI: `http://localhost:16686`

## API Documentation

Each microservice provides OpenAPI documentation:

- Swagger UI: `http://localhost:<port>/swagger-ui.html`
- OpenAPI JSON: `http://localhost:<port>/v3/api-docs`

## Key Features

### Multi-Tenancy
- Company-wise data isolation
- Row-level security with tenant context
- Custom domains per company
- Branding customization

### Scalability
- Independent scaling per microservice
- Horizontal Pod Autoscaling (HPA)
- Kubernetes-ready architecture
- Load balancing with Spring Cloud LoadBalancer

### Security
- JWT-based authentication
- OAuth2 integration
- Rate limiting
- DDoS protection
- Fraud detection
- PCI DSS compliance (Payment Service)

### Resilience
- Circuit breakers (Resilience4j)
- Retry mechanisms
- Bulkhead pattern
- Timeout controls
- Fallback strategies

### Event-Driven
- Apache Kafka for async communication
- Event sourcing
- CQRS pattern
- Event replay capabilities

## Testing Strategy

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### End-to-End Tests
```bash
mvn failsafe:integration-test
```

### Test Coverage
- Minimum coverage: 80%
- JaCoCo reports: `target/site/jacoco/index.html`

## CI/CD

GitHub Actions workflows are configured for:
- Build and test on push
- Docker image build and push
- Kubernetes deployment
- Security scanning
- Code quality checks

## Performance

### Expected Throughput
- API Gateway: 100,000+ requests/second
- Individual services: 10,000+ requests/second

### Response Time (p95)
- < 200ms for read operations
- < 500ms for write operations

### Availability
- Target: 99.99% uptime
- Multi-region deployment
- Auto-failover capabilities

## Security Considerations

- All passwords hashed with BCrypt
- JWT tokens with 15-minute expiry
- Refresh tokens with 7-day expiry
- HTTPS enforced in production
- Database encryption at rest
- Secrets management with Vault/AWS Secrets Manager

## Troubleshooting

### Service Discovery Issues
```bash
# Check Eureka dashboard
http://localhost:8761

# Verify service registration
curl http://localhost:8761/eureka/apps
```

### Database Connection Issues
```bash
# Test PostgreSQL connection
psql -h localhost -U postgres -d companyservice_db

# Check connection pool
curl http://localhost:<port>/actuator/metrics/hikaricp.connections.active
```

### Kafka Issues
```bash
# List topics
kafka-topics.sh --list --bootstrap-server localhost:9092

# Check consumer groups
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

Copyright © 2024 E-Commerce Platform Team. All rights reserved.

## Support

For questions and support:
- Email: support@ecommerce-platform.com
- Slack: #ecommerce-platform
- Documentation: https://docs.ecommerce-platform.com

## Roadmap

### Q1 2025
- [ ] Complete all 58 microservices implementation
- [ ] Frontend applications development
- [ ] Kubernetes deployment automation
- [ ] Performance optimization

### Q2 2025
- [ ] Machine learning recommendations
- [ ] Advanced analytics dashboards
- [ ] Mobile applications (iOS/Android)
- [ ] Multi-region deployment

### Q3 2025
- [ ] GraphQL API support
- [ ] Real-time notifications (WebSocket)
- [ ] Advanced fraud detection
- [ ] AI-powered customer support

### Q4 2025
- [ ] Blockchain integration for supply chain
- [ ] AR/VR features for product visualization
- [ ] Voice commerce integration
- [ ] Advanced personalization engine

---

**Built with ❤️ by the E-Commerce Platform Team**
