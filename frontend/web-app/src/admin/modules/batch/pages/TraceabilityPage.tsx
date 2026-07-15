import { memo, useState, useMemo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { BatchTimelineComponent } from '../components/BatchTimeline';
import { filterTimeline } from '../utils';

export const TraceabilityPage = memo(function TraceabilityPage() {
  const { timeline } = useBatchData();
  const [query, setQuery] = useState('');
  const filtered = useMemo(() => filterTimeline(timeline, query), [timeline, query]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Traceability" description="End-to-end batch traceability timeline." />
      <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search events by batch code, user, or note..." style={{ padding: '8px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)', width: '100%', maxWidth: 480 }} />
      <BatchTimelineComponent events={filtered} />
    </div>
  );
});

