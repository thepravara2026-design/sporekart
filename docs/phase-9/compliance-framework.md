# Compliance Framework

> Companion to [sprint-24-part-10.md](./sprint-24-part-10.md) and [validation-framework.md](./validation-framework.md). Mock Mode.

## Overview

The Compliance Framework manages regulatory, legal, and safety compliance for agricultural and food products in the SporeKart Enterprise PIM platform. It runs 14 compliance check categories per product and calculates a compliance score. All checks are mock. No real government API integration.

## 14 Compliance Check Categories

Each category represents a regulatory or compliance domain relevant to agricultural/food products sold in India:

| # | Category | ID | Description |
|---|----------|----|-------------|
| 1 | Label Compliance | `label` | Product label meets regulatory requirements |
| 2 | Manufacturer Details | `manufacturer` | Manufacturer name, address, contact present |
| 3 | Country of Origin | `country` | Country of origin declared |
| 4 | MRP Display | `mrp` | Maximum Retail Price displayed correctly |
| 5 | GST Compliance | `gst` | GST rate valid and correctly applied |
| 6 | HSN Code | `hsn` | Valid HSN code assigned |
| 7 | Expiry Date | `expiry` | Expiry/best-before date present and valid |
| 8 | Batch Number | `batch` | Batch/lot number present |
| 9 | Legal Metrology | `legal` | Net quantity, manufacturer details, customer care |
| 10 | Consumer Information | `consumer` | Consumer care info, return policy, usage instructions |
| 11 | Agricultural Compliance | `agriculture` | Seed/plant variety, growing method, certification |
| 12 | Food Safety | `food_safety` | FSSAI license displayed, food safety parameters |
| 13 | FSSAI Compliance | `fssai` | FSSAI registration/license number valid |
| 14 | Agri Marketing | `agri` | Agricultural Produce Marketing Committee (APMC) compliance |

## Types

```ts
// src/admin/modules/products/validation/types.ts

type ComplianceCategoryId =
  | 'label' | 'manufacturer' | 'country' | 'mrp' | 'gst' | 'hsn'
  | 'expiry' | 'batch' | 'legal' | 'consumer' | 'agriculture'
  | 'food_safety' | 'fssai' | 'agri';

interface ComplianceCheck {
  id: ComplianceCategoryId;
  label: string;
  passed: boolean;
  message: string;
  required: boolean;        // true = mandatory for publishing
  regulatoryBody?: string;  // e.g. 'FSSAI', 'BIS', 'APMC'
}

interface ComplianceResult {
  productId: string;
  productName: string;
  sku: string;
  checks: ComplianceCheck[];
  score: number;            // 0–100
  passed: number;
  total: number;
  timestamp: string;
  reviewedBy?: string;
  reviewedAt?: string;
  status: 'pending' | 'in_review' | 'approved' | 'rejected';
}
```

## Score Display

The compliance score is displayed as a `ScoreRing` with the following color coding:

| Score Range | Color | Label |
|-------------|-------|-------|
| 100 | Green | Fully Compliant |
| 80–99 | Blue | Mostly Compliant |
| 50–79 | Amber | Partially Compliant |
| 1–49 | Red | Non-Compliant |
| 0 | Dark Red | Critical Non-Compliance |

```tsx
<ScoreRing
  value={complianceResult.score}
  size={140}
  strokeWidth={14}
  color={getScoreColor(complianceResult.score)}
  label={`${complianceResult.passed}/${complianceResult.total} checks passed`}
/>
```

## Required Approvals

Certain compliance categories require human approval before publishing:

| Category | Requires Approval | Approver Role |
|----------|------------------|---------------|
| Label Compliance | Yes | Reviewer |
| FSSAI Compliance | Yes | Approver |
| Food Safety | Yes | Approver |
| Legal Metrology | Yes | Reviewer |
| Agri Marketing | Yes | Approver |
| All others | No | — |

The `ApprovalQueue` component shows products pending compliance approval with approve/reject actions. Approval is gated by the 5-tier permission matrix.

## ComplianceFramework Component

`ComplianceFramework.tsx` renders:

1. **Summary section**: Score ring + pass/total count + overall status
2. **Category grid**: 14 compliance categories as expandable cards
3. **Filter bar**: Filter by status (pass/fail) and requirement (mandatory/optional)
4. **Approval section**: Shown when product requires approval for outstanding categories
5. **Review history**: Previous reviews with reviewer name and date

