import React from 'react';
import { BreadcrumbItem, type Crumb } from './BreadcrumbItem';

export type { Crumb };

export interface BreadcrumbProps {
  crumbs: Crumb[];
  maxItems?: number;
  collapsedLabel?: string;
  className?: string;
  'aria-label'?: string;
}

const navStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  flexWrap: 'wrap',
  gap: 'var(--space-inline-xs)',
};

const listStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  flexWrap: 'wrap',
  gap: 'var(--space-inline-xs)',
  listStyle: 'none',
  margin: 0,
  padding: 0,
};

const separatorStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-caption)',
  userSelect: 'none',
};

const collapsedStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-caption)',
  cursor: 'pointer',
  border: 'none',
  background: 'none',
  padding: 0,
  fontFamily: 'var(--font-family-sans)',
  position: 'relative',
};

const tooltipStyle: React.CSSProperties = {
  position: 'absolute',
  top: '100%',
  left: '50%',
  transform: 'translateX(-50%)',
  marginTop: 4,
  padding: 'var(--space-stack-xs) var(--space-page-x)',
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-dropdown)',
  boxShadow: 'var(--shadow-lg)',
  zIndex: 'var(--z-dropdown)',
  whiteSpace: 'nowrap',
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-primary)',
};

export const Breadcrumb: React.FC<BreadcrumbProps> = ({
  crumbs,
  maxItems = 0,
  collapsedLabel = '...',
  className = '',
  'aria-label': ariaLabel = 'Breadcrumb',
}) => {
  const [showTooltip, setShowTooltip] = React.useState(false);
  const displayCrumbs = maxItems > 0 && crumbs.length > maxItems
    ? [crumbs[0], ...crumbs.slice(crumbs.length - (maxItems - 1))]
    : crumbs;

  const hiddenCrumbs = maxItems > 0 && crumbs.length > maxItems
    ? crumbs.slice(1, crumbs.length - (maxItems - 1))
    : [];

  const hasCollapse = hiddenCrumbs.length > 0;

  return (
    <nav className={className} aria-label={ariaLabel} style={navStyle}>
      <ol style={listStyle}>
        {displayCrumbs.map((crumb, idx) => {
          const isLast = idx === displayCrumbs.length - 1;
          const isCollapsed = hasCollapse && idx === 1;

          return (
            <React.Fragment key={idx}>
              {idx > 0 && (
                <li style={separatorStyle} aria-hidden="true">
                  <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M4.5 2L8.5 6L4.5 10" stroke="currentColor" strokeWidth="1.2" strokeLinecap="round" strokeLinejoin="round" />
                  </svg>
                </li>
              )}
              {isCollapsed ? (
                <li style={separatorStyle}>
                  <button
                    type="button"
                    style={collapsedStyle}
                    onClick={() => setShowTooltip(!showTooltip)}
                    onBlur={() => setShowTooltip(false)}
                    aria-label={collapsedLabel}
                  >
                    <span>{collapsedLabel}</span>
                    {showTooltip && (
                      <div style={tooltipStyle} role="tooltip">
                        {hiddenCrumbs.map((c, i) => (
                          <div key={i} style={{ padding: '2px 0' }}>{c.label}</div>
                        ))}
                      </div>
                    )}
                  </button>
                </li>
              ) : (
                <BreadcrumbItem crumb={crumb} isLast={isLast} />
              )}
            </React.Fragment>
          );
        })}
      </ol>
    </nav>
  );
};

Breadcrumb.displayName = 'Breadcrumb';

export default Breadcrumb;
