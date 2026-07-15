# Batch Lifecycle Framework

## Overview
The Batch Lifecycle Engine tracks each batch through 12 discrete states from creation through disposal. The lifecycle is visualized as a vertical timeline with status indicators.

## Lifecycle States (12 states)
| # | State | Variant | Description | Type |
|---|-------|---------|-------------|------|
| 1 | Created | info | Batch record created | Active |
| 2 | Quality Review | warning | Under quality review | Active |
| 3 | Approved | success | Batch approved for operations | Active |
| 4 | Operational | success | Batch active and operational | Active |
| 5 | Near Expiry | warning | Batch approaching expiry | Warning |
| 6 | Expired | danger | Batch has expired | Terminal |
| 7 | Returned | warning | Batch returned from customer | Terminal |
| 8 | Archived | neutral | Batch archived | Terminal |
| 9 | Rejected | danger | Batch rejected | Terminal |
| 10 | Blocked | danger | Batch blocked from operations | Hold |
| 11 | Recalled | danger | Batch recalled | Terminal |
| 12 | Disposed | neutral | Batch disposed | Terminal |

## State Transitions
```
Created → Quality Review → Approved → Operational
                                              ↓
                                       Near Expiry → Expired → Archived/Disposed
                                              ↓
                                          Returned
                                            
Rejected ← Quality Review
Blocked ← Operational
Recalled ← Operational
```

## Visual Lifecycle Timeline
The `BatchLifecycleTimeline` component renders all 12 states vertically:
- **Completed states**: Filled dot + colored connecting line
- **Current state**: Filled dot + "CURRENT" badge
- **Future states**: Empty dot + gray connecting line
- Each state shows its label and description

## Lifecycle Variant Colors
- success: Operational, Approved
- warning: Quality Review, Near Expiry, Returned
- danger: Expired, Rejected, Blocked, Recalled
- info: Created
- neutral: Archived, Disposed

## Future Extension Points
- Recall workflow (from any active state)
- Disposal workflow (from expired, recalled, rejected)
- Automatic state transitions (near expiry → expired)
- Batch hold/unhold workflows
- Batch merge/split operations
