import React from 'react';

export interface PageFooterProps {
  children?: React.ReactNode;
  className?: string;
}

export const PageFooter: React.FC<PageFooterProps> = ({
  children,
  className = '',
}) => {
  const style: React.CSSProperties = {
    borderTop: '1px solid var(--color-border-default)',
    padding: 'var(--space-stack-md) var(--space-page-x)',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    flexWrap: 'wrap',
    gap: 'var(--space-inline-md)',
    font: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
  };

  return (
    <footer className={`sk-page-footer ${className}`.trim()} style={style}>
      {children}
    </footer>
  );
};

PageFooter.displayName = 'PageFooter';
export default PageFooter;
