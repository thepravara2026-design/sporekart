import { memo } from 'react';
import { TransactionTable } from '../components/TransactionTable';
import { useMovementData } from '../hooks/useMovementData';
import { useMovementFilters } from '../hooks/useMovementFilters';

export const TransactionsPage = memo(function TransactionsPage() {
  const { transactions, loading } = useMovementData();
  const { resetFilters, activeFilterCount } = useMovementFilters();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Transactions ({transactions.length})</h3>
        <div style={{ display: 'flex', gap: 6 }}>
          {activeFilterCount > 0 && <button onClick={resetFilters} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Clear Filters ({activeFilterCount})</button>}
          <button onClick={() => {}} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Export All</button>
        </div>
      </div>
      <TransactionTable transactions={transactions} loading={loading} onSelect={() => {}} />
    </div>
  );
});
