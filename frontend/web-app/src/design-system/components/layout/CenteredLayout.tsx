import React from 'react';

export interface CenteredLayoutProps {
  children: React.ReactNode;
  maxWidth?: string;
}

export const CenteredLayout: React.FC<CenteredLayoutProps> = ({
  children,
  maxWidth = '480px',
}) => {
  const style: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '100vh',
    width: '100%',
  };

  const innerStyle: React.CSSProperties = {
    width: '100%',
    maxWidth,
    padding: 'var(--space-page-x)',
  };

  return (
    <div className="sk-centered-layout" style={style}>
      <div className="sk-centered-layout__inner" style={innerStyle}>
        {children}
      </div>
    </div>
  );
};

CenteredLayout.displayName = 'CenteredLayout';
export default CenteredLayout;
