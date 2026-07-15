import { Card } from '../../design-system/components/composite/Card';

export default function AdminProfile() {
  return (
    <div>
      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 2fr',
        gap: 'var(--space-component-gap)',
      }}>
        <Card variant="elevated" padding="lg">
          <div style={{
            width: 80, height: 80, borderRadius: 'var(--radius-full)',
            background: 'var(--color-bg-surface-raised)',
            margin: '0 auto var(--space-stack-md)', display: 'flex',
            alignItems: 'center', justifyContent: 'center',
            fontSize: 32, color: 'var(--color-text-secondary)',
          }}>
            ?
          </div>
          <h3 style={{ textAlign: 'center', margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>
            Admin User
          </h3>
          <p style={{ textAlign: 'center', margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            administrator@sporekart.com
          </p>
        </Card>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)' }}>Profile Settings</h3>
          <p style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            Profile management will be implemented in a future sprint. This section will include personal information,
            password management, notification preferences, and session management.
          </p>
        </Card>
      </div>
    </div>
  );
}
