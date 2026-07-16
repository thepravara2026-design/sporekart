import { useMemo } from 'react';
import { useEnrollment } from '../state/EnrollmentContext';
import { BatchCard } from '../components/BatchCard';
import { EmptyState } from '../components/EmptyStates';
import { BatchCardSkeleton } from '../components/Skeletons';

export function EnrollmentBatchManagementPage() {
  const { batches, isLoading } = useEnrollment();

  const groupedBatches = useMemo(() => {
    const groups: Record<string, typeof batches> = {};
    batches.forEach((b) => {
      const key = b.slot;
      if (!groups[key]) groups[key] = [];
      groups[key].push(b);
    });
    return Object.entries(groups).map(([key, items]) => ({ key, items }));
  }, [batches]);

  if (isLoading) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
        <div><h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Batch Management</h1></div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {Array.from({ length: 6 }).map((_, i) => <BatchCardSkeleton key={i} />)}
        </div>
      </div>
    );
  }

  if (batches.length === 0) return <EmptyState type="noActiveBatches" />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Batch Management</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Manage training batches and seat allocation
        </p>
      </div>

      <div style={{
        padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
        border: '1px solid var(--color-border-default)',
        background: 'var(--color-bg-surface-default)',
      }}>
        <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', marginBottom: 'var(--space-3)' }}>
          <div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Batches</span>
            <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{batches.length}</div>
          </div>
          <div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Capacity</span>
            <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>
              {batches.reduce((s, b) => s + b.capacity, 0)}
            </div>
          </div>
          <div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Filled</span>
            <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>
              {batches.reduce((s, b) => s + b.filledSeats, 0)}
            </div>
          </div>
          <div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Available Seats</span>
            <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>
              {batches.reduce((s, b) => s + b.availableSeats, 0)}
            </div>
          </div>
        </div>
      </div>

      {groupedBatches.map(({ key, items }) => (
        <section key={key} aria-label={`${key} batches`}>
          <h3 style={{
            fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)',
            textTransform: 'capitalize', margin: '0 0 var(--space-2) 0',
          }}>
            {key} Batches ({items.length})
          </h3>
          <div style={{
            display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
            gap: 'var(--space-component-gap)',
          }}>
            {items.map((batch) => (
              <BatchCard key={batch.id} batch={batch} />
            ))}
          </div>
        </section>
      ))}
    </div>
  );
}
