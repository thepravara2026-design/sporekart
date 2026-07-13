import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { PUBLIC_WEBSITE_ROUTES, type PublicRouteDef } from '../config';

function placeholderStyle(): React.CSSProperties {
  return {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'flex-start',
    gap: 'var(--space-3, 12px)',
    padding: 'var(--space-8, 48px)',
    borderRadius: 'var(--radius-lg, 12px)',
    border: '1px dashed var(--color-border-default, #e5e7eb)',
    backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
  };
}

export function PublicRoutePlaceholder({ route }: { route: PublicRouteDef }) {
  const crumbs = route.path === '/' ? [] : [{ label: 'Home', href: '/' }, { label: route.label }];

  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: route.label, description: route.description, canonical: `https://sporekart.example.com${route.path}` }}
    >
      <PublicContentContainer maxWidth="lg">
        <div style={placeholderStyle()}>
          <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 600, letterSpacing: '0.08em', textTransform: 'uppercase', color: 'var(--color-text-accent, #1d4ed8)' }}>
            Coming soon
          </span>
          <h1 style={{ margin: 0, fontSize: 'var(--text-title-xl, 32px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{route.label}</h1>
          <p style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', maxWidth: 640 }}>
            {route.description} This route is scaffolded as part of the public website foundation (Sprint 21, Part 1). Full page content is planned for later sprints.
          </p>
        </div>
      </PublicContentContainer>
    </PublicLayout>
  );
}

export const PUBLIC_ROUTE_PLACEHOLDERS: Record<string, () => JSX.Element> = Object.fromEntries(
  PUBLIC_WEBSITE_ROUTES.map((route) => [route.path, () => <PublicRoutePlaceholder route={route} />]),
);
