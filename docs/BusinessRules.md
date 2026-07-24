# Business Rules

## Cart Service

| ID | Rule | Enforcement |
|----|------|-------------|
| CART-1 | A cart belongs to exactly one customer | Cart.customerId required at creation |
| CART-2 | Items cannot be added to a checked-out or expired cart | CartService.addItem checks CartStatus |
| CART-3 | Item quantity must be positive | CartItem quantity field >= 1 |
| CART-4 | Checkout transitions cart to CHECKED_OUT | CartService.checkout() updates status |
| CART-5 | Duplicate product adds to existing item quantity | Cart.addItem() finds existing CartItem |
| CART-6 | Updating quantity to zero removes the item | Cart.updateItemQuantity(..., 0) removes item |
| CART-7 | Cart total is computed from items, not stored | Derived from items list |

## Content Service

| ID | Rule | Enforcement |
|----|------|-------------|
| CONT-1 | New reviews default to PENDING status | Review constructor sets PENDING |
| CONT-2 | Only ADMIN can moderate reviews | @PreAuthorize("hasRole('ADMIN')") on moderate endpoint |
| CONT-3 | Product reviews API returns only APPROVED reviews | ContentService.getProductReviews() filters by APPROVED |
| CONT-4 | A moderated review cannot be deleted | ContentService.deleteReview() checks isModerated |
| CONT-5 | Rating is 1-5 scale (validated elsewhere) | Domain model constraint in Review |

## Risk Service

| ID | Rule | Enforcement |
|----|------|-------------|
| RISK-1 | Risk level is derived from score (0-100) | RiskLevel.determineRiskLevel(score) static method |
| RISK-2 | Score < 25 = LOW, < 50 = MEDIUM, < 75 = HIGH, >= 75 = CRITICAL | RiskLevel enum mapping |
| RISK-3 | Risk assessment status transitions are linear | PENDING -> ASSESSED -> MITIGATED/ACCEPTED/ESCALATED |
| RISK-4 | Mitigate requires existing assessment | RiskService.mitigateRisk() fetches assessment first |
| RISK-5 | Escalate requires a reason | EscalateRiskRequest contains reason field |

## Search Service

| ID | Rule | Enforcement |
|----|------|-------------|
| SRC-1 | Search queries match against title, description, content, and tags | InMemorySearchRepository full-text matching |
| SRC-2 | Results are scored and returned in descending score order | SearchService.sortByScore() |
| SRC-3 | Search supports pagination (default page 0, size 20) | SearchQuery record with defaults |
| SRC-4 | Results include total count and pagination metadata | SearchResult record |
| SRC-5 | Each entity has one indexed document per entityType+entityId | SearchService.reindexEntity() replaces existing |

## Support Service

| ID | Rule | Enforcement |
|----|------|-------------|
| SUP-1 | Ticket status progression: OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED | SupportTicket.updateStatus() validates transitions |
| SUP-2 | Only OPEN tickets can be assigned | SupportTicket.assignTo() checks status |
| SUP-3 | Resolution must be provided when resolving | SupportTicket.resolve() requires resolution text |
| SUP-4 | Tickets track assignment history via assignedTo field | SupportTicket.assignedTo updated on each assignment |
| SUP-5 | Customer can view only their own tickets | SupportService.getCustomerTickets() |

## Cross-cutting Rules

| ID | Rule | Enforcement |
|----|------|-------------|
| GEN-1 | Admin endpoints require ADMIN role | @PreAuthorize("hasRole('ADMIN')") on controller methods |
| GEN-2 | Public endpoints require no authentication | No @PreAuthorize annotation |
| GEN-3 | Authenticated endpoints require valid session | isAuthenticated() in @PreAuthorize |
| GEN-4 | Domain entities use UUID as primary key | Entity.id fields are UUID/String |
| GEN-5 | Service layer validates business rules before persisting | Service classes check invariants before calling repository |
| GEN-6 | Repository ports abstract storage from domain | Domain layer depends only on repository interfaces |
