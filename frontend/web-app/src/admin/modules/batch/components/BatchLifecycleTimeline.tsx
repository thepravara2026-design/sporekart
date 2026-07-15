import { memo } from 'react';
import type { BatchLifecycleState } from '../types';
import { getLifecycleVariant } from '../utils';
import { BATCH_LIFECYCLE_STATES } from '../constants';
import { VARIANT_COLORS } from '../../../constants/variantColors';

export const BatchLifecycleTimeline = memo(function BatchLifecycleTimeline({ currentStatus }: { currentStatus: BatchLifecycleState }) {
  const currentIndex = BATCH_LIFECYCLE_STATES.findIndex((s) => s.value === currentStatus);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 0, padding: 16 }}>
      {BATCH_LIFECYCLE_STATES.map((state, idx) => {
        const isCompleted = idx <= currentIndex;
        const isCurrent = state.value === currentStatus;
        const color = VARIANT_COLORS[getLifecycleVariant(state.value)] ?? 'var(--color-text-tertiary)';

        return (
          <div key={state.value} style={{ display: 'flex', alignItems: 'stretch', gap: 12, minHeight: 48 }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: 24 }}>
              <div style={{ width: 12, height: 12, borderRadius: '50%', background: isCompleted ? color : 'var(--color-border)', border: `2px solid ${isCompleted ? color : 'var(--color-border)'}`, flexShrink: 0, marginTop: 4 }} />
              {idx < BATCH_LIFECYCLE_STATES.length - 1 && <div style={{ flex: 1, width: 2, background: isCompleted ? color : 'var(--color-border)', minHeight: 24 }} />}
            </div>
            <div style={{ flex: 1, paddingBottom: idx < BATCH_LIFECYCLE_STATES.length - 1 ? 8 : 0 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--text-body, 14px)', fontWeight: isCurrent ? 700 : 500, color: isCurrent ? 'var(--color-text-primary)' : isCompleted ? 'var(--color-text-primary)' : 'var(--color-text-tertiary)' }}>{state.label}</span>
                {isCurrent && <span style={{ fontSize: 'var(--text-caption, 11px)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', padding: '1px 8px', borderRadius: 'var(--radius-badge, 12px)', fontWeight: 600 }}>CURRENT</span>}
              </div>
              {state.description && <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption, 12px)', color: 'var(--color-text-tertiary)' }}>{state.description}</p>}
            </div>
          </div>
        );
      })}
    </div>
  );
});

