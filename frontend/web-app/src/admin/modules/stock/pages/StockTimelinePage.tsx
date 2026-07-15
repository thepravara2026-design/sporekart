import { memo, useState, useMemo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';
import { getStockRecords } from '../services/stockMockService';

export const StockTimelinePage = memo(function StockTimelinePage() {
  const [records] = useState(() => getStockRecords());

  const allEvents = useMemo(() => {
    const events = records.flatMap((r) => r.timeline.map((e) => ({ ...e, stockCode: r.code, itemName: r.inventoryItemName })));
    return events.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
  }, [records]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Global Stock Timeline" description="All stock events across every inventory item and warehouse. Track state changes, quantity updates, reservations and more." />

      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Showing {allEvents.length} events from {records.length} stock records
      </div>

      <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', maxHeight: 600, overflowY: 'auto' }}>
        <div style={{ padding: '12px 16px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface-hover)', display: 'flex', gap: 8, fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>
          <span style={{ flex: 1 }}>Event</span>
          <span style={{ width: 120 }}>Stock Record</span>
          <span style={{ width: 100 }}>Timestamp</span>
        </div>
        <div style={{ padding: '0 16px' }}>
          {allEvents.slice(0, 100).map((event, i) => (
            <div key={event.id} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '8px 0', borderBottom: i < 99 ? '1px solid var(--color-border)' : 'none' }}>
              <Icon name="activity" size={14} />
              <span style={{ flex: 1, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{event.action}</span>
              <span style={{ width: 120, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', fontFamily: 'monospace' }}>{event.stockCode}</span>
              <span style={{ width: 100, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{event.timestamp}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});
