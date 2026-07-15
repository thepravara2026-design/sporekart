# Quality Status Framework

## Overview
The Quality Status Framework tracks the quality inspection lifecycle of each batch independently of its operational lifecycle.

## Quality Statuses (8 statuses)
| Status | Priority | Variant | Description |
|--------|----------|---------|-------------|
| Pending Inspection | 1 | warning | Awaiting quality inspection |
| Under Review | 2 | info | Quality review in progress |
| Approved | 3 | success | Quality approved |
| Rejected | 4 | danger | Quality rejected |
| Blocked | 5 | danger | Quality blocked for compliance |
| Quarantined | 6 | danger | Quarantined for investigation |
| Returned | 7 | warning | Returned from customer |
| Disposed | 8 | neutral | Disposed after quality failure |

## Quality Status Badge
- Renders a colored badge using the variant system (success/warning/danger/info/neutral)
- Follows WCAG 2.2 AA contrast requirements via CSS variables
- Dot indicator with status label

## Quality Summary
The dashboard displays a quality summary grid showing count per quality status:
```
Pending Inspection: 6
Under Review: 3
Approved: 32
Rejected: 4
Blocked: 1
Quarantined: 1
```

## Future Extension Points
- Lab testing results attachment
- Quality score (numeric 0–100)
- Quality certificate links
- Inspection scheduling
- Automated quality rules
- Supplier quality ratings
