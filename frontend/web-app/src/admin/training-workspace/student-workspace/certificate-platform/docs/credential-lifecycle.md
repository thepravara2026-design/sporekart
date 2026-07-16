# Certificate Platform — Credential Lifecycle

## Lifecycle Stages

```
draft → pending-approval → approved → generated → issued → shared → verified
                                                                  ↓
                                                             expired
                                                             revoked
                                                             archived
```

## Stage Details

| Stage | Description | Next States |
|-------|-------------|-------------|
| **Draft** | Certificate template created, not yet submitted | pending-approval |
| **Pending Approval** | Awaiting administrative approval | approved, draft |
| **Approved** | Approved by authority | generated |
| **Generated** | Certificate document created | issued |
| **Issued** | Officially issued to student | shared, verified, expired |
| **Shared** | Student has shared the credential | verified, expired |
| **Verified** | Credential verified by third party | expired |
| **Expired** | Certificate validity period ended | (terminal) |
| **Revoked** | Certificate revoked by issuer | (terminal) |
| **Archived** | Certificate archived | (terminal) |

## CertificateTimeline Component

The `CertificateTimeline` component visualizes the first 7 stages (draft → verified) with color-coded dots and connecting lines. The current status is highlighted with a larger dot and semibold text.
