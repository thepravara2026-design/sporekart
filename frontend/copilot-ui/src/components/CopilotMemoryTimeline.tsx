import { useState, useMemo } from 'react';
import type { MemoryEntry } from '../types';

interface CopilotMemoryTimelineProps {
  memories: MemoryEntry[];
}

const memoryTypeLabels: Record<MemoryEntry['type'], string> = {
  action: 'Action',
  observation: 'Observation',
  decision: 'Decision',
  context: 'Context',
};

export function CopilotMemoryTimeline({ memories }: CopilotMemoryTimelineProps) {
  const [filter, setFilter] = useState<MemoryEntry['type'] | 'all'>('all');
  const [search, setSearch] = useState('');

  const filtered = useMemo(() => {
    let result = memories;
    if (filter !== 'all') result = result.filter((m) => m.type === filter);
    if (search.trim()) {
      const q = search.toLowerCase();
      result = result.filter((m) => m.summary.toLowerCase().includes(q));
    }
    return result;
  }, [memories, filter, search]);

  const formatTime = (ts: string) => {
    try {
      const d = new Date(ts);
      return d.toLocaleString(undefined, { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    } catch {
      return ts;
    }
  };

  return (
    <div className="cp-memory" role="region" aria-label="Memory timeline">
      <div className="cp-memory__controls">
        <input
          type="search"
          className="cp-memory__search"
          placeholder="Search memories..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          aria-label="Search memories"
        />
        <select
          className="cp-memory__filter"
          value={filter}
          onChange={(e) => setFilter(e.target.value as MemoryEntry['type'] | 'all')}
          aria-label="Filter by memory type"
        >
          <option value="all">All</option>
          {(Object.keys(memoryTypeLabels) as MemoryEntry['type'][]).map((t) => (
            <option key={t} value={t}>{memoryTypeLabels[t]}</option>
          ))}
        </select>
      </div>
      <div className="cp-memory__list" role="list">
        {filtered.length === 0 && (
          <div className="cp-memory__empty">No memories found</div>
        )}
        {filtered.map((m) => (
          <div key={m.id} className={`cp-memory__entry cp-memory__entry--${m.type}`} role="listitem">
            <div className="cp-memory__entry-header">
              <span className={`cp-memory__type-badge cp-memory__type-badge--${m.type}`}>
                {memoryTypeLabels[m.type]}
              </span>
              <span className="cp-memory__time">{formatTime(m.timestamp)}</span>
            </div>
            <div className="cp-memory__summary">{m.summary}</div>
            {m.detail && <div className="cp-memory__detail">{m.detail}</div>}
          </div>
        ))}
      </div>
    </div>
  );
}
