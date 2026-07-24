# Transaction Guide

## Transaction Strategy

All persistence operations use Spring's `@Transactional` annotation.

### Adapter-level Transactions

Every JPA adapter method is annotated `@Transactional`:

```java
@Primary
@Repository
@Transactional
public class JpaCartRepositoryAdapter implements CartRepositoryPort {
```

### Service-level Transactions

Service methods requiring multi-repository atomicity use `@Transactional`:

```java
@Service
@Transactional
public class CartService {
    public Cart checkout(String cartId) {
        Cart cart = cartPort.findById(cartId).orElseThrow(...);
        cart.checkout();
        return cartPort.save(cart);
    }
}
```

### Default Behavior

- Propagation: `REQUIRED` (joins existing transaction or creates new)
- Isolation: `DEFAULT` (database-specific)
- Rollback for: `RuntimeException` and `Error` (Spring default)
- Read-only hints: Not explicitly set (can be optimized per use case)

## ACID Guarantees

| Property | Implementation |
|----------|---------------|
| Atomicity | @Transactional ensures all-or-nothing execution |
| Consistency | Flyway migration validation + JPA entity mapping + constraints |
| Isolation | Database-level (read committed default in PostgreSQL) |
| Durability | PostgreSQL WAL + HikariCP connection pool |
