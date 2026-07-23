# Customer Copilot Developer Guide

**Version:** 0.2.0
**Last Updated:** 2026-07-23

---

## 1. Quick Start

### Prerequisites

- JDK 21
- Maven 3.9+
- Docker (for integration tests)
- Access to `shared-copilot` SDK (built locally or from Nexus)

### Build & Run

```bash
# Clone the repository
git clone https://github.com/sporekart/sporekart.git
cd sporekart

# Build shared-copilot SDK first
cd shared-copilot
mvn clean install -DskipTests

# Build and run Customer Copilot
cd customer-copilot-service
mvn clean install -DskipTests
mvn spring-boot:run
```

The service starts on port 8099:
```
http://localhost:8099/swagger-ui.html
http://localhost:8099/actuator/health
```

### Verify

```bash
curl http://localhost:8099/api/v1/copilot/customer/health
# {"status":"UP","service":"customer-copilot-service","version":"0.2.0"}
```

---

## 2. How the Customer Copilot Uses the Enterprise Copilot Framework

The Customer Copilot is built on the `shared-copilot` SDK, which provides the **Enterprise Copilot Framework**. Here is how the framework is consumed:

### 2.1 Engine Initialization

In `CustomerCopilotApplication.java`, the `CopilotEngine` bean is created using the SDK factory:

```java
@Bean
public CopilotEngine copilotEngine() {
    return CopilotSDK.createDefaultEngine();
}
```

### 2.2 Persona Registration

The `CustomerCopilotRegistrationService` loads the customer persona from `DefaultPersonas`:

```java
var persona = DefaultPersonas.customerPersona();
```

The persona defines:
- **Name**: "Customer Copilot"
- **Role**: Shopping and support assistant
- **Tone**: Friendly, helpful, knowledgeable
- **Constraints**: Never hallucinate, always cite sources, respect user privacy
- **Languages**: English (primary), Kannada-ready

### 2.3 Capability Registration

The service registers 15 capabilities at startup:

```java
private void registerCopilotCapabilities() {
    // 15 capabilities registered programmatically
    // Each capability maps to a tool that can be executed by ToolExecutor
}
```

Each capability has:
- A unique identifier
- A description (used for intent matching)
- An associated tool implementation
- Input/output schema

### 2.4 Session Management

Sessions are managed through `CopilotEngine`:

```java
// Create session
copilotEngine.startSession(sessionId, CopilotType.CUSTOMER, userContext);

// Process message
CompletableFuture<CopilotResponse> future = copilotEngine.processMessage(
    sessionId, message, userContext, pageContext
);

// End session
copilotEngine.endSession(sessionId);
```

### 2.5 Context Passing

`UserContext` and `PageContext` carry contextual information through the engine pipeline:

```java
UserContext userContext = new UserContext(userId, userName, email, language, roles, featureFlags);
PageContext pageContext = new PageContext(pageUrl, pageTitle, section, entityType, entityId);
```

---

## 3. Project Structure

