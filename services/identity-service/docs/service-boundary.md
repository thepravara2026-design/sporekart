# Identity Service Boundary

## Ownership

The Identity Service owns authentication, session validation, and customer profile state for the SporeKart platform.

## REST contracts

- OpenAPI contract: ../../contracts/openapi/identity-service.yaml
- Event contract: ../../contracts/asyncapi/identity-service.yaml

## Primary responsibilities

- Authenticate users with username and password or OTP challenge flows.
- Issue and validate access tokens and refresh tokens.
- Manage current customer profile data and profile update events.
- Publish identity domain events for downstream services.

## Security expectations

- Bearer JWT authentication is required for profile operations.
- Refresh token authentication is required for token renewal.
- All public endpoints must enforce input validation and rate limiting.
