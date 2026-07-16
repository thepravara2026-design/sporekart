import { useMemo, useState } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { InboxItemCard } from '../components/InboxItemCard';
import { MetricCard } from '../components/MetricCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';

export default function StudentInbox() {
  const { inbox, getFilteredInbox, toggleStar, toggleRead, page, pageSize, setPage, setPageSize } = useCommunication();
  const filtered = useMemo(() => getFilteredInbox(), [getFilteredInbox]);

  const unread = inbox.filter((i) => !i.isRead).length;
  const starred = inbox.filter((i) => i.isStarred).length;
  const important = inbox.filter((i) => i.isImportant).length;

  const tabs = [
    { key: 'all', label: 'All', count: filtered.length },
    { key: 'unread', label: 'Unread', count: filtered.filter((i) => !i.isRead).length },
    { key: 'important', label: 'Important', count: filtered.filter((i) => i.isImportant).length },
    { key: 'starred', label: 'Starred', count: filtered.filter((i) => i.isStarred).length },
  ];
  const [activeTab, setActiveTab] = useState('all');

  const displayItems = useMemo(() => {
    let items = filtered;
    if (activeTab === 'unread') items = items.filter((i) => !i.isRead);
    if (activeTab === 'important') items = items.filter((i) => i.isImportant);
    if (activeTab === 'starred') items = items.filter((i) => i.isStarred);
    return items.slice((page - 1) * pageSize, page * pageSize);
  }, [filtered, activeTab, page, pageSize]);

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Student Inbox</h1>
        <SharedFilters currentPage="communication/inbox" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Messages" value={inbox.length} icon="📥" />
        <MetricCard label="Unread" value={unread} icon="🔴" color="#dc2626" />
        <MetricCard label="Starred" value={starred} icon="⭐" color="#f59e0b" />
        <MetricCard label="Important" value={important} icon="❗" color="#2563eb" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.map((tab) => (
          <button key={tab.key} onClick={() => { setActiveTab(tab.key); setPage(1); }} style={{
            padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
            background: activeTab === tab.key ? 'var(--color-bg-primary-subtle)' : 'transparent',
            color: activeTab === tab.key ? 'var(--color-primary)' : 'var(--color-text-secondary)',
            fontWeight: activeTab === tab.key ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            fontSize: 'var(--text-body-sm)',
          }}>
            {tab.label} ({tab.count})
          </button>
        ))}
      </div>

      <DashboardWidget title="Messages" subtitle={`${displayItems.length} shown`}>
        {displayItems.length === 0 ? <EmptyState type="noMessages" /> : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayItems.map((item) => (
              <InboxItemCard key={item.id} item={item} onToggleRead={() => toggleRead(item.id)} onToggleStar={() => toggleStar(item.id)} />
            ))}
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
