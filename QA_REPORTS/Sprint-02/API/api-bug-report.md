# API Bug Report — QA Sprint 2 Part 5

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17
**Classification:** CONFIDENTIAL

---

## Bug Summary

| Total | Critical | High | Medium | Low |
|-------|----------|------|--------|-----|
| 24 | 12 | 8 | 3 | 1 |

---

## 🔴 Critical Bugs

### BUG-API-001: Authentication Bypass on 128 Endpoints
- **Endpoints:** catalog, order, payment, inventory, fulfillment, training, admin, analytics, notification, ai services (128 endpoints)
- **Method:** All
- **Environment:** All
- **Preconditions:** None
- **Steps:** Send any HTTP request to any endpoint
- **Expected:** 401 Unauthorized
- **Actual:** 200 OK — all endpoints process requests without authentication
- **Severity:** CRITICAL | **Priority:** P0
- **Root Cause:** `anyRequest().permitAll()` or missing SecurityConfig

### BUG-API-002: Circular Auth Dependency on /auth/register
- **Endpoint:** `POST /auth/register`
- **Method:** POST
- **Environment:** All
- **Steps:** Send POST to `/auth/register` with valid registration data
- **Expected:** 201 Created with new user
- **Actual:** 401 Unauthorized — registration endpoint itself requires authentication
- **Severity:** CRITICAL | **Priority:** P0
- **Root Cause:** `authenticated()` applied to entire `/auth/**` path

### BUG-API-003: IDOR — No Order Ownership Check
- **Endpoint:** `GET /orders/{id}`
- **Method:** GET
- **Environment:** All
- **Preconditions:** None
- **Steps:** `GET /orders/ANY_ID` without being the order owner
- **Expected:** 403 Forbidden or filtered results
- **Actual:** Returns full order details regardless of ownership
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-004: IDOR — No Customer Filter Validation
- **Endpoint:** `GET /orders?customerId=X`
- **Method:** GET
- **Environment:** All
- **Steps:** Supply any customerId parameter
- **Expected:** Return only authenticated user's orders
- **Actual:** Returns orders for any customerId
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-005: Mass Assignment — Map<String,String> DTOs
- **Endpoints:** 30+ endpoints in ai-service (Procurement, Supplier, Warehouse, B2b, Marketplace)
- **Method:** POST/PUT
- **Environment:** All
- **Root Cause:** `@RequestBody Map<String,String>` allows arbitrary field injection
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-006: Analytics Customer PII Exposed
- **Endpoint:** `GET /analytics/customers`
- **Method:** GET
- **Environment:** All
- **Detail:** No authentication, no field filtering — full customer data exposed
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-007: Analytics Payment Data Exposed
- **Endpoint:** `GET /analytics/payments`
- **Method:** GET
- **Environment:** All
- **Detail:** No authentication — payment transaction data exposed
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-008: Anyone Can Create Products
- **Endpoint:** `POST /products`
- **Method:** POST
- **Environment:** All
- **Detail:** No authentication or role check — anyone can add/modify catalog
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-009: Anyone Can Create Orders
- **Endpoint:** `POST /orders`
- **Method:** POST
- **Environment:** All
- **Detail:** No authentication — anyone can create orders
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-010: Anyone Can Create Shipments
- **Endpoint:** `POST /shipments`
- **Method:** POST
- **Environment:** All
- **Detail:** No authentication — anyone can create/manage shipments
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-011: Anyone Can Initiate Payments
- **Endpoint:** `POST /payments/create-order`
- **Method:** POST
- **Environment:** All
- **Detail:** No authentication — anyone can initiate mock payments
- **Severity:** CRITICAL | **Priority:** P0

### BUG-API-012: Anyone Can Send Notifications
- **Endpoint:** `POST /notifications`
- **Method:** POST
- **Environment:** All
- **Detail:** No authentication — anyone can send arbitrary notifications
- **Severity:** CRITICAL | **Priority:** P0

---

## 🟠 High Bugs

### BUG-API-013: No JWT Filter Implementation
- **Endpoint:** identity-service (all authenticated endpoints)
- **Detail:** JWT secret configured but no filter to validate tokens
- **Severity:** HIGH | **Priority:** P1

### BUG-API-014: No Error Boundary Standardization
- **Endpoint:** All services
- **Detail:** No consistent error response envelope (exception for ai-service GatewayController)
- **Severity:** HIGH | **Priority:** P1

