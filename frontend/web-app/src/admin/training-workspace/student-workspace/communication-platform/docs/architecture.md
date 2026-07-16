# Communication Platform — Architecture

## Overview

The Enterprise Student Communication, Notification & Engagement Platform (ESCNEP - Sprint 27 Part 10) extends the Student Workspace with comprehensive notification management, announcement center, student inbox, communication timeline, notification templates, communication preferences, and engagement analytics. Operates entirely in Mock Mode.

## Directory Structure

```
communication-platform/
  types.ts                                    — Domain model (18 communication types, 10 statuses, 5 priorities, 9 announcement categories, 9 timeline stages)
  data/mockData.ts                            — 8 deterministic generators (messages, announcements, inbox, timeline, preferences, templates, analytics, dashboard)
  state/CommunicationContext.tsx               — Context with search/filter/pagination + read/star actions
  components/
    DashboardWidget.tsx                        — Reusable widget wrapper
    StatusBadge.tsx                            — 10-variant message status badge
    PriorityBadge.tsx                          — 5-variant priority badge
    TypeBadge.tsx                              — 18-variant communication type badge
    MessageCard.tsx                            — Full message display card
    InboxItemCard.tsx                          — Inbox card with read/star toggles
    AnnouncementCard.tsx                       — Announcement display card
    TimelineEvent.tsx                          — Timeline event with stage icon and connector
    PreferenceCard.tsx                         — Preference toggle card with channels and types
    TemplateCard.tsx                           — Template display card with variables
    MetricCard.tsx                             — Single stat display card
    EmptyStates.tsx                            — 6 typed empty states
    Skeletons.tsx                              — Dashboard and list skeletons
    SharedFilters.tsx                          — Search bar + type/priority/course/batch filters
  pages/
    CommunicationIndex.tsx                     — Wrapper with sub-navigation tabs + provider
    EngagementDashboard.tsx                    — Platform KPIs, recent messages/announcements, priority distribution
    NotificationCenter.tsx                     — All notifications with search/filter
    AnnouncementCenter.tsx                     — All announcements with category filter
    StudentInbox.tsx                           — Inbox with all/unread/starred tabs, read/star actions
    CommunicationTimeline.tsx                  — Student lifecycle timeline with stage filter
    NotificationTemplates.tsx                  — Template library with type filter
    CommunicationAnalytics.tsx                 — Distribution, trends, course/batch breakdown
    CommunicationPreferences.tsx               — Per-student channel and notification type toggles
  docs/                                        — 16 documentation files
```
