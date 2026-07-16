# Certificate Platform — Verification Model

## Overview

The Verification Center manages certificate verification requests, providing a trusted mechanism for employers, institutions, and government agencies to validate credentials.

## Verification Status

| Status | Description |
|--------|-------------|
| unverified | No verification attempted |
| pending | Verification request submitted |
| verified | Credential verified successfully |
| failed | Verification failed (invalid credential) |
| expired | Verification period expired |

## Verification Methods

| Method | Description | Status |
|--------|-------------|--------|
| manual | Manual verification by admin | Mock ready |
| qr | QR code-based verification | Placeholder |
| employer | Employer portal verification | Placeholder |
| blockchain | Blockchain-based verification | Future |

## Verification Center Layout

- Summary widgets (total, verified, pending, failed)
- Filterable verification records list
- Click a record to see certificate lifecycle timeline
- Credential sharing panel with 8 share method placeholders

## Future Integrations

- Employer verification portal
- Government Skill Mission verification
- DigiLocker integration
- National Skill Registry
- Blockchain credential verification
