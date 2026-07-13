import React from 'react';
import { StatusIndicator } from '../../components/feedback';
import type { StatusType } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

const allStatuses: StatusType[] = ['online', 'offline', 'busy', 'pending', 'processing', 'completed', 'failed', 'queued', 'draft', 'archived'];

export default function StatusPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Status Indicators</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All 10 status types with size variants, labels, and animation states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>All Status Types</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          {allStatuses.map((status) => (
            <StateCard key={status} label={status.charAt(0).toUpperCase() + status.slice(1)}>
              <StatusIndicator status={status} size="md" label />
            </StateCard>
          ))}
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Size Variants</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          {(['sm', 'md', 'lg'] as const).map((size) => (
            <StateCard key={size} label={`Size: ${size.toUpperCase()}`}>
              <div style={{ display: 'flex', gap: '16px' }}>
                {allStatuses.slice(0, 5).map((status) => (
                  <StatusIndicator key={status} status={status} size={size} label />
                ))}
              </div>
            </StateCard>
          ))}
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>With and Without Labels</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="With Labels">
            <div style={{ display: 'flex', gap: '16px' }}>
              <StatusIndicator status="online" label />
              <StatusIndicator status="busy" label />
              <StatusIndicator status="offline" label />
            </div>
          </StateCard>
          <StateCard label="Without Labels (dot only)">
            <div style={{ display: 'flex', gap: '16px' }}>
              <StatusIndicator status="online" />
              <StatusIndicator status="busy" />
              <StatusIndicator status="offline" />
            </div>
          </StateCard>
          <StateCard label="Custom Labels">
            <div style={{ display: 'flex', gap: '16px' }}>
              <StatusIndicator status="completed" label="Done" />
              <StatusIndicator status="failed" label="Error" />
              <StatusIndicator status="draft" label="Unpublished" />
            </div>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Animated States</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Animated (processing, pending)">
            <div style={{ display: 'flex', gap: '16px' }}>
              <StatusIndicator status="processing" size="lg" label animated />
              <StatusIndicator status="pending" size="lg" label animated />
            </div>
          </StateCard>
          <StateCard label="Static (animated disabled)">
            <div style={{ display: 'flex', gap: '16px' }}>
              <StatusIndicator status="processing" size="lg" label animated={false} />
              <StatusIndicator status="pending" size="lg" label animated={false} />
            </div>
          </StateCard>
          <StateCard label="Non-animated types">
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px' }}>
              {allStatuses.filter((s) => s !== 'processing' && s !== 'pending').map((status) => (
                <StatusIndicator key={status} status={status} size="md" label />
              ))}
            </div>
          </StateCard>
        </div>
      </section>
    </div>
  );
}
