import { memo } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { buildTrainingBreadcrumbs } from '../../data/navigation';

export const WorkspaceBreadcrumb = memo(function WorkspaceBreadcrumb() {
  const location = useLocation();
  const navigate = useNavigate();
  const crumbs = buildTrainingBreadcrumbs(location.pathname);

  return (
    <nav aria-label="Training breadcrumb" style={{
      display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
      fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
      padding: '0 0 var(--space-stack-sm)',
      flexWrap: 'wrap',
    }}>
      {crumbs.map((crumb, i) => (
        <span key={i} style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
          {i > 0 && (
            <span style={{ color: 'var(--color-text-tertiary)', fontSize: 10 }} aria-hidden="true">
              /
            </span>
          )}
          {crumb.href ? (
            <button
              type="button"
              onClick={() => navigate(crumb.href!)}
              style={{
                background: 'none', border: 'none', cursor: 'pointer',
                color: i === crumbs.length - 1 ? 'var(--color-text-primary)' : 'var(--color-text-secondary)',
                fontWeight: i === crumbs.length - 1 ? 'var(--weight-medium)' : 'var(--weight-normal)',
                padding: '2px 4px', borderRadius: 'var(--radius-xs)',
                fontSize: 'inherit',
              }}
              aria-current={i === crumbs.length - 1 ? 'page' : undefined}
            >
              {crumb.label}
            </button>
          ) : (
            <span style={{
              color: 'var(--color-text-primary)',
              fontWeight: 'var(--weight-medium)',
            }}>
              {crumb.label}
            </span>
          )}
        </span>
      ))}
    </nav>
  );
});
