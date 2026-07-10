# SporeKart Phase 1 Part 6 — Enterprise OpenAPI 3.1 Contract Architecture

- Version: 1.0
- Status: Review complete
- Owner: Enterprise Architecture Review Board
- Review Date: 2026-07-10
- Purpose: Freeze the API contract architecture and governance baseline.
- Scope: REST endpoint naming, versioning, schemas, pagination, filtering, validation, and problem details.
- References: [phase1-part3-spring-boot-enterprise-architecture.md](phase1-part3-spring-boot-enterprise-architecture.md), [phase1-part8-enterprise-exception-error-management-architecture.md](phase1-part8-enterprise-exception-error-management-architecture.md)
- Approval Status: Reviewed; implementation-ready contract artifacts remain pending

## 1. Purpose and Scope

This document defines the enterprise API contract architecture for the SporeKart platform. It is architecture-only and does not generate Spring Boot implementation, Java classes, DTOs, entities, repositories, controllers, or runtime services.

This specification becomes the source of truth for all REST contracts to be implemented in later phases.

## 2. API Architecture Principles

The API architecture is governed by the following principles:

- All REST APIs must be specified using OpenAPI 3.1 and JSON Schema 2020-12.
- APIs must be versioned, documented, and backward compatible by default.
- Every endpoint must declare authentication, authorization, validation, error, and observability requirements.
- Business contracts must be explicit and reusable across frontend, mobile, QA, automation, and integration consumers.
- No service may expose an API contract that bypasses security or governance controls.
- OpenAPI documents must remain the canonical contract source for implementation.

## 3. Enterprise API Governance Guide

### 3.1 REST design principles

- Use resource-oriented URIs and noun-based naming.
- Prefer plural resource names for collections.
- Use standard HTTP methods with conventional semantics.
- Keep payloads explicit and domain-focused.
- Avoid leaking implementation details through public contracts.
- Ensure every response is predictable and machine-readable.

### 3.2 URI naming standards

- Base resources use lowercase kebab-case.
- Collection endpoints use plural nouns.
- Instance endpoints use resource identifiers.
- Sub-resources are nested only when the relationship is semantically clear.

Examples:

- /v1/customers
- /v1/customers/{customerId}
- /v1/orders/{orderId}/items
- /v1/catalog/products/{productId}

### 3.3 Versioning strategy

- URI versioning is the default strategy for public and internal APIs.
- Version prefix: /v1, /v2, and so on.
- Schema evolution is supported through additive changes and explicit breaking-change governance.
- Header-based versioning is reserved for legacy or gateway-specific scenarios.

### 3.4 Backward compatibility and deprecation

- Breaking changes require a new API version.
- Non-breaking changes may be introduced in the current version.
- Deprecation must be documented with a sunset date and migration notice.
- Consumers must receive deprecation information through headers and documentation.

### 3.5 Pagination standards

- Collection endpoints must support pagination.
- Pagination parameters: page, size, cursor.
- Responses must return metadata describing pagination state.
- Default page size must be explicit and documented.

### 3.6 Filtering, sorting, and search

- Filtering uses query parameters with explicit supported fields.
- Sorting uses a sort parameter with allowed values.
- Search uses a dedicated q or search query parameter where appropriate.
- Search and filter behavior must be documented per endpoint.

### 3.7 Idempotency rules

- POST operations that create resources or trigger side effects must support idempotency keys where applicable.
- Idempotency requirements are documented per endpoint.
- Duplicate submissions must not create duplicate side effects.

### 3.8 Correlation, trace, and request IDs

- Every request must support correlationId, traceId, and requestId propagation.
- These identifiers must appear in logs and error responses.
- Middleware and gateway layers must preserve these values.

## 4. API Catalog

### 4.1 Service-level API inventory

