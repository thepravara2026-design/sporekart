# Enterprise AI Workspace Folder Structure

```
ai-service/
├── pom.xml
├── README.md
├── Dockerfile
├── Dockerfile.placeholder
├── .env.example
├── config/
│   └── README.md
├── docs/
│   ├── README.md
│   ├── api/
│   ├── architecture/
│   ├── database/
│   ├── phase-4/
│   ├── phase-13/
│   │   ├── README.md
│   │   ├── architecture-overview.md
│   │   ├── memory-platform.md
│   │   ├── agent-runtime.md
│   │   └── folder-structure.md
│   ├── runbooks/
│   ├── security/
│   ├── sprints/
│   └── testing/
├── health/
│   └── README.md
├── tests/
│   └── README.md
└── src/
    ├── main/
    │   ├── java/com/sporekart/ai/
    │   │   ├── AiServiceApplication.java
    │   │   │
    │   │   ├── admin/                  (existing module)
    │   │   ├── analytics/              (existing module)
    │   │   ├── apiregistry/            (existing module)
    │   │   ├── application/            (existing shared)
    │   │   ├── approval/               (existing module)
    │   │   ├── assistant/              (existing module)
    │   │   ├── automation/             (existing module)
    │   │   ├── capabilitydiscovery/    (existing module)
    │   │   ├── chat/                   (existing module)
    │   │   ├── common/                 (existing shared)
    │   │   ├── compliance/             (existing module)
    │   │   ├── config/                 (existing config)
    │   │   ├── configregistry/         (existing module)
    │   │   ├── content/                (existing module)
    │   │   ├── conversation/           (existing module)
    │   │   ├── core/                   (existing module)
    │   │   ├── decision/               (existing module)
    │   │   ├── domain/                 (existing shared)
    │   │   ├── eventcatalog/           (existing module)
    │   │   ├── events/                 (NEW - Phase 13)
    │   │   ├── gateway/                (existing module)
    │   │   ├── governance/             (existing module)
    │   │   ├── infrastructure/         (existing shared)
    │   │   ├── interfaces/             (existing shared)
    │   │   ├── knowledge/              (existing module)
    │   │   ├── knowledgeregistry/      (existing module)
    │   │   ├── memory/                 (NEW - Phase 13)
    │   │   │   ├── api/
    │   │   │   ├── application/
    │   │   │   ├── config/
    │   │   │   ├── domain/
    │   │   │   ├── infrastructure/
    │   │   │   │   ├── kafka/
    │   │   │   │   ├── monitoring/
    │   │   │   │   ├── persistence/
    │   │   │   │   ├── redis/
    │   │   │   │   └── vector/
    │   │   │   └── interfaces/rest/
    │   │   │       └── dto/
    │   │   ├── monitoring/             (existing module)
    │   │   ├── policy/                 (existing module)
    │   │   ├── prompt/                 (existing module)
    │   │   ├── promptregistry/         (existing module)
    │   │   ├── provider/               (existing module)
    │   │   ├── providerregistry/       (existing module)
    │   │   ├── rag/                    (existing module)
    │   │   ├── risk/                   (existing module)
    │   │   ├── runtime/                (NEW - Phase 13)
    │   │   │   ├── api/
    │   │   │   ├── application/
    │   │   │   ├── config/
    │   │   │   ├── domain/
    │   │   │   ├── infrastructure/
    │   │   │   │   ├── executor/
    │   │   │   │   ├── kafka/
    │   │   │   │   ├── monitoring/
    │   │   │   │   ├── persistence/
    │   │   │   │   └── redis/
    │   │   │   └── interfaces/rest/
    │   │   │       └── dto/
    │   │   ├── search/                 (existing module)
    │   │   ├── semantic/               (existing module)
    │   │   ├── shared/                 (NEW - Phase 13)
    │   │   │   ├── constants/
    │   │   │   ├── enums/
    │   │   │   ├── interfaces/
    │   │   │   └── util/
    │   │   ├── usagetracking/          (existing module)
    │   │   └── workflow/               (existing module)
    │   │
    │   └── resources/
    │       ├── application.yml
    │       ├── application-local.yml
    │       ├── application-dev.yml
    │       ├── application-test.yml
    │       ├── application-prod.yml
    │       ├── logback-spring.xml
    │       └── db/migration/
    │           ├── V1__*.sql
    │           ├── V2__*.sql
    │           └── ...
    │
    └── test/java/com/sporekart/ai/
        ├── memory/
        │   └── MemoryModuleTest.java
        ├── runtime/
        │   └── AgentRuntimeModuleTest.java
        ├── shared/
        │   └── SharedModuleTest.java
        └── architecture/
            ├── ArchitectureTest.java
            ├── ModuleDependencyTest.java
            └── PackageStructureTest.java
```