```
customer-copilot-service/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/sporekart/customer/copilot/
    │   │   ├── CustomerCopilotApplication.java       # Main entry point
    │   │   ├── config/
    │   │   │   ├── CustomerCopilotConfig.java         # Configuration properties
    │   │   │   ├── OpenApiConfig.java                 # OpenAPI/Swagger config
    │   │   │   └── SecurityConfig.java                # Spring Security config
    │   │   ├── dto/
    │   │   │   ├── ChatRequest.java                   # Chat request DTO
    │   │   │   ├── ChatResponse.java                  # Chat response DTO
    │   │   │   ├── RecommendRequest.java              # Recommendation request
    │   │   │   ├── RecommendResponse.java             # Recommendation response
    │   │   │   ├── ProductSearchRequest.java          # Product search request
    │   │   │   ├── ProductSearchResponse.java         # Product search response
    │   │   │   ├── ContextResponse.java               # Context assembly response
    │   │   │   ├── FeedbackRequest.java               # Feedback request
    │   │   │   ├── OrderQueryRequest.java             # Order query request
    │   │   │   └── TrainingQueryRequest.java          # Training query request
    │   │   ├── domain/
    │   │   │   ├── ProductItem.java                   # Product domain model
    │   │   │   ├── ProductRecommendation.java         # Recommendation domain model
    │   │   │   ├── CustomerOrder.java                 # Order domain model
    │   │   │   ├── CustomerProfile.java               # Customer profile model
    │   │   │   ├── TrainingCourse.java                # Training course model
    │   │   │   ├── KnowledgeArticle.java              # Knowledge article model
    │   │   │   ├── ShoppingCartItem.java              # Cart item model
    │   │   │   └── ConversationMessage.java           # Conversation message model
    │   │   └── service/
    │   │       ├── CustomerCopilotOrchestrator.java   # Core orchestration logic
    │   │       ├── CustomerContextService.java        # Context management
    │   │       └── CustomerCopilotRegistrationService.java  # Capability registration
    │   └── resources/
    │       └── application.yml                        # Application configuration
    └── test/
        └── java/com/sporekart/customer/copilot/
            └── ...                                    # Unit and integration tests
```

---

## 4. Adding New Capabilities

### Step 1: Define the Capability

Register a new capability in `CustomerCopilotRegistrationService.registerCopilotCapabilities()`:

```java
private void registerCopilotCapabilities() {
    log.info("Capability 16/16: New Custom Capability");
    // Future: ToolRegistry.register("new_capability", new NewCapabilityTool());
}
```

### Step 2: Implement the Tool

Create a tool class that implements the capability logic:

```java
public class NewCapabilityTool implements CopilotTool {
    @Override
    public String getName() { return "new_capability"; }

    @Override
    public String getDescription() { return "Description for intent matching"; }

    @Override
    public ToolResult execute(ToolContext context) {
        // Access context, call downstream services, return results
        return new ToolResult("capability_output", Map.of("key", "value"));
    }
}
```

### Step 3: Register Intent Mappings

Add intent keywords to the `IntentRouter` so user messages map to the new capability.

### Step 4: Add DTOs (if needed)

Create request/response records in the `dto/` package.

### Step 5: Add API Endpoint (if needed)

Add a new endpoint in the controller:

```java
@PostMapping("/new-capability")
public ResponseEntity<?> newCapability(@Valid @RequestBody NewCapabilityRequest request) {
    // Delegate to orchestrator or tool
}
```

---

## 5. Extending Recommendation Strategies

### Step 1: Create the Strategy

Create a new class implementing the strategy interface:

```java
public class TrendingStrategy implements RecommendationStrategy {
    @Override
    public String getName() { return "TRENDING"; }

    @Override
    public double getWeight() { return 0.10; }

    @Override
    public Map<String, Double> execute(String customerId, RecommendationContext context) {
        // Score products based on trending data
        // Return Map<productId, score>
    }
}
```

### Step 2: Register in the Ensemble

Add the strategy to the `RecommendationEngine`'s strategy list:

```java
public RecommendationEngine() {
    this.strategies = List.of(
        new PersonalizedStrategy(),
        new ContextAwareStrategy(),
        new SeasonalStrategy(),
        new LocationAwareStrategy(),
        new CrossSellStrategy(),
        new UpsellStrategy(),
        new TrendingStrategy()  // New strategy
    );
}
```

### Step 3: Adjust Weights

Ensure the ensemble weights sum to 1.0. Adjust existing strategy weights to accommodate the new strategy.

---

## 6. Adding Product Categories

Product categories are managed in the **Catalog Service**, not in the Customer Copilot. The copilot queries the Catalog Service for products.

To add a new category:
1. Add the category to the Catalog Service's product schema
2. Tag products with the new category
3. Add seasonal/festival mappings in the Recommendation Engine
4. Add complementary product mappings for cross-sell
5. The Customer Copilot will automatically discover the new category through catalog queries

