import React from 'react';

export interface FullWidthLayoutProps {
  children: React.ReactNode;
}

export const FullWidthLayout: React.FC<FullWidthLayoutProps> = ({
  children,
}) => {
  const style: React.CSSProperties = {
    width: '100%',
    maxWidth: '100%',
  };

  return (
    <div className="sk-full-width-layout" style={style}>
      {children}
    </div>
  );
};

FullWidthLayout.displayName = 'FullWidthLayout';
export default FullWidthLayout;
