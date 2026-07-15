import { memo } from 'react';
import type { InsightData } from '../types';

const TYPE_STYLES: Record<string, { bg: string; color: string; icon: string }> = {
  alert: { bg: 'var(--color-danger-alpha)', color: 'var(--color-danger)', icon: '\u26A0' },
  warning: { bg: 'var(--color-warning-alpha)', color: 'var(--color-warning)', icon: '\u26A0' },
  info: { bg: 'var(--color-info-alpha)', color: 'var(--color-info)', icon: '\u2139' },
  success: { bg: 'var(--color-success-alpha)', color: 'var(--color-success)', icon: '\u2713' },
};

export const InsightCard = memo(function InsightCard({ insight }: { insight: InsightData }) {
  const style = TYPE_STYLES[insight.type] ?? TYPE_STYLES.info;
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', gap: 12 }}>
      <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-sm)', background: style.bg, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 16, flexShrink: 0 }}>{style.icon}</div>
      <div style={{ flex: 1 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: 8 }}>
          <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{insight.title}</span>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', whiteSpace: 'nowrap' }}>{new Date(insight.timestamp).toLocaleDateString()}</span>
        </div>
        <p style={{ margin: '4px 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{insight.description}</p>
        {insight.actionLabel && <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)', marginTop: 4 }}>{insight.actionLabel}</button>}
      </div>
    </div>
  );
});
