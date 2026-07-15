import { memo, useMemo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { useBatchSearch } from '../hooks';
import { useBatchFilters } from '../hooks/useBatchFilters';
import { BatchTable } from '../components/BatchTable';
import { filterBatches } from '../utils';
import { BATCH_EMPTY_STATES } from '../constants';
import { useBatchWorkspace } from '../contexts/BatchWorkspaceContext';

export const BatchRegistryPage = memo(function BatchRegistryPage() {
  const { batches } = useBatchData();
  const { query, setQuery, filtered: searched } = useBatchSearch(batches);
  const { filters, resetFilters, activeFilterCount } = useBatchFilters();
  const { setActiveSection, setProfileBatchId } = useBatchWorkspace();
  const filtered = useMemo(() => filterBatches(searched, filters), [searched, filters]);
  const emptyState = filtered.length === 0 && (query || activeFilterCount > 0) ? BATCH_EMPTY_STATES.noBatches : null;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Batch Registry" description="All batches — search, filter, manage." />
      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
        <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search by batch code, product, SKU, warehouse..." style={{ flex: 1, minWidth: 200, padding: '8px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }} />
        <button onClick={resetFilters} style={{ padding: '8px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}>Clear Filters ({activeFilterCount})</button>
      </div>
      {emptyState ? (
        <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <p style={{ fontWeight: 600, margin: '0 0 4px', color: 'var(--color-text-primary)' }}>{emptyState.title}</p>
          <p style={{ margin: '0 0 12px', fontSize: 'var(--text-body)' }}>{emptyState.message}</p>
          <button onClick={resetFilters} style={{ padding: '8px 16px', border: 'none', borderRadius: 'var(--radius-md)', background: 'var(--color-primary)', color: '#fff', cursor: 'pointer' }}>{emptyState.actionLabel ?? 'Clear'}</button>
        </div>
      ) : (
        <BatchTable batches={filtered} onSelect={(id) => { setProfileBatchId(id); setActiveSection('profile'); }} />
      )}
    </div>
  );
});
