# Trainer Copilot — Developer Guide

## Prerequisites

- Go 1.22+
- Docker Desktop 24+
- Kubernetes (minikube / kind for local)
- Protocol Buffers compiler (protoc) 25+
- Access to SporeKart Enterprise Copilot Framework SDK
- Access to Knowledge Base gRPC service

## Local Setup

```bash
# Clone the repository
git clone https://github.com/sporekart/trainer-copilot-service
cd trainer-copilot-service

# Copy default configuration
cp config/config.example.yaml config/config.yaml

# Install dependencies
go mod download

# Generate protobuf stubs
make proto

# Run locally
make run
```

The service starts on `http://localhost:8101`.

## Building and Testing

```bash
# Build binary
make build

# Build Docker image
make docker-build

# Run unit tests
make test

# Run integration tests
make test-integration

# Run linting
make lint

# Run all checks
make ci
```

### Test structure

```
internal/
  engine/
    lesson_generator_test.go
    assessment_test.go
    analytics_test.go
    certification_test.go
    batch_test.go
  api/
    handler_test.go
    middleware_test.go
```

## API Reference Overview

| Endpoint | Method | Description |
|---|---|---|
| `/v1/trainer-copilot/chat` | POST | Conversational copilot chat |
| `/v1/trainer-copilot/lessons/generate` | POST | Generate a lesson plan |
| `/v1/trainer-copilot/assessments/generate` | POST | Generate an assessment |
| `/v1/trainer-copilot/assessments/evaluate` | POST | Evaluate submitted answers |
| `/v1/trainer-copilot/batches` | GET | List all batches |
| `/v1/trainer-copilot/batches` | POST | Create a new batch |
| `/v1/trainer-copilot/batches/{batchId}` | GET | Get batch details |
| `/v1/trainer-copilot/analytics/batches/{batchId}` | GET | Batch analytics |
| `/v1/trainer-copilot/analytics/students/{studentId}` | GET | Individual student analytics |
| `/v1/trainer-copilot/analytics/trends` | GET | Trend analysis |
| `/v1/trainer-copilot/analytics/certification/{studentId}` | GET | Certification status |
| `/v1/trainer-copilot/health` | GET | Service health check |

Full reference: [API.md](./API.md) | OpenAPI: `contracts/openapi/trainer-copilot-service.yaml`

## Adding New Capabilities

1. **Define the engine interface** in `internal/engine/`.
2. **Implement the engine logic** following existing patterns (see `LessonGenerator` or `Assessment`).
3. **Add API handler** in `internal/api/` with routing, validation, and request/response types.
4. **Register the handler** in `internal/api/router.go`.
5. **Write unit tests** in the corresponding `_test.go` file.
6. **Update protobuf definitions** if the capability needs gRPC contracts.
7. **Add docs** — update this guide and the OpenAPI spec.

## Extending Engines

Each engine follows the interface pattern:

```go
type LessonGenerator interface {
    Generate(ctx context.Context, req *LessonRequest) (*LessonPlan, error)
}
```

To extend:

- **Add a new lesson type:** Implement the type-specific logic in a new file under `internal/engine/lesson/`.
- **Add a new assessment type:** Extend the assessment factory in `internal/engine/assessment/factory.go`.
- **Add a new analytics metric:** Add the computation function to `internal/engine/analytics/metrics.go` and register it in the aggregator.

### Configuration

Engines read configuration from `config/config.yaml`. Add new keys under the relevant engine section:

```yaml
lesson_generator:
  default_duration: 45
  max_sections: 10

assessment:
  max_questions: 50
  default_passing_score: 60
```
