import { Card } from '../../design-system/components/composite/Card';

const SETTINGS_GROUPS = [
  { title: 'General', description: 'Platform name, timezone, locale, and branding settings.' },
  { title: 'Security', description: 'Password policy, session timeout, MFA configuration, and IP allowlisting.' },
  { title: 'Notifications', description: 'Email, SMS, and in-app notification channel configuration.' },
  { title: 'Integrations', description: 'API keys, webhooks, and third-party service connections.' },
];

export default function AdminSettings() {
  return (
    <div>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}>
        {SETTINGS_GROUPS.map((g) => (
          <Card key={g.title} variant="elevated" padding="lg">
            <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>{g.title}</h3>
            <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
              {g.description}
            </p>
          </Card>
        ))}
      </div>
    </div>
  );
}