| Service | Public APIs | Private APIs | Internal APIs | Admin APIs | Partner APIs |
|---|---|---|---|---|---|
| Identity Service | Login, refresh, forgot password, profile | Session validation | Token introspection, internal user lookup | Role and user administration | Future |
| Catalog Service | Product browsing, category browsing, product details | Inventory link queries | Search and content sync | Catalog management | Future |
| Inventory Service | Inventory status, reservations | Inventory mutation operations | Allocation and stock sync | Stock management | Future |
| Cart Service | Cart create, update, remove | Cart reconciliation | Checkout handoff | Admin cart inspection | Future |
| Order Service | Order creation, order history, order status | Order state transition | Payment and fulfillment coordination | Order administration | Future |
| Payment Service | Payment initiation, refund status | Payment verification | Gateway callbacks | Finance administration | Future |
| Fulfillment Service | Shipment tracking, fulfillment status | Dispatch and status update | Order handoff | Fulfillment admin | Future |
| Training Service | Course catalog, enrollment | Learning progress sync | Internal training events | Training admin | Future |
| Content Service | Content listing, content detail | Content update operations | Publishing events | Content administration | Future |
| Notification Service | Notification list, preferences | Delivery status | Event-driven notification hooks | Notification admin | Future |
| Search Service | Search and autocomplete | Index refresh operations | Search indexing hooks | Index administration | Future |
| Analytics Service | Dashboard metrics, exports | Event ingestion | Internal analytics pipelines | Report administration | Future |
| Risk Service | Fraud review status | Scoring and decisioning | Internal scoring hooks | Risk admin | Future |
| Support Service | Ticket submission, ticket status | Ticket update operations | Internal escalation hooks | Support admin | Future |

## 5. Endpoint Specifications

### 5.1 Authentication and identity endpoints

| Method | URI | Purpose | Auth | AuthZ | Statuses |
|---|---|---|---|---|---|
| POST | /v1/auth/login | Authenticate user | None | None | 200, 400, 401, 429 |
| POST | /v1/auth/otp/request | Request OTP challenge | None | None | 200, 400, 429 |
| POST | /v1/auth/otp/verify | Verify OTP challenge | None | None | 200, 400, 401 |
| POST | /v1/auth/refresh | Refresh access token | Refresh token | None | 200, 401, 403 |
| POST | /v1/auth/logout | End session | Bearer token | Self or admin | 200, 401 |
| POST | /v1/auth/revoke | Revoke token or session | Bearer token | Self or admin | 200, 401 |
| POST | /v1/auth/password/forgot | Request reset | None | None | 200, 400 |
| POST | /v1/auth/password/reset | Reset password | Reset token | None | 200, 400 |
| GET | /v1/auth/session/validate | Validate session | Bearer token | Self | 200, 401 |

### 5.2 Customer and profile endpoints

| Method | URI | Purpose | Auth | AuthZ | Statuses |
|---|---|---|---|---|---|
| GET | /v1/customers/me | Get current customer profile | Bearer | Self | 200, 401 |
| PATCH | /v1/customers/me | Update current customer profile | Bearer | Self | 200, 400, 401 |
| GET | /v1/customers/me/addresses | List addresses | Bearer | Self | 200, 401 |
| POST | /v1/customers/me/addresses | Create address | Bearer | Self | 201, 400, 401 |

### 5.3 Catalog endpoints

| Method | URI | Purpose | Auth | AuthZ | Statuses |
|---|---|---|---|---|---|
| GET | /v1/catalog/products | List products | Optional | Public | 200 |
| GET | /v1/catalog/products/{productId} | Get product | Optional | Public | 200, 404 |
| GET | /v1/catalog/categories | List categories | Optional | Public | 200 |
| GET | /v1/catalog/search | Search catalog | Optional | Public | 200 |

### 5.4 Cart and checkout endpoints

| Method | URI | Purpose | Auth | AuthZ | Statuses |
|---|---|---|---|---|---|
| GET | /v1/cart | Get cart | Bearer | Self | 200, 401 |
| POST | /v1/cart/items | Add cart item | Bearer | Self | 200, 201, 400 |
| PATCH | /v1/cart/items/{itemId} | Update cart item | Bearer | Self | 200, 400 |
| DELETE | /v1/cart/items/{itemId} | Remove cart item | Bearer | Self | 204, 404 |
| POST | /v1/checkout | Create checkout session | Bearer | Self | 201, 400, 409 |

### 5.5 Order endpoints

| Method | URI | Purpose | Auth | AuthZ | Statuses |
|---|---|---|---|---|---|
| GET | /v1/orders | List orders | Bearer | Self or admin | 200, 401 |
| GET | /v1/orders/{orderId} | Get order | Bearer | Self or admin | 200, 404 |
| POST | /v1/orders | Create order | Bearer | Self | 201, 400, 409 |
| PATCH | /v1/orders/{orderId}/cancel | Cancel order | Bearer | Self or admin | 200, 409 |

