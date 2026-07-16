# Certificate Platform — State Management

## Architecture

Single React Context (`CertificateContext`) provides all data and filtering operations.

## State Shape

```typescript
interface CertificateState {
  certificates: Certificate[];
  badges: DigitalBadge[];
  achievements: Achievement[];
  wallets: CredentialWallet[];
  transcripts: AcademicTranscript[];
  verificationRecords: VerificationRecord[];
  analytics: CertificateAnalytics;
  dashboard: CertificateDashboard;
  searchTerm: string;
  statusFilter: CertificateStatus | 'all';
  typeFilter: CertificateType | 'all';
}
```

## Provided Values

| Value | Type | Purpose |
|-------|------|---------|
| All data arrays | `T[]` | Raw data for all 6 entity types + analytics + dashboard |
| searchTerm | `string` | Current search query |
| statusFilter | `CertificateStatus \| 'all'` | Current status filter |
| typeFilter | `CertificateType \| 'all'` | Current type filter |
| setSearchTerm | `(term) => void` | Update search term |
| setStatusFilter | `(status) => void` | Update status filter |
| setTypeFilter | `(type) => void` | Update type filter |
| getFilteredCertificates | `() => Certificate[]` | Computed filtered list |

## Performance

- Context value memoized with `useMemo`
- State setters wrapped with `useCallback`
- Filter computation memoized in consuming components
- Mock data generated once at module load
