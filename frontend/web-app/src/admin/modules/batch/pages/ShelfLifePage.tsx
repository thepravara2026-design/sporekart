import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { SHELF_LIFE_UNITS } from '../constants';

export const ShelfLifePage = memo(function ShelfLifePage() {
  const { batches } = useBatchData();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Shelf Life" description="Shelf life configurations and storage conditions." />
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Shelf Life Units</h3>
        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
          {SHELF_LIFE_UNITS.map((u) => (
            <span key={u.value} style={{ padding: '4px 14px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{u.label}</span>
          ))}
        </div>
      </div>
      <div style={{ overflowX: 'auto', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Batch Code</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Product</th>
              <th style={{ padding: 12, textAlign: 'right', fontWeight: 600, color: 'var(--color-text-primary)' }}>Shelf Life</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Unit</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Temperature</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Humidity</th>
            </tr>
          </thead>
          <tbody>
            {batches.slice(0, 20).map((b) => (
              <tr key={b.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 12, fontWeight: 500 }}>{b.batchCode}</td>
                <td style={{ padding: 12 }}>{b.product}</td>
                <td style={{ padding: 12, textAlign: 'right' }}>{b.shelfLife}</td>
                <td style={{ padding: 12 }}>{b.shelfLifeUnit}</td>
                <td style={{ padding: 12 }}>{b.storageConditions.temperature ?? '—'}</td>
                <td style={{ padding: 12 }}>{b.storageConditions.humidity ?? '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