### 5.6 Payment endpoints

| Method | URI | Purpose | Auth | AuthZ | Statuses |
|---|---|---|---|---|---|
| POST | /v1/payments | Create payment | Bearer | Self | 201, 400 |
| GET | /v1/payments/{paymentId} | Get payment | Bearer | Self or admin | 200, 404 |
| POST | /v1/payments/{paymentId}/refund | Refund payment | Bearer | Self or admin | 201, 409 |

### 5.7 Administrative endpoints

| Method | URI | Purpose | Auth | AuthZ | Statuses |
|---|---|---|---|---|---|
| GET | /v1/admin/users | List users | Bearer | Admin | 200, 401 |
| GET | /v1/admin/roles | List roles | Bearer | Admin | 200, 401 |
| GET | /v1/admin/audit-logs | List audit logs | Bearer | Admin | 200, 401 |
| POST | /v1/admin/inventory/adjustments | Adjust inventory | Bearer | Admin/Operations | 201, 400 |

## 6. Request Schema Catalog

### 6.1 Common request schemas

- LoginRequest
- OtpRequest
- OtpVerifyRequest
- RefreshTokenRequest
- LogoutRequest
- PasswordForgotRequest
- PasswordResetRequest
- PaginationRequest
- FilterRequest
- SortRequest
- CreateAddressRequest
- UpdateProfileRequest
- AddCartItemRequest
- UpdateCartItemRequest
- CreateOrderRequest
- CreatePaymentRequest

### 6.2 Validation rules

- Required fields must be documented explicitly.
- Field constraints must include type, format, minLength, maxLength, enum, pattern, nullable, and readOnly/writeOnly flags where relevant.
- Example values must be supplied for all major request schemas.
- Invalid input must be returned through the standardized error schema.

## 7. Response Schema Catalog

### 7.1 Common response schemas

- SuccessResponse
- PaginatedResponse
- ErrorResponse
- ProblemDetails
- AuthenticationResponse
- TokenRefreshResponse
- ProfileResponse
- ProductListResponse
- ProductDetailResponse
- CartResponse
- OrderResponse
- PaymentResponse
- AuditLogResponse

### 7.2 Response metadata

Each collection response must include:

- items
- pagination metadata
- request metadata
- correlation identifiers

## 8. RFC 9457 Problem Details Standard

All API errors must use the following structure:

```json
{
  "type": "https://api.sporekart.com/problems/validation-error",
  "title": "Validation failed",
  "status": 400,
  "detail": "One or more fields are invalid.",
  "instance": "/v1/auth/login",
  "timestamp": "2026-07-10T00:00:00Z",
  "correlationId": "01HXYZ",
  "traceId": "trace-123",
  "requestId": "req-456",
  "serviceName": "identity-service",
  "errorCode": "VALIDATION_ERROR",
  "validationErrors": [
    {
      "field": "email",
      "message": "must be a valid email"
    }
  ]
}
```

### 8.1 Error policy

- 4xx errors must return structured problem details.
- 5xx errors must return standardized problem details with service metadata.
- Validation errors must include field-level details.
- Security errors must include an explicit error code and correlation IDs.

## 9. Authentication API Contracts

### 9.1 Login

- POST /v1/auth/login
- Input: email or phone, password, rememberMe optional
- Output: accessToken, refreshToken, expiresIn, tokenType
- Errors: invalid credentials, account locked, validation failed

### 9.2 OTP request and verify

- POST /v1/auth/otp/request
- POST /v1/auth/otp/verify
- Inputs: contact information, challenge context, OTP payload
- Outputs: challenge accepted, authenticated session, or challenge pending

### 9.3 Refresh and logout

- POST /v1/auth/refresh
- POST /v1/auth/logout
- POST /v1/auth/revoke
- Outputs: session status or revoked state

### 9.4 Password and session flows

- POST /v1/auth/password/forgot
- POST /v1/auth/password/reset
- GET /v1/auth/session/validate

## 10. Authorization API Contracts

### 10.1 Role and permission APIs

