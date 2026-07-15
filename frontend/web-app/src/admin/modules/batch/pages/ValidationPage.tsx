import { memo, useMemo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { generateValidationErrors } from '../utils';

export const ValidationPage = memo(function ValidationPage() {
  const { batches } = useBatchData();
  const errors = useMemo(() => generateValidationErrors(batches), [batches]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Validation" description="Data validation and quality checks." />
      <div style={{ display: 'flex', gap: 8 }}>
        <span style={{ padding: '6px 14px', background: 'var(--color-danger-alpha)', color: 'var(--color-danger)', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 600 }}>{errors.filter((e) => e.severity === 'error').length} Errors</span>
        <span style={{ padding: '6px 14px', background: 'var(--color-warning-alpha)', color: 'var(--color-warning)', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 600 }}>{errors.filter((e) => e.severity === 'warning').length} Warnings</span>
        <span style={{ padding: '6px 14px', background: 'var(--color-info-alpha)', color: 'var(--color-info)', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 600 }}>{errors.filter((e) => e.severity === 'info').length} Info</span>
      </div>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {errors.length === 0 ? (
          <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No validation issues found.</div>
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }}>
            <thead>
              <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
                <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Field</th>
                <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Message</th>
                <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Severity</th>
              </tr>
            </thead>
            <tbody>
              {errors.map((err, idx) => (
                <tr key={idx} style={{ borderBottom: '1px solid var(--color-border)' }}>
                  <td style={{ padding: 12, fontWeight: 500 }}>{err.field}</td>
                  <td style={{ padding: 12 }}>{err.message}</td>
                  <td style={{ padding: 12 }}>
                    <span style={{ padding: '2px 10px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', background: err.severity === 'error' ? 'var(--color-danger-alpha)' : err.severity === 'warning' ? 'var(--color-warning-alpha)' : 'var(--color-info-alpha)', color: err.severity === 'error' ? 'var(--color-danger)' : err.severity === 'warning' ? 'var(--color-warning)' : 'var(--color-info)' }}>{err.severity}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
});

