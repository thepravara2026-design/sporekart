import { useMemo } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MessageCard } from '../components/MessageCard';
import { MetricCard } from '../components/MetricCard';
import { EmptyState } from '../components/EmptyStates';

export default function NotificationCenter() {
  const { messages, getFilteredMessages } = useCommunication();
  const filtered = useMemo(() => getFilteredMessages(), [getFilteredMessages]);
  const sent = messages.filter((m) => m.deliveryStatus === 'sent' || m.deliveryStatus === 'delivered').length;
  const delivered = messages.filter((m) => m.deliveryStatus === 'delivered').length;
  const read = messages.filter((m) => m.readStatus === 'read').length;

  const clearAll = () => {};

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Notification Center</h1>
        <SharedFilters currentPage="communication/notifications" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total" value={messages.length} icon="📨" />
        <MetricCard label="Sent" value={sent} icon="📤" color="#2563eb" />
        <MetricCard label="Delivered" value={delivered} icon="✅" color="#16a34a" />
        <MetricCard label="Read" value={read} icon="👁️" color="#8b5cf6" />
        <MetricCard label="Unread" value={messages.length - read} icon="🔴" color="#dc2626" />
      </div>

      <DashboardWidget title="All Notifications" subtitle={`${filtered.length} of ${messages.length} messages`} actions={
        <button onClick={clearAll} style={{ padding: '4px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}>Clear Filters</button>
      }>
        {filtered.length === 0 ? <EmptyState type="noNotifications" /> : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 8 }}>
            {filtered.map((m) => <MessageCard key={m.id} message={m} />)}
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
