import { memo } from 'react';
import { SectionHeader, StatisticsGrid } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';

const VALIDATION_METRICS = [
  { id: 'records', title: 'Total Records', value: '18,240', trend: 'up' as const, percentage: 5.8, comparison: 'vs last month', icon: 'database', color: 'var(--color-primary)' },
  { id: 'passed', title: 'Validation Passed', value: '17,832', trend: 'up' as const, percentage: 2.3, comparison: '97.8% pass rate', icon: 'check-circle', color: 'var(--color-success)' },
  { id: 'failed', title: 'Validation Failed', value: '408', trend: 'down' as const, percentage: 1.8, comparison: 'needs review', icon: 'alert-triangle', color: 'var(--color-danger)' },
  { id: 'health_ok', title: 'Healthy Records', value: '15,230', trend: 'up' as const, percentage: 1.2, comparison: '83.5% of total', icon: 'heart', color: 'var(--color-success)' },
  { id: 'needs_attention', title: 'Needs Attention', value: '3,010', trend: 'down' as const, percentage: 3.5, comparison: 'flagged for review', icon: 'flag', color: 'var(--color-warning)' },
  { id: 'last_run', title: 'Last Validation', value: '1h ago', trend: 'flat' as const, percentage: 0, comparison: 'scheduled hourly', icon: 'clock', color: 'var(--color-neutral)' },
];

export const ValidationPage = memo(function ValidationPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Validation" description="Stock data validation — check state consistency, health accuracy, timeline completeness and data integrity across all records." />

      <StatisticsGrid metrics={VALIDATION_METRICS} />

      <div style={{ border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', padding: '48px 24px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 12, textAlign: 'center' }}>
        <div style={{ width: 56, height: 56, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <Icon name="check-circle" size={28} />
        </div>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Validation Rules Engine</h2>
        <p style={{ margin: 0, color: 'var(--color-text-secondary)', maxWidth: 460 }}>Define validation rules for stock states, health thresholds, availability transitions and timeline completeness. Run bulk validations and review flagged records.</p>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '4px 10px', borderRadius: 'var(--radius-badge)' }}>Extensible · Sprint 25 Part 5 ready</span>
      </div>
    </div>
  );
});
