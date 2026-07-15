import { memo } from 'react';
import { ReceivingTable } from '../components/ReceivingTable';
import { useReceivingData } from '../hooks/useReceivingData';
import { useReceivingFilters } from '../hooks/useReceivingFilters';

export const GoodsReceiptPage = memo(function GoodsReceiptPage() {
  const { receipts, loading } = useReceivingData();
  const { resetFilters, activeFilterCount } = useReceivingFilters();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Goods Receipt Registry ({receipts.length})</h3>
        <div style={{ display: 'flex', gap: 6 }}>
          {activeFilterCount > 0 && <button onClick={resetFilters} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Clear Filters ({activeFilterCount})</button>}
          <button style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Export All</button>
        </div>
      </div>
      <ReceivingTable receipts={receipts} loading={loading} onSelect={() => {}} />
    </div>
  );
});
