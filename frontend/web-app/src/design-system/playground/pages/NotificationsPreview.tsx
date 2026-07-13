import React, { useState } from 'react';
import { NotificationBadge, NotificationCenter, NotificationGroup, NotificationItem, NotificationCategory, NotificationPriority, NotificationEmpty } from '../../components/feedback';
import type { NotificationItemData } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

const btnStyle: React.CSSProperties = { background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', padding: '8px 16px', fontSize: 'var(--text-button)', borderRadius: 'var(--radius-btn)', cursor: 'pointer', fontWeight: 500 };

const sampleNotifications: NotificationItemData[] = [
  { id: '1', type: 'success', title: 'Deployment completed', message: 'v2.4.1 has been deployed to production.', timestamp: new Date(Date.now() - 300000), read: false, category: 'deployments', priority: 'normal' },
  { id: '2', type: 'error', title: 'Build failed', message: 'Pipeline #1423 failed on the test stage.', timestamp: new Date(Date.now() - 1800000), read: false, category: 'builds', priority: 'urgent' },
  { id: '3', type: 'warning', title: 'Disk space warning', message: 'Server disk usage is at 85%.', timestamp: new Date(Date.now() - 7200000), read: false, category: 'alerts', priority: 'high' },
  { id: '4', type: 'info', title: 'New team member', message: 'Sarah has joined your project.', timestamp: new Date(Date.now() - 14400000), read: true, category: 'team', priority: 'low' },
  { id: '5', type: 'success', title: 'Backup completed', message: 'Daily backup completed successfully.', timestamp: new Date(Date.now() - 86400000), read: true, category: 'system', priority: 'normal' },
  { id: '6', type: 'info', title: 'Scheduled maintenance', message: 'System maintenance scheduled for Sunday 2 AM.', timestamp: new Date(Date.now() - 172800000), read: true, category: 'alerts', priority: 'normal' },
];

const categories = [
  { id: 'deployments', label: 'Deployments' },
  { id: 'builds', label: 'Builds' },
  { id: 'alerts', label: 'Alerts' },
  { id: 'team', label: 'Team' },
  { id: 'system', label: 'System' },
];

export default function NotificationsPreview() {
  const [notifOpen, setNotifOpen] = useState(false);
  const [activeCategory, setActiveCategory] = useState('all');
  const [notifications, setNotifications] = useState<NotificationItemData[]>(sampleNotifications);
  const [badgeCount, setBadgeCount] = useState(3);

  const handleMarkRead = (id: string) => {
    setNotifications((prev) => prev.map((n) => n.id === id ? { ...n, read: true } : n));
    setBadgeCount((prev) => Math.max(0, prev - 1));
  };

  const handleMarkAllRead = () => {
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
    setBadgeCount(0);
  };

  const handleClear = () => {};

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Notifications</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Notification center, badges, and grouping</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Notification Badge</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Badge with Count">
            <NotificationBadge count={badgeCount}>
              <button style={btnStyle} onClick={() => setNotifOpen(true)}>Inbox</button>
            </NotificationBadge>
          </StateCard>
          <StateCard label="Dot Badge">
            <NotificationBadge dot count={1}>
              <button style={btnStyle}>Alerts</button>
            </NotificationBadge>
          </StateCard>
          <StateCard label="Standalone Badge">
            <NotificationBadge count={badgeCount} />
          </StateCard>
          <StateCard label="Standalone Dot">
            <NotificationBadge dot count={1} />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Priority Indicators</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Priority Levels">
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: 'var(--text-caption)' }}><NotificationPriority priority="low" /> Low</span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: 'var(--text-caption)' }}><NotificationPriority priority="normal" /> Normal</span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: 'var(--text-caption)' }}><NotificationPriority priority="high" /> High</span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px', fontSize: 'var(--text-caption)' }}><NotificationPriority priority="urgent" /> Urgent</span>
            </div>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Notification Center</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Toggle Notification Center">
            <button style={btnStyle} onClick={() => setNotifOpen((p) => !p)}>
              {notifOpen ? 'Close Center' : 'Open Center'}
            </button>
            <NotificationCenter
              open={notifOpen}
              onClose={() => setNotifOpen(false)}
              notifications={notifications}
              onMarkRead={handleMarkRead}
              onMarkAllRead={handleMarkAllRead}
              onClear={handleClear}
              categories={categories.map((c) => ({ id: c.id, label: c.label }))}
              activeCategory={activeCategory}
              onCategoryChange={setActiveCategory}
            />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Grouped & Category Filters</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Category Filter Chips">
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
              <NotificationCategory label="All" count={notifications.length} active={activeCategory === 'all'} onClick={() => setActiveCategory('all')} />
              {categories.map((cat) => (
                <NotificationCategory key={cat.id} label={cat.label} count={notifications.filter((n) => n.category === cat.id).length} active={activeCategory === cat.id} onClick={() => setActiveCategory(cat.id)} />
              ))}
            </div>
          </StateCard>
          <StateCard label="Read/Unread States">
            <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', width: '100%' }}>
              <NotificationItem notification={{ ...sampleNotifications[0], read: false }} onMarkRead={handleMarkRead} />
              <NotificationItem notification={{ ...sampleNotifications[3], read: true }} />
            </div>
          </StateCard>
          <StateCard label="Empty State">
            <NotificationEmpty message="No notifications to show" action={{ label: 'Clear filters', onClick: () => setActiveCategory('all') }} />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Notification Group</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Group Example">
            <NotificationGroup label="Today" count={3}>
              {notifications.slice(0, 3).map((n) => (
                <NotificationItem key={n.id} notification={n} onMarkRead={handleMarkRead} />
              ))}
            </NotificationGroup>
          </StateCard>
        </div>
      </section>
    </div>
  );
}
