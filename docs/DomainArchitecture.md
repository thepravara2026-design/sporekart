# Domain Architecture

## Hexagonal Architecture per Service

```
┌──────────────────────────────────────────────┐
│                interfaces/rest               │
│            (@RestController, DTOs)           │
├──────────────────────────────────────────────┤
│               application/service            │
│            (Use case orchestrators)          │
├──────────────────────────────────────────────┤
│                   domain/                    │
│  ┌──────────────────────────────────────┐    │
│  │         model/ (Entities, VOs)       │    │
│  │         repository/ (Ports)          │    │
│  └──────────────────────────────────────┘    │
├──────────────────────────────────────────────┤
│             infrastructure/                  │
│    ┌──────────────────────────────────┐      │
│    │  persistence/ (InMemory / JPA)   │      │
│    └──────────────────────────────────┘      │
└──────────────────────────────────────────────┘
```

## Domain Model Relationships

### Cart (cart-service)

```
Cart (Aggregate Root)
├── id: String (UUID)
├── customerId: String
├── items: List<CartItem> (owned entities)
│   ├── id, productId, sku, name
│   ├── unitPrice, quantity
│   └── subtotal (derived)
├── status: CartStatus [ACTIVE, CHECKED_OUT, EXPIRED, ABANDONED]
├── createdAt, updatedAt
└── behaviors: addItem, updateItemQuantity, removeItem, checkout
```

### Order (order-service)

```
Order (Aggregate Root)
├── id: String (UUID)
├── customerId: String
├── amount: BigDecimal
├── items: List<OrderItem> (value objects)
│   ├── productId, quantity, unitPrice
├── status: OrderStatus (14-state lifecycle)
├── createdAt, updatedAt
├── deleted: boolean (soft delete)
└── behaviors: cancel, confirm
```

### Support Ticket (support-service)

```
SupportTicket (Mutable Entity)
├── id: String (UUID)
├── customerId, subject, description
├── category: SupportCategory [ORDER_ISSUE..FEEDBACK]
├── priority: SupportPriority [LOW..URGENT]
├── status: TicketStatus [OPEN..CLOSED]
├── assignedTo, resolution
├── createdAt, updatedAt, resolvedAt
└── behaviors: assignTo, updateStatus, resolve, addResolution
```

### Risk Assessment (risk-service)

```
RiskAssessment (Immutable-style Entity)
├── id: String (UUID)
├── entityType, entityId
├── riskScore: int, riskLevel: RiskLevel (derived)
├── factors: List<String>
├── assessedBy, assessedAt
├── status: RiskStatus [PENDING..ESCALATED]
├── createdAt, updatedAt
└── behavior: withStatus (returns new instance)
```

### Review (content-service)

```
Review (Immutable-style Entity)
├── id: String (UUID)
├── productId, customerId
├── rating, title, content
├── status: ReviewStatus [PENDING..FLAGGED]
├── moderatedBy, moderatedAt
├── createdAt, updatedAt
└── behavior: withModeration (returns new instance)
```

## Exception Hierarchy (Common Pattern)

```
RuntimeException
└── {Service}Exception (abstract)
    ├── {Entity}NotFoundException
    └── {Other}Exception

@RestControllerAdvice
└── GlobalExceptionHandler
    └── ProblemDetails (RFC 7807)
        ├── type, title, status, detail
        └── instance, timestamp, errors
```

## Enum Lifecycles

| Service | Enum | Valid Transitions |
|---------|------|-------------------|
| cart-service | CartStatus | ACTIVE -> CHECKED_OUT / EXPIRED / ABANDONED |
| content-service | ReviewStatus | PENDING -> APPROVED / REJECTED / FLAGGED |
| risk-service | RiskStatus | PENDING -> ASSESSED -> MITIGATED / ACCEPTED / ESCALATED |
| search-service | (none) | -- |
| support-service | TicketStatus | OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED (with WAITING_ON_CUSTOMER at any stage) |
| order-service | OrderStatus | PENDING_PAYMENT -> PAYMENT_PROCESSING -> PAYMENT_SUCCESS/PAYMENT_FAILED -> ... -> DELIVERED/CANCELLED/REFUNDED |
| payment-service | PaymentStatus | INITIATED -> AUTHORIZED -> CAPTURED / FAILED / REFUNDED |
| shipment-service | ShipmentStatus | CREATED -> PENDING_PICKUP -> IN_TRANSIT -> DELIVERED/FAILED/CANCELLED/RETURNED |
| catalog-service | ProductStatus | DRAFT -> PUBLISHED -> ARCHIVED/DEACTIVATED |
| training-service | TrainingStatus | DRAFT -> PUBLISHED -> ARCHIVED |
