import { memo, useMemo, useState } from 'react';
import { MOCK_ANNOUNCEMENTS } from '../data/communicationMockData';
import { CATEGORY_LABELS } from '../data/communicationTypes';
import { STATUS_OPTIONS } from '../data/communicationOptions';
import { formatDateTime } from '../data/communicationFormatters';
import { CommEmptyState, CommToolbar, PriorityBadge, StatusBadge } from '../components';

const HISTORY_STATUSES = new Set(['published', 'archived', 'expired']);

const CommunicationHistoryPage = memo(function CommunicationHistoryPage() {
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('');

  const rows = useMemo(() => {
    const q = search.trim().toLowerCase();
    return MOCK_ANNOUNCEMENTS
      .filter((a) => HISTORY_STATUSES.has(a.status))
      .filter((a) => (status ? a.status === status : true))
      .filter((a) => (q ? `${a.title} ${a.author}`.toLowerCase().includes(q) : true))
      .sort((a, b) => ((a.publishedAt ?? a.createdAt) < (b.publishedAt ?? b.createdAt) ? 1 : -1));
  }, [search, status]);

  return (
    <section id="panel-history" aria-labelledby="tab-history" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <CommToolbar
        search={search}
        searchPlaceholder="Search history…"
        onSearchChange={setSearch}
        activeFilterCount={status ? 1 : 0}
        onReset={() => { setSearch(''); setStatus(''); }}
        selects={[{ id: 'status', label: 'Status', value: status, options: STATUS_OPTIONS.filter((o) => HISTORY_STATUSES.has(o.value)), onChange: setStatus }]}
      />

      {rows.length === 0 ? (
        <CommEmptyState icon="archive" title="No history" description="Sent and archived communications will appear here." />
      ) : (
        <div style={{ overflowX: 'auto', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-lg)' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)', minWidth: 640 }}>
            <thead>
              <tr style={{ background: 'var(--color-bg-surface-muted)', textAlign: 'left' }}>
                <th style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>Title</th>
                <th style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>Category</th>
                <th style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>Status</th>
                <th style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>Priority</th>
                <th style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>Author</th>
                <th style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>Date</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((a) => (
                <tr key={a.id} style={{ borderTop: '1px solid var(--color-border-default)' }}>
                  <td style={{ padding: 'var(--space-3)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{a.title}</td>
                  <td style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)' }}>{CATEGORY_LABELS[a.category]}</td>
                  <td style={{ padding: 'var(--space-3)' }}><StatusBadge status={a.status} /></td>
                  <td style={{ padding: 'var(--space-3)' }}><PriorityBadge priority={a.priority} /></td>
                  <td style={{ padding: 'var(--space-3)', color: 'var(--color-text-secondary)' }}>{a.author}</td>
                  <td style={{ padding: 'var(--space-3)', color: 'var(--color-text-muted)', whiteSpace: 'nowrap' }}>{formatDateTime(a.publishedAt ?? a.createdAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
});

export default CommunicationHistoryPage;
