# Stock Workspace Layout & Navigation

## Layout Structure

```
┌──────────────────────────────────────────────────┐
│ Header (logo, search, user, notifications)        │
├──────────┬───────────────────────────────────────┤
│ Sidebar  │   Main Content Area                   │
│ (15 sec- │   (active section renders here)       │
│  tions)  │                                       │
│          │   Section-specific pages:             │
│          │   - Stock Dashboard (default)         │
│          │   - Stock Registry                    │
│          │   - Available Stock                   │
│          │   - Reserved Stock                    │
│          │   - Incoming Stock                    │
│          │   - Allocated Stock                   │
│          │   - Damaged Stock                     │
│          │   - Expired Stock                     │
│          │   - Blocked Stock                     │
│          │   - Stock Health                      │
│          │   - Timeline                          │
│          │   - Validation                        │
│          │   - Reports                           │
│          │   - Settings                          │
│          │   - Help                              │
└──────────┴───────────────────────────────────────┘
│ Footer (version, links, status)                   │
└──────────────────────────────────────────────────┘
```

## Sidebar Sections (15)
| # | Section | Icon | Route ID | Permissions |
|---|---------|------|----------|-------------|
| 1 | Stock Dashboard | bar-chart | dashboard | viewer+ |
| 2 | Stock Registry | database | registry | viewer+ |
| 3 | Available Stock | check-circle | available | viewer+ |
| 4 | Reserved Stock | clock | reserved | viewer+ |
| 5 | Incoming Stock | arrow-down | incoming | viewer+ |
| 6 | Allocated Stock | target | allocated | viewer+ |
| 7 | Damaged Stock | alert-triangle | damaged | viewer+ |
| 8 | Expired Stock | x-circle | expired | viewer+ |
| 9 | Blocked Stock | shield-off | blocked | viewer+ |
| 10 | Stock Health | heartbeat | health | viewer+ |
| 11 | Timeline | activity | timeline | viewer+ |
| 12 | Validation | check-square | validation | inventory_operator+ |
| 13 | Reports | file-text | reports | inventory_operator+ |
| 14 | Settings | settings | settings | inventory_manager+ |
| 15 | Help | help-circle | help | viewer+ |

## Responsive Behavior
- **Desktop (1025–1920px)**: Sidebar always visible, collapsible
- **Tablet (601–1024px)**: Sidebar collapses to icon-only, expandable
- **Mobile (320–600px)**: Sidebar hidden behind hamburger, full-screen overlay
