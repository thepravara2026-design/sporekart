import { useState, useCallback } from 'react';
import { OfflineBanner, ReconnectNotice, useOnlineStatus } from '../../offline';
import { SessionTimeoutWarning, SessionExpired } from '../../session';
import { OperationalStateDisplay } from '../../operational-states';

export function OfflinePreview() {
  const { status, isOnline } = useOnlineStatus();
  const [simulateOffline, setSimulateOffline] = useState(false);
  const [showReconnect, setShowReconnect] = useState(false);
  const [sessionWarning, setSessionWarning] = useState(false);
  const [sessionExpired, setSessionExpired] = useState(false);
  const [savedPage] = useState('/admin/orders/1234');

  const handleRetry = useCallback(() => {
    setSimulateOffline(false);
    setShowReconnect(true);
    setTimeout(() => setShowReconnect(false), 3000);
  }, []);

  const handleExtend = useCallback(() => {
    setSessionWarning(false);
  }, []);

  const handleSessionEnd = useCallback(() => {
    setSessionWarning(false);
    setSessionExpired(true);
  }, []);

  return (
    <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0 }}>Offline Experience & Session Awareness</h2>

      {/* Offline Simulation */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Offline Simulation</h3>
        <div style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
          <button
            onClick={() => setSimulateOffline(!simulateOffline)}
            style={{
              padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)',
              background: simulateOffline ? 'var(--color-error)' : 'var(--color-surface)',
              color: simulateOffline ? '#fff' : 'var(--color-text-primary)',
              cursor: 'pointer', fontSize: 'var(--text-body)',
            }}
          >
            {simulateOffline ? 'Restore connection' : 'Simulate offline'}
          </button>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'flex', alignItems: 'center' }}>
            Browser status: <strong style={{ marginLeft: 4, color: isOnline ? 'var(--color-success)' : 'var(--color-error)' }}>{status}</strong>
          </span>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
          <OfflineBanner show={simulateOffline} onRetry={handleRetry} />
          <ReconnectNotice show={showReconnect} />
        </div>
      </section>

      {/* Offline State Examples */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Offline & Connection States</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 12 }}>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
            <OperationalStateDisplay state="offline" />
          </div>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
            <OperationalStateDisplay
              state="offline"
              config={{
                action: { label: 'Retry connection', onClick: handleRetry },
                secondaryAction: { label: 'View cached data', onClick: () => alert('Cached data') },
              }}
            />
          </div>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
            <OperationalStateDisplay state="server_unavailable" />
          </div>
        </div>
      </section>

      {/* Session Timers */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Session Awareness</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
          <div style={{ display: 'flex', gap: 8 }}>
            <button
              onClick={() => setSessionWarning(true)}
              style={{ padding: '8px 16px', border: '1px solid var(--color-warning)', borderRadius: 'var(--radius-md)', background: 'transparent', cursor: 'pointer', color: 'var(--color-warning)', fontSize: 'var(--text-body)' }}
            >
              Show timeout warning
            </button>
            <button
              onClick={() => setSessionExpired(true)}
              style={{ padding: '8px 16px', border: '1px solid var(--color-error)', borderRadius: 'var(--radius-md)', background: 'transparent', cursor: 'pointer', color: 'var(--color-error)', fontSize: 'var(--text-body)' }}
            >
              Show session expired
            </button>
          </div>

          <div style={{ display: 'flex', gap: 16, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            <span>Session state: <strong>{sessionExpired ? 'expired' : sessionWarning ? 'timeout_warning' : 'active'}</strong></span>
            <span>Saved page: <strong>{savedPage}</strong></span>
          </div>

          <SessionTimeoutWarning
            open={sessionWarning}
            onExtend={handleExtend}
            onLogout={handleSessionEnd}
            warningDuration={30}
          />

          {sessionExpired && (
            <SessionExpired
              onReauthenticate={() => { setSessionExpired(false); setSessionWarning(false); }}
              savedPage={savedPage}
            />
          )}
        </div>
      </section>

      {/* Cached Data Placeholder */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Offline Actions & Cached Data</h3>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          <div style={{ flex: 1, minWidth: 200, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
            <h4 style={{ margin: '0 0 8px', fontSize: 'var(--text-body)' }}>Cached Orders</h4>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 8 }}>
              Last synced: 5 min ago
            </div>
            <div style={{ padding: '8px 12px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-caption)' }}>
              {[1, 2, 3].map((i) => (
                <div key={i} style={{ padding: '4px 0', borderBottom: '1px solid var(--color-border)', display: 'flex', justifyContent: 'space-between' }}>
                  <span>Order #{1230 + i}</span>
                  <span style={{ color: 'var(--color-text-tertiary)' }}>${(99.99 * i).toFixed(2)}</span>
                </div>
              ))}
            </div>
            <div style={{ marginTop: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
              📦 Available offline
            </div>
          </div>
          <div style={{ flex: 1, minWidth: 200, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
            <h4 style={{ margin: '0 0 8px', fontSize: 'var(--text-body)' }}>Offline Actions</h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              {[
                { label: 'View cached products', icon: 'package' },
                { label: 'Review order history', icon: 'shopping-cart' },
                { label: 'Check customer list', icon: 'users' },
              ].map((action) => (
                <button
                  key={action.label}
                  onClick={() => alert(action.label)}
                  style={{
                    padding: '8px 12px', border: 'none', borderRadius: 'var(--radius-sm)',
                    background: 'var(--color-surface-hover)', cursor: 'pointer',
                    color: 'var(--color-text-primary)', textAlign: 'left', fontSize: 'var(--text-caption)',
                    display: 'flex', alignItems: 'center', gap: 8,
                  }}
                >
                  {action.label}
                </button>
              ))}
            </div>
            <div style={{ marginTop: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
              ⚡ Queued for sync when online
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
