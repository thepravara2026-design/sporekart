# Quality Dashboard

## Overview

The Quality Dashboard provides real-time visibility into the health of the design system. It tracks component coverage, documentation completeness, accessibility compliance, responsive validation, review progress, and approval percentages. The dashboard is available at `/design-system/quality`.

## Metrics

### Component Coverage

**Definition:** The percentage of planned components that have been implemented, tested, and documented.

```
Component Coverage = (Implemented Components / Planned Components) × 100
```

| Tier | Planned | Implemented | Coverage |
|------|---------|-------------|----------|
| P0 (Core) | 10 | 10 | 100% |
| P1 (Forms) | 5 | 5 | 100% |
| P2 (Display) | 20 | 20 | 100% |
| P3 (Navigation) | 15 | 15 | 100% |
| P4 (Feedback) | 12 | 12 | 100% |
| P5 (Charts) | 11 | 11 | 100% |
| P6 (Layout) | 5 | 5 | 100% |
| **Total** | **78** | **78** | **100%** |

**Planned** is the total count of manifest entries. **Implemented** is entries with `status: 'stable'` or `status: 'beta'`. Excludes `status: 'deprecated'`.

### Documentation Coverage

**Definition:** The percentage of components that have complete documentation (docs path exists, description non-empty, props documented, usage examples present).

```
Documentation Coverage = (Components with Complete Docs / Total Stable + Beta Components) × 100
```

**Breakdown by status:**

| Status | Components | Documented | Coverage |
|--------|------------|------------|----------|
| Stable | 45 | 45 | 100% |
| Beta | 25 | 22 | 88% |
| Deprecated | 8 | 8 | 100% |

**Doc completeness criteria:**
- `docsPath` in manifest points to an existing file
- `description` is non-empty and ≥ 10 characters
- `designPurpose` is filled
- `businessUsage` is filled
- `variants` has at least 1 entry
- `states` has at least 2 entries (default + one other)

### Accessibility Compliance

**Definition:** The percentage of components passing automated axe-core audits with zero violations.

```
A11y Compliance = (Components with a11yStatus: 'pass' / Total Components) × 100
```

| Status | Count | Percentage |
|--------|-------|------------|
| ✅ Pass | 62 | 79% |
| ⚠️ Partial | 12 | 15% |
| ❌ Needs Review | 4 | 5% |

**Sources:**
- Automated: axe-core (CI gate, every PR)
- Manual: Quarterly a11y audit by Principal Accessibility Architect
- Standards: WCAG 2.2 AA

### Responsive Validation

**Definition:** The percentage of components verified at all four responsive breakpoints.

```
Responsive Validation = (Components with responsiveStatus: 'pass' / Total Components) × 100
```

| Status | Count | Percentage |
|--------|-------|------------|
| ✅ Pass | 58 | 74% |
| ⚠️ Partial | 15 | 19% |
| ❌ Not Tested | 5 | 6% |

### Review Progress

**Definition:** The distribution of components across review pipeline stages.

| Stage | Count | Percentage |
|-------|-------|------------|
| Preview (Stage 1–2) | 8 | 10% |
| In Review (Stage 3–5) | 12 | 15% |
| Approved (Stage 6) | 50 | 64% |
| Frozen (Stage 7) | 8 | 10% |

### Approval Percentage

**Definition:** The percentage of reviewed components that have been approved (not returned for changes).

```
Approval % = (Approved Reviews / Total Reviews) × 100
```

Calculated per sprint:

| Sprint | Reviewed | Approved | Returned | Approval % |
|--------|----------|----------|----------|------------|
| Sprint 20 Part 8 | 15 | 14 | 1 | 93% |
| Sprint 20 Parts 2–7 | 42 | 40 | 2 | 95% |
| Sprint 19 Parts 1A–1E | 28 | 26 | 2 | 93% |

## How Metrics Are Calculated

### Data Source

All metrics are derived from the `componentManifest.ts` file and `tokenManifest.ts` file. No separate data store is needed. The quality dashboard reads the manifests at runtime and computes all metrics client-side.

### Calculation Frequency

Metrics are computed **on page load** from the current manifest data. They reflect the state of the last committed manifest.

### Refresh

To update metrics:
1. Update `componentManifest.ts` with current data
2. Rebuild the application
3. Navigate to `/design-system/quality`

## Dashboard Display

The quality dashboard presents metrics using the following components:

- **Coverage rings** — circular progress indicators for each major category
- **Trend charts** — approval percentage over the last 5 sprints
- **Breakdown tables** — per-component status breakdown
- **Status badges** — color-coded status indicators
- **Warning banners** — for metrics below target thresholds

### Target Thresholds

| Metric | Target | Warning | Critical |
|--------|--------|---------|----------|
| Component Coverage | ≥ 95% | ≥ 80% | < 80% |
| Documentation Coverage | ≥ 90% | ≥ 70% | < 70% |
| A11y Compliance | ≥ 90% | ≥ 75% | < 75% |
| Responsive Validation | ≥ 85% | ≥ 70% | < 70% |
| Approval % | ≥ 90% | ≥ 75% | < 75% |

## Export

The quality dashboard supports exporting the current metrics as PDF for use in sprint reviews and release meetings. The export button generates a formatted report with all metrics, breakdown tables, and trend chart screenshots.
