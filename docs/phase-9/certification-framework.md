# Certification Framework

> Companion to [sprint-24-part-10.md](./sprint-24-part-10.md) and [compliance-framework.md](./compliance-framework.md). Mock Mode.

## Overview

The Certification Framework awards products with badges based on compliance scores, quality assurance results, and marketplace readiness. There are 6 certification levels with emoji badges, validity periods, and issuer tracking. All certifications are mock with no legal standing. No real certification authority integration.

## 6 Certification Levels

| Level | Emoji | Code | Minimum Score Required | Validity | Auto-Renewable |
|-------|-------|------|-----------------------|----------|----------------|
| Bronze | 🥉 | `bronze` | QA ≥ 50, Compliance ≥ 50 | 6 months | Yes |
| Silver | 🥈 | `silver` | QA ≥ 70, Compliance ≥ 70 | 12 months | Yes |
| Gold | 🥇 | `gold` | QA ≥ 85, Compliance ≥ 85 | 24 months | Yes |
| Enterprise | 💎 | `enterprise` | QA ≥ 95, Compliance ≥ 95, Marketplace ≥ 80 | 36 months | No |
| Marketplace Ready | 🏪 | `marketplace_ready` | Marketplace ≥ 80, all channels ≥ 60 | 12 months | Yes |
| Export Ready | 🌐 | `export_ready` | Compliance ≥ 90, Export channel ≥ 80 | 24 months | No |

## Types

```ts
// src/admin/modules/products/validation/types.ts

type CertificationLevel =
  | 'bronze'
  | 'silver'
  | 'gold'
  | 'enterprise'
  | 'marketplace_ready'
  | 'export_ready';

interface Certification {
  id: string;
  productId: string;
  productName: string;
  level: CertificationLevel;
  badge: string;                // emoji character
  label: string;                // display name
  score: number;                // qualifying score
  issuedBy: string;             // issuer name
  issuedAt: string;             // ISO date
  expiresAt: string;            // ISO date
  validityMonths: number;
  autoRenew: boolean;
  status: 'active' | 'expired' | 'revoked' | 'pending_renewal';
  certificateUrl?: string;      // placeholder for PDF certificate
}
```

## Badge System

Badges render via the `BadgeDisplay` component:

```tsx
<BadgeDisplay
  level="gold"
  badge="🥇"
  label="Gold Certified"
  issuedBy="SporeKart Quality Assurance"
  issuedAt="2026-07-15"
  expiresAt="2028-07-15"
  status="active"
  size="lg"
/>
```

Each badge shows:
- Emoji icon
- Level name
- Issuer name
- Issue date
- Expiry date
- Status indicator (active=green, expired=red, revoked=grey, pending_renewal=amber)

## Validity Periods

| Level | Starts | Expires | Renewal Window |
|-------|--------|---------|----------------|
| Bronze | Issue date | +6 months | 30 days before expiry |
| Silver | Issue date | +12 months | 30 days before expiry |
| Gold | Issue date | +24 months | 60 days before expiry |
| Enterprise | Issue date | +36 months | 90 days before expiry (manual) |
| Marketplace Ready | Issue date | +12 months | 30 days before expiry |
| Export Ready | Issue date | +24 months | 60 days before expiry (manual) |

## Issued By Tracking

Each certification records the issuer:

```ts
const ISSUERS = {
  system: 'SporeKart Quality Assurance',
  reviewer: 'Quality Reviewer',
  approver: 'Compliance Approver',
  admin: 'System Administrator',
  external: 'External Certification Body',  // future
};
```

The `issuedBy` field is set based on the role of the user who grants the certification. This provides an audit trail for compliance purposes.

## Auto-Renewal Placeholder

```ts
// Auto-renewal logic (future backend integration)
interface AutoRenewalConfig {
  enabled: boolean;
  checkIntervalDays: number;
  notifyBeforeDays: number;
  requireRevalidation: boolean;
  maxRenewals: number;        // 0 = unlimited
}

const AUTO_RENEWAL_CONFIG: Record<CertificationLevel, AutoRenewalConfig> = {
  bronze:            { enabled: true,  checkIntervalDays: 30, notifyBeforeDays: 30, requireRevalidation: false, maxRenewals: 0 },
  silver:            { enabled: true,  checkIntervalDays: 30, notifyBeforeDays: 30, requireRevalidation: false, maxRenewals: 0 },
  gold:              { enabled: true,  checkIntervalDays: 30, notifyBeforeDays: 60, requireRevalidation: false, maxRenewals: 0 },
  enterprise:        { enabled: false, checkIntervalDays: 90, notifyBeforeDays: 90, requireRevalidation: true,  maxRenewals: 1 },
  marketplace_ready: { enabled: true,  checkIntervalDays: 30, notifyBeforeDays: 30, requireRevalidation: false, maxRenewals: 0 },
  export_ready:      { enabled: false, checkIntervalDays: 60, notifyBeforeDays: 60, requireRevalidation: true,  maxRenewals: 1 },
};
```

