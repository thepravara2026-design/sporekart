import { memo } from 'react';
import { SectionHeader, StatisticsGrid } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';

const VALIDATION_METRICS = [
  { id: 'coverage', title: 'Classification Coverage', value: '95.8%', trend: 'up' as const, percentage: 2.1, comparison: 'vs last month', icon: 'bookmark' as const, color: 'var(--color-success)' },
  { id: 'mapping', title: 'Mapping Completeness', value: '98.2%', trend: 'up' as const, percentage: 1.4, comparison: 'products mapped', icon: 'shopping-bag' as const, color: 'var(--color-success)' },
  { id: 'compliance', title: 'Lifecycle Compliance', value: '92.3%', trend: 'up' as const, percentage: 3.7, comparison: 'items in valid state', icon: 'activity' as const, color: 'var(--color-info)' },
  { id: 'issues', title: 'Items with Issues', value: '23', trend: 'down' as const, percentage: 15.0, comparison: 'needs review', icon: 'alert-triangle' as const, color: 'var(--color-danger)' },
  { id: 'missing', title: 'Missing Classification', value: '620', trend: 'down' as const, percentage: 8.9, comparison: 'vs last month', icon: 'bookmark' as const, color: 'var(--color-warning)' },
  { id: 'last_run', title: 'Last Validation Run', value: '2h ago', trend: 'flat' as const, percentage: 0, comparison: 'scheduled daily', icon: 'clock' as const, color: 'var(--color-neutral)' },
];

export const ValidationPage = memo(function ValidationPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Data Validation" description="Validate inventory item data integrity — classification completeness, mapping consistency, lifecycle compliance, and metadata quality." />

      <StatisticsGrid metrics={VALIDATION_METRICS} />

      <div style={{ border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', padding: '48px 24px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 12, textAlign: 'center' }}>
        <div style={{ width: 56, height: 56, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <Icon name="check-circle" size={28} />
        </div>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Validation Rules Engine</h2>
        <p style={{ margin: 0, color: 'var(--color-text-secondary)', maxWidth: 460 }}>Define validation rules, run bulk checks, and view detailed reports on data quality issues across all inventory items.</p>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '4px 10px', borderRadius: 'var(--radius-badge)' }}>Extensible · Sprint 25 Part 4 ready</span>
      </div>
    </div>
  );
});
