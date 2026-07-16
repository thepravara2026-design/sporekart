import { useMemo, useState } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MessageCard } from '../components/MessageCard';
import { MetricCard } from '../components/MetricCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';

export default function NotificationCenter() {
  const { messages, getFilteredMessages, page, pageSize, setPage, setPageSize } = useCommunication();
  const filtered = useMemo(() => getFilteredMessages(), [getFilteredMessages]);
  const sent = messages.filter((m) => m.deliveryStatus === 'sent' || m.deliveryStatus === 'delivered').length;
  const delivered = messages.filter((m) => m.deliveryStatus === 'delivered').length;
  const read = messages.filter((m) => m.readStatus === 'read').length;

  const segments = [
    { key: 'all', label: 'All', count: filtered.length },
    { key: 'unread', label: 'Unread', count: filtered.filter((m) => m.readStatus !== 'read').length },
    { key: 'read', label: 'Read', count: filtered.filter((m) => m.readStatus === 'read').length },
    { key: 'archived', label: 'Archived', count: filtered.filter((m) => m.deliveryStatus === 'archived').length },
  ];
  const [activeSegment, setActiveSegment] = useState('all');

  const segmentItems = useMemo(() => {
    let items = filtered;
    if (activeSegment === 'unread') items = items.filter((m) => m.readStatus !== 'read');
    if (activeSegment === 'read') items = items.filter((m) => m.readStatus === 'read');
    if (activeSegment === 'archived') items = items.filter((m) => m.deliveryStatus === 'archived');
    return items.slice((page - 1) * pageSize, page * pageSize);
  }, [filtered, activeSegment, page, pageSize]);

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Notification Center</h1>
        <SharedFilters currentPage="communication/notifications" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total" value={messages.length} icon="📨" />
        <MetricCard label="Sent" value={sent} icon="📤" color="#2563eb" />
        <MetricCard label="Delivered" value={delivered} icon="✅" color="#16a34a" />
        <MetricCard label="Read" value={read} icon="👁️" color="#8b5cf6" />
        <MetricCard label="Unread" value={messages.length - read} icon="🔴" color="#dc2626" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {segments.map((seg) => (
          <button key={seg.key} onClick={() => { setActiveSegment(seg.key); setPage(1); }} style={{
            padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
            background: activeSegment === seg.key ? 'var(--color-bg-primary-subtle)' : 'transparent',
            color: activeSegment === seg.key ? 'var(--color-primary)' : 'var(--color-text-secondary)',
            fontWeight: activeSegment === seg.key ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            fontSize: 'var(--text-body-sm)',
          }}>{seg.label} ({seg.count})</button>
        ))}
      </div>

      <DashboardWidget title="Notifications" subtitle={`${filtered.length} total`}>
        {segmentItems.length === 0 ? <EmptyState type="noNotifications" /> : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 8 }}>
              {segmentItems.map((m) => <MessageCard key={m.id} message={m} />)}
            </div>
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
