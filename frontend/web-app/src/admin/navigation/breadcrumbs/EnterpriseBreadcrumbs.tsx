import { memo } from 'react';
import { useNavigate } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';

interface Crumb {
  label: string;
  href?: string;
  icon?: string;
}

interface EnterpriseBreadcrumbsProps {
  crumbs: Crumb[];
  maxItems?: number;
}

export const EnterpriseBreadcrumbs = memo(function EnterpriseBreadcrumbs({ crumbs, maxItems = 4 }: EnterpriseBreadcrumbsProps) {
  const navigate = useNavigate();

  if (crumbs.length === 0) return null;

  const displayCrumbs = crumbs.length > maxItems
    ? [crumbs[0], { label: '...', href: undefined }, ...crumbs.slice(-(maxItems - 1))]
    : crumbs;

  return (
    <nav aria-label="Breadcrumbs">
      <ol
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 4,
          listStyle: 'none',
          margin: 0,
          padding: 0,
          flexWrap: 'wrap',
        }}
      >
        {displayCrumbs.map((crumb, i) => {
          const isLast = i === displayCrumbs.length - 1;
          return (
            <li key={`${crumb.label}-${i}`} style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
              {i > 0 && (
                <Icon name="chevron-right" size={12} style={{ color: 'var(--color-text-tertiary)' }} />
              )}
              {isLast ? (
                <span
                  aria-current="page"
                  style={{
                    fontSize: 'var(--text-caption)',
                    color: 'var(--color-text-primary)',
                    fontWeight: 600,
                    display: 'flex',
                    alignItems: 'center',
                    gap: 4,
                  }}
                >
                  {crumb.icon && <Icon name={crumb.icon} size={12} />}
                  {crumb.label}
                </span>
              ) : (
                <button
                  onClick={() => crumb.href && navigate(crumb.href)}
                  disabled={!crumb.href}
                  style={{
                    fontSize: 'var(--text-caption)',
                    color: crumb.href ? 'var(--color-text-secondary)' : 'var(--color-text-tertiary)',
                    background: 'none',
                    border: 'none',
                    cursor: crumb.href ? 'pointer' : 'default',
                    padding: 0,
                    display: 'flex',
                    alignItems: 'center',
                    gap: 4,
                  }}
                >
                  {crumb.icon && <Icon name={crumb.icon} size={12} />}
                  {crumb.label}
                </button>
              )}
            </li>
          );
        })}
      </ol>
    </nav>
  );
});