Auto-renewable certifications automatically re-issue when the product maintains qualifying scores at the check interval. Non-auto-renewable levels (Enterprise, Export Ready) require manual re-validation by an Approver or Admin.

## Certification Dashboard

`CertificationDashboard.tsx` provides:

1. **Stats bar**: Total certified, active, expiring soon (30 days), expired counts
2. **Badge grid**: All certifications displayed as `BadgeDisplay` cards
3. **Filter bar**: Filter by level, status, issuer, date range
4. **Issue certification**: Modal form to grant new certification (role-gated)
5. **Renew action**: Button to renew expiring certifications
6. **Revoke action**: Modal confirmation to revoke (role-gated)

```tsx
<CertificationDashboard>
  <CertificationStats
    total={certifications.length}
    active={activeCount}
    expiringSoon={expiringCount}
    expired={expiredCount}
  />
  <CertificationFilterBar
    levels={filterLevels}
    status={filterStatus}
    onFilterChange={setFilters}
  />
  <CertificationGrid>
    {filteredCertifications.map(cert => (
      <BadgeDisplay key={cert.id} {...cert} />
    ))}
  </CertificationGrid>
  <CertificationActions
    onIssue={handleIssue}
    onRenew={handleRenew}
    onRevoke={handleRevoke}
  />
</CertificationDashboard>
```

## Certification Workflow

```
Product meets score thresholds
  → System suggests certification level
  → Reviewer/Approver reviews compliance
  → Certification granted (or rejected)
  → Badge displayed on product profile
  → Auto-renewal monitoring begins
  → Expiry notification sent 30 days before
  → Manual renewal for Enterprise/Export levels
```

## Mock Data

```ts
const MOCK_CERTIFICATIONS: Certification[] = [
  {
    id: 'CERT-001',
    productId: 'SK-PROD-1001',
    productName: 'Premium White Mushroom',
    level: 'gold',
    badge: '🥇',
    label: 'Gold Certified',
    score: 88,
    issuedBy: 'Quality Reviewer',
    issuedAt: '2026-01-15T10:00:00Z',
    expiresAt: '2028-01-15T10:00:00Z',
    validityMonths: 24,
    autoRenew: true,
    status: 'active',
  },
  {
    id: 'CERT-002',
    productId: 'SK-PROD-1002',
    productName: 'Organic Dried Shiitake',
    level: 'enterprise',
    badge: '💎',
    label: 'Enterprise Certified',
    score: 97,
    issuedBy: 'System Administrator',
    issuedAt: '2026-03-01T10:00:00Z',
    expiresAt: '2029-03-01T10:00:00Z',
    validityMonths: 36,
    autoRenew: false,
    status: 'active',
  },
  {
    id: 'CERT-003',
    productId: 'SK-PROD-1003',
    productName: 'Oyster Mushroom Spawn Kit',
    level: 'marketplace_ready',
    badge: '🏪',
    label: 'Marketplace Ready',
    score: 82,
    issuedBy: 'SporeKart Quality Assurance',
    issuedAt: '2026-06-01T10:00:00Z',
    expiresAt: '2027-06-01T10:00:00Z',
    validityMonths: 12,
    autoRenew: true,
    status: 'active',
  },
  // 9 more certifications...
];
```

## Permissions

| Action | Viewer | Editor | Reviewer | Approver | Admin |
|--------|--------|--------|----------|----------|-------|
| View certifications | ✓ | ✓ | ✓ | ✓ | ✓ |
| Suggest certification | | ✓ | ✓ | ✓ | ✓ |
| Issue certification | | | ✓ | ✓ | ✓ |
| Renew certification | | | ✓ | ✓ | ✓ |
| Revoke certification | | | | ✓ | ✓ |
| Grant Enterprise level | | | | ✓ | ✓ |
| Manage certification rules | | | | | ✓ |

## Future Integration

- **PDF certificate generation**: Downloadable certificate with QR code
- **Blockchain verification**: Certificate hash stored on ledger for tamper-proof verification
- **External certification bodies**: Integration with ISO, Organic, FSSAI certification authorities
- **Public badge API**: Embeddable badge for external marketplaces and websites
- **Bulk certification**: Grant certifications to multiple products at once

## Mock Mode

All certifications are mock. No actual certification authority is involved. Badges have no legal standing. Certificate PDF download is a placeholder. The auto-renewal system simulates renewal but does not actually re-validate against any external standards. Expiry dates are mock data and do not reflect real product shelf life or regulatory validity.
