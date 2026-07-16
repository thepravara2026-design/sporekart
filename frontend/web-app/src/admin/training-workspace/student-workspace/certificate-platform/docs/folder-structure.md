# Certificate Platform — Folder Structure

```
certificate-platform/
├── types.ts                                    # Domain model types & labels
├── data/
│   └── mockData.ts                             # 8 deterministic generators
├── state/
│   └── CertificateContext.tsx                  # Context + provider + hook
├── components/
│   ├── CertificateStatusBadge.tsx              # Status badge (10 variants)
│   ├── CertificateCard.tsx                     # Certificate card
│   ├── CertificateTable.tsx                    # Certificate registry table
│   ├── BadgeCard.tsx                           # Digital badge card
│   ├── AchievementCard.tsx                     # Achievement card
│   ├── DashboardWidget.tsx                     # Stat widget
│   ├── EmptyStates.tsx                         # 6 empty states
│   ├── Skeletons.tsx                           # 3 skeleton variants
│   ├── WalletSummary.tsx                       # Wallet summary card
│   ├── TranscriptTable.tsx                     # Transcript course records table
│   ├── TranscriptSummary.tsx                   # Transcript overview
│   ├── VerificationCard.tsx                    # Verification record card
│   ├── CertificateTimeline.tsx                 # 7-stage lifecycle timeline
│   ├── CredentialSharePanel.tsx                # 8-item share panel
│   └── AnalyticsPanel.tsx                      # 4-dimension analytics
├── pages/
│   ├── CertificateIndex.tsx                    # Nav wrapper
│   ├── CertificateDashboardPage.tsx             # Executive dashboard
│   ├── CertificateRegistryPage.tsx              # Certificate registry
│   ├── CredentialWalletPage.tsx                 # Student credential wallet
│   ├── AchievementCenterPage.tsx                # Achievement management
│   ├── DigitalBadgesPage.tsx                    # Digital badge collection
│   ├── AcademicTranscriptPage.tsx               # Academic transcript
│   ├── VerificationCenterPage.tsx               # Verification center
│   └── CertificateAnalyticsPage.tsx             # Certificate analytics
└── docs/
    ├── architecture.md
    ├── credential-lifecycle.md
    ├── wallet-architecture.md
    ├── achievement-architecture.md
    ├── transcript-model.md
    ├── verification-model.md
    ├── analytics-architecture.md
    ├── folder-structure.md
    ├── component-inventory.md
    ├── state-management.md
    ├── responsive.md
    ├── accessibility.md
    ├── performance.md
    ├── future-integration-readiness.md
    ├── developer-guide.md
    └── sprint27-part8-completion.md
```
