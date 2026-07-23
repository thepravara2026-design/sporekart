# Operations Guide

## Startup / Shutdown

### Starting the Service

```bash
# Production (Docker)
docker compose up -d admin-copilot-service

# With specific profile
docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d admin-copilot-service

# Local development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# As a JAR
java -jar admin-copilot-service.jar --spring.profiles.active=prod
```

### Startup Sequence

The service performs these steps in order on startup:

1. **Configuration loading** — Loads `application.yml` and profile-specific configs
2. **Database migration** — Flyway runs pending migrations for report jobs and alerts
3. **Cache warming** — Pre-loads dashboard metrics from platform services
4. **Health registration** — Registers with the service registry (Eureka/Consul)
5. **Insight engine initialization** — Loads alert rules and schedules the first insight evaluation
6. **Ready** — `GET /actuator/health/readiness` returns 200

### Shutdown

```bash
# Graceful shutdown (Docker)
docker compose stop admin-copilot-service

# Graceful shutdown (JAR)
kill -TERM <pid>
```

The service handles `SIGTERM` by:
1. Completing in-flight report generation jobs (with 30-second hard limit)
2. Draining the audit event queue
3. Closing HTTP connections
4. Unregistering from the service registry

### Startup Verification

```bash
# Health check
curl -s http://localhost:8100/actuator/health | jq .
# Expected: {"status":"UP"}

# Readiness check
curl -s http://localhost:8100/actuator/health/readiness | jq .
# Expected: {"status":"UP"}

# Liveness check
curl -s http://localhost:8100/actuator/health/liveness | jq .
# Expected: {"status":"UP"}
```

## Health Checks

### Endpoints

| Endpoint | Purpose | Expected Status |
|---|---|---|
| `GET /actuator/health` | Overall health | UP, DEGRADED, or DOWN |
| `GET /actuator/health/readiness` | Ready for traffic | UP or DOWN |
| `GET /actuator/health/liveness` | Process is alive | UP or DOWN |

### Health Indicators

| Indicator | Checks | Degraded When | Down When |
|---|---|---|---|
| `diskSpace` | Available disk space | < 10% free | < 5% free |
| `ping` | Basic JVM health | — | JVM unavailable |
| `db` | Database connectivity | Connection pool > 80% | Cannot connect |
| `analyticsService` | Analytics service health | Latency > 1s | 5xx or timeout |
| `orderService` | Order service health | Latency > 1s | 5xx or timeout |
| `inventoryService` | Inventory service health | Latency > 1s | 5xx or timeout |
| `trainingService` | Training service health | Latency > 1s | 5xx or timeout |
| `paymentService` | Payment service health | Latency > 1s | 5xx or timeout |
| `riskService` | Risk service health | Latency > 1s | 5xx or timeout |

### Custom Health Check

```java
@Component
public class AnalyticsServiceHealthIndicator implements HealthIndicator {
    private final AnalyticsServiceClient client;

    @Override
    public Health health() {
        try {
            long start = System.currentTimeMillis();
            var response = client.healthCheck();
            long latency = System.currentTimeMillis() - start;

            if (response.getStatusCode().is2xxSuccessful()) {
                if (latency > 1000) {
                    return Health.down()
                        .withDetail("service", "analytics-service")
                        .withDetail("latencyMs", latency)
                        .withDetail("message", "Response time exceeded threshold")
                        .build();
                }
                return Health.up()
                    .withDetail("service", "analytics-service")
                    .withDetail("latencyMs", latency)
                    .build();
            }
            return Health.down()
                .withDetail("service", "analytics-service")
                .withDetail("statusCode", response.getStatusCodeValue())
                .build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
```

## Performance Tuning for Analytics Queries

### Database Query Optimization

1. **Index strategy**: Ensure the analytics database has composite indexes on `(metric, timestamp)` and `(product_id, timestamp)` for range queries.

   ```sql
   CREATE INDEX idx_metrics_lookup
       ON analytics_metrics (metric, timestamp DESC)
       WHERE metric IN ('revenue', 'orders', 'conversion');

   CREATE INDEX idx_product_daily
       ON sales_daily (product_id, date DESC)
       INCLUDE (revenue, units, orders);
   ```

2. **Materialized views**: Pre-aggregate daily metrics into materialized views refreshed hourly.

   ```sql
   CREATE MATERIALIZED VIEW mv_dashboard_daily AS
   SELECT
       date_trunc('day', created_at) AS day,
       COUNT(*) AS orders,
       SUM(amount) AS revenue,
       COUNT(DISTINCT customer_id) AS customers
   FROM orders
   WHERE status IN ('ORDER_CONFIRMED', 'SHIPPED')
   GROUP BY 1;
   ```

3. **Time-bounded queries**: Always include `periodStart` and `periodEnd` parameters. Never query without time bounds.

### Connection Pool Tuning

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 5000
      max-lifetime: 600000
