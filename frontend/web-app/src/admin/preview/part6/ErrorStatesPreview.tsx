import { useState } from 'react';
import { GlobalErrorBoundary, ModuleErrorBoundary, PageErrorBoundary, ComponentErrorBoundary } from '../../error-boundaries';
import { OperationalStateDisplay, LoadingSkeleton } from '../../operational-states';

function BrokenComponent() {
  const [throwError, setThrowError] = useState(false);
  if (throwError) throw new Error('Simulated component crash');
  return (
    <button onClick={() => setThrowError(true)} style={{ padding: '8px 16px', border: '1px solid var(--color-error)', borderRadius: 'var(--radius-md)', background: 'transparent', cursor: 'pointer', color: 'var(--color-error)' }}>
      Crash this component
    </button>
  );
}

export function ErrorStatesPreview() {
  const [loading, setLoading] = useState(true);

  return (
    <GlobalErrorBoundary>
      <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
        <h2 style={{ margin: 0 }}>Error Handling & Operational States</h2>

        {/* Error Boundaries */}
        <section>
          <h3 style={{ marginBottom: 8 }}>Error Boundary Levels</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            <div style={{ padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 8 }}>ModuleErrorBoundary (Dashboard Module)</div>
              <ModuleErrorBoundary moduleName="Dashboard Module">
                <div style={{ padding: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
                  Dashboard module content — <BrokenComponent />
                </div>
              </ModuleErrorBoundary>
            </div>
            <div style={{ padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 8 }}>PageErrorBoundary (Orders Page)</div>
              <PageErrorBoundary pageName="Orders Page">
                <div style={{ padding: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
                  Orders page content — <BrokenComponent />
                </div>
              </PageErrorBoundary>
            </div>
            <div style={{ padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 8 }}>ComponentErrorBoundary (ChartWidget)</div>
              <ComponentErrorBoundary componentName="ChartWidget" fallback={<div style={{ padding: 16, color: 'var(--color-text-tertiary)', textAlign: 'center' }}>📊 Chart widget unavailable</div>}>
                <div style={{ padding: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
                  Chart widget — <BrokenComponent />
                </div>
              </ComponentErrorBoundary>
            </div>
          </div>
        </section>

        {/* Loading Skeletons */}
        <section>
          <h3 style={{ marginBottom: 8 }}>Loading Skeletons</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            <button onClick={() => setLoading(!loading)} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', width: 150 }}>
              Toggle loading
            </button>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
            <div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>Text skeleton</div>
              <LoadingSkeleton lines={4} />
            </div>
            <div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>Card skeleton</div>
              <LoadingSkeleton variant="card" />
            </div>
            <div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>Table skeleton</div>
              <LoadingSkeleton variant="table" lines={4} />
            </div>
            <div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>Sidebar skeleton</div>
              <LoadingSkeleton variant="sidebar" lines={5} />
            </div>
          </div>
        </section>

        {/* All Operational States */}
        <section>
          <h3 style={{ marginBottom: 8 }}>All Operational States</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 12 }}>
            {(['loading', 'empty', 'no_data', 'permission_denied', 'unauthorized', 'forbidden', 'offline', 'maintenance', 'system_updating', 'feature_disabled', 'server_unavailable', 'unexpected_error'] as const).map((s) => (
              <div key={s} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
                <div style={{ padding: '4px 8px', fontSize: 10, color: 'var(--color-text-tertiary)', textTransform: 'uppercase', background: 'var(--color-surface)' }}>{s.replace(/_/g, ' ')}</div>
                <OperationalStateDisplay state={s} />
              </div>
            ))}
          </div>
        </section>

        {/* With action buttons */}
        <section>
          <h3 style={{ marginBottom: 8 }}>States with Actions</h3>
          <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
            <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 260 }}>
              <OperationalStateDisplay
                state="unexpected_error"
                config={{
                  title: 'Failed to load data',
                  description: 'Unable to fetch orders. Please check your connection.',
                  action: { label: 'Retry', onClick: () => alert('Retry clicked') },
                  secondaryAction: { label: 'Contact support', onClick: () => alert('Contact support clicked') },
                }}
              />
            </div>
            <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 260 }}>
              <OperationalStateDisplay
                state="empty"
                config={{
                  title: 'No orders yet',
                  description: 'Get started by creating your first order.',
                  action: { label: 'Create order', onClick: () => alert('Create order clicked') },
                }}
              />
            </div>
            <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 260 }}>
              <OperationalStateDisplay
                state="maintenance"
                config={{
                  title: 'Scheduled Maintenance',
                  description: 'Orders will be back in 2 hours.',
                  action: { label: 'Check status', onClick: () => alert('Status clicked') },
                }}
              />
            </div>
          </div>
        </section>
      </div>
    </GlobalErrorBoundary>
  );
}
