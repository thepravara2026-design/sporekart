# Alert Intelligence Service

## Overview
Enterprise anomaly detection and alert intelligence platform. Provides real-time alert management, business risk assessment, anomaly detection, and event timeline tracking.

## Service Details
- **Port:** 8095
- **Context Path:** `/api/v1/alerts`
- **Java:** 21
- **Spring Boot:** 3.3.3
- **Architecture:** Hexagonal (Ports & Adapters) with DDD

## Quick Start
```bash
cd services/alert-intelligence-service
mvn spring-boot:run
```

## Key Features
- Alert lifecycle: OPEN → ACKNOWLEDGED → RESOLVED
- 6 alert categories, 5 severity levels, 5 priority levels
- 10 risk categories with composite scoring
- 8 anomaly detection types
- 9 timeline event types
- In-memory TTL caching
- 11 telemetry metrics
- 22 REST endpoints

## API Documentation
Once running, visit: http://localhost:8095/swagger-ui.html

## Test
```bash
mvn test          # 83 tests (domain, engine, service, infra, SDK, controller, context)
```
