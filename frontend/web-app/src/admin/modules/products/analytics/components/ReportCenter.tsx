import React from 'react';
import type { AnalyticsReport } from '../types';

interface ReportCenterProps {
  reports: AnalyticsReport[];
}

export const ReportCenter: React.FC<ReportCenterProps> = React.memo(({ reports }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Report center">
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }} role="list" aria-label="Available reports">
      {reports.map((r) => (
        <div key={r.id} style={{ display: 'flex', flexDirection: 'column', gap: 10, padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }} role="listitem">
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <span style={{ fontSize: 24 }} aria-hidden="true">{r.icon}</span>
            <div style={{ flex: 1, minWidth: 0 }}>
              <h3 style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 600, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{r.title}</h3>
              <span style={{ display: 'inline-block', padding: '1px 8px', borderRadius: 10, fontSize: 'var(--text-caption)', background: 'color-mix(in srgb, var(--color-accent-blue) 10%, transparent)', color: 'var(--color-accent-blue)', marginTop: 4 }}>{r.type}</span>
            </div>
          </div>
          <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', lineHeight: 1.5 }}>{r.description}</p>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 'auto' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(r.generatedAt).toLocaleDateString()} &middot; {r.generatedBy}</span>
            <button style={{ padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-raised)', color: 'var(--color-accent-blue)', fontSize: 'var(--text-caption)', cursor: 'pointer', fontWeight: 500 }}>View Report</button>
          </div>
        </div>
      ))}
    </div>
  </div>
));

export default ReportCenter;
