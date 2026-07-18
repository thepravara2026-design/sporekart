# Bug Deduplication Report — Sprint C

## Overview

| Metric | Value |
|--------|-------|
| Total raw bug entries across all 4 registers | 32 |
| Unique unique defects after dedup | 19 |
| Duplicates removed | 13 |
| Eligible P2 Medium items for Sprint C | 9 |

## Deduplication Details

### Group 1: Build Crash Super-Bug

**Primary**: `BUG-S3-CRIT-001` — CSS-in-JS indexed property access broken in production build

| Duplicate | Reason | Disposition |
|-----------|--------|-------------|
| BUG-S3-CRIT-002 | All protected routes crash from same root cause | Merged into CRIT-001 |
| BUG-S3-P1-001 | Part 1 tracking duplicate | Removed |
| BUG-S3-MED-005 | 14 console errors per page — symptom, not cause | Merged into CRIT-001 |
| BUG-S3-P1-006 | Same console errors, Part 1 tracking | Merged into CRIT-001 |
| BUG-S3-LOW-007 | Missing nav links — secondary effect of crash | Merged into CRIT-001 |
| BUG-S3-MED-006 | Guest redirect hidden — secondary effect of crash | Merged into CRIT-001 |
| BUG-S3-P1-005 | Guest redirect — same root cause | Merged into CRIT-001 |

**Total deduplicated**: 8 items → 1

### Group 2: Role Switcher

**Primary**: `BUG-S3-HIGH-003` — Role switcher component absent

| Duplicate | Reason | Disposition |
|-----------|--------|-------------|
| BUG-S3-P1-002 | Part 1 tracking duplicate | Removed |

**Total deduplicated**: 2 items → 1

### Group 3: Standalone Items (No Duplicates)

| ID | Title |
|----|-------|
| BUG-S3-HIGH-004 | No ARIA landmarks |
| BUG-S3-MED-006 | Mobile nav empty |
| BUG-S3-P1-003 | Double upload buttons |
| BUG-S3-P1-004 | Save button disabled |
| BUG-S3-P1-007 | Notification badge |
| BUG-S3-HIGH-005 | Auth context infinite loop |
| BUG-S3-LOW-007 | LitVue title |
| BUG-S3-P3-001 | No observability |
| BUG-S3-P3-002 | No security headers |
| BUG-S3-P3-003 | No Service Worker |
| BUG-S3-P3-004 | Auth client stub |
| BUG-S3-P3-005 | Duplicate toasts |
| BUG-S3-P3-006 | No 404 page |
| BUG-S3-P3-007 | No audit timestamps |
| BUG-S3-P3-008 | No performance tooling |
| BUG-S3-P1-004 | Admin routes without auth |

## Deduplication Summary

| Original Count | Unique Count | Removed | Reason |
|---------------|-------------|---------|--------|
| 32            | 19          | 13      | Cross-part tracking duplicates + symptom/root-cause merges |
