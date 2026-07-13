import { Link as RouterLink } from 'react-router-dom';
import { Icon } from '../design-system/icons/Icon';

export interface BreadcrumbFoundationProps {
  items: { label: string; href?: string }[];
}

export function BreadcrumbFoundation({ items }: BreadcrumbFoundationProps) {
  if (!items.length) {
    return null;
  }

  const style: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    flexWrap: 'wrap',
    gap: 'var(--space-1, 4px)',
    fontFamily: 'var(--font-family-sans, system-ui)',
    fontSize: 'var(--text-body-sm, 14px)',
    color: 'var(--color-text-secondary, #4b5563)',
    padding: 'var(--space-3, 12px) 0',
  };

  return (
    <nav className="sk-public-breadcrumb" aria-label="Breadcrumb" style={{ padding: '0 var(--space-5, 24px)', maxWidth: 'var(--container-xl, 1200px)', margin: '0 auto', width: '100%' }}>
      <ol style={{ ...style, listStyle: 'none', margin: 0, padding: 'var(--space-3, 12px) 0' }}>
        {items.map((item, index) => {
          const isLast = index === items.length - 1;
          return (
            <li key={`${item.label}-${index}`} style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1, 4px)' }}>
              {item.href && !isLast ? (
                <RouterLink to={item.href} style={{ color: 'var(--color-text-accent, #1d4ed8)', textDecoration: 'none' }}>
                  {item.label}
                </RouterLink>
              ) : (
                <span style={{ color: 'var(--color-text-primary, #1f2933)', fontWeight: 500 }} aria-current={isLast ? 'page' : undefined}>
                  {item.label}
                </span>
              )}
              {!isLast && <Icon name="chevron-right" size={14} aria-label="Next" color="var(--color-text-muted, #9ca3af)" />}
            </li>
          );
        })}
      </ol>
    </nav>
  );
}
