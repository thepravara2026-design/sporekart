# Alert API Specification

## Base URL
```
http://localhost:8095/api/v1/alerts
```

## Authentication
All endpoints require `Basic` auth with ADMIN role.

## Endpoints

### Health
| Method | Path | Description |
|---|---|---|
| GET | `/health` | Service health check |

### Alerts CRUD
| Method | Path | Description |
|---|---|---|
| GET | `/` | List all alerts |
| GET | `/{id}` | Get alert by ID |
| POST | `/` | Create alert (via AlertBuilder) |
| POST | `/generate` | Generate seeded alerts |
| DELETE | `/{id}` | Delete alert |

### Query
| Method | Path | Query Params |
|---|---|---|
| GET | `/category/{category}` | — |
| GET | `/severity/{severity}` | — |
| GET | `/status/{status}` | — |
| GET | `/source/{source}` | — |
| GET | `/timerange` | `start`, `end` (ISO instant) |

### Actions
| Method | Path | Description |
|---|---|---|
| POST | `/{id}/acknowledge` | Acknowledge alert |
| POST | `/{id}/resolve` | Resolve alert |

### History
| Method | Path | Description |
|---|---|---|
| GET | `/{id}/history` | Alert timeline history |

### Risks
| Method | Path | Description |
|---|---|---|
| GET | `/risks` | List all risks |
| GET | `/risks/{id}` | Get risk by ID |
| GET | `/risks/summary` | Risk summary (distribution + avg) |

### Anomalies
| Method | Path | Description |
|---|---|---|
| GET | `/anomalies` | List all anomalies |
| POST | `/anomalies/detect` | Detect anomalies |

### Timeline
| Method | Path | Description |
|---|---|---|
| GET | `/timeline` | Full event timeline |
| GET | `/timeline/type/{type}` | Filter by event type |

### Telemetry
| Method | Path | Description |
|---|---|---|
| GET | `/telemetry/metrics` | Current metric values |
| GET | `/telemetry/history` | Metric history |

### Cache
| Method | Path | Description |
|---|---|---|
| GET | `/cache/stats` | Cache hit/miss/size stats |
| POST | `/cache/clear` | Clear entire cache |

## Response Format
```json
{
  "status": 200,
  "data": { ... },
  "message": "Success"
}
```
