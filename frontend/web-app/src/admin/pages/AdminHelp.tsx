import { Card } from '../../design-system/components/composite/Card';

const HELP_SECTIONS = [
  { title: 'Getting Started', description: 'Learn the basics of the Enterprise Admin Platform.' },
  { title: 'User Management', description: 'How to create, edit, and manage user accounts.' },
  { title: 'Content Moderation', description: 'Moderate and manage platform content.' },
  { title: 'Configuration', description: 'Platform configuration and environment settings.' },
  { title: 'Monitoring', description: 'System monitoring, alerts, and health checks.' },
  { title: 'FAQ', description: 'Frequently asked questions about the admin platform.' },
];

export default function AdminHelp() {
  return (
    <div>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}>
        {HELP_SECTIONS.map((h) => (
          <Card key={h.title} variant="outlined" padding="lg" hoverable>
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>{h.title}</h3>
            <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
              {h.description}
            </p>
          </Card>
        ))}
      </div>
    </div>
  );
}
