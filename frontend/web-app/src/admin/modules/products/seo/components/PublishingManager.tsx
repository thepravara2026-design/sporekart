import React from 'react';
import { MOCK_PUBLISHING_EVENTS, PUBLISHING_STATUS_FLOW, PUBLISHING_STATUS_COLORS } from '../mock/mockPublishing';

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const flow: React.CSSProperties = { display: 'flex', alignItems: 'center', gap: 4, padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', overflow: 'auto' };
const stepBase: React.CSSProperties = { padding: '6px 14px', borderRadius: 20, fontSize: 'var(--text-body-xs)', fontWeight: 600, whiteSpace: 'nowrap' };
const timeline: React.CSSProperties = { borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' };
const ev: React.CSSProperties = { display: 'flex', gap: 12, padding: '10px 16px', borderBottom: '1px solid var(--color-border)', alignItems: 'center', fontSize: 'var(--text-body-xs)' };

export const PublishingManager: React.FC = React.memo(() => {
  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Publishing Workflow</h2>
      <div style={flow}>
        {PUBLISHING_STATUS_FLOW.map((status, i) => (
          <React.Fragment key={status}>
            <div style={{ ...stepBase, background: `${PUBLISHING_STATUS_COLORS[status]}20`, color: PUBLISHING_STATUS_COLORS[status] }}>
              {status.replace('_', ' ')}
            </div>
            {i < PUBLISHING_STATUS_FLOW.length - 1 && <span style={{ color: 'var(--color-text-tertiary)', fontSize: 12 }}>→</span>}
          </React.Fragment>
        ))}
      </div>

      <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginTop: 8 }}>Recent Publishing Events</h3>
      <div style={timeline}>
        {MOCK_PUBLISHING_EVENTS.slice(0, 8).map((p) => (
          <div key={p.id} style={ev}>
            <span style={{ width: 12, height: 12, borderRadius: '50%', background: PUBLISHING_STATUS_COLORS[p.toStatus], flexShrink: 0 }} />
            <div style={{ flex: 1 }}>
              <span style={{ fontWeight: 500 }}>{p.productName}</span>
              <span style={{ color: 'var(--color-text-tertiary)', marginLeft: 4 }}>
                {p.fromStatus} → {p.toStatus}
              </span>
            </div>
            <span style={{ color: 'var(--color-text-tertiary)' }}>{p.changedBy}</span>
            <span style={{ color: 'var(--color-text-tertiary)', fontSize: 11 }}>{new Date(p.changedAt).toLocaleDateString()}</span>
          </div>
        ))}
      </div>
    </div>
  );
});
