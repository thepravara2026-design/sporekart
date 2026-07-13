import React from 'react';

export interface ErrorLayoutProps {
  children: React.ReactNode;
}

export const ErrorLayout: React.FC<ErrorLayoutProps> = ({
  children,
}) => {
  const style: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '100vh',
    background: 'var(--color-bg-background)',
  };

  return (
    <div className="sk-error-layout" style={style}>
      {children}
    </div>
  );
};

ErrorLayout.displayName = 'ErrorLayout';
export default ErrorLayout;