```

### Client HTTP Pool Tuning

```yaml
sporekart:
  copilot:
    admin:
      http-client:
        max-connections: 100
        max-connections-per-route: 20
        connection-timeout-ms: 2000
        read-timeout-ms: 10000
        keep-alive-seconds: 30
```

### Parallel Data Fetching

The AnalyticsEngine fetches data from multiple services in parallel:

```java
public AdminDashboard getDashboard() {
    CompletableFuture<SalesSummary> sales = CompletableFuture
        .supplyAsync(() -> orderClient.getSalesSummary(Period.today()));
    CompletableFuture<CustomerSummary> customers = CompletableFuture
        .supplyAsync(() -> analyticsClient.getCustomerSummary(Period.today()));
    CompletableFuture<InventorySummary> inventory = CompletableFuture
        .supplyAsync(() -> inventoryClient.getSummary());
    CompletableFuture<TrainingSummary> training = CompletableFuture
        .supplyAsync(() -> trainingClient.getSummary());
    CompletableFuture<PlatformHealth> platform = CompletableFuture
        .supplyAsync(() -> checkPlatformHealth());

    return CompletableFuture.allOf(sales, customers, inventory, training, platform)
        .thenApply(v -> new AdminDashboard(
            Instant.now(), Period.today(),
            sales.join(), customers.join(),
            inventory.join(), training.join(), platform.join()))
        .join();
}
```

## Cache Management

### Cache Configuration

```yaml
sporekart:
  copilot:
    admin:
      cache:
        enabled: true
        default-ttl-seconds: 300
        max-size: 10000
        redis:
          enabled: true
          host: redis-master
          port: 6379
          password: ${REDIS_PASSWORD}
```

### Cache Regions

| Cache Region | TTL | Max Entries | Content |
|---|---|---|---|
| `dashboard` | 60s | 10 | Dashboard snapshot |
| `insights` | 300s | 100 | Generated business insights |
| `forecasts` | 600s | 50 | Forecast results |
| `alerts` | 60s | 100 | Active alerts |
| `metrics` | 120s | 1000 | Individual metric queries |

### Manual Cache Operations

```bash
# Clear all caches
curl -X POST http://localhost:8100/actuator/caches/clear

# Clear specific cache
curl -X DELETE http://localhost:8100/actuator/caches/dashboard

# View cache statistics
curl http://localhost:8100/actuator/caches
```

### Cache Configuration Code

```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisFactory) {
        return RedisCacheManager.builder(redisFactory)
            .cacheDefaults(defaultConfig())
            .withInitialCacheConfigurations(cacheConfigs())
            .build();
    }

    private RedisCacheConfiguration defaultConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(300))
            .disableCachingNullValues();
    }

    private Map<String, RedisCacheConfiguration> cacheConfigs() {
        return Map.of(
            "dashboard", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60)),
            "insights", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(300)),
            "forecasts", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
        );
    }
}
```

## Troubleshooting

### "Dashboard shows no data"

**Possible causes and fixes:**

| Cause | Check | Resolution |
|---|---|---|
| Analytics service is down | `GET /actuator/health` on analytics-service | Restart analytics-service |
| No orders today | Query the order service directly | Verify order pipeline is processing |
| Cache returned stale empty data | Clear the dashboard cache | `curl -X DELETE /actuator/caches/dashboard` |
| Data source connection failed | Check database connectivity | Verify DB credentials in Vault |
| Timezone mismatch | Verify period parameters | Ensure UTC alignment across services |

**Diagnostic commands:**

```bash
# Check analytics service health
curl http://analytics-service:8001/actuator/health

# Check if orders exist today
curl "http://order-service:8002/api/orders?createdAfter=$(date -u +%Y-%m-%dT00:00:00Z)" \
  -H "Authorization: Bearer <token>"

# Verify cache contents
redis-cli --raw keys 'dashboard:*' | while read key; do
  echo "$key: $(redis-cli ttl $key)s remaining"
done

# Check service logs for errors
docker logs admin-copilot-service --tail 100 | grep ERROR
```

### "Forecasts inaccurate"

**Possible causes and fixes:**

| Cause | Check | Resolution |
|---|---|---|
| Insufficient historical data | Verify data exists for the lookback period | Provide at least 30 days of data |
| Recent promotional spike | Check if a promotion ran during the period | Exclude promotional periods from training |
| Algorithm mismatch | Check which algorithm was auto-selected | Manually specify a more appropriate algorithm |
| Data gaps > 20% | Run data quality check | Fill gaps with interpolation or exclude period |
| Seasonal pattern changed | Compare year-over-year seasonality | Reset seasonal decomposition with more recent data |

**Diagnostic commands:**

```bash
# Check data availability
curl "http://analytics-service:8001/analytics/sales?periodStart=2026-06-01&periodEnd=2026-07-22" \
  -H "Authorization: Bearer <token>" | jq '.data | length'

