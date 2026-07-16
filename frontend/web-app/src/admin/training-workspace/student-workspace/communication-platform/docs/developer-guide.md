# Communication Platform — Developer Guide

## Setup

No additional setup required. All state is mock-based. To integrate with real backends:

1. Replace `data/mockData.ts` generators with API calls in CommunicationContext
2. Each getter function (`getMessages`, `getAnnouncements`, etc.) should call the corresponding API endpoint
3. Update `getFilteredMessages`, `getFilteredAnnouncements`, `getFilteredInbox` to use server-side filtering

## Adding a New Communication Type

1. Add the type string literal to `CommunicationType` in types.ts
2. Add label to `COMMUNICATION_TYPE_LABELS`
3. Add color to `TypeBadge.tsx` COLORS map
4. Add template in `generateTemplates()` in mockData.ts (optional)
5. Type will automatically appear in filter dropdowns

## Adding a New Channel

1. Add boolean field to `CommunicationPreference` in types.ts
2. Add channel entry to `PreferenceCard.tsx` channels array
3. Toggle will automatically function
4. Future: wire to actual delivery provider

## Architecture Rules

- All communication originates from this platform (no scattered notification logic)
- Templates are the single source of truth for message formatting
- Preferences control all channel routing
- Analytics are computed from the message store
- NO direct backend/database/API calls in mock mode
