import { useApp } from '../../context';
import { Card } from '../../design-system/components/composite/Card';

const WELCOME = {
  title: 'Welcome to the Enterprise Admin Platform',
  description:
    'This is your central workspace for managing SporeKart. Business modules will be implemented in upcoming sprints.',
};

const WIDGETS = [
  { id: 'users', title: 'Users', description: 'User management will appear here.', color: '#3b82f6' },
  { id: 'content', title: 'Content', description: 'Content moderation will appear here.', color: '#10b981' },
  { id: 'analytics', title: 'Analytics', description: 'Analytics dashboard will appear here.', color: '#f59e0b' },
  { id: 'system', title: 'System', description: 'System health will appear here.', color: '#8b5cf6' },
];

const QUICK_ACTIONS = [
  { label: 'Create User', description: 'Add a new user account' },
  { label: 'View Reports', description: 'Access platform reports' },
  { label: 'System Check', description: 'Run health diagnostics' },
  { label: 'Help Center', description: 'Browse admin documentation' },
];

export default function AdminDashboard() {
  const { activeRole } = useApp();

  return (
    <div>
      <div className="admin-dashboard__welcome" style={{
        background: 'linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark, #1a5c3a) 100%)',
        borderRadius: 'var(--radius-card)',
        padding: 'var(--space-stack-lg) var(--space-page-x)',
        marginBottom: 'var(--space-section-gap)',
        color: 'var(--color-text-on-primary)',
      }}>
        <h2 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h2)' }}>{WELCOME.title}</h2>
        <p style={{ margin: 0, opacity: 0.85, fontSize: 'var(--text-body)' }}>{WELCOME.description}</p>
      </div>

      <div style={{
        display: 'grid', gridTemplateColumns: '1fr 1fr',
        gap: 'var(--space-component-gap)',
        marginBottom: 'var(--space-section-gap)',
      }}>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Workspace Summary</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>Active Role</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{activeRole}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>Environment</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>Development</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>Version</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>1.0.0</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>Modules</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>6 layout routes</span>
            </div>
          </div>
        </Card>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Pinned Widgets</h3>
          <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            <p>Pin widgets to this area for quick access. Use the pin icon on any widget to add it here.</p>
          </div>
        </Card>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))',
        gap: 'var(--space-component-gap)',
        marginBottom: 'var(--space-section-gap)',
      }}>
        {WIDGETS.map((w) => (
          <Card key={w.id} variant="elevated" padding="lg">
            <div style={{
              width: 40, height: 40, borderRadius: 'var(--radius-md)',
              background: w.color, opacity: 0.15, marginBottom: 'var(--space-stack-sm)',
            }} />
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>{w.title}</h3>
            <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
              {w.description}
            </p>
          </Card>
        ))}
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
        gap: 'var(--space-component-gap)',
        marginBottom: 'var(--space-section-gap)',
      }}>
        <div style={{ gridColumn: '1 / -1' }}>
          <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h3)' }}>Quick Actions</h3>
        </div>
        {QUICK_ACTIONS.map((a) => (
          <Card key={a.label} variant="outlined" padding="md" hoverable>
            <h4 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-body-lg)' }}>{a.label}</h4>
            <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
              {a.description}
            </p>
          </Card>
        ))}
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: 'var(--space-component-gap)',
      }}>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)' }}>Recent Activity</h3>
          <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            <p>No recent activity to display.</p>
          </div>
        </Card>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)' }}>Announcements</h3>
          <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            <p>No announcements at this time.</p>
          </div>
        </Card>
      </div>
    </div>
  );
}
