# Publishing Readiness

> Companion to [sprint-24-part-10.md](./sprint-24-part-10.md) and [publishing-workflow.md](./publishing-workflow.md). Mock Mode.

## Overview

Publishing Readiness determines whether a product is ready to be published based on quality assurance, compliance, certification, and marketplace readiness results. It assigns a readiness score (0–100) and one of 6 status states, tracks blockers, and surfaces warnings. All readiness calculations are mock. No real publishing gate integration.

## 6 Readiness Status States

| # | State | ID | Description | Score Range | Can Publish? |
|---|-------|----|-------------|-------------|--------------|
| 1 | Ready | `ready` | All checks pass, product is ready to publish | 90–100 | Yes |
| 2 | Needs Review | `needs_review` | Minor issues found, review recommended before publishing | 70–89 | Recommended |
| 3 | Blocked | `blocked` | Critical issues preventing publication | 0–49 | No |
| 4 | Incomplete | `incomplete` | Required data missing | 50–69 | No |
| 5 | Compliance Failure | `compliance_failure` | Compliance checks not passed | Any | No |
| 6 | Awaiting Approval | `awaiting_approval` | Submitted for approval, pending decision | Any | No |

## Readiness Score Calculation

```ts
interface PublishingReadinessResult {
  productId: string;
  productName: string;
  sku: string;
  score: number;                      // 0–100
  status: PublishingStatus;
  blockers: Blocker[];
  warnings: string[];
  qaScore: number;
  complianceScore: number;
  certificationLevel?: string;
  marketplaceScore: number;
  completenessScore: number;
  timestamp: string;
  lastUpdated: string;
}

type PublishingStatus =
  | 'ready'
  | 'needs_review'
  | 'blocked'
  | 'incomplete'
  | 'compliance_failure'
  | 'awaiting_approval';

function calculateReadinessScore(
  qaScore: number,
  complianceScore: number,
  marketplaceScore: number,
  completenessScore: number
): number {
  // Weighted calculation
  const weights = {
    qa: 0.35,
    compliance: 0.35,
    marketplace: 0.15,
    completeness: 0.15,
  };

  return Math.round(
    (qaScore * weights.qa) +
    (complianceScore * weights.compliance) +
    (marketplaceScore * weights.marketplace) +
    (completenessScore * weights.completeness)
  );
}

function determineStatus(score: number, blockers: Blocker[]): PublishingStatus {
  if (blockers.some(b => b.type === 'compliance')) return 'compliance_failure';
  if (blockers.some(b => b.type === 'critical')) return 'blocked';
  if (blockers.some(b => b.type === 'approval')) return 'awaiting_approval';
  if (score >= 90) return 'ready';
  if (score >= 70) return 'needs_review';
  return 'incomplete';
}
```

## Blocker Tracking

Blockers are issues that prevent publishing. Each blocker has a type and severity:

```ts
interface Blocker {
  id: string;
  type: 'critical' | 'compliance' | 'approval' | 'data';
  severity: 'error' | 'warning';
  source: string;              // which module raised the blocker
  message: string;
  field?: string;              // related field, if applicable
  requiredAction: string;      // what the user needs to do
}

interface BlockerResolution {
  blockerId: string;
  resolvedBy: string;
  resolvedAt: string;
  resolution: 'fixed' | 'overridden' | 'waived';
  notes?: string;
}
```

### Common Blockers

| Blocker | Type | Source | Required Action |
|---------|------|--------|-----------------|
| Required fields missing | `data` | QA Engine | Fill in missing fields |
| Broken references | `critical` | QA Engine | Fix product references |
| Compliance check failed | `compliance` | Compliance Framework | Address compliance issues |
| Approval pending | `approval` | Approval Queue | Wait for approval |
| Score below threshold | `critical` | Publishing Readiness | Improve product data |
| FSSAI compliance failure | `compliance` | Compliance Framework | Update FSSAI information |
| Image validation failed | `data` | QA Engine | Upload valid images |

## Warning System

Warnings are non-blocking advisories that suggest improvements:

```ts
interface ReadinessWarning {
  id: string;
  message: string;
  category: 'quality' | 'compliance' | 'marketplace' | 'completeness';
  recommendation: string;
  sourceScore?: number;       // score that triggered the warning
}

const WARNING_THRESHOLDS = {
  qaScoreBelow: 80,               // Warn if QA score < 80
  complianceScoreBelow: 80,       // Warn if compliance score < 80
  marketplaceScoreBelow: 70,      // Warn if marketplace score < 70
  completenessScoreBelow: 75,     // Warn if completeness score < 75
  missingImages: true,            // Warn if no images
  missingDescription: true,       // Warn if no description
  certExpiringSoon: 30,           // Warn if certification expires within N days
};
```

## PublishingReadiness Component

`PublishingReadiness.tsx` renders:

