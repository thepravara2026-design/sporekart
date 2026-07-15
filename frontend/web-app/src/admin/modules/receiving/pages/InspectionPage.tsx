import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { getInspectionVariant } from '../utils';

export const InspectionPage = memo(function InspectionPage() {
  const { inspections, loading } = useReceivingData();

  if (loading) return <div>Loading...</div>;
  if (inspections.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No inspections found.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Quality Inspection ({inspections.length})</h3>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Receipt</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Product</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Type</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Inspector</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Status</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Quality</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Packaging</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Damage</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Verified</th>
            </tr>
          </thead>
          <tbody>
            {inspections.map((ins) => (
              <tr key={ins.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 12, fontWeight: 500 }}>{ins.receiptNumber}</td>
                <td style={{ padding: 12 }}>{ins.product}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{ins.type.replace(/_/g, ' ')}</td>
                <td style={{ padding: 12 }}>{ins.inspector}</td>
                <td style={{ padding: 12 }}><StatusBadge status={ins.status.replace(/_/g, ' ')} variant={getInspectionVariant(ins.status) as 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'} /></td>
                <td style={{ padding: 12 }}>{ins.productQuality ? '\u2713' : '\u2717'}</td>
                <td style={{ padding: 12 }}>{ins.packagingQuality ? '\u2713' : '\u2717'}</td>
                <td style={{ padding: 12 }}>{ins.damageDetected ? '\u2717' : '\u2713'}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{ins.quantityMatch ? 'Qty OK' : 'Qty Issue'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
