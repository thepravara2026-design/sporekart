# Reporting Service

## Overview
Enterprise Reporting & Business Intelligence Platform. Centralized reporting engine for generating executive, operational, financial, AI, inventory, marketplace, compliance, and business reports.

## Service Details
- **Port:** 8096
- **Context Path:** `/api/v1/reports`
- **Java:** 21
- **Spring Boot:** 3.3.3
- **Architecture:** Hexagonal (Ports & Adapters) with DDD

## Quick Start
```bash
cd services/reporting-service
mvn spring-boot:run
```

## Key Features
- 22 REST endpoints across 7 domains
- 17 report categories with reusable templates
- BI aggregation from Analytics, Prediction, Decision, Alert engines
- Mock PDF/Excel/CSV/JSON export
- Mock schedule engine (daily/weekly/monthly/quarterly/yearly/custom)
- TTL-based in-memory caching
- 9 telemetry metrics
- 180 unit tests

## API Documentation
Once running: http://localhost:8096/swagger-ui.html

## Test
```bash
mvn test          # 180 tests (domain, engine, SDK, service, infrastructure, controller, context)
```
