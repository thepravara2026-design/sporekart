import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';

const REPORT_TEMPLATES = [
  { id: 'r1', title: 'Stock Health Report', description: 'Complete health assessment across all stock records.', icon: 'heart' },
  { id: 'r2', title: 'Availability Report', description: 'Available, limited and unavailable stock summary.', icon: 'check-circle' },
  { id: 'r3', title: 'Low Stock Report', description: 'Items below low stock and critical thresholds.', icon: 'alert-triangle' },
  { id: 'r4', title: 'Reserved Stock Report', description: 'All reserved stock by reservation type.', icon: 'lock' },
  { id: 'r5', title: 'Warehouse Stock Report', description: 'Stock distribution by warehouse.', icon: 'home' },
  { id: 'r6', title: 'Category Report', description: 'Stock grouped by product category.', icon: 'bookmark' },
  { id: 'r7', title: 'Brand Report', description: 'Stock grouped by brand.', icon: 'tag' },
  { id: 'r8', title: 'Custom Report Builder', description: 'Build ad-hoc reports with custom filters and columns.', icon: 'settings', placeholder: true },
];

export const ReportsPage = memo(function ReportsPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Reports" description="Generate and export stock reports — health, availability, low stock, reservations and distribution by warehouse, category and brand." />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {REPORT_TEMPLATES.map((r) => (
          <div key={r.id} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 12, opacity: r.placeholder ? 0.5 : 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Icon name={r.icon} size={18} />
              </div>
              <h4 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{r.title}</h4>
            </div>
            <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{r.description}</p>
            {r.placeholder ? (
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '4px 10px', borderRadius: 'var(--radius-badge)', alignSelf: 'flex-start' }}>Coming soon</span>
            ) : (
              <div style={{ display: 'flex', gap: 8 }}>
                <button style={{ padding: '6px 14px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-primary)', fontSize: 'var(--text-body)', fontWeight: 500 }}><Icon name="download" size={14} /> Export CSV</button>
                <button style={{ padding: '6px 14px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}><Icon name="eye" size={14} /> Preview</button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
});
