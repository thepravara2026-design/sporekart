import React from 'react';

export interface PageContainerProps {
  children: React.ReactNode;
  className?: string;
}

export const PageContainer: React.FC<PageContainerProps> = ({
  children,
  className = '',
}) => {
  const style: React.CSSProperties = {
    padding: 'var(--space-page-y) var(--space-page-x)',
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-section-gap)',
  };

  return (
    <div className={`sk-page-container ${className}`.trim()} style={style}>
      {children}
    </div>
  );
};

PageContainer.displayName = 'PageContainer';
export default PageContainer;
