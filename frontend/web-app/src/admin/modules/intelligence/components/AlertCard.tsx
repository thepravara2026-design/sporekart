import { memo } from 'react';
import type { AlertData } from '../types';

const SEVERITY_COLORS: Record<string, string> = { critical: 'var(--color-danger)', high: 'var(--color-warning)', medium: 'var(--color-info)', low: 'var(--color-neutral)' };
const SEVERITY_ICONS: Record<string, string> = { critical: '\u26A0', high: '\u26A0', medium: '\u2139', low: '\u2139' };

export const AlertCard = memo(function AlertCard({ alert }: { alert: AlertData }) {
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: `1px solid ${SEVERITY_COLORS[alert.severity]}`, borderLeft: `4px solid ${SEVERITY_COLORS[alert.severity]}`, padding: 14, display: 'flex', gap: 12, opacity: alert.acknowledged ? 0.6 : 1 }}>
      <span style={{ fontSize: 16, color: SEVERITY_COLORS[alert.severity] }}>{SEVERITY_ICONS[alert.severity]}</span>
      <div style={{ flex: 1 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: 8 }}>
          <div>
            <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{alert.title}</span>
            <span style={{ marginLeft: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '1px 6px', borderRadius: 'var(--radius-sm)' }}>{alert.category}</span>
          </div>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', whiteSpace: 'nowrap' }}>{new Date(alert.timestamp).toLocaleDateString()}</span>
        </div>
        <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{alert.message}</p>
      </div>
    </div>
  );
});
