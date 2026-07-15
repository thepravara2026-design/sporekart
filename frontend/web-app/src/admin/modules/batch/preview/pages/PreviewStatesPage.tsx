import { memo } from 'react';
import { BATCH_LIFECYCLE_STATES, EXPIRY_STATUSES, QUALITY_STATUSES } from '../../constants';

function BadgeDemo({ label, color }: { label: string; color: string }) {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '6px 12px', background: `${color}1A`, borderRadius: 'var(--radius-md)', fontSize: 'var(--text-body)' }}>
      <span style={{ width: 8, height: 8, borderRadius: '50%', background: color, flexShrink: 0 }} />
      <span style={{ fontWeight: 500, color }}>{label}</span>
    </div>
  );
}

export const PreviewStatesPage = memo(function PreviewStatesPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>States Reference</h2>
      <div>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Batch Lifecycle ({BATCH_LIFECYCLE_STATES.length} states)</h3>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
          {BATCH_LIFECYCLE_STATES.map((s: { value: string; label: string; variant: string }) => (
            <BadgeDemo key={s.value} label={s.label} color={s.variant === 'success' ? 'var(--color-success)' : s.variant === 'warning' ? 'var(--color-warning)' : s.variant === 'danger' ? 'var(--color-danger)' : s.variant === 'info' ? 'var(--color-info)' : 'var(--color-neutral)'} />
          ))}
        </div>
      </div>
      <div>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Expiry Status ({EXPIRY_STATUSES.length} statuses)</h3>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
          {EXPIRY_STATUSES.map((s: { value: string; label: string; variant: string }) => (
            <BadgeDemo key={s.value} label={s.label} color={s.variant === 'success' ? 'var(--color-success)' : s.variant === 'warning' ? 'var(--color-warning)' : s.variant === 'danger' ? 'var(--color-danger)' : s.variant === 'info' ? 'var(--color-info)' : 'var(--color-neutral)'} />
          ))}
        </div>
      </div>
      <div>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Quality Status ({QUALITY_STATUSES.length} statuses)</h3>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
          {QUALITY_STATUSES.map((s: { value: string; label: string; variant: string }) => (
            <BadgeDemo key={s.value} label={s.label} color={s.variant === 'success' ? 'var(--color-success)' : s.variant === 'warning' ? 'var(--color-warning)' : s.variant === 'danger' ? 'var(--color-danger)' : s.variant === 'info' ? 'var(--color-info)' : 'var(--color-neutral)'} />
          ))}
        </div>
      </div>
    </div>
  );
});
