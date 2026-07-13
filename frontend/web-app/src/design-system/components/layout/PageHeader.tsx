import React from 'react';

export interface PageHeaderProps {
  title: string;
  description?: string;
  actions?: React.ReactNode;
  className?: string;
}

export const PageHeader: React.FC<PageHeaderProps> = ({
  title,
  description,
  actions,
  className = '',
}) => {
  const wrapperStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    gap: 'var(--space-inline-md)',
    flexWrap: 'wrap',
  };

  const textGroupStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
  };

  const titleStyle: React.CSSProperties = {
    font: 'var(--text-h1)',
    fontWeight: 'var(--weight-bold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const descStyle: React.CSSProperties = {
    font: 'var(--text-body)',
    color: 'var(--color-text-secondary)',
    margin: 0,
  };

  return (
    <div className={`sk-page-header ${className}`.trim()} style={wrapperStyle}>
      <div className="sk-page-header__text-group" style={textGroupStyle}>
        <h1 className="sk-page-header__title" style={titleStyle}>{title}</h1>
        {description && <p className="sk-page-header__desc" style={descStyle}>{description}</p>}
      </div>
      {actions && (
        <div className="sk-page-header__actions" style={{ display: 'flex', gap: 'var(--space-inline-sm)', alignItems: 'center', flexShrink: 0 }}>
          {actions}
        </div>
      )}
    </div>
  );
};

PageHeader.displayName = 'PageHeader';
export default PageHeader;
