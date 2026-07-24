# Repository Standards

## Port Interface (Domain Layer)

```java
public interface EntityRepositoryPort {
    Entity save(Entity entity);
    Optional<Entity> findById(String id);
    List<Entity> findAll();
    void deleteById(String id);
}
```

## JPA Adapter (Infrastructure Layer)

```java
@Primary
@Repository
@Transactional
public class JpaEntityRepositoryAdapter implements EntityRepositoryPort {
    private final EntityJpaRepository jpaRepository;

    public JpaEntityRepositoryAdapter(EntityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Entity save(Entity entity) {
        return jpaRepository.save(EntityEntity.fromDomain(entity)).toDomain();
    }

    @Override
    public Optional<Entity> findById(String id) {
        return jpaRepository.findById(id).map(EntityEntity::toDomain);
    }

    @Override
    public List<Entity> findAll() {
        return jpaRepository.findAll().stream().map(EntityEntity::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
```

## JPA Entity

```java
@Entity
@Table(name = "entities")
public class EntityEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected EntityEntity() {}

    public static EntityEntity fromDomain(Entity domain) { ... }
    public Entity toDomain() { ... }
}
```

## InMemory Adapter (Test/Dev)

```java
@Repository
public class InMemoryEntityRepository implements EntityRepositoryPort {
    private final Map<String, Entity> store = new ConcurrentHashMap<>();

    @Override
    public Entity save(Entity entity) {
        store.put(entity.getId(), entity);
        return entity;
    }
    // ...
}
```

## Naming Conventions

| Layer | Convention | Example |
|-------|-----------|---------|
| Port interface | {Entity}RepositoryPort | CartRepositoryPort |
| JPA adapter | Jpa{Entity}RepositoryAdapter | JpaCartRepositoryAdapter |
| JPA repository | {Entity}JpaRepository | CartJpaRepository |
| JPA entity | {Entity}Entity | CartEntity |
| InMemory repo | InMemory{Entity}Repository | InMemoryCartRepository |

## Service Catalog — JPA Implementations

| Service | Port | JPA Adapter | JPA Repo | Entity | InMemory |
|---------|------|-------------|----------|--------|---------|
| admin-service | AdminRepositoryPort | JpaAdminRepositoryAdapter | AdminSupportTicketJpaRepository, AdminApprovalJpaRepository | SupportTicketEntity, ApprovalRequestEntity | InMemoryAdminRepository |
| analytics-service | AnalyticsRepositoryPort | JpaAnalyticsRepositoryAdapter | WidgetJpaRepository, ReportJpaRepository, SeoJpaRepository | DashboardWidgetEntity, ReportRequestEntity, SeoMetadataEntity | InMemoryAnalyticsRepository |
| cart-service | CartRepositoryPort | JpaCartRepositoryAdapter | CartJpaRepository, CartItemJpaRepository | CartEntity, CartItemEntity | InMemoryCartRepository |
| catalog-service | ProductRepositoryPort | JpaCatalogRepositoryAdapter | CatalogJpaRepository | ProductEntity | InMemoryProductRepository |
| content-service | ContentRepositoryPort | JpaContentRepositoryAdapter | ContentJpaRepository | ReviewEntity | InMemoryContentRepository |
| fulfillment-service | ShipmentRepositoryPort | JpaShipmentRepositoryAdapter | ShipmentJpaRepository | ShipmentEntity, ShipmentItemEntity | InMemoryShipmentRepository |
| identity-service | UserRepositoryPort | JpaUserRepositoryAdapter | SpringDataUserRepository | UserEntity, RoleEntity, PermissionEntity | None |
| inventory-service | InventoryRepositoryPort | JpaInventoryRepositoryAdapter | InventoryJpaRepository | InventoryItemEntity | InMemoryInventoryRepository |
| notification-service | NotificationRepositoryPort | JpaNotificationRepositoryAdapter | NotificationJpaRepository | NotificationMessageEntity | InMemoryNotificationRepository |
| order-service | OrderRepositoryPort | JpaOrderRepositoryAdapter | OrderJpaRepository | OrderEntity, OrderItemEntity | InMemoryOrderRepository |
| payment-service | PaymentRepositoryPort | JpaPaymentRepositoryAdapter | PaymentJpaRepository | PaymentEntity | InMemoryPaymentRepository |
| risk-service | RiskRepositoryPort | JpaRiskRepositoryAdapter | RiskJpaRepository | RiskAssessmentEntity | InMemoryRiskRepository |
| search-service | SearchRepositoryPort | JpaSearchRepositoryAdapter | SearchJpaRepository | SearchDocumentEntity | InMemorySearchRepository |
| support-service | SupportRepositoryPort | JpaSupportRepositoryAdapter | SupportJpaRepository | SupportTicketEntity | InMemorySupportRepository |
| training-service | TrainingRepositoryPort | JpaTrainingRepositoryAdapter | TrainingJpaRepository, GrowerProfileJpaRepository | TrainingProgramEntity, GrowerProfileEntity | InMemoryTrainingRepository |