1. **Status banner**: Large colored banner showing current status with icon
2. **Score ring**: Overall readiness score (0–100)
3. **Score breakdown**: QA, Compliance, Marketplace, Completeness sub-scores
4. **Blocker list**: All active blockers with resolution actions
5. **Warning list**: Advisory warnings with recommendations
6. **Action panel**: Publish button (enabled only when `status === 'ready'`), Request Review button

```tsx
<PublishingReadiness productId={productId}>
  <StatusBanner status={status} score={score} />
  <ScoreRing value={score} size={160} strokeWidth={16} />
  <ScoreBreakdown
    scores={{
      qa: qaScore,
      compliance: complianceScore,
      marketplace: marketplaceScore,
      completeness: completenessScore,
    }}
  />
  <BlockerList blockers={blockers} onResolve={handleResolveBlocker} />
  <WarningList warnings={warnings} onDismiss={handleDismissWarning} />
  <ReadinessActions
    canPublish={status === 'ready'}
    canRequestReview={status === 'incomplete' || status === 'needs_review'}
    onPublish={handlePublish}
    onRequestReview={handleRequestReview}
  />
</PublishingReadiness>
```

## Integration with Publishing Workflow

The publishing readiness module integrates directly with the publishing workflow (see [publishing-workflow.md](./publishing-workflow.md)):

```ts
// Integration point: PublishingPanel checks readiness before allowing publish
function canPublish(productId: string): boolean {
  const readiness = getReadinessResult(productId);
  return readiness.status === 'ready';
}

// Publishing workflow checks
const PUBLISHING_GATES = {
  requireQAPass: true,
  requireCompliancePass: true,
  requireCertification: false,     // configurable
  requireMarketplaceReadiness: false, // configurable
  minimumScore: 80,                // configurable threshold
};
```

When a user attempts to publish, the `PublishingPanel` calls `canPublish()` which checks the readiness result. If the product is not ready, the panel shows the blockers and prevents publishing.

## State Transitions

```
                ┌──────────────┐
                │  Incomplete  │
                └──────┬───────┘
                       │ data completed
                ┌──────▼───────┐
          ┌─────│ Needs Review │◄────┐
          │     └──────┬───────┘     │
          │            │ reviewed    │
          │     ┌──────▼────────┐    │
          │     │ Awaiting      │    │
          │     │ Approval      │    │
          │     └──────┬────────┘    │
          │            │ approved    │
          │     ┌──────▼───────┐    │
          │     │    Ready     │─────┤ (re-check changes)
          │     └──────┬───────┘    │
          │            │ publish    │
          │     ┌──────▼───────┐    │
          │     │  Published   │    │
          │     └──────────────┘    │
          │                         │
          │  ┌─────────────────┐    │
          ├──│ Blocked         │────┘
          │  └─────────────────┘
          │
          │  ┌──────────────────────┐
          └──│ Compliance Failure   │
             └──────────────────────┘
```

## Mock Data

```ts
const MOCK_PUBLISHING_READINESS: PublishingReadinessResult[] = [
  {
    productId: 'SK-PROD-1001',
    productName: 'Premium White Mushroom',
    sku: 'SK-PWM-001',
    score: 83,
    status: 'needs_review',
    blockers: [
      {
        id: 'BLK-001',
        type: 'data',
        severity: 'error',
        source: 'Field Completeness',
        message: '2 required fields missing: brand, meta description',
        field: 'brand',
        requiredAction: 'Fill in brand and meta description fields',
      },
    ],
    warnings: [
      { id: 'WARN-001', message: 'QA score (83) below 85 threshold for Gold certification', category: 'quality', recommendation: 'Improve product data to reach Gold certification', sourceScore: 83 },
      { id: 'WARN-002', message: 'Image validation check has warnings', category: 'quality', recommendation: 'Review image requirements', sourceScore: 75 },
    ],
    qaScore: 83,
    complianceScore: 86,
    certificationLevel: 'silver',
    marketplaceScore: 78,
    completenessScore: 85,
    timestamp: '2026-07-15T10:30:00Z',
    lastUpdated: '2026-07-15T10:30:00Z',
  },
  // 11 more products...
];
```

## Permissions

| Action | Viewer | Editor | Reviewer | Approver | Admin |
|--------|--------|--------|----------|----------|-------|
| View readiness | ✓ | ✓ | ✓ | ✓ | ✓ |
| Resolve blockers | | ✓ | ✓ | ✓ | ✓ |
| Override blockers | | | | ✓ | ✓ |
| Publish (if ready) | | ✓ | ✓ | ✓ | ✓ |
| Force publish | | | | ✓ | ✓ |
| Configure thresholds | | | | | ✓ |

## Mock Mode

All readiness scores are calculated from mock QA, compliance, marketplace, and completeness data. No actual publishing gate enforces the readiness check — the publish action is always a mock transition. Blockers are informational only and do not actually prevent any action. The status state machine is simulated within the component state and does not persist.
