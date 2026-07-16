# Certificate Platform — Architecture

## Overview

The Enterprise Certificate, Digital Credential & Academic Achievement Management Platform (ECDCAP - Sprint 27 Part 8) extends the Student Workspace with comprehensive certificate lifecycle management, digital credential wallet, achievement tracking, academic transcripts, verification services, and credential analytics. Operates entirely in Mock Mode.

## Directory Structure

```
certificate-platform/
  types.ts                                — Domain model (15 cert types, 10 statuses, 12 achievement types, 10 badge types)
  data/mockData.ts                        — 25 students × 1-3 certs each (~200+ records across 8 generators)
  state/CertificateContext.tsx             — Context with search/filter/pagination
  components/
    CertificateStatusBadge.tsx             — Color-coded status badge (10 variants)
    CertificateCard.tsx                    — Certificate display card
    CertificateTable.tsx                   — 6-column certificate registry table
    BadgeCard.tsx                          — Digital badge card with NFT support
    AchievementCard.tsx                    — Achievement card with points
    DashboardWidget.tsx                    — Reusable stat widget (5 variants)
    EmptyStates.tsx                        — 6 typed empty states
    Skeletons.tsx                          — Table, dashboard, wallet skeletons
    WalletSummary.tsx                      — Student credential wallet summary
    TranscriptTable.tsx                    — 7-column academic transcript table
    TranscriptSummary.tsx                  — Transcript overview with GPA/credits
    VerificationCard.tsx                   — Verification record card
    CertificateTimeline.tsx                — Certificate lifecycle timeline (7 stages)
    CredentialSharePanel.tsx               — Share credential placeholder panel
    AnalyticsPanel.tsx                     — 4-section analytics (by course/month/achievement/badge)
  pages/
    CertificateIndex.tsx                   — Wrapper with sub-navigation tabs
    CertificateDashboardPage.tsx            — Executive dashboard (9 widgets + recent items)
    CertificateRegistryPage.tsx             — Full certificate registry with search/filter/pagination
    CredentialWalletPage.tsx                — Dual-pane wallet with student selector + tabs
    AchievementCenterPage.tsx               — Achievement grid with type filter
    DigitalBadgesPage.tsx                   — Badge grid with type filter + NFT indicators
    AcademicTranscriptPage.tsx              — Student selector + transcript summary + course records
    VerificationCenterPage.tsx              — Verification records + certificate lifecycle + share panel
    CertificateAnalyticsPage.tsx            — 4-dimension analytics (course, month, achievement, badge)
  docs/                                    — 16 documentation files
```
