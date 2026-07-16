# Certificate Platform — Wallet Architecture

## Overview

The Credential Wallet is the central repository for all student credentials: certificates, digital badges, and achievements.

## Wallet Data Model

```
CredentialWallet
  ├── studentId, studentName
  ├── certificates: Certificate[]     — All issued certificates
  ├── badges: DigitalBadge[]          — All earned badges
  ├── achievements: Achievement[]     — All achievements
  ├── totalCredentials                — Sum of all credential types
  ├── activeCredentials               — Count of active credentials
  └── sharedCredentials               — Count of shared credentials
```

## Wallet Page Layout

- **Left pane**: Student list with search (filter by name)
- **Right pane**: Selected student's wallet with:
  - WalletSummary (student name, credential counts)
  - Tab switcher (Certificates / Badges / Achievements)
  - Content grid filtered by active tab

## Future Extensions

- Blockchain credential storage
- Verifiable credential (VC) support
- Open Badges 3.0 compatibility
- Credential revocation list management
- Bulk credential operations
