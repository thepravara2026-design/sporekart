import { Card } from '../../design-system/components/composite/Card';

export default function AdminWorkspace() {
  return (
    <div>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Workspace Overview</h3>
          <p style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            Your enterprise workspace. Module configuration and workspace settings will be available here in a future sprint.
          </p>
        </Card>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Pinned Items</h3>
          <p style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            Pin frequently accessed pages for quick access. Pin support coming soon.
          </p>
        </Card>
        <Card variant="elevated" padding="lg">
          <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>Recent Pages</h3>
          <p style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            Your recently visited pages will be listed here.
          </p>
        </Card>
      </div>
    </div>
  );
}
