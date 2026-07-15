import { useState } from 'react';
import { PermissionProvider, PermissionGate, PermissionGateAll, usePermissions } from '../../permissions';
import { FeatureFlagProvider, FeatureGate, useFeatureFlag } from '../../feature-flags';
import { GlobalErrorBoundary, ComponentErrorBoundary } from '../../error-boundaries';
import { SystemStatusPanel, useSystemStatus } from '../../system-status';
import { useOnlineStatus, OfflineBanner, ReconnectNotice } from '../../offline';
import { useSession, SessionTimeoutWarning, SessionExpired } from '../../session';
import { AuditInfo, AuditTimeline, VersionHistory } from '../../audit';
import { OperationalStateDisplay } from '../../operational-states';
import type { AuditEntry, AuditMeta } from '../../audit/types';

const demoMeta: AuditMeta = {
  createdBy: 'John Doe',
  createdAt: '2026-07-10 09:15 AM',
  updatedBy: 'Jane Smith',
  updatedAt: '2026-07-14 11:30 AM',
  version: 3,
};

const demoAuditEntries: AuditEntry[] = [
  { id: 'a1', action: 'Order #1234 created', performedBy: 'John Doe', timestamp: '2 hours ago', details: 'Total: $249.99', resource: 'orders' },
  { id: 'a2', action: 'Product "Wireless Mouse" updated', performedBy: 'Jane Smith', timestamp: '5 hours ago', details: 'Price changed from $29.99 to $24.99', resource: 'products' },
  { id: 'a3', action: 'Customer account merged', performedBy: 'Admin', timestamp: '1 day ago', details: 'Merged duplicate accounts', resource: 'customers' },
  { id: 'a4', action: 'Inventory adjustment', performedBy: 'Mike Lee', timestamp: '2 days ago', details: 'Added 50 units', resource: 'inventory' },
  { id: 'a5', action: 'Bulk export completed', performedBy: 'Sarah Wilson', timestamp: '3 days ago', details: '250 orders exported to CSV', resource: 'orders' },
];

const demoVersions = [
  { version: 3, label: 'Updated pricing and description', timestamp: '2026-07-14 11:30 AM', author: 'Jane Smith', current: true },
  { version: 2, label: 'Changed category to Electronics', timestamp: '2026-07-12 02:15 PM', author: 'John Doe' },
  { version: 1, label: 'Initial product listing', timestamp: '2026-07-10 09:15 AM', author: 'John Doe' },
];

