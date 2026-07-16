# Certificate Platform — Component Inventory

## Components (15)

| Component | Type | Inputs | Purpose |
|-----------|------|--------|---------|
| CertificateStatusBadge | Display | status | 10-variant color-coded status badge |
| CertificateCard | Display | certificate, onSelect?, selected? | Certificate display card |
| CertificateTable | Display | certificates, onSelect?, selectedId? | 6-column registry table |
| BadgeCard | Display | badge | Digital badge card with NFT indicator |
| AchievementCard | Display | achievement | Achievement card with points |
| DashboardWidget | Display | label, value, variant?, subtitle? | Reusable stat widget |
| EmptyStates | Display | type, onClearFilters? | 6 typed empty states |
| Skeletons | Display | (none) | Table, dashboard, wallet skeletons |
| WalletSummary | Display | wallet | Wallet summary with credential counts |
| TranscriptTable | Display | transcript | 7-column course records table |
| TranscriptSummary | Display | transcript | GPA, credits, course summary |
| VerificationCard | Display | record | Verification record card |
| CertificateTimeline | Display | certificate | 7-stage lifecycle timeline |
| CredentialSharePanel | Display | (none) | 8-method share placeholder panel |
| AnalyticsPanel | Display | analytics | 4-dimension analytics visualizations |

## Pages (9)

| Page | Section | Purpose |
|------|---------|---------|
| CertificateIndex | (wrapper) | Tab-based navigation |
| CertificateDashboardPage | certificates | Executive dashboard |
| CertificateRegistryPage | certificates/registry | Full registry with search/filter |
| CredentialWalletPage | certificates/wallet | Student credential wallet |
| AchievementCenterPage | certificates/achievements | Achievement management |
| DigitalBadgesPage | certificates/badges | Digital badge collection |
| AcademicTranscriptPage | certificates/transcript | Academic transcript |
| VerificationCenterPage | certificates/verification | Verification center |
| CertificateAnalyticsPage | certificates/analytics | Certificate analytics |
