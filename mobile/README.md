# SporeKart Mobile Platform

Enterprise-grade mobile applications for iOS and Android using React Native and Expo.

## Sprint 15 Objective

Create a unified mobile platform supporting:

- **Customer App** - Browse, search, order, track shipments
- **Grower App** - Training, certifications, field notes
- **Dealer App** - Orders, inventory, pricing, customer management
- **Admin Companion** - Dashboard, approvals, alerts

## Architecture

```
mobile/
├── shared-core/          # Reusable core libraries
├── customer-app/         # Customer mobile application
├── grower-app/           # Grower/Trainer mobile application
├── dealer-app/           # Dealer mobile application
└── admin-companion/      # Admin mobile companion
```

## Stack

- **Framework**: React Native with Expo
- **Language**: TypeScript/JavaScript
- **State Management**: Zustand + React Query
- **Offline**: SQLite + MMKV
- **Auth**: OTP, JWT, Biometric
- **Notifications**: Firebase Cloud Messaging (FCM)
- **Storage**: Encrypted local storage
- **API**: REST with RFC 9457 error handling

## Core Features

### Authentication
- OTP login with JWT tokens
- Refresh token management
- Biometric authentication (fingerprint, face)
- Device registration and trusted devices
- Session management with offline login cache

### Offline-First
- SQLite for local data persistence
- MMKV for key-value storage
- Offline queue for requests
- Automatic background sync
- Conflict resolution strategies
- Manual and auto sync triggers

### Notifications
- Push notifications via Firebase Cloud Messaging
- Topic-based subscriptions
- Background notification handling
- Notification history and delivery tracking

### File Management
- Camera and gallery integration
- Image compression
- PDF viewing
- Document upload with offline queuing
- Certificate downloads

### Device Features
- QR and barcode scanning
- GPS location tracking
- Deep linking
- Share API integration
- Clipboard access
- Biometric authentication

## Mobile APIs

### Device Management
- `POST /mobile/register-device` - Register mobile device
- `GET /mobile/config` - Get mobile configuration
- `GET /mobile/version` - Get app version info

### Offline Sync
- `POST /mobile/sync` - Sync offline changes
- `POST /mobile/offline-sync` - Initiate offline sync
- `GET /mobile/sync-status` - Get sync status

### Notifications
- `POST /mobile/push-token` - Register push token
- `GET /mobile/notifications` - Get notification history

### Settings
- `GET /mobile/settings` - Get user mobile settings

## Database

### Mobile-Specific Tables
- `mobile_devices` - Device registration
- `device_sessions` - Session management
- `offline_sync_logs` - Sync history
- `push_notification_tokens` - FCM token storage
- `mobile_preferences` - User preferences

## Security

- Encrypted local storage for sensitive data
- Secure token storage in device keychain
- Certificate pinning for API connections
- Root detection and tamper detection
- Biometric authentication hooks
- Audit logging for sensitive operations

## Offline Sync Strategy

1. **Local Storage**: SQLite for structured data, MMKV for KV pairs
2. **Sync Queue**: Failed requests queued for retry
3. **Conflict Resolution**: Last-write-wins with versioning
4. **Background Sync**: Automatic retry with exponential backoff
5. **Manual Sync**: User-triggered full sync

## Push Notification Flow

1. App registers device with backend
2. Backend stores FCM token
3. Backend publishes notifications to Firebase
4. Firebase delivers to device
5. App processes foreground/background notification
6. App publishes `PushNotificationOpened` event
7. Backend tracks delivery and opens

## Key Workflows

### Customer Order Flow
1. Browse products (offline capable)
2. Add to cart (local SQLite)
3. Checkout (online required)
4. Order confirmation
5. Real-time tracking updates
6. Delivery confirmation

### Grower Training Flow
1. Browse available courses
2. Enroll in course
3. View offline learning content
4. Mark attendance
5. Complete course
6. Download certificate
7. Request AI assistance

### Dealer Inventory Flow
1. View inventory levels (synced)
2. Create bulk orders
3. Request quotations
4. Manage customer relationships
5. View sales dashboard
6. Offline order queuing

### Admin Approvals Flow
1. Dashboard with pending approvals
2. Vendor registration approvals
3. Bulk order approvals
4. Credit limit increases
5. System alerts and notifications

## Testing

- Unit tests for all business logic
- Integration tests for API communication
- Offline sync tests
- Push notification tests
- Device integration tests
- Accessibility tests
- Performance benchmarks

Target: >90% business logic coverage

## Documentation

- [Mobile Architecture Guide](./docs/ARCHITECTURE.md)
- [Offline Sync Implementation](./docs/OFFLINE_SYNC.md)
- [Push Notification Setup](./docs/PUSH_NOTIFICATIONS.md)
- [Security Guide](./docs/SECURITY.md)
- [API Documentation](./docs/API.md)
- [Deployment Guide](./docs/DEPLOYMENT.md)

## Getting Started

### Prerequisites
- Node.js 18+
- Expo CLI
- React Native dev environment
- Firebase project setup
- Backend API running

### Installation

```bash
# Navigate to shared-core
cd mobile/shared-core
npm install

# Navigate to app (e.g., customer-app)
cd ../customer-app
npm install
npx expo start
```

### Running on Device

```bash
# Scan QR code with Expo Go app
npm run start

# Or build for iOS
npm run ios

# Or build for Android
npm run android
```

## Environment Configuration

Create `.env` files in each app:

```env
API_BASE_URL=https://api.sporekart.com
FIREBASE_PROJECT_ID=sporekart-prod
FIREBASE_MESSAGING_SENDER_ID=xxxxx
FIREBASE_API_KEY=xxxxx
BIOMETRIC_ENABLED=true
OFFLINE_SYNC_INTERVAL=300000
```

## Features by App

### Customer App
- Product browsing and search
- Wishlist management
- Shopping cart with offline support
- Order management
- Order tracking
- Training registration
- Profile and address management
- Notifications
- AI assistant chat

### Grower App
- Training dashboard
- Course enrollment and progress
- Attendance marking
- Certificate management
- Knowledge base
- AI assistant integration
- Field notes with photos
- Offline learning content
- Image upload queue

### Dealer App
- Order management
- Inventory tracking
- Bulk order creation
- Quotation requests
- Customer relationship management
- Sales dashboard
- Notifications
- Offline order queuing

### Admin Companion
- Dashboard with KPIs
- Pending approvals
- Order management
- Inventory alerts
- Support tickets
- Quick actions
- Notifications

## Performance Targets

- App startup: <3 seconds
- Screen transition: <300ms
- API response: <2 seconds (with retry)
- Offline sync: <30 seconds
- Local search: <100ms

## Known Limitations (Phase 0)

- In-memory push notification queue (upgrade to persistent in Phase 1)
- Simplified conflict resolution (last-write-wins only)
- Local-only biometric validation (integrate with backend in Phase 1)
- Mock AI assistant (integrate with actual LLM in Phase 1)
- No real-time collaboration (add in Phase 2)

## Future Enhancements (Phase 1+)

- Real-time updates via WebSocket
- Advanced conflict resolution
- Predictive caching
- Video streaming
- AR product visualization
- ML-based recommendations
- IoT device integration

## Contributing

Follow the [Contributing Guide](../CONTRIBUTING.md).

## License

Proprietary - SporeKart

---

**Sprint 15 Status**: Foundation structure created. Core services, offline sync, and push notifications scaffolded. Backend APIs in development.
