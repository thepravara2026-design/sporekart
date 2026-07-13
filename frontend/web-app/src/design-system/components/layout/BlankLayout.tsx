import React from 'react';

export interface BlankLayoutProps {
  children: React.ReactNode;
}

export const BlankLayout: React.FC<BlankLayoutProps> = ({
  children,
}) => {
  return <>{children}</>;
};

BlankLayout.displayName = 'BlankLayout';
export default BlankLayout;
