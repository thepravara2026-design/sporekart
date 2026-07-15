# Offline Support

## Architecture

Browser-native online/offline event tracking with UI components for status indication, reconnection notices, and cached data placeholder. Session awareness integrates with the offline experience via timeout/expiry dialogs.

```
useOnlineStatus()
  ├─ status: 'online' | 'offline' | 'slow_connection'
  ├─ isOnline: boolean
  └─ isOffline: boolean

OfflineBanner (sticky warning bar with retry)
ReconnectNotice (success toast on reconnection)
```

## Usage

```tsx
function App() {
  const { status, isOnline, isOffline } = useOnlineStatus();
  const [showOffline, setShowOffline] = useState(false);

  return (
    <>
      <OfflineBanner show={isOffline || showOffline} onRetry={() => setShowOffline(false)} />
      <ReconnectNotice show={isOnline && wasOffline} />
      <MainContent />
    </>
  );
}
```

## Session Integration

```tsx
const session = useSession();

<SessionTimeoutWarning open={session.isWarning} onExtend={session.extendSession} onLogout={session.endSession} />
<SessionExpired onReauthenticate={handleReauth} savedPage={session.savedPage} />
```

## Cached Data Placeholder

Display cached data when offline:

```tsx
{isOffline && (
  <div>
    <h4>Cached Orders</h4>
    <p>Last synced: 5 min ago</p>
    <ul>{cachedOrders.map(order => <li key={order.id}>{order.label}</li>)}</ul>
  </div>
)}
```

## Extension Points

- `slow_connection` status for adaptive loading
- Background sync queue for offline mutations
- Service Worker integration for full offline support