function PermissionsDemoSection() {
  const { can, role, setRole } = usePermissions();
  const { isEnabled } = useFeatureFlag();
  const [showOffline, setShowOffline] = useState(false);
  const { status } = useOnlineStatus();
  const session = useSession({ timeoutDuration: 300000, warningDuration: 120000 });
  const { services } = useSystemStatus();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 32 }}>
      {/* Role Selector */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Permission Role Switcher</h3>
        <select
          value={role}
          onChange={(e) => setRole(e.target.value)}
          style={{ padding: '8px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}
        >
          {['super_admin', 'administrator', 'manager', 'inventory_manager', 'viewer'].map((r) => (
            <option key={r} value={r}>{r.replace('_', ' ').replace(/\b\w/g, (c) => c.toUpperCase())}</option>
          ))}
        </select>
      </section>

      {/* Permission Gates */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Permission Gates</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
          <div><strong>Role:</strong> {role}</div>
          <div><strong>can('view', 'orders'):</strong> {can('view', 'orders') ? '✅' : '❌'}</div>
          <div><strong>can('create', 'orders'):</strong> {can('create', 'orders') ? '✅' : '❌'}</div>
          <div><strong>can('delete', 'orders'):</strong> {can('delete', 'orders') ? '✅' : '❌'}</div>
          <div><strong>can('approve', 'orders'):</strong> {can('approve', 'orders') ? '✅' : '❌'}</div>
          <div style={{ marginTop: 8, display: 'flex', gap: 8, flexWrap: 'wrap' }}>
            <PermissionGate action="create" resource="orders" fallback={<span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>❌ Create order (denied)</span>}>
              <span style={{ color: 'var(--color-success)', fontSize: 'var(--text-caption)' }}>✅ Create order (allowed)</span>
            </PermissionGate>
            <PermissionGate action="delete" resource="orders" fallback={<span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>❌ Delete order (denied)</span>}>
              <span style={{ color: 'var(--color-success)', fontSize: 'var(--text-caption)' }}>✅ Delete order (allowed)</span>
            </PermissionGate>
            <PermissionGateAll actions={['view', 'create', 'update']} resource="orders" fallback={<span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>❌ All CRUD orders (denied)</span>}>
              <span style={{ color: 'var(--color-success)', fontSize: 'var(--text-caption)' }}>✅ All CRUD orders (allowed)</span>
            </PermissionGateAll>
          </div>
        </div>
      </section>

      {/* Feature Flags */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Feature Gates</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
          <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
            {['bulk-import', 'advanced-analytics', 'dark-mode', 'inventory-forecast', 'voice-commands'].map((key) => (
              <div key={key} style={{ padding: '8px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)' }}>
                <div style={{ fontSize: 'var(--text-caption)', fontWeight: 600 }}>{key}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
                  {isEnabled(key) ? '✅ Enabled' : '❌ Disabled'} · Visible: {isEnabled(key) ? '✅' : '❌'}
                </div>
              </div>
            ))}
          </div>
          <FeatureGate flag="advanced-analytics" fallback={<div style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Advanced Analytics: ❌ Disabled</div>}>
            <div style={{ fontSize: 'var(--text-caption)' }}>Advanced Analytics: ✅ Enabled</div>
          </FeatureGate>
        </div>
      </section>

      {/* Operational States */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Operational States</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12 }}>
          {(['loading', 'empty', 'no_data', 'forbidden', 'offline', 'maintenance', 'feature_disabled', 'server_unavailable'] as const).map((s) => (
            <div key={s} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
              <div style={{ padding: '4px 8px', fontSize: 10, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>{s}</div>
              <OperationalStateDisplay state={s} compact />
            </div>
          ))}
        </div>
      </section>

      {/* Error Boundaries */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Error Boundaries</h3>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          <ComponentErrorBoundary componentName="TestWidget" fallback={<div style={{ padding: 16, color: 'var(--color-text-tertiary)' }}>Widget failed to load</div>}>
            <div style={{ padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)' }}>Normal widget content</div>
          </ComponentErrorBoundary>
          <ErrorTrigger />
        </div>
      </section>

      {/* System Status */}
      <section>
        <h3 style={{ marginBottom: 8 }}>System Status</h3>
        <div style={{ maxWidth: 400 }}>
          <SystemStatusPanel services={services} lastUpdated="Just now" />
        </div>
      </section>

      {/* Session Awareness */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Session Awareness</h3>
        <div style={{ display: 'flex', gap: 8 }}>
          <button onClick={() => session.extendSession()} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)' }}>
            Extend session
          </button>
          <button onClick={() => session.endSession()} style={{ padding: '6px 14px', border: '1px solid var(--color-error)', borderRadius: 'var(--radius-md)', background: 'transparent', cursor: 'pointer', color: 'var(--color-error)' }}>
            End session (test expired state)
          </button>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'flex', alignItems: 'center' }}>
            State: {session.state}
          </span>
        </div>
        <SessionTimeoutWarning open={session.isWarning} onExtend={session.extendSession} onLogout={session.endSession} />
        <SessionExpired onReauthenticate={() => session.extendSession()} savedPage={session.savedPage} />
      </section>

      {/* Offline */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Offline Experience</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
          <OfflineBanner show={showOffline} onRetry={() => setShowOffline(false)} />
          <ReconnectNotice show={status === 'online' && showOffline === false} />
          <button onClick={() => setShowOffline(!showOffline)} style={{ padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', width: 200 }}>
            {showOffline ? 'Restore connection' : 'Simulate offline'}
          </button>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Actual status: {status}</span>
        </div>
      </section>

      {/* Audit Components */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Audit-Aware Components</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>AuditInfo (full):</div>
            <AuditInfo meta={demoMeta} />
          </div>
          <div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>AuditInfo (compact):</div>
            <AuditInfo meta={demoMeta} compact />
          </div>
          <div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>AuditTimeline:</div>
            <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
              <AuditTimeline entries={demoAuditEntries} />
            </div>
          </div>
          <div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>VersionHistory:</div>
            <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
              <VersionHistory versions={demoVersions} />
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}

function ErrorTrigger() {
  const [shouldThrow, setShouldThrow] = useState(false);
  if (shouldThrow) throw new Error('Test error from user action');
  return (
    <button
      onClick={() => setShouldThrow(true)}
      style={{ padding: '8px 16px', border: '1px solid var(--color-error)', borderRadius: 'var(--radius-md)', background: 'transparent', cursor: 'pointer', color: 'var(--color-error)' }}
    >
      Trigger test error
    </button>
  );
}

export function SecurityPreview() {
  return (
    <PermissionProvider initialRole="super_admin">
      <FeatureFlagProvider>
        <GlobalErrorBoundary>
          <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
            <h2 style={{ margin: 0 }}>Part 6 — Enterprise Security, Permissions & Operational State Framework</h2>
            <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>
              Comprehensive preview of all Part 6 frameworks: permissions, feature flags, operational states, error boundaries, system status, session awareness, offline support, and audit-aware components.
            </p>
            <PermissionsDemoSection />
          </div>
        </GlobalErrorBoundary>
      </FeatureFlagProvider>
    </PermissionProvider>
  );
}
