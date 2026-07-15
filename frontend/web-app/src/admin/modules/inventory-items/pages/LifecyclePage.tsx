import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { LifecycleTimeline } from '../components/LifecycleTimeline';
import { LIFECYCLE_STAGES } from '../constants';
import { getItems } from '../services/inventoryItemMockService';

export const LifecyclePage = memo(function LifecyclePage() {
  const [items] = useState(() => getItems());
  const activeItem = items[0];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Lifecycle Management" description="Track and manage the lifecycle stages of every inventory item — from draft through active to archived. Each stage transition is recorded as an auditable event." />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 8 }}>
        {LIFECYCLE_STAGES.map((s) => {
          const count = items.filter((i) => i.lifecycle === s.value).length;
          return (
            <div key={s.value} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', padding: '12px 16px', background: 'var(--color-surface)', textAlign: 'center' }}>
              <div style={{ fontSize: 'var(--text-h2)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{count}</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginTop: 4 }}>{s.label}</div>
            </div>
          );
        })}
      </div>

      {activeItem && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>
            Sample Lifecycle: {activeItem.name}
          </h3>
          <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
            Current stage: <strong>{activeItem.lifecycle.replace(/_/g, ' ')}</strong>
          </p>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)' }}>
            <LifecycleTimeline events={activeItem.lifecycleEvents} />
          </div>
        </div>
      )}
    </div>
  );
});
