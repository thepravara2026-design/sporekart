import { memo, useState, useMemo } from 'react';
import { useBatchData } from '../../hooks/useBatchData';
import { BatchTimelineComponent } from '../../components/BatchTimeline';
import { filterTimeline } from '../../utils';

export const PreviewTimelinePage = memo(function PreviewTimelinePage() {
  const { timeline } = useBatchData();
  const [query, setQuery] = useState('');
  const filtered = useMemo(() => filterTimeline(timeline, query), [timeline, query]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Timeline</h2>
      <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search timeline events..." style={{ padding: '8px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)', width: '100%', maxWidth: 480 }} />
      <BatchTimelineComponent events={filtered} />
    </div>
  );
});
