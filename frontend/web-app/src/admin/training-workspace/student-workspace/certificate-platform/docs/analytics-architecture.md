# Certificate Platform — Analytics Architecture

## Overview

The Certificate Analytics module provides quantitative insights into credential issuance, verification, achievement distribution, and badge distribution across the platform.

## Analytics Dimensions (4)

### 1. Certificates by Course
Horizontal bar chart showing certificate count per course. Uses max-value normalization for proportional bar widths.

### 2. Certificates by Month
Vertical bar chart showing monthly issuance trends (Jan–Jun). Bar heights proportional to max monthly count.

### 3. Achievement Distribution
Horizontal bar chart showing count per achievement type (top 8 shown). Yellow color scheme.

### 4. Badge Distribution
Horizontal bar chart showing count per badge type (top 8 shown). Cyan color scheme.

## Summary Metrics

| Metric | Color | Description |
|--------|-------|-------------|
| Verification Rate | default | % of certificates verified |
| Completion Rate | green | % of certificates issued |
| Verification Requests | blue | Count of pending requests |
| Badges Issued | yellow | Total badges awarded |

## Data Sources

All analytics derived from mock data in `getAnalytics()`:
- certificatesByCourse: aggregated from Certificate[]
- certificatesByMonth: monthly distribution
- achievementDistribution: aggregated from Achievement[]
- badgeDistribution: aggregated from DigitalBadge[]