### BUG-API-015: Cart Service is Empty Placeholder
- **Endpoint:** cart-service (all)
- **Detail:** Entire service has no controllers — cart operations cannot be performed
- **Severity:** HIGH | **Priority:** P1

### BUG-API-016: Content Service is Empty Placeholder
- **Endpoint:** content-service (all)
- **Detail:** Entire service has no controllers — content operations cannot be performed
- **Severity:** HIGH | **Priority:** P1

### BUG-API-017: Search Service is Empty Placeholder
- **Endpoint:** search-service (all)
- **Detail:** Entire service has no controllers — search operations cannot be performed
- **Severity:** HIGH | **Priority:** P1

### BUG-API-018: Support Service is Empty Placeholder
- **Endpoint:** support-service (all)
- **Detail:** Entire service has no controllers — support ticket operations cannot be performed
- **Severity:** HIGH | **Priority:** P1

### BUG-API-019: GET /users/me Returns Placeholder
- **Endpoint:** `GET /users/me`
- **Method:** GET
- **Detail:** Returns string `"identity-ok"` instead of user account object
- **Severity:** HIGH | **Priority:** P1

### BUG-API-020: No Pagination on Any List Endpoint
- **Endpoint:** All list endpoints (GET /products, GET /orders, GET /trainings, etc.)
- **Detail:** Returns raw arrays with no pagination metadata — unbounded payloads possible
- **Severity:** HIGH | **Priority:** P1

---

## 🟡 Medium Bugs

### BUG-API-021: No CORS Configuration
- **Endpoint:** All services
- **Detail:** No `cors()` config — default behavior may block legitimate cross-origin requests
- **Severity:** MEDIUM | **Priority:** P2

### BUG-API-022: No Rate Limiting
- **Endpoint:** All services
- **Detail:** No 429 responses configured — DoS possible
- **Severity:** MEDIUM | **Priority:** P2

### BUG-API-023: Frontend Dashboards Send No Auth Headers
- **Endpoint:** All dashboard frontends
- **Detail:** admin-control-plane, risk, governance, compliance, approval, automation, registry-center — zero auth headers sent
- **Severity:** MEDIUM | **Priority:** P2

---

## 🟢 Low Bugs

### BUG-API-024: Missing @JsonInclude(NON_NULL)
- **Endpoint:** All services
- **Detail:** Null fields leak into JSON responses — inconsistent response shapes
- **Severity:** LOW | **Priority:** P3

---

## Bug Distribution by Service

| Service | Critical | High | Medium | Low | Total |
|---------|----------|------|--------|-----|-------|
| identity-service | 2 | 1 | 0 | 0 | 3 |
| catalog-service | 1 | 0 | 0 | 0 | 1 |
| cart-service | 0 | 1 | 0 | 0 | 1 |
| order-service | 2 | 0 | 0 | 0 | 2 |
| payment-service | 1 | 0 | 0 | 0 | 1 |
| notification-service | 1 | 0 | 0 | 0 | 1 |
| fulfillment-service | 1 | 0 | 0 | 0 | 1 |
| training-service | 1 | 0 | 0 | 0 | 1 |
| admin-service | 1 | 0 | 0 | 0 | 1 |
| analytics-service | 2 | 0 | 0 | 0 | 2 |
| ai-service | 1 | 0 | 0 | 0 | 1 |
| content-service | 0 | 1 | 0 | 0 | 1 |
| search-service | 0 | 1 | 0 | 0 | 1 |
| support-service | 0 | 1 | 0 | 0 | 1 |
| global (all services) | 0 | 3 | 3 | 1 | 7 |

---

## Blocker Assessment

**All 12 critical bugs are BLOCKERS.** The API layer is not safe for any environment beyond local development.

| Blocker | Reason |
|---------|--------|
| Auth bypass (128 endpoints) | Complete data access without any credentials |
| Circular registration | New users cannot register |
| IDOR on orders | Customer order data exposed |
| Mass assignment | Domain corruption via arbitrary field injection |
| PII exposure | Customer data leaked via analytics |
| Payment exposure | Financial data leaked via analytics |
| Unauthenticated CRUD | Anyone can create products, orders, shipments, payments, notifications |

---

*End of API Bug Report — 24 bugs (12 critical, 8 high, 3 medium, 1 low)*