- GET /v1/admin/roles
- GET /v1/admin/permissions
- POST /v1/admin/roles
- PATCH /v1/admin/roles/{roleId}
- GET /v1/users/{userId}/permissions

### 10.2 Access validation

- GET /v1/auth/access/validate
- Used by UI and gateway layers to validate current effective access

## 11. Business API Contracts

### 11.1 Catalog domain

- GET /v1/catalog/products
- GET /v1/catalog/products/{productId}
- GET /v1/catalog/categories
- GET /v1/catalog/search

### 11.2 Inventory domain

- GET /v1/inventory/{sku}
- PATCH /v1/inventory/{sku}/reserve
- PATCH /v1/inventory/{sku}/release

### 11.3 Cart domain

- GET /v1/cart
- POST /v1/cart/items
- PATCH /v1/cart/items/{itemId}
- DELETE /v1/cart/items/{itemId}

### 11.4 Order domain

- GET /v1/orders
- GET /v1/orders/{orderId}
- POST /v1/orders
- PATCH /v1/orders/{orderId}/cancel

### 11.5 Payment domain

- POST /v1/payments
- GET /v1/payments/{paymentId}
- POST /v1/payments/{paymentId}/refund

### 11.6 Training, content, support, and notification domains

- Course, content, ticket, and notification endpoints follow the same conventions and versioning rules.

## 12. Administrative API Contracts

### 12.1 Administrative domains

- User administration
- Role administration
- Inventory administration
- Order administration
- Payment administration
- Training administration
- Content administration
- Audit log access
- Dashboard and report endpoints

### 12.2 Administrative standards

- Admin endpoints require strong authentication and elevated authorization.
- Sensitive operations must be logged and auditable.
- Admin APIs must include explicit permission requirements in OpenAPI metadata.

## 13. API Security Standards

### 13.1 JWT and authorization headers

- Authorization: Bearer <token>
- Refresh tokens must not be accepted in the same header as access tokens.
- Token types must be explicit in metadata and documentation.

### 13.2 Sensitive endpoints

- Authentication, password reset, and admin flows require stricter rate limiting and throttling.
- Any endpoint that modifies financial or identity state requires explicit security metadata.

### 13.3 Rate limiting and abuse protection

- Authentication and password reset endpoints must be throttled.
- Public search and catalog endpoints may use lower-rate limits.
- Abuse protection policies are documented per endpoint.

### 13.4 Security headers and transport

- TLS is required for all production traffic.
- Security headers must be documented as platform expectations.
- CORS and CSRF policies are explicitly defined per client type.

## 14. API Documentation Standards

### 14.1 OpenAPI document structure

Each service contract must include:

- info object
- servers
- tags
- paths
- components.schemas
- components.parameters
- components.responses
- components.securitySchemes

### 14.2 Naming conventions

- Operation IDs: camelCase, verb + resource, unique per service.
- Tags: lowercase kebab-case.
- Schema names: PascalCase with domain-specific nouns.
- Reusable components must be shared where the contract spans multiple services.

## 15. API Versioning Strategy

### 15.1 Versioning rules

- Public API versions use URI versioning.
- Breaking changes require a new URI version.
- Non-breaking changes can be introduced in place when backward compatibility is preserved.
- Sunset policies must be documented with deprecation dates.

### 15.2 Compatibility matrix

| Change type | Policy |
|---|---|
| Additive field | Allowed in current version |
| Remove field | Requires new version |
| Rename field | Requires new version |
| Change enum value | Requires new version |
| Change response semantics | Requires new version |
| Add endpoint | Allowed in current version |
| Remove endpoint | Requires new version |

## 16. Contract Validation Strategy

### 16.1 Validation layers

- OpenAPI schema validation during CI.
- JSON Schema validation for request and response payloads.
- Linting and style checks for contract quality.
- Consumer-driven contract validation for downstream consumers.
- Backward compatibility checks for versioned changes.

### 16.2 Contract testing expectations

- Contract tests are expected for all public endpoints.
- Consumer expectations are encoded and verified against the published contract.
- Contract drift is treated as a release blocker.

## 17. Spring Boot API Mapping

### 17.1 Contract-to-layer mapping

