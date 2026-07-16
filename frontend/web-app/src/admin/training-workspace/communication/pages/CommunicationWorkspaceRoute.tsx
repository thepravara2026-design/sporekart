import { memo } from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { Icon } from '../../../../design-system/icons/Icon';
import { MOCK_NOTIFICATIONS } from '../data/communicationMockData';

interface CommScope {
  key: string;
  label: string;
  path: string;
  icon: string;
  enabled: boolean;
  badge?: number;
}

const unread = MOCK_NOTIFICATIONS.filter((n) => !n.read).length;

const COMM_SCOPES: CommScope[] = [
  { key: 'overview', label: 'Overview', path: '/admin/training/communication/overview', icon: 'message-square', enabled: true },
  { key: 'announcements', label: 'Announcements', path: '/admin/training/communication/announcements', icon: 'speaker', enabled: true },
  { key: 'notifications', label: 'Notifications', path: '/admin/training/communication/notifications', icon: 'bell', enabled: true, badge: unread },
  { key: 'scheduled', label: 'Scheduled', path: '/admin/training/communication/scheduled', icon: 'clock', enabled: true },
  { key: 'templates', label: 'Templates', path: '/admin/training/communication/templates', icon: 'file', enabled: true },
  { key: 'history', label: 'History', path: '/admin/training/communication/history', icon: 'archive', enabled: true },
  { key: 'delivery', label: 'Delivery Queue', path: '/admin/training/communication/delivery', icon: 'send', enabled: true },
  { key: 'channels', label: 'Future Channels', path: '/admin/training/communication/channels', icon: 'radio', enabled: true },
  { key: 'statistics', label: 'Statistics', path: '/admin/training/communication/statistics', icon: 'bar-chart', enabled: true },
];

const CommunicationWorkspaceRoute = memo(function CommunicationWorkspaceRoute() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)', minWidth: 0 }}>
      <header style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)' }}>
        <Icon name="message-circle" size={20} color="var(--color-primary)" />
        <div>
          <h1 style={{ margin: 0, fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)' }}>
            Communication Center
          </h1>
          <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
            Announcements, notifications, templates &amp; delivery (Mock Mode)
          </p>
        </div>
      </header>

      <nav
        aria-label="Communication sections"
        style={{
          display: 'flex',
          flexWrap: 'wrap',
          gap: 'var(--space-2)',
          borderBottom: '1px solid var(--color-border-default)',
          paddingBottom: 'var(--space-2)',
        }}
      >
        {COMM_SCOPES.map((scope) => (
          <NavLink
            key={scope.key}
            to={scope.path}
            style={({ isActive }) => ({
              display: 'inline-flex',
              alignItems: 'center',
              gap: 6,
              padding: 'var(--space-1) var(--space-3)',
              fontSize: 'var(--text-body-sm)',
              textDecoration: 'none',
              borderRadius: 'var(--radius-md)',
              background: isActive ? 'var(--color-bg-primary-weak)' : 'transparent',
              color: isActive ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontWeight: isActive ? 600 : 400,
            })}
          >
            <Icon name={scope.icon} size={14} color="currentColor" />
            {scope.label}
            {typeof scope.badge === 'number' && scope.badge > 0 && (
              <span
                style={{
                  minWidth: 16, height: 16, padding: '0 4px', borderRadius: 8,
                  background: 'var(--color-danger-600)', color: '#fff',
                  fontSize: 'var(--text-caption)', fontWeight: 600,
                  display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                }}
              >
                {scope.badge > 99 ? '99+' : scope.badge}
              </span>
            )}
          </NavLink>
        ))}
      </nav>

      <Outlet />
    </div>
  );
});

export default CommunicationWorkspaceRoute;
