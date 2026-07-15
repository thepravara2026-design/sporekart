import React, { useState } from 'react';
import { NOTIFICATIONS, EngagementNotification } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const NotificationsPage: React.FC = () => {
  const [notifications, setNotifications] = useState<EngagementNotification[]>(NOTIFICATIONS);
  const [activeFilter, setActiveFilter] = useState<'all' | 'unread' | 'order' | 'training' | 'promo'>('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const handleToggleRead = (id: string) => {
    setNotifications(prev =>
      prev.map(n => (n.id === id ? { ...n, read: !n.read } : n))
    );
  };

  const handleMarkAllRead = () => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })));
    setToastMessage('All notifications marked as read.');
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handleDelete = (id: string, e: React.MouseEvent) => {
    e.stopPropagation();
    setNotifications(prev => prev.filter(n => n.id !== id));
  };

  const getIconForType = (type: string) => {
    switch (type) {
      case 'order': return { symbol: '📦', color: '#3b82f6' };
      case 'training': return { symbol: '🎓', color: '#10b981' };
      case 'price': return { symbol: '🏷️', color: '#f59e0b' };
      case 'promo': return { symbol: '✨', color: '#ec4899' };
      case 'system': return { symbol: '⚙️', color: '#6b7280' };
      default: return { symbol: '🔔', color: 'var(--color-primary)' };
    }
  };

  // Filter & Search logic
  const filteredNotifications = notifications.filter(n => {
    const matchesSearch = n.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                          n.description.toLowerCase().includes(searchQuery.toLowerCase());
    
    if (!matchesSearch) return false;
    
    if (activeFilter === 'unread') return !n.read;
    if (activeFilter === 'order') return n.type === 'order';
    if (activeFilter === 'training') return n.type === 'training';
    if (activeFilter === 'promo') return n.type === 'promo' || n.type === 'price';
    return true;
  });

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="info" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Header and top buttons */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
            Notification Center
          </h2>
          <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
            You have <strong>{unreadCount} unread</strong> notifications.
          </p>
        </div>

        {unreadCount > 0 && (
          <button
            type="button"
            className="cw-btn cw-btn--outlined cw-btn--sm"
            onClick={handleMarkAllRead}
          >
            Mark all read
          </button>
        )}
      </div>

      {/* Filter tab row & Search input */}
      <div 
        style={{ 
          display: 'flex', 
          justifyContent: 'space-between', 
          alignItems: 'center', 
          borderBottom: '1px solid var(--color-border-default)',
          paddingBottom: '12px',
          flexWrap: 'wrap',
          gap: '16px'
        }}
      >
        <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
          {[
            { id: 'all', label: 'All' },
            { id: 'unread', label: `Unread (${unreadCount})` },
            { id: 'order', label: 'Orders' },
            { id: 'training', label: 'Training' },
            { id: 'promo', label: 'Price drop & Promos' },
          ].map((tab) => (
            <button
              key={tab.id}
              type="button"
              onClick={() => setActiveFilter(tab.id as any)}
              style={{
                border: 'none',
                background: activeFilter === tab.id ? 'var(--color-bg-primary-weak)' : 'transparent',
                padding: '6px 12px',
                borderRadius: 'var(--radius-sm)',
                fontSize: 'var(--text-body-sm)',
                fontWeight: activeFilter === tab.id ? 'var(--weight-bold)' : 'var(--weight-medium)',
                color: activeFilter === tab.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                cursor: 'pointer',
                transition: 'all 0.1s ease',
              }}
            >
              {tab.label}
            </button>
          ))}
        </div>

        <div style={{ position: 'relative', width: '100%', maxWidth: '240px' }}>
          <span style={{ position: 'absolute', left: '10px', top: '10px', color: 'var(--color-text-secondary)', display: 'flex' }}>
            <Icon name="search" size={14} color="currentColor" />
          </span>
          <input
            type="text"
            placeholder="Search alerts..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{
              width: '100%',
              padding: '6px 12px 6px 32px',
              border: '1px solid var(--color-border-default)',
              borderRadius: 'var(--radius-md)',
              fontSize: 'var(--text-body-sm)',
              outline: 'none',
              background: 'var(--color-bg-surface-default)',
              color: 'var(--color-text-primary)'
            }}
          />
        </div>
      </div>

      {/* Notifications list */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        {filteredNotifications.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
            <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>🔔</div>
            <div>
              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>No notifications found</h3>
              <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>You are completely up-to-date.</p>
            </div>
          </div>
        ) : (
          filteredNotifications.map((notif) => {
            const meta = getIconForType(notif.type);
            return (
              <Card
                key={notif.id}
                variant="outlined"
                padding="md"
                onClick={() => handleToggleRead(notif.id)}
                style={{
                  display: 'flex',
                  gap: '16px',
                  alignItems: 'center',
                  cursor: 'pointer',
                  borderLeft: notif.read ? '1px solid var(--color-border-default)' : '4px solid var(--color-bg-primary-default)',
                  background: notif.read ? 'transparent' : 'var(--color-bg-surface-secondary, #fcfcfc)',
                  transition: 'all 0.15s ease',
                  position: 'relative',
                }}
              >
                {/* Icon Circle */}
                <div 
                  style={{ 
                    width: '40px', 
                    height: '40px', 
                    borderRadius: '50%', 
                    background: notif.read ? '#f1f5f9' : 'var(--color-bg-primary-weak)', 
                    display: 'flex', 
                    alignItems: 'center', 
                    justifyContent: 'center', 
                    fontSize: '20px',
                    flexShrink: 0
                  }}
                >
                  {meta.symbol}
                </div>

                {/* Text Content */}
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline', gap: '8px' }}>
                    <h4 style={{ 
                      fontSize: 'var(--text-body-sm)', 
                      fontWeight: notif.read ? 'var(--weight-semibold)' : 'var(--weight-bold)', 
                      color: 'var(--color-text-primary)', 
                      margin: 0,
                      textOverflow: 'ellipsis',
                      overflow: 'hidden',
                      whiteSpace: 'nowrap'
                    }}>
                      {notif.title}
                    </h4>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', flexShrink: 0 }}>
                      {notif.timestamp}
                    </span>
                  </div>
                  <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '4px 0 0', lineHeight: '1.4' }}>
                    {notif.description}
                  </p>
                </div>

                {/* Actions */}
                <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                  {!notif.read && (
                    <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: 'var(--color-bg-primary-default)' }} />
                  )}
                  <button
                    type="button"
                    aria-label="Dismiss alert"
                    onClick={(e) => handleDelete(notif.id, e)}
                    style={{
                      border: 'none',
                      background: 'none',
                      cursor: 'pointer',
                      color: 'var(--color-text-secondary)',
                      padding: '4px',
                      display: 'flex',
                      alignItems: 'center',
                    }}
                  >
                    <Icon name="x" size={14} color="currentColor" />
                  </button>
                </div>
              </Card>
            );
          })
        )}
      </div>
    </div>
  );
};
export default NotificationsPage;
