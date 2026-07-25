# Developer Guide

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Port 8097 available

### Build & Run
```bash
cd services/workflow-orchestrator-service
mvn clean compile
mvn test
mvn spring-boot:run
```

### Configuration
All configuration is centralized in `application.yml` under the `workflow` prefix:

| Key | Default | Description |
|---|---|---|
| workflow.engine.enabled | true | Enable engine |
| workflow.engine.simulation | true | Enable simulation mode |
| workflow.runtime.max-concurrent | 100 | Max concurrent workflows |
| workflow.queue.max-size | 1000 | Queue capacity |
| workflow.retry.max-attempts | 3 | Max retries per instance |
| workflow.retry.backoff-ms | 1000 | Retry backoff |
| workflow.cache.ttl-seconds | 300 | Cache TTL |
| workflow.cache.max-size | 500 | Cache max entries |
| workflow.telemetry.enabled | true | Enable telemetry |
| workflow.approval.require-all | true | Require all approvals |
| workflow.debug | false | Debug mode |

### Project Structure
```
src/main/java/com/sporekart/workflow/
├── WorkflowOrchestratorApplication.java
├── domain/
│   ├── model/         # Records, enums
│   ├── repository/    # Port interfaces
│   └── engine/        # Business logic
├── application/
│   ├── service/       # Orchestration
│   └── sdk/           # Client API
├── infrastructure/
│   ├── persistence/   # In-memory repo
│   ├── queue/         # Mock queue
│   ├── executor/      # Mock actions
│   └── cache/         # TTL cache
├── interfaces/
│   └── rest/          # Controllers
└── config/            # Spring config
```

### Adding a New Workflow Type
1. Add to `WorkflowType` enum
2. Add definition in `WorkflowDefinitionEngine`
3. Add decisions in `WorkflowDecisionEngine`
4. Add test coverage

### Testing
- 178 unit tests across 22 test classes
- Tests cover: domain, engines, services, SDK, infrastructure, controllers
- All tests use mocked dependencies and in-memory storage
- Controller tests use Spring MockMvc
