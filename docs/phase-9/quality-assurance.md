# Quality Assurance Engine

> Companion to [sprint-24-part-10.md](./sprint-24-part-10.md) and [validation-framework.md](./validation-framework.md). Mock Mode.

## Overview

The Quality Assurance (QA) Engine runs 12 automated checks against each product to assess data quality, completeness, and consistency. It is the core validation layer between product creation and publishing readiness. All checks run on mock data. No backend, no real product database.

## 12 Quality Checks

Each check is a pure function returning `{ passed: boolean, message: string }`. Checks are independent — a failure in one does not block others.

| # | Check | ID | What It Validates |
|---|-------|----|-------------------|
| 1 | Required Fields | `required` | All mandatory fields are populated |
| 2 | Field Length | `length` | Field values within min/max length constraints |
| 3 | Duplicate Detection | `duplicate` | Product name/SKU does not match existing entries |
| 4 | Invalid Values | `invalid` | No invalid characters, types, or out-of-range values |
| 5 | Broken References | `broken_ref` | All references (category, type, variants) resolve |
| 6 | Formatting | `formatting` | Email, URL, phone, tax ID formats are valid |
| 7 | Naming Convention | `naming` | Product name, slug, meta title follow conventions |
| 8 | Image Validation | `images` | Images present, meet resolution, aspect ratio, size limits |
| 9 | Video Validation | `videos` | Video URLs valid, duration acceptable, thumbnails present |
| 10 | Spec Validation | `specs` | Specification fields populated, types match definitions |
| 11 | Variant Validation | `variants` | Variant attributes consistent, no gaps in variant matrix |
| 12 | Document Validation | `docs` | Required documents attached, certificates valid |

## Score Calculation

```ts
interface QAResult {
  productId: string;
  checks: QACheck[];
  score: number;       // 0–100
  passed: number;
  total: number;
  timestamp: string;
}

function calculateScore(checks: QACheck[]): number {
  const passed = checks.filter(c => c.passed).length;
  const total = checks.length; // always 12
  return Math.round((passed / total) * 100);
}
```

The score is displayed as a `ScoreRing` component — an SVG circle with animated percentage fill. Color coding:

| Score Range | Color | Label |
|-------------|-------|-------|
| 90–100 | Green | Excellent |
| 70–89 | Blue | Good |
| 50–69 | Amber | Needs Improvement |
| 0–49 | Red | Failed |

## Types

```ts
// src/admin/modules/products/validation/types.ts

type QACheckId =
  | 'required' | 'length' | 'duplicate' | 'invalid'
  | 'broken_ref' | 'formatting' | 'naming'
  | 'images' | 'videos' | 'specs' | 'variants' | 'docs';

interface QACheck {
  id: QACheckId;
  label: string;
  passed: boolean;
  message: string;
  category: 'critical' | 'major' | 'minor';
}

interface QAResult {
  productId: string;
  productName: string;
  sku: string;
  checks: QACheck[];
  score: number;
  passed: number;
  total: number;
  timestamp: string;
  runBy: string;
}
```

## Categories

Checks are grouped by severity for display and filtering:

| Category | Checks | Behaviour |
|----------|--------|-----------|
| Critical | `required`, `broken_ref` | Blocks publishing |
| Major | `duplicate`, `invalid`, `images`, `variants` | Requires review |
| Minor | `length`, `formatting`, `naming`, `videos`, `specs`, `docs` | Advisory warnings |

## Integration with Completeness Engine

The QA Engine integrates with the Field Completeness system (`FieldCompleteness.tsx`) to cross-reference results:

```ts
// Integration with completeness scoring
function getCompletenessImpact(qaResult: QAResult): CompletenessImpact {
  return {
    totalFields: 48,
    populatedFields: qaResult.checks
      .filter(c => c.passed)
      .reduce((sum, c) => sum + getFieldCountForCheck(c.id), 0),
    completenessScore: calculateCompletenessScore(qaResult),
  };
}
```

The completeness engine feeds into the product health dashboard and publishing readiness calculation.

## QualityAssuranceEngine Component

`QualityAssuranceEngine.tsx` renders:

1. **Summary bar**: Score ring + pass/total + status label
2. **Check list**: 12 rows with expandable detail per check
3. **Filter bar**: Filter by category (critical/major/minor) and status (pass/fail)
4. **Re-run button**: Re-runs all 12 checks (re-computes from mock data)
5. **Product selector**: Dropdown to switch between products

```tsx
// Conceptual layout
<QualityAssuranceEngine>
  <ScoreRing value={score} size={120} strokeWidth={12} />
  <QASummary passed={passed} total={total} />
  <QAFilterBar category={filterCategory} onCategoryChange={setFilter} />
  <QACheckList checks={filteredChecks} onToggleDetail={setExpanded} />
  <QAActions onRerun={handleRerun} />
</QualityAssuranceEngine>
```

## Mock Data

```ts
// mock/mockValidationData.ts (excerpt)
const MOCK_QA_RESULTS: QAResult[] = [
  {
    productId: 'SK-PROD-1001',
    productName: 'Premium White Mushroom',
    sku: 'SK-PWM-001',
    checks: [
      { id: 'required',    passed: true,  message: 'All required fields present', category: 'critical' },
      { id: 'length',      passed: true,  message: 'Field lengths within limits', category: 'minor' },
      { id: 'duplicate',   passed: false, message: 'Similar product name detected: "Premium White Mushrooms"', category: 'major' },
      { id: 'invalid',     passed: true,  message: 'No invalid values found', category: 'major' },
      { id: 'broken_ref',  passed: true,  message: 'All references valid', category: 'critical' },
      { id: 'formatting',  passed: true,  message: 'Formats valid', category: 'minor' },
      { id: 'naming',      passed: true,  message: 'Naming conventions followed', category: 'minor' },
      { id: 'images',      passed: true,  message: '3 images present, all meet resolution', category: 'major' },
      { id: 'videos',      passed: false, message: 'No product video uploaded', category: 'minor' },
      { id: 'specs',       passed: true,  message: 'Specifications complete', category: 'minor' },
      { id: 'variants',    passed: true,  message: 'Variant attributes consistent', category: 'major' },
      { id: 'docs',        passed: true,  message: 'Required documents attached', category: 'minor' },
    ],
    score: 83,
    passed: 10,
    total: 12,
    timestamp: '2026-07-15T10:30:00Z',
    runBy: 'System',
  },
  // 11 more products...
];
```

## Future AI-Assisted Validation

Planned enhancements for future sprints:

- **AI suggestions**: Machine learning model suggests fixes for failed checks based on product category
- **Smart duplicate detection**: Semantic similarity scoring beyond exact name matching
- **Image quality AI**: Auto-assessment of image clarity, composition, and relevance
- **Description quality scoring**: NLP-based assessment of product description completeness and readability
- **Automated categorization**: AI predicts missing category/tags based on product attributes
- **Bulk re-validation**: Run QA on all products with a single action

## Permissions

| Action | Viewer | Editor | Reviewer | Approver | Admin |
|--------|--------|--------|----------|----------|-------|
| View QA results | ✓ | ✓ | ✓ | ✓ | ✓ |
| Run QA | | ✓ | ✓ | ✓ | ✓ |
| Re-run QA | | ✓ | ✓ | ✓ | ✓ |
| Export QA report | | | ✓ | ✓ | ✓ |
| Override check result | | | | ✓ | ✓ |

## Mock Mode

All quality checks run against mock data only. No products are actually validated against a real database. No duplicate detection against live catalog. No image analysis. The re-run button re-computes from the same mock data set with randomized pass/fail for demo purposes.
