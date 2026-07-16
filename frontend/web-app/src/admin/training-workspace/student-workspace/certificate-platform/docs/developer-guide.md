# Certificate Platform — Developer Guide

## Adding a New Certificate Type

1. Add to `CertificateType` union in `types.ts`
2. Add entry to `CERTIFICATE_TYPE_LABELS`
3. Add to `CERT_TYPES` array in `mockData.ts` if it should appear in generated data

## Adding a New Achievement Type

1. Add to `AchievementType` union in `types.ts`
2. Add entry to `ACHIEVEMENT_TYPE_LABELS`
3. Add to `ACH_TYPES` array in `mockData.ts`

## Adding a New Badge Type

1. Add to `BadgeType` union in `types.ts`
2. Add entry to `BADGE_TYPE_LABELS`
3. Add to `BADGE_TYPES` array in `mockData.ts`

## Adding a New Certificate Status

1. Add to `CertificateStatus` union in `types.ts`
2. Add entries to `CERTIFICATE_STATUS_LABELS` and `CERTIFICATE_STATUS_VARIANTS`
3. Add to `CERT_STATUSES` array in `mockData.ts`
4. Add to `statusOrder` in `CertificateTimeline.tsx`
5. Add to `statusColors` in `CertificateTimeline.tsx`

## Adding a New Verification Method

1. Add to `VerificationRecord.verificationMethod` union in `types.ts`
2. Update `VerificationCard.tsx` display logic

## Connecting to a Real API

1. Replace mock data imports in `CertificateContext.tsx` with async fetch calls
2. Add loading/error states
3. Remove `mockData.ts` dependency
4. Update types if API contract differs

## Testing

- Run `npm run typecheck` for TypeScript validation
- Verify all 9 tabs render correctly
- Test search/filter/pagination on CertificateRegistryPage
- Test wallet student selection and tab switching
- Test transcript student selection
- Test verification center record selection and timeline
- Verify responsive layout at 320px, 768px, 1024px+