For location-aware recommendations, update `RecommendationEngine`:
```java
private static final Map<String, List<String>> REGIONAL_CATEGORIES = Map.of(
    "north-india", List.of("button-mushroom", "compost", ...),
    "south-india", List.of("oyster-mushroom", "tropical-kits", ...),
    // Add new region/category mappings here
);
```

---

## 7. Customizing the Persona

The persona is defined in `CustomerCopilotRegistrationService`:

```java
var persona = DefaultPersonas.customerPersona();
```

To customize, modify the persona builder or create a custom persona:

```java
Persona customPersona = Persona.builder()
    .name("SporeKart Assistant")
    .role("Mushroom Cultivation Expert")
    .tone("Educational and encouraging")
    .constraints(List.of(
        "Always provide citations for cultivation advice",
        "Never recommend pesticides without safety warnings",
        "Support multilingual responses (English, Kannada, Hindi)"
    ))
    .build();
```

Persona properties that can be customized:

| Property | Description | Example |
|---|---|---|
| `name` | Display name | "SporeKart Assistant" |
| `role` | Role description | "Mushroom Cultivation Expert" |
| `tone` | Tone of voice | "Educational and encouraging" |
| `constraints` | Behavioral constraints | List of rules |
| `languages` | Supported languages | ["en", "kn", "hi"] |
| `greeting` | Initial greeting | "Namaste! How can I help you grow?" |
| `maxResponseLength` | Max response characters | 2000 |
| `includeSuggestions` | Show follow-up suggestions | true |

---

## 8. Integration Testing Guide

### Test Configuration

Use `application-test.yml` for integration tests:

```yaml
customer:
  copilot:
    streaming-enabled: false
    max-context-length: 5

spring:
  datasource:
    url: jdbc:h2:mem:testdb
```

### Writing Integration Tests

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerCopilotIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testChatEndpoint() throws Exception {
        String request = """
            {
                "message": "show me oyster mushrooms",
                "sessionId": "test-session-1"
            }
            """;

        mockMvc.perform(post("/api/v1/copilot/customer/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer test-token")
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sessionId").value("test-session-1"))
            .andExpect(jsonPath("$.message").isString());
    }

    @Test
    void testRecommendEndpoint() throws Exception {
        String request = """
            {
                "customerId": "test-customer-1",
                "category": "mushroom-spawn",
                "limit": 5
            }
            """;

        mockMvc.perform(post("/api/v1/copilot/customer/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer test-token")
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.recommendations").isArray());
    }

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/copilot/customer/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(post("/api/v1/copilot/customer/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"test\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void testValidationError() throws Exception {
        String request = """
            {
                "message": ""
            }
            """;

        mockMvc.perform(post("/api/v1/copilot/customer/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer test-token")
                .content(request))
            .andExpect(status().isBadRequest());
    }
}
```

### Running Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# Specific test class
mvn test -Dtest=CustomerCopilotIntegrationTest
```

---

## 9. Local Development Setup

### Full Stack Local

```bash
# Start dependent services using Docker Compose
docker-compose -f docker/docker-compose.yml up -d \
  catalog-service \
  order-service \
  identity-service \
  training-service

# Start Customer Copilot
cd customer-copilot-service
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Environment Variables

```bash
# Required
export SPRING_PROFILES_ACTIVE=local
export CATALOG_SERVICE_URL=http://localhost:8083
export ORDER_SERVICE_URL=http://localhost:8086
export TRAINING_SERVICE_URL=http://localhost:8090
export KNOWLEDGE_SERVICE_URL=http://localhost:8095
export IDENTITY_SERVICE_URL=http://localhost:8081
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

### Debugging

```bash
# Remote debug on port 5005
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```

### Code Style

- Java 21 records for DTOs and domain models
- Constructor validation in compact constructors
- SLF4J for logging
- Spring Boot 3.3.x conventions
- Follow hexagonal architecture: `interfaces` → `application` → `domain` → `infrastructure`
