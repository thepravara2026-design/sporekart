import React from 'react';

export interface PageToolbarProps {
  children: React.ReactNode;
  className?: string;
}

export const PageToolbar: React.FC<PageToolbarProps> = ({
  children,
  className = '',
}) => {
  const style: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-sm)',
    alignItems: 'center',
    flexWrap: 'wrap',
  };

  return (
    <div className={`sk-page-toolbar ${className}`.trim()} style={style}>
      {children}
    </div>
  );
};

PageToolbar.displayName = 'PageToolbar';
export default PageToolbar;
