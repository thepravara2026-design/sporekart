import { Button } from '../../../design-system/components/core/Button';

interface PlaceholderPageProps {
  title: string;
  description: string;
  icon: React.ReactNode;
  primaryAction?: { label: string; href: string };
  secondaryActions?: { label: string; href: string }[];
}

export default function PlaceholderPage({ title, description, icon, primaryAction, secondaryActions = [] }: PlaceholderPageProps) {
  return (
    <div className="cw-empty">
      <div className="cw-empty__icon" aria-hidden="true">
        {icon}
      </div>
      <h1 className="cw-empty__title">{title}</h1>
      <p className="cw-empty__desc">{description}</p>
      {primaryAction && (
        <Button size="lg" onClick={() => window.location.href = primaryAction.href}>
          {primaryAction.label}
        </Button>
      )}
      {secondaryActions.length > 0 && (
        <div style={{ display: 'flex', gap: 'var(--space-inline-sm)', flexWrap: 'wrap', justifyContent: 'center', marginTop: 'var(--space-stack-md)' }}>
          {secondaryActions.map((a, i) => (
            <Button key={i} variant="secondary" size="md" onClick={() => window.location.href = a.href}>
              {a.label}
            </Button>
          ))}
        </div>
      )}
    </div>
  );
}