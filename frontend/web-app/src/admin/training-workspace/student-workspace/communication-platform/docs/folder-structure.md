# Communication Platform — Folder Structure

```
communication-platform/
├── types.ts                                    # Domain model types, labels & constants
├── data/
│   └── mockData.ts                             # 8 deterministic generators
├── state/
│   └── CommunicationContext.tsx                # Context + provider + hook
├── components/
│   ├── DashboardWidget.tsx                     # Reusable widget wrapper
│   ├── StatusBadge.tsx                         # Message status badge (10 variants)
│   ├── PriorityBadge.tsx                       # Priority badge (5 variants)
│   ├── TypeBadge.tsx                           # Type badge (18 variants)
│   ├── MessageCard.tsx                         # Full message card
│   ├── InboxItemCard.tsx                       # Inbox item with read/star
│   ├── AnnouncementCard.tsx                    # Announcement card
│   ├── TimelineEvent.tsx                       # Timeline event with connector
│   ├── PreferenceCard.tsx                      # Preference toggles
│   ├── TemplateCard.tsx                        # Template display
│   ├── MetricCard.tsx                          # Stat metric card
│   ├── EmptyStates.tsx                         # 6 empty states
│   ├── Skeletons.tsx                           # 2 skeleton variants
│   └── SharedFilters.tsx                       # Global filter controls
├── pages/
│   ├── CommunicationIndex.tsx                  # Nav wrapper with CommunicationProvider
│   ├── EngagementDashboard.tsx                 # Platform KPIs & overview
│   ├── NotificationCenter.tsx                  # All notifications
│   ├── AnnouncementCenter.tsx                  # All announcements
│   ├── StudentInbox.tsx                        # Student message inbox
│   ├── CommunicationTimeline.tsx               # Student lifecycle
│   ├── NotificationTemplates.tsx               # Template library
│   ├── CommunicationAnalytics.tsx              # Metrics & trends
│   └── CommunicationPreferences.tsx            # Student preferences
└── docs/
    ├── architecture.md
    ├── notification-lifecycle.md
    ├── inbox-architecture.md
    ├── announcement-architecture.md
    ├── template-architecture.md
    ├── analytics-architecture.md
    ├── folder-structure.md
    ├── component-inventory.md
    ├── state-management.md
    ├── responsive.md
    ├── accessibility.md
    ├── performance.md
    ├── future-integration-readiness.md
    ├── developer-guide.md
    ├── communication-preference-model.md
    └── sprint27-part10-completion.md
```