# View which algorithm was used
curl -X POST http://localhost:8100/api/v1/copilot/admin/forecast \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"metric": "revenue", "horizon": 7}' | jq '.summary.algorithm'

# Get forecast accuracy metrics
curl http://localhost:8100/api/v1/copilot/admin/forecast/accuracy/revenue \
  -H "Authorization: Bearer <token>"
```

### "Alerts not firing"

**Possible causes and fixes:**

| Cause | Check | Resolution |
|---|---|---|
| Threshold misconfiguration | Review alert rule thresholds | Update thresholds in configuration |
| Metrics not being reported | Check if source service sends metrics | Verify metric collection pipeline |
| Alert rule is disabled | Check enabled flag in config | Set `enabled: true` |
| Deduplication suppresses alerts | Check if same alert already active | Set `allowDuplicates: true` for testing |
| Notification channel failing | Check notification service health | Verify email/PagerDuty integration |

**Diagnostic commands:**

```bash
# List all configured alert rules
curl http://localhost:8100/actuator/alert-rules \
  -H "Authorization: Bearer <token>"

# Manually trigger alert evaluation
curl -X POST http://localhost:8100/actuator/alerts/evaluate \
  -H "Authorization: Bearer <token>"

# Check recent alert history
curl "http://localhost:8100/api/v1/copilot/admin/alerts?status=all&since=2026-07-22T00:00:00Z" \
  -H "Authorization: Bearer <token>"

# Test notification delivery
curl -X POST http://localhost:8100/actuator/alerts/test-notification \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"channel": "email", "recipient": "ops@sporekart.example"}'
```

### "Report generation slow"

**Possible causes and fixes:**

| Cause | Check | Resolution |
|---|---|---|
| Large data volume | Check requested period and dimensions | Limit period to 90 days, reduce dimensions |
| Missing indexes | Run EXPLAIN on generated queries | Add composite indexes |
| Under-provisioned workers | Check thread pool utilization | Increase `report.thread-pool-size` |
| Slow upstream services | Check each service's latency | Investigate and optimize slow services |
| Memory pressure | Check JVM heap usage | Increase heap or reduce batch size |

**Configuration adjustments:**

```yaml
sporekart:
  copilot:
    admin:
      report:
        thread-pool-size: 4
        max-records: 10000
        max-period-days: 365
        csv-batch-size: 5000
```

**Diagnostic commands:**

```bash
# View thread pool status
curl http://localhost:8100/actuator/threaddump | jq '.threads[] | select(.name | startswith("report-"))'

# Check report job queue depth
curl http://localhost:8100/actuator/metrics/report.jobs.queued

# View recent report generation times
curl http://localhost:8100/actuator/metrics/report.generation.time

# Profile a slow report
curl -X POST http://localhost:8100/api/v1/copilot/admin/report \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "sales",
    "periodStart": "2026-01-01T00:00:00Z",
    "periodEnd": "2026-07-23T23:59:59Z",
    "granularity": "day"
  }' -w "\nTime: %{time_total}s\n"
```

### General Diagnostic Endpoints

| Endpoint | Description |
|---|---|
| `GET /actuator/health` | Overall health status |
| `GET /actuator/info` | Build and version info |
| `GET /actuator/metrics` | Available metrics |
| `GET /actuator/metrics/{name}` | Specific metric value |
| `GET /actuator/env` | Environment properties |
| `GET /actuator/loggers` | Logger configuration |
| `POST /actuator/loggers/{name}` | Change log level at runtime |
| `GET /actuator/threaddump` | JVM thread dump |
| `GET /actuator/heapdump` | JVM heap dump (binary) |
| `GET /actuator/httpexchanges` | Recent HTTP request/response exchanges |
| `GET /actuator/caches` | Cache statistics |

### Logging

```yaml
logging:
  level:
    com.sporekart.copilot.admin: INFO
    com.sporekart.copilot.admin.infrastructure.client: DEBUG
    org.springframework.cloud.gateway: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - correlationId=%X{correlationId} %msg%n"
```

**Runtime log level changes:**

```bash
# Enable debug logging for service clients
curl -X POST http://localhost:8100/actuator/loggers/com.sporekart.copilot.admin.infrastructure.client \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'

# Reset to default
curl -X POST http://localhost:8100/actuator/loggers/com.sporekart.copilot.admin.infrastructure.client \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": null}'
```

### Common Ports Reference

| Service | Port |
|---|---|
| Admin Copilot Service | 8100 |
| Analytics Service | 8001 |
| Order Service | 8002 |
| Inventory Service | 8003 |
| Training Service | 8004 |
| Payment Service | 8005 |
| Risk Service | 8006 |
| Identity Service | 8007 |
| Notification Service | 8008 |
| Gateway Service | 8080 |
| Redis (cache) | 6379 |
| PostgreSQL (data) | 5432 |
| Kafka (events) | 9092 |
