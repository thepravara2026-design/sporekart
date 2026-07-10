# Deployment Guide

## Prerequisites
- Java 21
- Docker
- Maven 3.9+
- Access to the target Supabase PostgreSQL environment
- Access to Redis and Kafka services
- Access to container registry

## Build
- Build the backend services with Maven.
- Build frontend assets with Vite.
- Build container images with Docker.

## Deploy
1. Apply database migrations with Flyway.
2. Start Redis and Kafka dependencies.
3. Deploy the Spring Boot services.
4. Deploy the frontend build.
5. Validate health and metrics endpoints.

## Post-deployment Validation
- Check /actuator/health
- Check Grafana/Prometheus targets
- Validate OpenAPI documentation is reachable
- Run smoke tests for auth, catalog, orders, admin, analytics, and SEO
