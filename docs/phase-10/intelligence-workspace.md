# Intelligence Workspace

## Overview
The Intelligence Workspace provides a 14-section tabbed interface for exploring all inventory analytics data. Navigation is handled via section tabs at the top of the workspace, with each section rendering its corresponding page component.

## Section Navigation
- Tab bar with all 14 sections displayed horizontally
- Active tab highlighted with primary color background and bottom border
- Scrollable horizontally for overflow
- Managed by `IntelligenceWorkspaceContext`

## Section Pages
| Section | Description |
|---------|-------------|
| Overview | Executive KPIs + Inventory Health summary |
| Executive KPIs | 8 KPI metric cards + bar/line charts |
| Inventory Health | 6 health scores + expiry risk breakdown |
| Warehouse Health | Warehouse metrics + utilization chart |
| Stock Health | Stock status distribution + health chart |
| Batch Health | Batch quality analytics + distribution |
| Movement Analytics | Movement volume + by-warehouse chart |
| Receiving Analytics | Receiving metrics + volume chart |
| Forecasting | 12-month demand forecast + trend chart |
| Insights | Actionable recommendations list |
| Reports | Warehouse + stock reports with charts |
| Alerts | System alerts with severity indicators |
| Settings | Intelligence configuration |
| Help | Usage documentation |

## State Management
- `IntelligenceWorkspaceContext` provides active section and search query state
- Section changes update the dashboard router component
- Individual hooks manage data fetching, filtering, search, responsive state, and pagination
