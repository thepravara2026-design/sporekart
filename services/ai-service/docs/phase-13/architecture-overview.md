# Enterprise AI Platform Architecture

**Version:** 1.0.0  
**Status:** Foundation  

---

## Module Dependency Diagram

```mermaid
graph TD
    subgraph API_Layer["API Layer (Interfaces)"]
        REST["REST Controllers<br/>(interfaces/rest/)"]
        Internal["Internal APIs<br/>(interfaces/internal/)"]
    end

    subgraph Application_Layer["Application Layer"]
        Services["Application Services<br/>(application/)"]
        Ports["API Ports<br/>(api/)"]
    end

    subgraph Domain_Layer["Domain Layer"]
        Models["Domain Models<br/>(domain/)"]
        Enums["Enums & Records<br/>(domain/)"]
    end

    subgraph Infrastructure_Layer["Infrastructure Layer"]
        Persistence[(PostgreSQL<br/>persistence/)]
        Cache[(Redis<br/>redis/)]
        Events[Kafka<br/>(kafka/)]
        Monitoring[Monitoring<br/>(monitoring/)]
        Vector[(Vector Store<br/>vector/)]
    end

    REST --> Ports
    Internal --> Ports
    Ports --> Services
    Services --> Models
    Services --> Persistence
    Services --> Cache
    Services --> Events
    Services --> Monitoring
    Services --> Vector
    Events --> Domain_Events[(Outbox/Kafka Topics)]
```

---

## Module Architecture (per module)

```mermaid
graph LR
    subgraph Module["Module (e.g. memory, runtime)"]
        direction TB
        API["api/<br/>Interfaces"]
        APP["application/<br/>Implementations"]
        DOM["domain/<br/>Models"]
        CONFIG["config/<br/>Configuration"]
        INFRA["infrastructure/<br/>Adapters"]
        INTF["interfaces/rest/<br/>Controllers"]
        
        INTF --> API
        API --> APP
        APP --> DOM
        APP --> INFRA
        CONFIG --> APP
    end
    
    style API fill:#e1f5fe
    style APP fill:#c8e6c9
    style DOM fill:#fff9c4
    style INFRA fill:#ffe0b2
    style INTF fill:#f3e5f5
    style CONFIG fill:#f5f5f5
```

---

## Enterprise AI Platform

```mermaid
graph TB
    subgraph Enterprise_AI["Enterprise AI Platform"]
        Gateway["AI Gateway"]
        Provider["Provider Platform"]
        Prompt["Prompt Platform"]
        Memory["Memory Platform"]
        Runtime["Agent Runtime"]
        Knowledge["Knowledge Platform"]
        Semantic["Semantic Search"]
        Conversation["Conversation Platform"]
        Copilot["Enterprise Copilots"]
        Analytics["AI Analytics"]
        Governance["AI Governance"]
        Security["AI Security"]
    end

    subgraph Infrastructure["Shared Infrastructure"]
        Kafka[("Kafka Events")]
        Redis[("Redis Cache")]
        Postgres[("PostgreSQL")]
        Vector[(Vector Store)]
        Monitor[("Monitoring")]
    end

    Gateway --> Kafka
    Provider --> Kafka
    Prompt --> Kafka
    Memory --> Kafka
    Runtime --> Kafka
    Knowledge --> Kafka
    Semantic --> Kafka
    Conversation --> Kafka

    Gateway --> Redis
    Provider --> Redis
    Prompt --> Redis
    Memory --> Redis
    Runtime --> Redis
    Knowledge --> Redis
    Semantic --> Redis
    Conversation --> Redis

    Memory --> Postgres
    Runtime --> Postgres
    Knowledge --> Postgres
    Semantic --> Postgres
    Conversation --> Postgres

    Memory --> Vector
    Semantic --> Vector

    Gateway --> Monitor
    Provider --> Monitor
    Prompt --> Monitor
    Memory --> Monitor
    Runtime --> Monitor
    Analytics --> Monitor
    Governance --> Monitor

    Runtime --> Memory
    Runtime --> Knowledge
    Runtime --> Gateway
    Copilot --> Runtime
    Copilot --> Conversation
```

---

## Layer Isolation

```mermaid
flowchart TD
    subgraph Controllers["Controller Layer"]
        C1["@RestController"]
    end

    subgraph Service["Service Layer"]
        S1["@Service"]
    end

    subgraph Repository["Repository Layer"]
        R1["@Repository (Spring Data)"]
    end

    subgraph Entity["Entity Layer"]
        E1["@Entity (JPA)"]
    end

    subgraph Domain["Domain Layer"]
        D1["Record / Enum"]
    end

    C1 --> S1
    S1 --> R1
    R1 --> E1
    S1 --> D1
    
    style Domain fill:#fff9c4,stroke:#f9a825
    style Controllers fill:#f3e5f5,stroke:#7b1fa2
    style Service fill:#c8e6c9,stroke:#388e3c
    style Repository fill:#ffe0b2,stroke:#e65100
    style Entity fill:#ffccbc,stroke:#bf360c
```

---

## Package Naming Convention

```
com.sporekart.ai
├── {module}/
│   ├── api/                     -- Inbound port interfaces
│   ├── application/             -- @Service implementations
│   ├── config/                  -- @ConfigurationProperties
│   ├── domain/                  -- Records, enums (no framework)
│   ├── infrastructure/
│   │   ├── kafka/               -- Kafka publishers
│   │   ├── monitoring/          -- Micrometer metrics
│   │   ├── persistence/         -- @Entity, @Repository
│   │   └── redis/               -- Cache services
│   └── interfaces/
│       └── rest/                -- @RestController
│           └── dto/             -- Request/Response records
├── events/                      -- Domain/Integration events
├── shared/
│   ├── constants/               -- Constants
│   ├── enums/                   -- Shared enums
│   ├── interfaces/              -- Base interfaces
│   └── util/                    -- Utilities
└── config/                      -- Global config
```

---

## Technology Stack

- **Java 21** — Records, sealed classes, pattern matching
- **Spring Boot 3.3.3** — Web, JPA, Security, Actuator, Cache, Validation
- **Spring Modulith 1.2.4** — Module boundary verification
- **Kafka** — Event-driven messaging
- **Redis** — Caching
- **PostgreSQL** — Primary database (H2 for dev/test)
- **Flyway** — Database migrations
- **OpenSearch** — Full-text search (via Vector Store)
- **Micrometer + Prometheus** — Metrics and monitoring
- **SpringDoc OpenAPI** — API documentation
- **ArchUnit** — Architecture validation
