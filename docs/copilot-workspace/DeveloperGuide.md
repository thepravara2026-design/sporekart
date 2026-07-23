# Developer Guide — Copilot Workspace Service

**Version:** 1.0.0  
**Last Updated:** 2026-07-23  

---

## Setup

### Prerequisites

- Java 17+ (or language runtime used by the project)
- Docker & Docker Compose
- Access to copilot service endpoints (Marketing, Sales, Service, Commerce)

### Local Development

```bash
# Clone the repository
git clone <repo-url>
cd services/copilot-workspace-service

# Copy environment config
cp .env.example .env

# Start dependencies (Redis)
docker-compose up -d redis

# Run the service
./gradlew bootRun
# or: npm run dev

# Verify
curl http://localhost:8103/health
```

---

## Project Structure

```
copilot-workspace-service/
├── src/
│   ├── main/
│   │   ├── java/.../
│   │   │   ├── workspace/          # Workspace Manager
│   │   │   ├── routing/            # Copilot Router / Routing Engine
│   │   │   ├── collaboration/      # Collaboration Engine
│   │   │   ├── context/            # Context Broker
│   │   │   ├── memory/             # Memory Broker
│   │   │   ├── copilot/            # Copilot client adapters
│   │   │   ├── api/                # REST controllers
│   │   │   └── config/             # Configuration
│   │   └── resources/
│   │       ├── application.yml
│   │       └── routing-rules.json
│   └── test/
├── contracts/
│   └── openapi/
│       └── copilot-workspace-service.yaml
├── docker/
├── docs/
│   └── copilot-workspace/
└── Dockerfile
```

---

## Extending the Service

### Adding a New Copilot

1. **Create the copilot adapter** in `src/main/java/.../copilot/` (implement the `CopilotClient` interface).
2. **Register the copilot** in `application.yml`:
   ```yaml
   copilot-workspace:
     copilots:
       - name: new-copilot
         displayName: "New Copilot"
         endpoint: http://new-copilot-service:8104
         healthPath: /health
         chatPath: /chat
         default: false
   ```
3. **Add routing rules** in `config/routing-rules.json` (see RoutingEngine.md).
4. **Update the OpenAPI spec** if the new copilot introduces new capabilities.
5. **Write integration tests** that verify routing, chat, and handoff for the new copilot.

### Adding Custom Routing Rules

Edit `config/routing-rules.json`:

```json
{
  "rules": [
    {
      "intent": "new_copilot_query",
      "patterns": ["pattern1", "pattern2"],
      "targetCopilot": "new-copilot",
      "minConfidence": 0.6
    }
  ]
}
```

The service supports hot-reload of routing rules by sending `SIGHUP` or via a `/admin/reload` endpoint (if enabled).

---

## Configuration Reference

| Key | Default | Description |
|-----|---------|-------------|
| `server.port` | 8103 | Service port |
| `copilot-workspace.default-copilot` | commerce | Fallback copilot name |
| `copilot-workspace.collaboration.timeout-per-copilot` | 15s | Per-copilot timeout |
| `copilot-workspace.collaboration.timeout-total` | 30s | Total collaboration timeout |
| `copilot-workspace.memory.ttl.shared` | 24h | Shared memory TTL |
| `copilot-workspace.memory.ttl.scratch` | 1h | Copilot scratch TTL |
| `copilot-workspace.context.ttl` | 24h | Context TTL |
| `copilot-workspace.routing.deep-classifier-enabled` | true | Enable ML classifier fallback |

---

## Testing

```bash
# Unit tests
./gradlew test

# Integration tests
./gradlew integrationTest

# Test specific component
./gradlew test --tests "*RoutingEngineTest*"
```

---

## API Reference

Detailed API documentation is available in:
- `contracts/openapi/copilot-workspace-service.yaml`
- `docs/copilot-workspace/API.md`