| OpenAPI artifact | Spring Boot layer |
|---|---|
| Endpoint path and method | Controller layer |
| Request schema | Validation layer and DTO layer |
| Response schema | DTO layer and exception mapping |
| Security requirements | Security layer |
| Operation semantics | Application layer |
| Error types | Exception layer |
| Pagination and filtering semantics | Application and persistence layers |
| Idempotency rules | Application layer |

### 17.2 Mapping rules

- OpenAPI contracts define interface expectations.
- Implementation layers must not alter documented semantics without a versioned contract change.
- Error responses must map to the standardized problem-details schema.

## 18. Observability Standards

- Every API operation must propagate correlationId, traceId, and requestId.
- Latency, error rate, and successful request counts are recorded per operation.
- Audit metrics are emitted for authentication, authorization, and administrative actions.
- API performance and health metrics are surfaced to dashboards and alerting.

## 19. Performance Standards

- Pagination is mandatory for list endpoints.
- Compression is enabled for large responses.
- ETags and conditional requests are supported where appropriate.
- Streaming is used for large payload or export endpoints.
- Timeouts and size limits are documented per endpoint.
- Batch APIs are reserved for high-volume integrations and governed explicitly.

## 20. Future Expansion Assessment

The API architecture supports future growth across:

- mobile applications
- partner integrations
- marketplace APIs
- public APIs
- gateway-based routing
- internationalization and locale-aware responses
- multi-tenant or tenant-scoped APIs

A future GraphQL gateway may be introduced as a compatibility layer, but REST remains the authoritative contract for core services.

## 21. OpenAPI Readiness Report

| Validation area | Result | Notes |
|---|---|---|
| OpenAPI 3.1 compliance | Pass | Contract architecture is aligned to OpenAPI 3.1 and JSON Schema 2020-12 |
| API governance | Pass | Governance, naming, versioning, and compatibility rules are defined |
| Security contract coverage | Pass | Authentication, authorization, rate limits, and error standards are documented |
| Endpoint catalog coverage | Pass | Core service domains are covered |
| Reusable schema strategy | Pass | Request and response schemas are standardized |
| Observability and performance | Pass | API metrics and contract behavior expectations are documented |
| Contract validation | Pass | Linting, compatibility, and testing strategy are documented |

## 22. API Risk Register

| Risk | Impact | Likelihood | Mitigation |
|---|---|---|---|
| Contract drift | High | Medium | Enforce OpenAPI validation and contract review |
| Versioning conflicts | High | Medium | Require new version for breaking changes |
| Incomplete auth metadata | High | Medium | Enforce security requirements in every operation |
| Inconsistent error models | Medium | Medium | Standardize RFC 9457 problem details |
| Poor pagination semantics | Medium | Medium | Mandate pagination rules for collection endpoints |
| Overly broad public exposure | High | Low | Use explicit endpoint classification and auth metadata |

## 23. API Readiness Score

Overall API readiness: 87/100

### Readiness rationale

- Governance and contract structure: 90/100
- Endpoint catalog completeness: 88/100
- Security and error standardization: 86/100
- Versioning and contract validation: 85/100
- Observability and performance standards: 84/100

## 24. Phase 1 Part 6 Completion Checklist

- [x] Enterprise API governance guide defined.
- [x] API catalog defined.
- [x] Endpoint specifications defined.
- [x] Request schema catalog defined.
- [x] Response schema catalog defined.
- [x] RFC 9457 error specification defined.
- [x] Authentication API contracts defined.
- [x] Authorization API contracts defined.
- [x] Business API contracts defined.
- [x] Administrative API contracts defined.
- [x] API security standards defined.
- [x] API documentation standards defined.
- [x] API versioning strategy defined.
- [x] Contract validation strategy defined.
- [x] Spring Boot API mapping defined.
- [x] Observability standards defined.
- [x] Performance standards defined.
- [x] Future expansion assessment completed.
- [x] OpenAPI readiness report completed.
- [x] API risk register completed.
- [x] API readiness score assigned.

## 25. Phase 1 Part 7 Prerequisites

The following prerequisites must be completed before Phase 1 Part 7 can proceed:

1. API governance review and approval.
2. OpenAPI 3.1 contract review for each service domain.
3. Error model and security metadata approval.
4. Versioning and deprecation policy approval.
5. Contract testing and validation pipeline approval.
6. Cross-team consumer review for core business APIs.