```tsx
<ComplianceFramework productId={productId}>
  <ComplianceSummary score={score} passed={passed} total={total} />
  <ComplianceFilterBar status={filter} onStatusChange={setFilter} />
  <ComplianceCheckList checks={filteredChecks} />
  <ApprovalSection
    required={requiresApproval}
    onApprove={handleApprove}
    onReject={handleReject}
  />
</ComplianceFramework>
```

## Future Government API Integration

The framework is designed for future integration with Indian government regulatory APIs:

### FSSAI API (Food Safety)
- Validate FSSAI license numbers in real-time
- Check license expiry dates
- Verify food product categorization
- Planned: `POST /api/v1/compliance/fssai/validate`

### BIS API (Bureau of Indian Standards)
- Validate BIS registration for applicable products
- Check ISI mark certification
- Planned: `POST /api/v1/compliance/bis/validate`

### Agri Marketing API (APMC)
- Verify APMC mandi registration
- Check agricultural produce compliance
- Planned: `POST /api/v1/compliance/apmc/validate`

```ts
// Future integration placeholder
interface GovernmentAPIIntegration {
  fssai: {
    validateLicense: (licenseNumber: string) => Promise<FSSAIValidation>;
    checkCompliance: (productId: string) => Promise<FSSAICompliance>;
  };
  bis: {
    validateRegistration: (regNumber: string) => Promise<BISValidation>;
  };
  agriMarketing: {
    verifyAPMC: (registrationId: string) => Promise<APMCValidation>;
  };
}
```

## Certification Levels

Compliance score directly maps to certification eligibility:

| Certification Level | Minimum Compliance Score | Validity |
|--------------------|------------------------|----------|
| Bronze | 50 | 6 months |
| Silver | 70 | 12 months |
| Gold | 85 | 24 months |
| Enterprise | 95 | 36 months |
| Marketplace Ready | 80 | 12 months |
| Export Ready | 90 | 24 months |

See [certification-framework.md](./certification-framework.md) for full certification details.

## Mock Data

```ts
// mock/mockValidationData.ts (excerpt)
const MOCK_COMPLIANCE_RESULTS: ComplianceResult[] = [
  {
    productId: 'SK-PROD-1001',
    productName: 'Premium White Mushroom',
    sku: 'SK-PWM-001',
    checks: [
      { id: 'label',        passed: true,  message: 'Label meets FSSAI packaging rules', required: true, regulatoryBody: 'FSSAI' },
      { id: 'manufacturer', passed: true,  message: 'Manufacturer details complete', required: true },
      { id: 'country',      passed: true,  message: 'Country of origin: India', required: true },
      { id: 'mrp',          passed: true,  message: 'MRP displayed correctly', required: true },
      { id: 'gst',          passed: true,  message: 'GST 5% (fresh vegetables)', required: true },
      { id: 'hsn',          passed: true,  message: 'HSN 0709.51.00', required: true },
      { id: 'expiry',       passed: true,  message: 'Best before 7 days from packaging', required: true },
      { id: 'batch',        passed: true,  message: 'Batch number present', required: true },
      { id: 'legal',        passed: false, message: 'Net quantity missing on label', required: true },
      { id: 'consumer',     passed: true,  message: 'Consumer info present', required: false },
      { id: 'agriculture',  passed: true,  message: 'Grown in controlled environment', required: false },
      { id: 'food_safety',  passed: true,  message: 'Food safety parameters within limits', required: true, regulatoryBody: 'FSSAI' },
      { id: 'fssai',        passed: true,  message: 'FSSAI license valid until Dec 2026', required: true, regulatoryBody: 'FSSAI' },
      { id: 'agri',         passed: false, message: 'APMC registration pending', required: false, regulatoryBody: 'APMC' },
    ],
    score: 86,
    passed: 12,
    total: 14,
    timestamp: '2026-07-15T10:30:00Z',
    status: 'pending',
  },
  // 11 more products...
];
```

## Permissions

| Action | Viewer | Editor | Reviewer | Approver | Admin |
|--------|--------|--------|----------|----------|-------|
| View compliance | ✓ | ✓ | ✓ | ✓ | ✓ |
| Run compliance check | | ✓ | ✓ | ✓ | ✓ |
| Approve compliance | | | ✓ | ✓ | ✓ |
| Override compliance | | | | ✓ | ✓ |
| Manage compliance rules | | | | | ✓ |

## Mock Mode

All compliance checks run against mock data. No FSSAI, BIS, or APMC API calls are made. Compliance status is determined from static mock data sets. The approval workflow is simulated with no real persistence. Certification granted is always mock and has no legal standing.
