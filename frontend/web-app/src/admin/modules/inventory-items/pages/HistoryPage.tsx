import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';

const HISTORY_EVENTS = [
  { id: 'h1', action: 'Item Created', item: 'INV-8241', user: 'Admin User', timestamp: '2 hours ago', details: 'Inventory item created from product PRD-001 variant VAR-001' },
  { id: 'h2', action: 'Classification Updated', item: 'INV-2291', user: 'Inventory Manager', timestamp: '5 hours ago', details: 'Type changed from raw_material to finished_good' },
  { id: 'h3', action: 'Lifecycle Changed', item: 'INV-3120', user: 'System', timestamp: '1 day ago', details: 'Stage changed from pending_approval to active' },
  { id: 'h4', action: 'SKU Linked', item: 'INV-4418', user: 'Operator', timestamp: '1 day ago', details: 'SKU SKU-4421 associated with inventory item' },
  { id: 'h5', action: 'Product Mapping Updated', item: 'PRD-312', user: 'Admin User', timestamp: '2 days ago', details: 'Product remapped to 14 inventory items' },
  { id: 'h6', action: 'Bulk Classification', item: '230 items', user: 'Inventory Manager', timestamp: '3 days ago', details: 'Bulk classification update applied to spawn product line' },
  { id: 'h7', action: 'Item Archived', item: 'INV-5590', user: 'Admin User', timestamp: '5 days ago', details: 'Item lifecycle changed to archived' },
  { id: 'h8', action: 'Validation Passed', item: 'All Items', user: 'System', timestamp: '1 week ago', details: 'Full validation run completed with 98.3% pass rate' },
];

export const HistoryPage = memo(function HistoryPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Change History" description="Audit trail of all modifications made to inventory items, mappings, classifications, and lifecycle transitions." />

      <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden', background: 'var(--color-surface)' }}>
        {HISTORY_EVENTS.map((e, i) => (
          <div key={e.id} style={{ display: 'flex', gap: 12, padding: '12px 16px', borderBottom: i < HISTORY_EVENTS.length - 1 ? '1px solid var(--color-border)' : 'none' }}>
            <div style={{ width: 32, height: 32, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
              <Icon name="clock" size={16} />
            </div>
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
                <span style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{e.action}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{e.timestamp}</span>
              </div>
              <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{e.details}</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>By {e.user} · {e.item}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
