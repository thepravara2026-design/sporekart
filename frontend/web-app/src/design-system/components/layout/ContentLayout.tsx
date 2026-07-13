import React from 'react';
import { ContentContainer } from './ContentContainer';

export interface ContentLayoutProps {
  children: React.ReactNode;
  maxWidth?: 'sm' | 'md' | 'lg' | 'prose' | 'full';
}

const maxWidthMap: Record<string, string> = {
  sm: '640px',
  md: '768px',
  lg: '1024px',
  prose: 'var(--container-prose)',
  full: '100%',
};

export const ContentLayout: React.FC<ContentLayoutProps> = ({
  children,
  maxWidth = 'lg',
}) => {
  return (
    <ContentContainer maxWidth={maxWidthMap[maxWidth]}>
      {children}
    </ContentContainer>
  );
};

ContentLayout.displayName = 'ContentLayout';
export default ContentLayout;
