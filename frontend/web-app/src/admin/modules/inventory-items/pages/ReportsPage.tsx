import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';

const REPORT_TEMPLATES = [
  { id: 'r1', title: 'Item Registry Export', description: 'Full inventory item list with classifications and lifecycle.', icon: 'file-text' },
  { id: 'r2', title: 'Product Mapping Report', description: 'Products and their associated inventory item counts.', icon: 'shopping-bag' },
  { id: 'r3', title: 'SKU Association Report', description: 'All SKU-to-inventory item links with variant details.', icon: 'hash' },
  { id: 'r4', title: 'Classification Summary', description: 'Items grouped by classification type and grade.', icon: 'bookmark' },
  { id: 'r5', title: 'Lifecycle Status Report', description: 'Items grouped by lifecycle stage with event counts.', icon: 'activity' },
  { id: 'r6', title: 'Data Quality Report', description: 'Validation issues, missing data, and mapping gaps.', icon: 'check-circle' },
  { id: 'r7', title: 'Change History Report', description: 'Audit trail of all inventory item modifications.', icon: 'clock' },
  { id: 'r8', title: 'Custom Report Builder', description: 'Build ad-hoc reports with custom filters and columns.', icon: 'settings', placeholder: true },
];

export const ReportsPage = memo(function ReportsPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Reports" description="Generate and download operational reports for inventory items — including mapping, classification, lifecycle, and data quality summaries." />

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
