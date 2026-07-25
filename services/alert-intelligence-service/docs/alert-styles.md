# Alert Styles

## Design System Tokens

### Severity Colors
| Token | Value | Usage |
|---|---|---|
| --severity-critical | #dc2626 | CRITICAL badge, border |
| --severity-high | #ea580c | HIGH badge, border |
| --severity-medium | #ca8a04 | MEDIUM badge, border |
| --severity-low | #2563eb | LOW badge, border |
| --severity-info | #6b7280 | INFO badge, border |

### Priority Colors
| Token | Value | Usage |
|---|---|---|
| --priority-p0 | #991b1b | P0 badge |
| --priority-p1 | #c2410c | P1 badge |
| --priority-p2 | #a16207 | P2 badge |
| --priority-p3 | #1d4ed8 | P3 badge |
| --priority-p4 | #4b5563 | P4 badge |

### Status Colors
| Token | Value | Usage |
|---|---|---|
| --status-open | #2563eb | OPEN badge |
| --status-acknowledged | #ca8a04 | ACKNOWLEDGED badge |
| --status-resolved | #16a34a | RESOLVED badge |
| --status-closed | #6b7280 | CLOSED badge |
| --status-escalated | #dc2626 | ESCALATED badge |

### Metric Card Colors
| Token | Value |
|---|---|
| --metric-total | #1e40af |
| --metric-critical | #991b1b |
| --metric-high | #c2410c |
| --metric-open | #1d4ed8 |

### Layout
- Alert table: full-width, scrollable, sticky header
- Metric cards: 4-column grid, min-width 200px
- Risk score bars: inline progress bar, height 8px, rounded
- Risk table: full-width with min 120px columns
- Timeline: max-width 800px, centered, vertical line

### Component States
- Hover: `rgba(0,0,0,0.05)` background
- Active: `rgba(0,0,0,0.1)` background
- Transition: `all 0.15s ease`
